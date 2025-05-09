/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.PhieuNhapDAO;
import DTO.PhieuNhapDTO;

import java.sql.*;
import java.util.*;

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
    
    public boolean themPhieuNhap(PhieuNhapDTO pn) {
    return PhieuNhapDAO.themPhieuNhap(pn);
}
    

}
