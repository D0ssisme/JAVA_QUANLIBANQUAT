/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO.ThongKe;

public class ThongKeQuatTheoNamDTO {
    private String maQuat;
    private String tenQuat;
    private int soLuongBan;
    private double tongTien;

    public ThongKeQuatTheoNamDTO(String maQuat, String tenQuat, int soLuongBan, double tongTien) {
        this.maQuat = maQuat;
        this.tenQuat = tenQuat;
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
