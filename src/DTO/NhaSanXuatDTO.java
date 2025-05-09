/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author nguye
 */

public class NhaSanXuatDTO {
    public String maNSX;
    public String tenNSX;
    public String diaChi;
    public String sdtNSX;

    // Constructor không tham số
    public NhaSanXuatDTO() {
    }

    // Constructor đầy đủ tham số
    public NhaSanXuatDTO(String maNSX, String tenNSX, String diaChi, String sdtNSX) {
        this.maNSX = maNSX;
        this.tenNSX = tenNSX;
        this.diaChi = diaChi;
        this.sdtNSX = sdtNSX;
    }

    public String getMaNSX() {
        return maNSX;
    }

    public void setMaNSX(String maNSX) {
        this.maNSX = maNSX;
    }

    public String getTenNSX() {
        return tenNSX;
    }

    public void setTenNSX(String tenNSX) {
        this.tenNSX = tenNSX;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSdtNSX() {
        return sdtNSX;
    }

    public void setSdtNSX(String sdtNSX) {
        this.sdtNSX = sdtNSX;
    }
}