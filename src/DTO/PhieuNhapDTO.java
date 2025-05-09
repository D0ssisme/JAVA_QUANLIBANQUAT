/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author nguye
 */
import java.time.LocalDate;

public class PhieuNhapDTO {
    public String MaPhieuNhap;
    public LocalDate NgayNhap;
    public String MaNCC;
    public String MaNhanVien;
    public int TongTien;

    // Constructor mặc định
    public PhieuNhapDTO() {
    }

    // Constructor đầy đủ
    public PhieuNhapDTO(String MaPhieuNhap, LocalDate NgayNhap, String MaNCC, String MaNhanVien, int TongTien) {
        this.MaPhieuNhap = MaPhieuNhap;
        this.NgayNhap = NgayNhap;
        this.MaNCC = MaNCC;
        this.MaNhanVien = MaNhanVien;
        this.TongTien = TongTien;
    }

    // Getters và Setters
    public String getMaPhieuNhap() {
        return MaPhieuNhap;
    }

    public void setMaPhieuNhap(String maPhieuNhap) {
        MaPhieuNhap = maPhieuNhap;
    }

    public LocalDate getNgayNhap() {
        return NgayNhap;
    }

    public void setNgayNhap(LocalDate ngayNhap) {
        NgayNhap = ngayNhap;
    }

    public String getMaNCC() {
        return MaNCC;
    }

    public void setMaNCC(String maNCC) {
        MaNCC = maNCC;
    }

    public String getMaNhanVien() {
        return MaNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        MaNhanVien = maNhanVien;
    }

    public int getTongTien() {
        return TongTien;
    }

    public void setTongTien(int tongTien) {
        TongTien = tongTien;
    }
}

