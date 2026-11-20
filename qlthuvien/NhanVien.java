public class NhanVien {
    private String maNV;
    private String hoTen;
    private String soDienThoai;
    private String chucVu;

    public NhanVien(String maNV, String hoTen, String soDienThoai, String chucVu) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.chucVu = chucVu;
    }

    public String getMaNV() { return maNV; }
    public String getHoTen() { return hoTen; }
    public String getSoDienThoai() { return soDienThoai; }
    public String getChucVu() { return chucVu; }
}
