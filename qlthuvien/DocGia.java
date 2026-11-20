import java.time.LocalDate;

public class DocGia {
    private String maDG;
    private String hoTen;
    private String soDienThoai;
    private String diaChi;
    private LocalDate ngayDangKy;

    public DocGia(String maDG, String hoTen, String soDienThoai,
                  String diaChi, LocalDate ngayDangKy) {
        this.maDG = maDG;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.ngayDangKy = ngayDangKy;
    }

    public String getMaDG() { return maDG; }
    public String getHoTen() { return hoTen; }
    public String getSoDienThoai() { return soDienThoai; }
    public String getDiaChi() { return diaChi; }
    public LocalDate getNgayDangKy() { return ngayDangKy; }
}
