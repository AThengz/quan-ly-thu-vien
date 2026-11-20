import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final SachDAO dao = new SachDAO();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println();
            System.out.println("===== QUAN LY THU VIEN - SACH =====");
            System.out.println("1. Xem danh sach");
            System.out.println("2. Them sach");
            System.out.println("3. Sua sach");
            System.out.println("4. Xoa sach");
            System.out.println("5. Tim kiem (ten sach / tac gia)");
            System.out.println("0. Thoat");
            System.out.print("Chon: ");
            String chon = sc.nextLine().trim();

            try {
                switch (chon) {
                    case "1": hienThi(dao.getAll()); break;
                    case "2": them(); break;
                    case "3": sua(); break;
                    case "4": xoa(); break;
                    case "5":
                        System.out.print("Tu khoa: ");
                        hienThi(dao.search(sc.nextLine().trim()));
                        break;
                    case "0": System.out.println("Tam biet!"); return;
                    default: System.out.println("Lua chon khong hop le!");
                }
            } catch (SQLException e) {
                System.out.println("Loi CSDL: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Nam XB va so luong phai la so!");
            }
        }
    }

    private static void hienThi(List<Sach> ds) {
        if (ds.isEmpty()) {
            System.out.println("(Khong co du lieu)");
            return;
        }
        ds.forEach(System.out::println);
    }

    private static Sach nhapSach(String maSach) {
        System.out.print("Ten sach: ");
        String ten = sc.nextLine().trim();
        System.out.print("Tac gia: ");
        String tg = sc.nextLine().trim();
        System.out.print("Nam XB (bo trong neu khong co): ");
        String nam = sc.nextLine().trim();
        System.out.print("So luong: ");
        int sl = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Ma loai (L01, L02, L03, L04): ");
        String loai = sc.nextLine().trim();
        return new Sach(maSach, ten, tg, nam.isEmpty() ? null : Integer.valueOf(nam), sl, loai);
    }

    private static void them() throws SQLException {
        System.out.print("Ma sach: ");
        String ma = sc.nextLine().trim();
        if (dao.findById(ma) != null) {
            System.out.println("Ma sach da ton tai!");
            return;
        }
        System.out.println(dao.insert(nhapSach(ma)) ? "Them thanh cong!" : "Them that bai!");
    }

    private static void sua() throws SQLException {
        System.out.print("Nhap ma sach can sua: ");
        String ma = sc.nextLine().trim();
        if (dao.findById(ma) == null) {
            System.out.println("Khong tim thay sach!");
            return;
        }
        System.out.println(dao.update(nhapSach(ma)) ? "Sua thanh cong!" : "Sua that bai!");
    }

    private static void xoa() throws SQLException {
        System.out.print("Nhap ma sach can xoa: ");
        String ma = sc.nextLine().trim();
        try {
            System.out.println(dao.delete(ma) ? "Xoa thanh cong!" : "Khong tim thay sach!");
        } catch (SQLException e) {
            System.out.println("Khong the xoa: sach nay dang nam trong phieu muon.");
        }
    }
}
