/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DTO.ChiTietPhieuNhapDTO;
import DTO.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietPhieuNhapDAO {

    // Lấy tất cả chi tiết phiếu nhập
    public static List<ChiTietPhieuNhapDTO> getAllChiTietPhieuNhap() {
        List<ChiTietPhieuNhapDTO> ds = new ArrayList<>();

        String sql = "SELECT * FROM chitiet_phieunhap";  // Đảm bảo đúng tên bảng

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                String maPhieuNhap = rs.getString("MaPhieuNhap");
                String maQuat = rs.getString("MaQuat");
                int soLuong = rs.getInt("SoLuong");
                int donGia = rs.getInt("DonGia");
                int thanhTien = rs.getInt("ThanhTien");

                ChiTietPhieuNhapDTO ct = new ChiTietPhieuNhapDTO(maPhieuNhap, maQuat, soLuong, donGia);
                ds.add(ct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ds;
    }
   public static List<ChiTietPhieuNhapDTO> getChiTietPhieuNhapByMaPN(String maPN) {
    List<ChiTietPhieuNhapDTO> ds = new ArrayList<>();

    String sql = "SELECT * FROM chitiet_phieunhap WHERE MaPhieuNhap = ?";

    try (
        Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)
    ) {
        stmt.setString(1, maPN);
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String maPhieuNhap = rs.getString("MaPhieuNhap");
                String maQuat = rs.getString("MaQuat");
                int soLuong = rs.getInt("SoLuong");
                int donGia = rs.getInt("DonGia");
                int thanhTien = rs.getInt("ThanhTien");

                ChiTietPhieuNhapDTO ct = new ChiTietPhieuNhapDTO(maPhieuNhap, maQuat, soLuong, donGia);
                ds.add(ct);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return ds;
}

    public static boolean themChiTietPhieuNhap(ChiTietPhieuNhapDTO ct) {
    String sql = "INSERT INTO chitiet_phieunhap (MaPhieuNhap, MaQuat, SoLuong, DonGia, ThanhTien) VALUES (?, ?, ?, ?, ?)";
    Connection conn = null;
    PreparedStatement stmt = null;
    
    try {
        conn = DBConnection.getConnection();
        conn.setAutoCommit(false);  // Tắt auto-commit
        
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, ct.getMaPhieuNhap());
        stmt.setString(2, ct.getMaQuat());
        stmt.setInt(3, ct.getSoLuong());
        stmt.setInt(4, ct.getDonGia());
        stmt.setInt(5, ct.getThanhTien());
        
        int rowsAffected = stmt.executeUpdate();
        
        // Kiểm tra xem có thêm được bản ghi không
        if (rowsAffected > 0) {
            conn.commit();  // Commit chỉ khi thành công
            System.out.println("Thêm chi tiết phiếu nhập thành công. Rows affected: " + rowsAffected);
            return true;
        } else {
            conn.rollback();  // Rollback nếu không thêm được
            System.out.println("Không thêm được chi tiết phiếu nhập. Rows affected: " + rowsAffected);
            return false;
        }
    } catch (SQLException e) {
        System.out.println("Lỗi SQL khi thêm chi tiết phiếu nhập: " + e.getMessage());
        e.printStackTrace();
        if (conn != null) {
            try {
                conn.rollback();  // Rollback nếu có lỗi
                System.out.println("Đã rollback transaction");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    } finally {
        // Đóng tất cả các resource
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.setAutoCommit(true);  // Reset lại auto-commit
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
    
    
}
