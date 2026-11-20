import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DocGiaDAO {

    private static final String COLS = "MaDG, HoTen, SoDienThoai, DiaChi, NgayDangKy";

    public List<DocGia> getAll() throws SQLException {
        String sql = "SELECT " + COLS + " FROM DocGia";
        List<DocGia> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<DocGia> search(String keyword) throws SQLException {
        String sql = "SELECT " + COLS + " FROM DocGia WHERE HoTen LIKE ? OR SoDienThoai LIKE ?";
        List<DocGia> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setNString(1, kw);
            ps.setString(2, kw);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public DocGia findById(String maDG) throws SQLException {
        String sql = "SELECT " + COLS + " FROM DocGia WHERE MaDG = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDG);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public boolean insert(DocGia d) throws SQLException {
        String sql = "INSERT INTO DocGia (MaDG, HoTen, SoDienThoai, DiaChi, NgayDangKy) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getMaDG());
            ps.setNString(2, d.getHoTen());
            ps.setString(3, d.getSoDienThoai());
            ps.setNString(4, d.getDiaChi());
            ps.setDate(5, Date.valueOf(d.getNgayDangKy()));
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(DocGia d) throws SQLException {
        String sql = "UPDATE DocGia SET HoTen = ?, SoDienThoai = ?, DiaChi = ?, NgayDangKy = ? "
                   + "WHERE MaDG = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setNString(1, d.getHoTen());
            ps.setString(2, d.getSoDienThoai());
            ps.setNString(3, d.getDiaChi());
            ps.setDate(4, Date.valueOf(d.getNgayDangKy()));
            ps.setString(5, d.getMaDG());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(String maDG) throws SQLException {
        String sql = "DELETE FROM DocGia WHERE MaDG = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDG);
            return ps.executeUpdate() > 0;
        }
    }

    private DocGia mapRow(ResultSet rs) throws SQLException {
        return new DocGia(
                rs.getString("MaDG"),
                rs.getNString("HoTen"),
                rs.getString("SoDienThoai"),
                rs.getNString("DiaChi"),
                rs.getDate("NgayDangKy").toLocalDate());
    }
}
