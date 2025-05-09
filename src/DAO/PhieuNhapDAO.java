/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO ;

/**
 *
 * @author nguye
 */

import DTO.DBConnection;


import DTO.PhieuNhapDTO;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PhieuNhapDAO {
    
 public static String taoMaPhieuNhapTuDong() {
    String sql = "SELECT MaPhieuNhap FROM PhieuNhap ORDER BY MaPhieuNhap DESC LIMIT 1";
    try (
        Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()
    ) {
        if (rs.next()) {
            String maCu = rs.getString("MaPhieuNhap"); // Ví dụ: PN007
            int so = Integer.parseInt(maCu.substring(2)); // Cắt bỏ 'PN' -> 7
            so++; // Tăng lên 1
            return String.format("PN%03d", so); // -> PN008
        } else {
            return "PN001"; // Nếu chưa có mã nào
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return "PN001"; // Trả về mặc định nếu lỗi
    }
}

    // Lấy danh sách tất cả phiếu nhập
    public static List<PhieuNhapDTO> getAllPhieuNhap() {
        List<PhieuNhapDTO> ds = new ArrayList<>();

        String sql = "SELECT * FROM PhieuNhap";  // hoặc đặt tên bảng đúng theo DB của bạn

        try (
            Connection conn = DBConnection.getConnection();  // bạn cần có class DBConnection
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                String maPhieuNhap = rs.getString("MaPhieuNhap");
                LocalDate ngayNhap = rs.getDate("NgayNhap").toLocalDate();
                String maNCC = rs.getString("MaNCC");
                String maNV = rs.getString("MaNhanVien");
                int tongTien = rs.getInt("TongTien");

                PhieuNhapDTO pn = new PhieuNhapDTO(maPhieuNhap, ngayNhap, maNCC, maNV, tongTien);
                ds.add(pn);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ds;
    }
    
    // Thêm một phiếu nhập mới
public static boolean themPhieuNhap(PhieuNhapDTO pn) {
    String sql = "INSERT INTO PhieuNhap (MaPhieuNhap, NgayNhap, MaNCC, MaNhanVien, TongTien) VALUES (?, ?, ?, ?, ?)";

    try (
        Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)
    ) {
        stmt.setString(1, pn.getMaPhieuNhap());
        stmt.setDate(2, Date.valueOf(pn.getNgayNhap())); // LocalDate -> java.sql.Date
        stmt.setString(3, pn.getMaNCC());
        stmt.setString(4, pn.getMaNhanVien());
        stmt.setInt(5, pn.getTongTien());

        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

public static PhieuNhapDTO findPhieuNhapFromMaPN(String maPN) {
    String sql = "SELECT * FROM PhieuNhap WHERE MaPhieuNhap = ?";
    
    try (
        Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)
    ) {
        stmt.setString(1, maPN);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String maPhieuNhap = rs.getString("MaPhieuNhap");
            LocalDate ngayNhap = rs.getDate("NgayNhap").toLocalDate();
            String maNCC = rs.getString("MaNCC");
            String maNhanVien = rs.getString("MaNhanVien");
            int tongTien = rs.getInt("TongTien");

            return new PhieuNhapDTO(maPhieuNhap, ngayNhap, maNCC, maNhanVien, tongTien);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return null; // Nếu không tìm thấy
}


        

   
}

