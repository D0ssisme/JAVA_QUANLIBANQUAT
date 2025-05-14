package DTO.ThongKe;

public class ThongKeKhachHangTheoNgayDTO {
    private String maKH;
    private String tenKH;
    private int soLanMua;
    private double tongTien;
    
    public ThongKeKhachHangTheoNgayDTO(String maKH, String tenKH, int soLanMua, double tongTien) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.soLanMua = soLanMua;
        this.tongTien = tongTien;
    }
    
    public String getMaKH() {
        return maKH;
    }
    
    public String getTenKH() {
        return tenKH;
    }
    
    public int getSoLanMua() {
        return soLanMua;
    }
    
    public double getTongTien() {
        return tongTien;
    }
    
    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }
    
    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }
    
    public void setSoLanMua(int soLanMua) {
        this.soLanMua = soLanMua;
    }
    
    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }
}