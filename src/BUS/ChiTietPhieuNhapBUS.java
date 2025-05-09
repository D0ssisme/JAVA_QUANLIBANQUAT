/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.ChiTietPhieuNhapDAO;
import DTO.ChiTietPhieuNhapDTO;

import java.util.List;

public class ChiTietPhieuNhapBUS {

    // Lấy toàn bộ danh sách chi tiết phiếu nhập
    public List<ChiTietPhieuNhapDTO> layTatCa() {
        return ChiTietPhieuNhapDAO.getAllChiTietPhieuNhap();
    }
    public boolean themChiTietPhieuNhap(ChiTietPhieuNhapDTO ct) {
    return ChiTietPhieuNhapDAO.themChiTietPhieuNhap(ct);
}   
  
    public static List<ChiTietPhieuNhapDTO> getChiTietPhieuNhapByMaPN(String maPN) {
        return ChiTietPhieuNhapDAO.getChiTietPhieuNhapByMaPN(maPN);
    }




  
    // Có thể bổ sung các chức năng khác như cập nhật, xóa nếu cần
}
