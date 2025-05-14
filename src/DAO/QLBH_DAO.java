package DAO;

import DTO.QLBH_DTO;
import DTO.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QLBH_DAO {

    public static int insert(QLBH_DTO bh) {
        int result = 0;
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "INSERT INTO quanlibaohanh (MaBaoHanh, MaQuat, MaKhachHang, ThoiGianBaoHanh, TrangThai) VALUES (?, ?, ?, ?, ?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, bh.getMaBaoHanh());
            pst.setString(2, bh.getMaQuat());
            pst.setString(3, bh.getMaKhachHang());
            pst.setDate(4, new Date(bh.getThoiGianBaoHanh().getTime()));
            pst.setString(5, bh.getTrangThai());

            result = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) pst.close();
                DBConnection.closeConnection(); // Sử dụng phương thức mới
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static int update(QLBH_DTO bh) {
        int result = 0;
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "UPDATE quanlibaohanh SET MaQuat=?, MaKhachHang=?, ThoiGianBaoHanh=?, TrangThai=? WHERE MaBaoHanh=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, bh.getMaQuat());
            pst.setString(2, bh.getMaKhachHang());
            pst.setDate(3, new Date(bh.getThoiGianBaoHanh().getTime()));
            pst.setString(4, bh.getTrangThai());
            pst.setString(5, bh.getMaBaoHanh());

            result = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) pst.close();
                DBConnection.closeConnection(); // Sử dụng phương thức mới
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static int delete(String maBaoHanh) {
        int result = 0;
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "DELETE FROM quanlibaohanh WHERE MaBaoHanh=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, maBaoHanh);
            result = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) pst.close();
                DBConnection.closeConnection(); // Sử dụng phương thức mới
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static QLBH_DTO selectById(String maBaoHanh) {
        QLBH_DTO bh = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM quanlibaohanh WHERE MaBaoHanh=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, maBaoHanh);
            rs = pst.executeQuery();
            if (rs.next()) {
                bh = new QLBH_DTO(
                    rs.getString("MaBaoHanh"),
                    rs.getString("MaQuat"),
                    rs.getString("MaKhachHang"),
                    rs.getDate("ThoiGianBaoHanh"),
                    rs.getString("TrangThai")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                DBConnection.closeConnection(); // Sử dụng phương thức mới
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return bh;
    }

    public static List<QLBH_DTO> selectAll() {
        List<QLBH_DTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM quanlibaohanh";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                QLBH_DTO bh = new QLBH_DTO(
                    rs.getString("MaBaoHanh"),
                    rs.getString("MaQuat"),
                    rs.getString("MaKhachHang"),
                    rs.getDate("ThoiGianBaoHanh"),
                    rs.getString("TrangThai")
                );
                list.add(bh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                DBConnection.closeConnection(); // Sử dụng phương thức mới
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return list;
    }
    
    public static List<QLBH_DTO> selectByMaKhachHang(String maKhachHang) {
        List<QLBH_DTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM quanlibaohanh WHERE MaKhachHang=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, maKhachHang);
            rs = pst.executeQuery();
            while (rs.next()) {
                QLBH_DTO bh = new QLBH_DTO(
                    rs.getString("MaBaoHanh"),
                    rs.getString("MaQuat"),
                    rs.getString("MaKhachHang"),
                    rs.getDate("ThoiGianBaoHanh"),
                    rs.getString("TrangThai")
                );
                list.add(bh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                DBConnection.closeConnection(); // Sử dụng phương thức mới
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return list;
    }
    
    public static List<QLBH_DTO> selectByMaQuat(String maQuat) {
        List<QLBH_DTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM quanlibaohanh WHERE MaQuat=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, maQuat);
            rs = pst.executeQuery();
            while (rs.next()) {
                QLBH_DTO bh = new QLBH_DTO(
                    rs.getString("MaBaoHanh"),
                    rs.getString("MaQuat"),
                    rs.getString("MaKhachHang"),
                    rs.getDate("ThoiGianBaoHanh"),
                    rs.getString("TrangThai")
                );
                list.add(bh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                DBConnection.closeConnection(); // Sử dụng phương thức mới
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return list;
    }
    
    public static List<QLBH_DTO> selectByTrangThai(String trangThai) {
        List<QLBH_DTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM quanlibaohanh WHERE TrangThai LIKE ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, "%" + trangThai + "%");
            rs = pst.executeQuery();
            while (rs.next()) {
                QLBH_DTO bh = new QLBH_DTO(
                    rs.getString("MaBaoHanh"),
                    rs.getString("MaQuat"),
                    rs.getString("MaKhachHang"),
                    rs.getDate("ThoiGianBaoHanh"),
                    rs.getString("TrangThai")
                );
                list.add(bh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                DBConnection.closeConnection(); // Sử dụng phương thức mới
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return list;
    }
    
    public static boolean isMaBaoHanhExists(String maBaoHanh) {
        boolean exists = false;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM quanlibaohanh WHERE MaBaoHanh=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, maBaoHanh);
            rs = pst.executeQuery();
            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                DBConnection.closeConnection(); // Sử dụng phương thức mới
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return exists;
    }
}