/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO.ThongKe;

/**
 *
 * @author Admin
 */
public class ThongKeQuatTheoNgayDTO {
    private String maQuat;
    private String tenQuat;
    private String ngay;
    private int soLuongBan;
    private double tongTien;

    public ThongKeQuatTheoNgayDTO(String maQuat, String tenQuat, String ngay, int soLuongBan, double tongTien) {
        this.maQuat = maQuat;
        this.tenQuat = tenQuat;
        this.ngay = ngay;
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

    public void setTenQuat(String tenQuat) {
        this.tenQuat = tenQuat;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
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
