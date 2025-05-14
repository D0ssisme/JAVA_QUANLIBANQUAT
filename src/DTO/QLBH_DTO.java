package DTO;

import java.util.Date;
public class QLBH_DTO {
    private String maBaoHanh;
    private String maQuat;
    private String maKhachHang;
    private Date thoiGianBaoHanh;
    private String trangThai;
    
    public QLBH_DTO() {}
    public QLBH_DTO(String maBaoHanh, String maQuat, String maKhachHang, Date thoiGianBaoHanh, String trangThai) {
        this.maBaoHanh = maBaoHanh;
        this.maQuat = maQuat;
        this.maKhachHang = maKhachHang;
        this.thoiGianBaoHanh = thoiGianBaoHanh;
        this.trangThai = trangThai;
    }
    public String getMaBaoHanh() {
        return maBaoHanh;
    }
    public void setMaBaoHanh(String maBaoHanh) {
        this.maBaoHanh = maBaoHanh;
    }
    public String getMaQuat() {
        return maQuat;
    }
    public void setMaQuat(String maQuat) {
        this.maQuat = maQuat;
    }
    public String getMaKhachHang() {
        return maKhachHang;
    }
    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }
    public Date getThoiGianBaoHanh() {
        return thoiGianBaoHanh;
    }
    public void setThoiGianBaoHanh(Date thoiGianBaoHanh) {
        this.thoiGianBaoHanh = thoiGianBaoHanh;
    }
    public String getTrangThai() {
        return trangThai;
    }
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
    @Override
    public String toString() {
        return "QLBH_DTO{" + 
                "maBaoHanh='" + maBaoHanh + '\'' + 
                ", maQuat='" + maQuat + '\'' + 
                ", maKhachHang='" + maKhachHang + '\'' + 
                ", thoiGianBaoHanh=" + thoiGianBaoHanh + 
                ", trangThai='" + trangThai + '\'' + 
                '}';
    }
}