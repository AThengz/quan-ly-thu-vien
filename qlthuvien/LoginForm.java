import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginForm extends JFrame {

    private final NhanVienDAO dao = new NhanVienDAO();
    private final JTextField txtMaNV = new JTextField(15);
    private final JPasswordField txtMatKhau = new JPasswordField(15);

    public LoginForm() {
        setTitle("Dang nhap - Quan ly thu vien");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel title = new JLabel("DANG NHAP HE THONG", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        title.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));

        JPanel form = new JPanel(new GridLayout(2, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        form.add(new JLabel("Ma nhan vien"));
        form.add(txtMaNV);
        form.add(new JLabel("Mat khau"));
        form.add(txtMatKhau);

        JButton btnLogin = new JButton("Dang nhap");
        JButton btnExit = new JButton("Thoat");
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttons.add(btnLogin);
        buttons.add(btnExit);

        add(title, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> dangNhap());
        txtMatKhau.addActionListener(e -> dangNhap());
        btnExit.addActionListener(e -> System.exit(0));
        getRootPane().setDefaultButton(btnLogin);

        pack();
        setLocationRelativeTo(null);
    }

    private void dangNhap() {
        String ma = txtMaNV.getText().trim();
        String mk = new String(txtMatKhau.getPassword());
        if (ma.isEmpty() || mk.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui long nhap ma nhan vien va mat khau!",
                    "Thong bao", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            NhanVien nv = dao.login(ma, mk);
            if (nv == null) {
                JOptionPane.showMessageDialog(this, "Sai ma nhan vien hoac mat khau!",
                        "Dang nhap that bai", JOptionPane.ERROR_MESSAGE);
                txtMatKhau.setText("");
                return;
            }
            new SachForm(nv).setVisible(true);
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Loi ket noi CSDL: " + ex.getMessage(),
                    "Loi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
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
        java.awt.EventQueue.invokeLater(() -> new LoginForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
