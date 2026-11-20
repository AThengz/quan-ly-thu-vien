import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class SachForm extends JFrame {

    private interface DbAction {
        void run() throws SQLException;
    }

    private final SachDAO dao = new SachDAO();
    private final NhanVien nhanVien;
    private DocGiaForm docGiaForm;

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"Ma sach", "Ten sach", "Tac gia", "Nam XB", "So luong", "Ma loai"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    private final JTextField txtMa = new JTextField();
    private final JTextField txtTen = new JTextField();
    private final JTextField txtTacGia = new JTextField();
    private final JTextField txtNamXB = new JTextField();
    private final JTextField txtSoLuong = new JTextField();
    private final JComboBox<String> cboLoai = new JComboBox<>();
    private final JTextField txtTim = new JTextField(20);

    public SachForm(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
        setTitle("Quan ly sach");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 620);
        setLocationRelativeTo(null);

        add(buildTop(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buildBottom(), BorderLayout.SOUTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFormFromRow();
            }
        });

        run(() -> {
            for (String loai : dao.getAllMaLoai()) {
                cboLoai.addItem(loai);
            }
            loadTable(dao.getAll());
        });
    }

    private JPanel buildTop() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        JLabel lblUser = new JLabel("Xin chao: " + nhanVien.getHoTen() + " (" + nhanVien.getChucVu() + ")");
        lblUser.setFont(lblUser.getFont().deriveFont(Font.BOLD));

        JButton btnDocGia = new JButton("Quan ly doc gia");
        JButton btnLogout = new JButton("Dang xuat");
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.add(btnDocGia);
        right.add(btnLogout);

        JButton btnTim = new JButton("Tim kiem");
        JButton btnAll = new JButton("Hien tat ca");
        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        search.add(new JLabel("Ten sach / tac gia:"));
        search.add(txtTim);
        search.add(btnTim);
        search.add(btnAll);

        top.add(lblUser, BorderLayout.WEST);
        top.add(right, BorderLayout.EAST);
        top.add(search, BorderLayout.SOUTH);

        btnDocGia.addActionListener(e -> moFormDocGia());
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginForm().setVisible(true);
        });
        btnTim.addActionListener(e -> timKiem());
        txtTim.addActionListener(e -> timKiem());
        btnAll.addActionListener(e -> run(() -> {
            txtTim.setText("");
            loadTable(dao.getAll());
        }));
        return top;
    }

    private JPanel buildBottom() {
        JPanel form = new JPanel(new GridLayout(0, 4, 10, 8));
        form.add(new JLabel("Ma sach"));
        form.add(txtMa);
        form.add(new JLabel("Ten sach"));
        form.add(txtTen);
        form.add(new JLabel("Tac gia"));
        form.add(txtTacGia);
        form.add(new JLabel("Nam XB"));
        form.add(txtNamXB);
        form.add(new JLabel("So luong"));
        form.add(txtSoLuong);
        form.add(new JLabel("Ma loai"));
        form.add(cboLoai);

        JButton btnThem = new JButton("Them");
        JButton btnSua = new JButton("Sua");
        JButton btnXoa = new JButton("Xoa");
        JButton btnMoi = new JButton("Lam moi");
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        buttons.add(btnThem);
        buttons.add(btnSua);
        buttons.add(btnXoa);
        buttons.add(btnMoi);

        btnThem.addActionListener(e -> them());
        btnSua.addActionListener(e -> sua());
        btnXoa.addActionListener(e -> xoa());
        btnMoi.addActionListener(e -> clearForm());

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottom.add(form, BorderLayout.CENTER);
        bottom.add(buttons, BorderLayout.SOUTH);
        return bottom;
    }

    private void moFormDocGia() {
        if (docGiaForm == null || !docGiaForm.isDisplayable()) {
            docGiaForm = new DocGiaForm();
        }
        docGiaForm.setVisible(true);
        docGiaForm.toFront();
    }

    private void loadTable(List<Sach> list) {
        model.setRowCount(0);
        for (Sach s : list) {
            model.addRow(new Object[]{
                    s.getMaSach(), s.getTenSach(), s.getTacGia(),
                    s.getNamXB() == null ? "" : s.getNamXB(),
                    s.getSoLuong(), s.getMaLoai()});
        }
    }

    private void fillFormFromRow() {
        int r = table.getSelectedRow();
        if (r < 0) {
            return;
        }
        txtMa.setText(String.valueOf(model.getValueAt(r, 0)));
        txtTen.setText(String.valueOf(model.getValueAt(r, 1)));
        txtTacGia.setText(String.valueOf(model.getValueAt(r, 2)));
        txtNamXB.setText(String.valueOf(model.getValueAt(r, 3)));
        txtSoLuong.setText(String.valueOf(model.getValueAt(r, 4)));
        cboLoai.setSelectedItem(String.valueOf(model.getValueAt(r, 5)));
    }

    private void clearForm() {
        table.clearSelection();
        txtMa.setText("");
        txtTen.setText("");
        txtTacGia.setText("");
        txtNamXB.setText("");
        txtSoLuong.setText("");
        if (cboLoai.getItemCount() > 0) {
            cboLoai.setSelectedIndex(0);
        }
        txtMa.requestFocus();
    }

    private Sach readForm() {
        String ma = txtMa.getText().trim();
        String ten = txtTen.getText().trim();
        String tg = txtTacGia.getText().trim();
        if (ma.isEmpty() || ten.isEmpty() || tg.isEmpty()) {
            throw new IllegalArgumentException("Ma sach, ten sach va tac gia khong duoc de trong!");
        }
        if (cboLoai.getSelectedItem() == null) {
            throw new IllegalArgumentException("Chua co ma loai de chon!");
        }
        Integer nam = null;
        String namText = txtNamXB.getText().trim();
        if (!namText.isEmpty()) {
            try {
                nam = Integer.valueOf(namText);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Nam XB phai la so nguyen!");
            }
            if (nam <= 0) {
                throw new IllegalArgumentException("Nam XB phai lon hon 0!");
            }
        }
        int sl;
        try {
            sl = Integer.parseInt(txtSoLuong.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("So luong phai la so nguyen!");
        }
        if (sl < 0) {
            throw new IllegalArgumentException("So luong khong duoc am!");
        }
        return new Sach(ma, ten, tg, nam, sl, (String) cboLoai.getSelectedItem());
    }

    private void them() {
        run(() -> {
            Sach s = readForm();
            if (dao.findById(s.getMaSach()) != null) {
                info("Ma sach da ton tai!");
                return;
            }
            if (dao.insert(s)) {
                info("Them thanh cong!");
                loadTable(dao.getAll());
                clearForm();
            }
        });
    }

    private void sua() {
        run(() -> {
            Sach s = readForm();
            if (dao.findById(s.getMaSach()) == null) {
                info("Khong tim thay ma sach can sua!");
                return;
            }
            if (dao.update(s)) {
                info("Sua thanh cong!");
                loadTable(dao.getAll());
                clearForm();
            }
        });
    }

    private void xoa() {
        String ma = txtMa.getText().trim();
        if (ma.isEmpty()) {
            info("Hay chon mot sach trong bang de xoa!");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this, "Ban co chac muon xoa sach " + ma + "?",
                "Xac nhan xoa", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            if (dao.delete(ma)) {
                info("Xoa thanh cong!");
                loadTable(dao.getAll());
                clearForm();
            } else {
                info("Khong tim thay sach can xoa!");
            }
        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) {
                info("Khong the xoa: sach nay dang nam trong phieu muon.");
            } else {
                error(e);
            }
        }
    }

    private void timKiem() {
        run(() -> {
            String kw = txtTim.getText().trim();
            loadTable(kw.isEmpty() ? dao.getAll() : dao.search(kw));
        });
    }

    private void run(DbAction action) {
        try {
            action.run();
        } catch (IllegalArgumentException e) {
            info(e.getMessage());
        } catch (SQLException e) {
            error(e);
        }
    }

    private void info(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Thong bao", JOptionPane.INFORMATION_MESSAGE);
    }

    private void error(SQLException e) {
        JOptionPane.showMessageDialog(this, "Loi CSDL: " + e.getMessage(), "Loi", JOptionPane.ERROR_MESSAGE);
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new SachForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
