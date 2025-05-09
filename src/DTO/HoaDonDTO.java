package DTO;

import java.sql.Date;

public class HoaDonDTO {

    private String maHoaDon;
    private String maKhachHang;
    private String maNhanVien;
    private Date ngayLap;
    private String maSuKienKM;
    private int tongTien;

    public HoaDonDTO() {
        // Constructor mặc định
    }

    public HoaDonDTO(String maHoaDon, String maKhachHang, String maNhanVien, Date ngayLap, String maSuKienKM, int tongTien) {
        this.maHoaDon = maHoaDon;
        this.maKhachHang = maKhachHang;
        this.maNhanVien = maNhanVien;
        this.ngayLap = ngayLap;
        this.maSuKienKM = maSuKienKM;
        this.tongTien = tongTien;
    }

    // Getter và Setter
    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public Date getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(Date ngayLap) {
        this.ngayLap = ngayLap;
    }

    public String getMaSuKienKM() {
        return maSuKienKM;
    }

    public void setMaSuKienKM(String maSuKienKM) {
        this.maSuKienKM = maSuKienKM;
    }

    public int getTongTien() {
        return tongTien;
    }

    public void setTongTien(int tongTien) {
        this.tongTien = tongTien;
    }
}
