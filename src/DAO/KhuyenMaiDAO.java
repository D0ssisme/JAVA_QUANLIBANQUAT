package DAO;

import DTO.DBConnection;
import DTO.KhuyenMaiDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho bảng su_kien_khuyen_mai
 */
public class KhuyenMaiDAO {

    /**
     * Lấy toàn bộ danh sách khuyến mãi
     */
    public static List<KhuyenMaiDTO> selectAll() {
        List<KhuyenMaiDTO> list = new ArrayList<>();
        String sql = "SELECT MaSKKhuyenMai, TenKhuyenMai, PhanTramGiam, NgayBatDau, NgayKetThuc, Loai FROM su_kien_khuyen_mai";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                KhuyenMaiDTO km = new KhuyenMaiDTO(
                        rs.getString("MaSKKhuyenMai"),
                        rs.getString("TenKhuyenMai"),
                        rs.getInt("PhanTramGiam"),
                        rs.getDate("NgayBatDau"),
                        rs.getDate("NgayKetThuc"),
                        rs.getInt("Loai")
                );
                list.add(km);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy khuyến mãi theo mã
     */
    public static KhuyenMaiDTO selectById(String maKM) {
        String sql = "SELECT TenKhuyenMai, PhanTramGiam, NgayBatDau, NgayKetThuc, Loai "
                + "FROM su_kien_khuyen_mai WHERE MaSKKhuyenMai = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKM);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new KhuyenMaiDTO(
                            maKM,
                            rs.getString("TenKhuyenMai"),
                            rs.getInt("PhanTramGiam"),
                            rs.getDate("NgayBatDau"),
                            rs.getDate("NgayKetThuc"),
                            rs.getInt("Loai")
                    );
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Thêm mới khuyến mãi
     */
    public static boolean insert(KhuyenMaiDTO km) {
        String sql = "INSERT INTO su_kien_khuyen_mai "
                + "(MaSKKhuyenMai, TenKhuyenMai, PhanTramGiam, NgayBatDau, NgayKetThuc, Loai) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, km.getMaSKKhuyenMai());
            ps.setString(2, km.getTenKhuyenMai());
            ps.setInt(3, km.getPhanTramGiam());
            ps.setDate(4, new java.sql.Date(km.getNgayBatDau().getTime()));
            ps.setDate(5, new java.sql.Date(km.getNgayKetThuc().getTime()));
            ps.setInt(6, km.getLoai());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean update(KhuyenMaiDTO km) {
        String sql = "UPDATE su_kien_khuyen_mai SET "
                + "TenKhuyenMai = ?, PhanTramGiam = ?, NgayBatDau = ?, NgayKetThuc = ?, Loai = ? "
                + "WHERE MaSKKhuyenMai = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, km.getTenKhuyenMai());
            ps.setInt(2, km.getPhanTramGiam());
            ps.setDate(3, new java.sql.Date(km.getNgayBatDau().getTime()));
            ps.setDate(4, new java.sql.Date(km.getNgayKetThuc().getTime()));
            ps.setInt(5, km.getLoai());
            ps.setString(6, km.getMaSKKhuyenMai());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa khuyến mãi theo mã
     */
    public static boolean delete(String maKM) {
        String sql = "DELETE FROM su_kien_khuyen_mai WHERE MaSKKhuyenMai = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKM);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        // Test selectAll
        selectAll().forEach(System.out::println);
    }
}
