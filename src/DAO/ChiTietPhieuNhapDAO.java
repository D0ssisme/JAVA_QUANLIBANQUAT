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

  public boolean isChiTietExist(String maPN, String maQuat) {
    String sql = "SELECT * FROM chitiet_phieunhap WHERE MaPhieuNhap = ? AND MaQuat = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, maPN);
        ps.setString(2, maQuat);
        ResultSet rs = ps.executeQuery();
        return rs.next(); // Nếu có dữ liệu thì tồn tại

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    
  public boolean updateSoluong(String mapn, int soLuong,String maQuat) {
    String sql = "UPDATE chitiet_phieunhap SET SoLuong = ? WHERE MaPhieuNhap = ? AND MaQuat=?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, soLuong);
        ps.setString(2, mapn);
        ps.setString(3, maQuat);

        int rowsAffected = ps.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("✅ Cập nhật số lượng thành công (" + rowsAffected + " dòng).");
            return true;
            
        } else {
            System.out.println("⚠️ Không tìm thấy dòng nào để cập nhật.");
            return false;
        }
    

    } catch (SQLException e) {
        System.err.println("❌ Lỗi khi cập nhật số lượng:");
        e.printStackTrace();
        return false;
    }
}
       
    
    

    public static List<ChiTietPhieuNhapDTO> getAllChiTietPhieuNhap() {
        List<ChiTietPhieuNhapDTO> ds = new ArrayList<>();

        String sql = "SELECT * FROM chitiet_phieunhap"; 

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
    public ChiTietPhieuNhapDTO getChiTietPhieuNhapbyMaQuat(String maPN,String maQuat )
    {
        String sql="SELECT * FROM chitiet_phieunhap WHERE MaPhieuNhap=? AND MaQuat=?";
        try(Connection conn=DBConnection.getConnection();
        PreparedStatement ps=conn.prepareStatement(sql))
        {
            ps.setString(1, maPN);
            ps.setString(2, maQuat);
            ResultSet rs= ps.executeQuery();
            while(rs.next())
            {
                String Mapn=rs.getString("MaPhieuNhap");
                String MaQuat=rs.getString("MaQuat");
                int soluong=rs.getInt("SoLuong");
                int dongia=rs.getInt("DonGia");
                ChiTietPhieuNhapDTO ctpn=new ChiTietPhieuNhapDTO(Mapn,MaQuat,soluong,dongia);
                return ctpn;
            }
            
            
        }
        catch(SQLException e)
        {
            e.printStackTrace();
    }
        return null;
    }
        
    
    
    
   public  List<ChiTietPhieuNhapDTO> getChiTietPhieuNhapByMaPN(String maPN) {
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

    public  boolean themChiTietPhieuNhap(ChiTietPhieuNhapDTO ct) {
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
        
       
        if (rowsAffected > 0) {
            conn.commit();  
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
                conn.rollback();  
                System.out.println("Đã rollback transaction");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    } finally {

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.setAutoCommit(true);  
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
    
    
}
