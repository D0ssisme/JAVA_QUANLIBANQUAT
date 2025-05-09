/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author nguye
 */
public class ChiTietPhieuNhapDTO {
    private String maPhieuNhap;
    private String maQuat;
    private int soLuong;
    private int donGia;
    private int thanhTien;

    // Constructor mặc định
    public ChiTietPhieuNhapDTO() {}

    // Constructor đầy đủ
    public ChiTietPhieuNhapDTO(String maPhieuNhap, String maQuat, int soLuong, int donGia) {
        this.maPhieuNhap = maPhieuNhap;
        this.maQuat = maQuat;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = soLuong*donGia;
    }

    // Getters và Setters
    public String getMaPhieuNhap() {
        return maPhieuNhap;
    }

    public void setMaPhieuNhap(String maPhieuNhap) {
        this.maPhieuNhap = maPhieuNhap;
    }

    public String getMaQuat() {
        return maQuat;
    }

    public void setMaQuat(String maQuat) {
        this.maQuat = maQuat;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getDonGia() {
        return donGia;
    }

    public void setDonGia(int donGia) {
        this.donGia = donGia;
    }

    public int getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(int thanhTien) {
        this.thanhTien = thanhTien;
    }
      public void printDetails() {
        System.out.println("maphieunhap: " + maPhieuNhap);
        System.out.println("maquat: " + maQuat);
        System.out.println("soluong: " + soLuong);
        System.out.println("dongia: " + donGia);
        System.out.println("thanhtien: " + thanhTien);
    }
}
