/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.PhieuNhapDAO;
import DTO.PhieuNhapDTO;

import BUS.NhaCungCapBUS;

import java.sql.*;
import java.util.*;
import java.time.LocalDate;
import java.time.ZoneId;


import DTO.DBConnection;

public class PhieuNhapBUS {
    public PhieuNhapDAO dao=new PhieuNhapDAO();
     public  String taoMaPhieuNhapTuDong() {
        return dao.taoMaPhieuNhapTuDong();
    }
    public List<PhieuNhapDTO> getAllPhieuNhap() {
        return dao.getAllPhieuNhap();
    }
    public PhieuNhapDTO findphieunhapfrommapn(String mapn)
    {
        return dao.findPhieuNhapFromMaPN(mapn);
    }

 
    public boolean themPhieuNhap(PhieuNhapDTO pn) 
    {
    return dao.themPhieuNhap(pn);
    }
    
    
 
    
   public List<PhieuNhapDTO> get_filter(String tenncc, String manv, java.util.Date ngaystar, java.util.Date ngayend, int sotienstar, int sotienend) {
    List<PhieuNhapDTO> allPhieuNhap = getAllPhieuNhap(); 
    List<PhieuNhapDTO> filteredList = new ArrayList<>();
    NhaCungCapBUS nccbus=new NhaCungCapBUS();
    
 
    for (PhieuNhapDTO phieuNhap : allPhieuNhap) {
        boolean match = true;
        
    
        if (tenncc != null && !tenncc.equals("Tất cả") && !tenncc.isEmpty()) {
            if (!nccbus.layTenNhaCungCapTheoMa(phieuNhap.getMaNCC()).equals(tenncc)) {
                match = false;
            }
        }

       
        if (manv != null && !manv.equals("Tất cả") && !manv.isEmpty()) {
            if (!phieuNhap.getMaNhanVien().equals(manv)) {
                match = false;
            }
        }
        
       
        if (ngaystar != null || ngayend != null) {
            LocalDate phieuNhapDate = phieuNhap.getNgayNhap(); // Giả sử getNgayNhap trả về LocalDate

            // Chuyển đổi ngày bắt đầu và ngày kết thúc từ java.util.Date sang LocalDate
            LocalDate ngayBatDau = (ngaystar != null)
                ? ngaystar.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                : null;

            LocalDate ngayKetThuc = (ngayend != null)
                ? ngayend.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                : null;

            if (ngayBatDau != null && phieuNhapDate.isBefore(ngayBatDau)) {
                match = false;
            }

            if (ngayKetThuc != null && phieuNhapDate.isAfter(ngayKetThuc)) {
                match = false;
            }
        }
        
   
        int tongTien = phieuNhap.getTongTien(); 
        if (sotienstar > 0 && tongTien < sotienstar) {
            match = false;
        }
        
        if (sotienend > 0 && tongTien > sotienend) {
            match = false;
        }
        

        if (match) {
            filteredList.add(phieuNhap);
        }
    }
       System.out.println(filteredList.size());
    return filteredList;
}

    
    
    
    
    
    
    
}