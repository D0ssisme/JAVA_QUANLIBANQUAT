package DAO;

import DTO.HoaDonDTO;
import DTO.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {

    public static int insert(HoaDonDTO hd) {
        int result = 0;
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "INSERT INTO hoadon (MaHoaDon, MaKhachHang, MaNhanVien, NgayLap, MaSuKienKM, TongTien) VALUES (?, ?, ?, ?, ?, ?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, hd.getMaHoaDon());
            pst.setString(2, hd.getMaKhachHang());
            pst.setString(3, hd.getMaNhanVien());
            pst.setDate(4, hd.getNgayLap());
            pst.setString(5, hd.getMaSuKienKM());
            pst.setInt(6, hd.getTongTien());

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

    public static int update(HoaDonDTO hd) {
        int result = 0;
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "UPDATE hoadon SET MaKhachHang=?, MaNhanVien=?, NgayLap=?, MaSuKienKM=?, TongTien=? WHERE MaHoaDon=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, hd.getMaKhachHang());
            pst.setString(2, hd.getMaNhanVien());
            pst.setDate(3, hd.getNgayLap());
            pst.setString(4, hd.getMaSuKienKM());
            pst.setInt(5, hd.getTongTien());
            pst.setString(6, hd.getMaHoaDon());

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

    public static int delete(String maHoaDon) {
        int result = 0;
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "DELETE FROM hoadon WHERE MaHoaDon=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, maHoaDon);
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

    public static HoaDonDTO selectById(String maHoaDon) {
        HoaDonDTO hd = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM hoadon WHERE MaHoaDon=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, maHoaDon);
            rs = pst.executeQuery();
            if (rs.next()) {
                hd = new HoaDonDTO(
                    rs.getString("MaHoaDon"),
                    rs.getString("MaKhachHang"),
                    rs.getString("MaNhanVien"),
                    rs.getDate("NgayLap"),
                    rs.getString("MaSuKienKM"),
                    rs.getInt("TongTien")
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
        return hd;
    }

    public static List<HoaDonDTO> selectAll() {
        List<HoaDonDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM hoadon";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                HoaDonDTO hd = new HoaDonDTO(
                    rs.getString("MaHoaDon"),
                    rs.getString("MaKhachHang"),
                    rs.getString("MaNhanVien"),
                    rs.getDate("NgayLap"),
                    rs.getString("MaSuKienKM"),
                    rs.getInt("TongTien")
                );
                list.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                DBConnection.closeConnection(); // Sử dụng phương thức mới
            } catch (SQLException ex) {
            }
        }
        return list;
    }
}