/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO.ThongKe;

/**
 *
 * @author Admin
 */
public class ThongKeQuatTheoThangDTO {
    private String maQuat;
    private String tenQuat;
    private int thang;
    private int soLuongBan;
    private double tongTien;

    public ThongKeQuatTheoThangDTO(String maQuat, String tenQuat, int thang, int soLuongBan, double tongTien) {
        this.maQuat = maQuat;
        this.tenQuat = tenQuat;
        this.thang = thang;
        this.soLuongBan = soLuongBan;
        this.tongTien = tongTien;
    }

    public String getMaQuat() {
        return maQuat;
    }

    public void setMaQuat(String maQuat) {
        this.maQuat = maQuat;
    }

    public String getTenQuat() {
        return tenQuat;
    }

    public void setTenLoaiQuat(String tenLoaiQuat) {
        this.tenQuat = tenLoaiQuat;
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
