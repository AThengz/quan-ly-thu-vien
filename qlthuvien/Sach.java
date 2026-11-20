public class Sach {
    private String maSach;
    private String tenSach;
    private String tacGia;
    private Integer namXB;
    private int soLuong;
    private String maLoai;

    public Sach() {}

    public Sach(String maSach, String tenSach, String tacGia,
                Integer namXB, int soLuong, String maLoai) {
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.tacGia = tacGia;
        this.namXB = namXB;
        this.soLuong = soLuong;
        this.maLoai = maLoai;
    }

    public String getMaSach() { return maSach; }
    public void setMaSach(String maSach) { this.maSach = maSach; }

    public String getTenSach() { return tenSach; }
    public void setTenSach(String tenSach) { this.tenSach = tenSach; }

    public String getTacGia() { return tacGia; }
    public void setTacGia(String tacGia) { this.tacGia = tacGia; }

    public Integer getNamXB() { return namXB; }
    public void setNamXB(Integer namXB) { this.namXB = namXB; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public String getMaLoai() { return maLoai; }
    public void setMaLoai(String maLoai) { this.maLoai = maLoai; }

    @Override
    public String toString() {
        return String.format("%-6s | %-35s | %-18s | %-5s | SL: %-3d | %s",
                maSach, tenSach, tacGia, namXB == null ? "-" : namXB, soLuong, maLoai);
    }
}
