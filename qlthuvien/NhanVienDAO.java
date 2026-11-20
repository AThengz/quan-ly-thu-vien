import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NhanVienDAO {

    public NhanVien login(String maNV, String matKhau) throws SQLException {
        String sql = "SELECT MaNV, HoTen, SoDienThoai, ChucVu FROM NhanVien "
                   + "WHERE MaNV = ? AND MatKhau = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            ps.setString(2, matKhau);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new NhanVien(
                            rs.getString("MaNV"),
                            rs.getNString("HoTen"),
                            rs.getString("SoDienThoai"),
                            rs.getNString("ChucVu"));
                }
                return null;
            }
        }
    }
}
