import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class SachDAO {

    public List<Sach> getAll() throws SQLException {
        String sql = "SELECT MaSach, TenSach, TacGia, NamXB, SoLuong, MaLoai FROM Sach";
        List<Sach> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Sach> search(String keyword) throws SQLException {
        String sql = "SELECT MaSach, TenSach, TacGia, NamXB, SoLuong, MaLoai FROM Sach "
                   + "WHERE TenSach LIKE ? OR TacGia LIKE ?";
        List<Sach> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setNString(1, kw);
            ps.setNString(2, kw);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public Sach findById(String maSach) throws SQLException {
        String sql = "SELECT MaSach, TenSach, TacGia, NamXB, SoLuong, MaLoai FROM Sach WHERE MaSach = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSach);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public boolean insert(Sach s) throws SQLException {
        String sql = "INSERT INTO Sach (MaSach, TenSach, TacGia, NamXB, SoLuong, MaLoai) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getMaSach());
            ps.setNString(2, s.getTenSach());
            ps.setNString(3, s.getTacGia());
            if (s.getNamXB() == null) ps.setNull(4, Types.INTEGER);
            else ps.setInt(4, s.getNamXB());
            ps.setInt(5, s.getSoLuong());
            ps.setString(6, s.getMaLoai());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(Sach s) throws SQLException {
        String sql = "UPDATE Sach SET TenSach = ?, TacGia = ?, NamXB = ?, SoLuong = ?, MaLoai = ? "
                   + "WHERE MaSach = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setNString(1, s.getTenSach());
            ps.setNString(2, s.getTacGia());
            if (s.getNamXB() == null) ps.setNull(3, Types.INTEGER);
            else ps.setInt(3, s.getNamXB());
            ps.setInt(4, s.getSoLuong());
            ps.setString(5, s.getMaLoai());
            ps.setString(6, s.getMaSach());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(String maSach) throws SQLException {
        String sql = "DELETE FROM Sach WHERE MaSach = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSach);
            return ps.executeUpdate() > 0;
        }
    }

    private Sach mapRow(ResultSet rs) throws SQLException {
        int nam = rs.getInt("NamXB");
        Integer namXB = rs.wasNull() ? null : nam;
        return new Sach(
                rs.getString("MaSach"),
                rs.getNString("TenSach"),
                rs.getNString("TacGia"),
                namXB,
                rs.getInt("SoLuong"),
                rs.getString("MaLoai"));
    }
}
