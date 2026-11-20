package qlthuvien;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class DocGiaForm extends JFrame {

    private interface DbAction {
        void run() throws SQLException;
    }

    private final DocGiaDAO dao = new DocGiaDAO();

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"Ma DG", "Ho ten", "So dien thoai", "Dia chi", "Ngay dang ky"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    private final JTextField txtMa = new JTextField();
    private final JTextField txtHoTen = new JTextField();
    private final JTextField txtSdt = new JTextField();
    private final JTextField txtDiaChi = new JTextField();
    private final JTextField txtNgay = new JTextField(LocalDate.now().toString());
    private final JTextField txtTim = new JTextField(20);

    public DocGiaForm() {
        setTitle("Quan ly doc gia");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 580);
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

        run(() -> loadTable(dao.getAll()));
    }

    private JPanel buildTop() {
        JButton btnTim = new JButton("Tim kiem");
        JButton btnAll = new JButton("Hien tat ca");
        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        search.add(new JLabel("Ho ten / so dien thoai:"));
        search.add(txtTim);
        search.add(btnTim);
        search.add(btnAll);

        btnTim.addActionListener(e -> timKiem());
        txtTim.addActionListener(e -> timKiem());
        btnAll.addActionListener(e -> run(() -> {
            txtTim.setText("");
            loadTable(dao.getAll());
        }));

        JPanel top = new JPanel(new BorderLayout());
        top.add(search, BorderLayout.CENTER);
        return top;
    }

    private JPanel buildBottom() {
        JPanel form = new JPanel(new GridLayout(0, 4, 10, 8));
        form.add(new JLabel("Ma doc gia"));
        form.add(txtMa);
        form.add(new JLabel("Ho ten"));
        form.add(txtHoTen);
        form.add(new JLabel("So dien thoai"));
        form.add(txtSdt);
        form.add(new JLabel("Dia chi"));
        form.add(txtDiaChi);
        form.add(new JLabel("Ngay dang ky (yyyy-MM-dd)"));
        form.add(txtNgay);
        form.add(new JLabel());
        form.add(new JLabel());

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

    private void loadTable(List<DocGia> list) {
        model.setRowCount(0);
        for (DocGia d : list) {
            model.addRow(new Object[]{
                    d.getMaDG(), d.getHoTen(), d.getSoDienThoai(),
                    d.getDiaChi() == null ? "" : d.getDiaChi(),
                    d.getNgayDangKy().toString()});
        }
    }

    private void fillFormFromRow() {
        int r = table.getSelectedRow();
        if (r < 0) {
            return;
        }
        txtMa.setText(String.valueOf(model.getValueAt(r, 0)));
        txtHoTen.setText(String.valueOf(model.getValueAt(r, 1)));
        txtSdt.setText(String.valueOf(model.getValueAt(r, 2)));
        txtDiaChi.setText(String.valueOf(model.getValueAt(r, 3)));
        txtNgay.setText(String.valueOf(model.getValueAt(r, 4)));
    }

    private void clearForm() {
        table.clearSelection();
        txtMa.setText("");
        txtHoTen.setText("");
        txtSdt.setText("");
        txtDiaChi.setText("");
        txtNgay.setText(LocalDate.now().toString());
        txtMa.requestFocus();
    }

    private DocGia readForm() {
        String ma = txtMa.getText().trim();
        String ten = txtHoTen.getText().trim();
        String sdt = txtSdt.getText().trim();
        if (ma.isEmpty() || ten.isEmpty() || sdt.isEmpty()) {
            throw new IllegalArgumentException("Ma doc gia, ho ten va so dien thoai khong duoc de trong!");
        }
        if (!sdt.matches("\\d{8,15}")) {
            throw new IllegalArgumentException("So dien thoai chi gom chu so (8 den 15 so)!");
        }
        LocalDate ngay;
        try {
            ngay = LocalDate.parse(txtNgay.getText().trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Ngay dang ky phai co dang yyyy-MM-dd, vi du 2026-11-20!");
        }
        String diaChi = txtDiaChi.getText().trim();
        return new DocGia(ma, ten, sdt, diaChi.isEmpty() ? null : diaChi, ngay);
    }

    private void them() {
        run(() -> {
            DocGia d = readForm();
            if (dao.findById(d.getMaDG()) != null) {
                info("Ma doc gia da ton tai!");
                return;
            }
            if (dao.insert(d)) {
                info("Them thanh cong!");
                loadTable(dao.getAll());
                clearForm();
            }
        });
    }

    private void sua() {
        run(() -> {
            DocGia d = readForm();
            if (dao.findById(d.getMaDG()) == null) {
                info("Khong tim thay ma doc gia can sua!");
                return;
            }
            if (dao.update(d)) {
                info("Sua thanh cong!");
                loadTable(dao.getAll());
                clearForm();
            }
        });
    }

    private void xoa() {
        String ma = txtMa.getText().trim();
        if (ma.isEmpty()) {
            info("Hay chon mot doc gia trong bang de xoa!");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this, "Ban co chac muon xoa doc gia " + ma + "?",
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
                info("Khong tim thay doc gia can xoa!");
            }
        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) {
                info("Khong the xoa: doc gia nay da co phieu muon.");
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
        java.awt.EventQueue.invokeLater(() -> new DocGiaForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
