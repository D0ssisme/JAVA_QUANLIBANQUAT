package DTO;

public class ThongKeDTO {
    private String ten;
    private int giaTri;

    public ThongKeDTO(String ten, int giaTri) {
        this.ten = ten;
        this.giaTri = giaTri;
    }

    public String getTen() {
        return ten;
    }

    public int getGiaTri() {
        return giaTri;
    }
}
