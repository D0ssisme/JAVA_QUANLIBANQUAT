package DTO;

public class KhachHangDTO {

    private String maKhachHang;
    private String hoTenKH;
    private String Sdt_KH;  
    private String diaChiKH;
    private int tongTienDaMua;  
    private int TrangThai;

    // Constructor, getter, setter
    public KhachHangDTO(){}

    public KhachHangDTO(String maKhachHang, String hoTenKH, String sdtKH, String diaChiKH, int tongTienDaMua,int TrangThai) {
        this.maKhachHang = maKhachHang;
        this.hoTenKH = hoTenKH;
        this.Sdt_KH = sdtKH;
        this.diaChiKH = diaChiKH;
        this.tongTienDaMua = tongTienDaMua;        
        this.TrangThai = TrangThai;

    }

    // Getter và Setter
    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getHoTenKH() {
        return hoTenKH;
    }

    public void setHoTenKH(String hoTenKH) {
        this.hoTenKH = hoTenKH;
    }

    public String getSdtKH() {
        return Sdt_KH;
    }

    public void setSdtKH(String sdtKH) {
        this.Sdt_KH = sdtKH;
    }

    public String getDiaChiKH() {
        return diaChiKH;
    }

    public void setDiaChiKH(String diaChiKH) {
        this.diaChiKH = diaChiKH;
    }

    public int getTongTienDaMua() {
        return tongTienDaMua;
    }

    public void setTongTienDaMua(int tongTienDaMua) {
        this.tongTienDaMua = tongTienDaMua;
    }
    public int getTrangThai(){
        return this.TrangThai;
    }
    public void setTrangThai(int TrangThai){
        this.TrangThai = TrangThai;
    }
}
