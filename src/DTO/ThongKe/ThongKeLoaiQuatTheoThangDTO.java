package DTO.ThongKe;

public class ThongKeLoaiQuatTheoThangDTO {
    private String maLoaiQuat;
    private String tenLoaiQuat;
    private int thang;
    private int soLuongBan;
    private double tongTien;

    public ThongKeLoaiQuatTheoThangDTO(String maLoaiQuat, String tenLoaiQuat, int thang, int soLuongBan, double tongTien) {
        this.maLoaiQuat = maLoaiQuat;
        this.tenLoaiQuat = tenLoaiQuat;
        this.thang = thang;
        this.soLuongBan = soLuongBan;
        this.tongTien = tongTien;
    }

    public String getMaLoaiQuat() {
        return maLoaiQuat;
    }

    public void setMaLoaiQuat(String maLoaiQuat) {
        this.maLoaiQuat = maLoaiQuat;
    }

    public String getTenLoaiQuat() {
        return tenLoaiQuat;
    }

    public void setTenLoaiQuat(String tenLoaiQuat) {
        this.tenLoaiQuat = tenLoaiQuat;
    }

    public int getThang() {
        return thang;
    }

    public void setThang(int thang) {
        this.thang = thang;
    }

    public int getSoLuongBan() {
        return soLuongBan;
    }

    public void setSoLuongBan(int soLuongBan) {
        this.soLuongBan = soLuongBan;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }
}
