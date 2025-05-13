/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.ChiTietPhieuNhapDAO;
import DTO.ChiTietPhieuNhapDTO;

import java.util.List;


public class ChiTietPhieuNhapBUS {
    public ChiTietPhieuNhapDAO dao=new ChiTietPhieuNhapDAO();
    
    public ChiTietPhieuNhapDTO getChiTietPhieuNhapByMaQuat(String maPN,String maQuat)
    {
        return dao.getChiTietPhieuNhapbyMaQuat(maPN, maQuat);
    }
    
    public boolean checkexits(String maPn,String maQuat)
    {
        return dao.isChiTietExist(maPn, maQuat);
    }
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
    
    public boolean updatesoluong(String mapn,int soluong,String maQuat)
    {
       return dao.updateSoluong(mapn, soluong,maQuat);
       
   
    }




  
    // Có thể bổ sung các chức năng khác như cập nhật, xóa nếu cần
}
