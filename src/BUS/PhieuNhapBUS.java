/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.PhieuNhapDAO;
import DTO.PhieuNhapDTO;

import java.sql.*;
import java.util.*;
import java.time.LocalDate;
import java.time.ZoneId;


import DTO.DBConnection;

public class PhieuNhapBUS {

     public static String taoMaPhieuNhapTuDong() {
        return PhieuNhapDAO.taoMaPhieuNhapTuDong();
    }
    public List<PhieuNhapDTO> getAllPhieuNhap() {
        return PhieuNhapDAO.getAllPhieuNhap();
    }
    public PhieuNhapDTO findphieunhapfrommapn(String mapn)
    {
        return PhieuNhapDAO.findPhieuNhapFromMaPN(mapn);
    }

    public static Map<String, String> getTenNhaCungCapMap() {
        Map<String, String> map = new HashMap<>();
        String sql = "SELECT MaNCC, TenNCC FROM nha_cung_cap";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String ma = rs.getString("MaNCC");
                String ten = rs.getString("TenNCC");
                map.put(ma, ten);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Hoặc log ra file nếu có hệ thống log
        }

        return map;
    }
    
    public boolean themPhieuNhap(PhieuNhapDTO pn) 
    {
    return PhieuNhapDAO.themPhieuNhap(pn);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
   public List<PhieuNhapDTO> get_filter(String mancc, String manv, java.util.Date ngaystar, java.util.Date ngayend, int sotienstar, int sotienend) {
    List<PhieuNhapDTO> allPhieuNhap = getAllPhieuNhap(); // Giả sử bạn có phương thức lấy tất cả phiếu nhập
    List<PhieuNhapDTO> filteredList = new ArrayList<>();
    
    // Lọc danh sách dựa trên các điều kiện được cung cấp
    for (PhieuNhapDTO phieuNhap : allPhieuNhap) {
        boolean match = true;
        
        // Lọc theo mã nhà cung cấp
        if (mancc != null && !mancc.equals("Tất cả") && !mancc.isEmpty()) {
            if (!phieuNhap.getMaNCC().equals(mancc)) {
                match = false;
            }
        }

        // Lọc theo mã nhân viên
        if (manv != null && !manv.equals("Tất cả") && !manv.isEmpty()) {
            if (!phieuNhap.getMaNhanVien().equals(manv)) {
                match = false;
            }
        }
        
        // Lọc theo khoảng ngày
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
        
        // Lọc theo khoảng số tiền
        int tongTien = phieuNhap.getTongTien(); // Giả sử có phương thức này để lấy tổng tiền
        if (sotienstar > 0 && tongTien < sotienstar) {
            match = false;
        }
        
        if (sotienend > 0 && tongTien > sotienend) {
            match = false;
        }
        
        // Thêm vào danh sách kết quả nếu thỏa mãn tất cả điều kiện
        if (match) {
            filteredList.add(phieuNhap);
        }
    }
       System.out.println(filteredList.size());
    return filteredList;
}

    
    
    
    
    
    
    
}