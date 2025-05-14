  package DTO;

public class LoaiSanPhamDTO {
    private String maLoaiSanPham;
    private String tenLoai;
    private String trangThai;
    private String moTa;

    public LoaiSanPhamDTO() { }

    public LoaiSanPhamDTO(String maLoaiSanPham, String tenLoai, String trangThai, String moTa) {
        this.maLoaiSanPham = maLoaiSanPham;
        this.tenLoai = tenLoai;
        this.trangThai = trangThai;
        this.moTa = moTa;
    }

    public String getMaLoaiSanPham() {
        return maLoaiSanPham;
    }

    public void setMaLoaiSanPham(String maLoaiSanPham) {
        this.maLoaiSanPham = maLoaiSanPham;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
    @Override
    public String toString() {
        return tenLoai;
    }

}
