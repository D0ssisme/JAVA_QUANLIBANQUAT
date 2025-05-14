/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO.ThongKe;

public class ThongKeLoaiQuatTheoNamDTO {
     private String maLoaiQuat;
    private String tenLoaiQuat;
    private int soLuongBan;
    private double tongTien;


    public ThongKeLoaiQuatTheoNamDTO(String maLoaiQuat, String tenLoaiQuat, int soLuongBan, double tongTien) {
        this.maLoaiQuat = maLoaiQuat;
        this.tenLoaiQuat = tenLoaiQuat;
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
