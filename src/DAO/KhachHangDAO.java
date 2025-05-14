package DAO;

import DTO.DBConnection;
import DTO.KhachHangDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho bảng khachhang
 */
public class KhachHangDAO {

    /**
     * Lấy toàn bộ danh sách khách hàng
     */
    public static List<KhachHangDTO> selectAll() {
        List<KhachHangDTO> list = new ArrayList<>();
        String sql = "SELECT MaKhachHang, HoTenKH, Sdt_KH, DiaChiKH, TongTienDaMua, TrangThai FROM khachhang";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                KhachHangDTO kh = new KhachHangDTO(
                        rs.getString("MaKhachHang"),
                        rs.getString("HoTenKH"),
                        rs.getString("Sdt_KH"),
                        rs.getString("DiaChiKH"),
                        rs.getInt("TongTienDaMua"),
                        rs.getInt("TrangThai")
                );
                list.add(kh);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy khách hàng theo mã
     */
    public KhachHangDTO selectById(String maKh) {
        String sql = "SELECT HoTenKH, Sdt_KH, DiaChiKH, TongTienDaMua, TrangThai FROM khachhang WHERE MaKhachHang = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKh);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new KhachHangDTO(
                            maKh,
                            rs.getString("HoTenKH"),
                            rs.getString("Sdt_KH"),
                            rs.getString("DiaChiKH"),
                            rs.getInt("TongTienDaMua"),
                            rs.getInt("TrangThai")
                    );
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean exists(String maKh) {
        String sql = "SELECT 1 FROM khachhang WHERE MaKhachHang = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKh);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Thêm mới khách hàng
     */
    public boolean insert(KhachHangDTO kh) {
        String sql = "INSERT INTO khachhang (MaKhachHang, HoTenKH, Sdt_KH, DiaChiKH, TongTienDaMua, TrangThai) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getMaKhachHang());
            ps.setString(2, kh.getHoTenKH());
            ps.setString(3, kh.getSdtKH());
            ps.setString(4, kh.getDiaChiKH());
            ps.setInt(5, kh.getTongTienDaMua());
            ps.setInt(6, kh.getTrangThai());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật khách hàng
     */
    public boolean update(KhachHangDTO kh) {
        String sql = "UPDATE khachhang SET HoTenKH = ?, Sdt_KH = ?, DiaChiKH = ?, TongTienDaMua = ?, TrangThai = ? WHERE MaKhachHang = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getHoTenKH());      // Tham số 1: HoTenKH
            ps.setString(2, kh.getSdtKH());        // Tham số 2: Sdt_KH
            ps.setString(3, kh.getDiaChiKH());     // Tham số 3: DiaChiKH
            ps.setInt(4, kh.getTongTienDaMua());   // Tham số 4: TongTienDaMua
            ps.setInt(5, kh.getTrangThai());       // Tham số 5: TrangThai
            ps.setString(6, kh.getMaKhachHang());  // Tham số 6: WHERE MaKhachHang
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa khách hàng theo mã
     */
    /**
     * Soft-delete khách hàng (chuyển trạng thái từ 1 → 0)
     */
    public boolean delete(String maKh) {
        String sql = "DELETE FROM khachhang WHERE MaKhachHang = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKh);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Khôi phục khách hàng (chuyển trạng thái từ 0 → 1)
     */
    public boolean restore(String maKh) {
        String sql = "UPDATE khachhang SET TrangThai = 1 WHERE MaKhachHang = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKh);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật chỉ cột TrangThai
     */
    public boolean updateStatus(String maKh, int trangThai) {
        String sql = "UPDATE khachhang SET TrangThai = ? WHERE MaKhachHang = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, trangThai);
            ps.setString(2, maKh);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        KhachHangDAO dao = new KhachHangDAO();
        // Test selectAll
        dao.selectAll().forEach(System.out::println);
    }
}
