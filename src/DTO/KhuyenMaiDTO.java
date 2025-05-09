package DTO;

import java.util.Date;

public class KhuyenMaiDTO{ 
    private String maSKKhuyenMai;
    private String tenKhuyenMai;
    private int phanTramGiam;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private int loai;

    // Constructor đầy đủ
    public KhuyenMaiDTO(String maSKKhuyenMai, String tenKhuyenMai, int phanTramGiam, Date ngayBatDau, Date ngayKetThuc,int loai) {
        this.maSKKhuyenMai = maSKKhuyenMai;
        this.tenKhuyenMai = tenKhuyenMai;
        this.phanTramGiam = phanTramGiam;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.loai = loai;
    }

    // Getters và Setters
    public String getMaSKKhuyenMai() {
        return maSKKhuyenMai;
    }

    public void setMaSKKhuyenMai(String maSKKhuyenMai) {
        this.maSKKhuyenMai = maSKKhuyenMai;
    }

    public String getTenKhuyenMai() {
        return tenKhuyenMai;
    }

    public void setTenKhuyenMai(String tenKhuyenMai) {
        this.tenKhuyenMai = tenKhuyenMai;
    }

    public int getPhanTramGiam() {
        return phanTramGiam;
    }

    public void setPhanTramGiam(int phanTramGiam) {
        this.phanTramGiam = phanTramGiam;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }


    public int getLoai() {
        return loai;
    }

    public void setLoai(int loai) {
        this.loai = loai;
    }
}
