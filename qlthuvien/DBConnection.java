import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String SERVER = "localhost";
    private static final String PORT = "1433";
    private static final String DATABASE = "QLThuVien";
    private static final String USER = "sa";
    private static final String PASSWORD = "123456";

    private static final String URL =
            "jdbc:sqlserver://" + SERVER + ":" + PORT
            + ";databaseName=" + DATABASE
            + ";encrypt=true;trustServerCertificate=true";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Chua them thu vien mssql-jdbc vao project!", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("Ket noi SQL Server thanh cong: " + conn.getCatalog());
        } catch (SQLException e) {
            System.out.println("Ket noi that bai: " + e.getMessage());
        }
    }
}
