package DTO;

public class ChiTietHoaDonDTO {
    private String maHoaDon;
    private String maQuat;
    private int soLuong;
    private int donGia;
    private int thanhTien;
    private String maBaoHanh;

    // Default constructor
    public ChiTietHoaDonDTO() {
    }

    // Full constructor
    public ChiTietHoaDonDTO(String maHoaDon, String maQuat, int soLuong, int donGia, int thanhTien, String maBaoHanh) {
        this.maHoaDon = maHoaDon;
        this.maQuat = maQuat;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
        this.maBaoHanh = maBaoHanh;
    }

    // Getters and Setters
    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
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

    public String getMaBaoHanh() {
        return maBaoHanh;
    }

    public void setMaBaoHanh(String maBaoHanh) {
        this.maBaoHanh = maBaoHanh;
    }
}