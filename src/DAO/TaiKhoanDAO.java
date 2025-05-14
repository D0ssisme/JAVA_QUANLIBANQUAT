
package DAO;

import DTO.DBConnection;
import DTO.TaiKhoanDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp thực hiện các thao tác CRUD (Create, Read, Update, Delete) 
 */
public class TaiKhoanDAO {
    /**
     * Lấy tất cả tài khoản từ CSDL
     * 
     * @return Danh sách tất cả tài khoản
     */
    public List<TaiKhoanDTO> getAll() throws SQLException {
        List<TaiKhoanDTO> list = new ArrayList<>();
        String sql = "SELECT MaTaiKhoan, MaNhanVien, TenTaiKhoan, MatKhau, VaiTro FROM taikhoan";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new TaiKhoanDTO(
                    rs.getString("MaTaiKhoan"),
                    rs.getString("MaNhanVien"),
                    rs.getString("TenTaiKhoan"),
                    rs.getString("MatKhau"),
                    rs.getString("VaiTro")
                ));
            }
        }
        return list;
    }

    /**
     * Lấy tài khoản theo mã tài khoản
     * 
     * @param ma Mã tài khoản cần tìm
     * @return Đối tượng TaiKhoanDTO nếu tìm thấy, null nếu không tìm thấy
     */
    public TaiKhoanDTO getByMa(String ma) throws SQLException {
        String sql = "SELECT MaTaiKhoan, MaNhanVien, TenTaiKhoan, MatKhau, VaiTro FROM taikhoan WHERE MaTaiKhoan=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ma);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TaiKhoanDTO(
                        rs.getString("MaTaiKhoan"),
                        rs.getString("MaNhanVien"),
                        rs.getString("TenTaiKhoan"),
                        rs.getString("MatKhau"),
                        rs.getString("VaiTro")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Lấy tài khoản theo tên đăng nhập
     * Dùng để kiểm tra tài khoản đã tồn tại hay chưa khi thêm mới
     * 
     * @param user Tên đăng nhập cần kiểm tra
     * @return Đối tượng TaiKhoanDTO nếu tìm thấy, null nếu không tìm thấy
     */
    public TaiKhoanDTO getByUsername(String user) throws SQLException {
        String sql = "SELECT MaTaiKhoan, MaNhanVien, TenTaiKhoan, MatKhau, VaiTro FROM taikhoan WHERE TenTaiKhoan=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TaiKhoanDTO(
                        rs.getString("MaTaiKhoan"),
                        rs.getString("MaNhanVien"),
                        rs.getString("TenTaiKhoan"),
                        rs.getString("MatKhau"),
                        rs.getString("VaiTro")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Thêm tài khoản mới vào CSDL
     * 
     * @param tk Đối tượng tài khoản cần thêm
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean insert(TaiKhoanDTO tk) throws SQLException {
        String sql = "INSERT INTO taikhoan (MaTaiKhoan, TenTaiKhoan, MatKhau, VaiTro, MaNhanVien) VALUES (?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tk.getMaTaiKhoan());
            ps.setString(2, tk.getUsername());
            ps.setString(3, tk.getPassword());
            ps.setString(4, tk.getVaiTro());
            ps.setString(5, tk.getMaNhanVien());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật thông tin tài khoản
     * 
     * @param tk Đối tượng tài khoản với thông tin đã cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean update(TaiKhoanDTO tk) throws SQLException {
        String sql = "UPDATE taikhoan SET TenTaiKhoan=?, MatKhau=?, VaiTro=?, MaNhanVien=? WHERE MaTaiKhoan=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tk.getUsername());
            ps.setString(2, tk.getPassword());
            ps.setString(3, tk.getVaiTro());
            ps.setString(4, tk.getMaNhanVien());
            ps.setString(5, tk.getMaTaiKhoan());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Xóa tài khoản khỏi CSDL
     * 
     * @param ma Mã tài khoản cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean delete(String ma) throws SQLException {
        String sql = "DELETE FROM taikhoan WHERE MaTaiKhoan=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ma);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Lấy danh sách mã nhân viên chưa có tài khoản
     * Dùng cho dialog Thêm tài khoản
     * 
     * @return Danh sách mã nhân viên chưa có tài khoản
     */
    public List<String> getNhanVienWithoutAccount() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT nv.MaNhanVien FROM nhanvien nv "
                   + "LEFT JOIN taikhoan tk ON nv.MaNhanVien = tk.MaNhanVien "
                   + "WHERE tk.MaTaiKhoan IS NULL";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(rs.getString("MaNhanVien"));
        }
        return list;
    }

    /**
     * Lấy danh sách mã nhân viên đã có tài khoản
     * Dùng cho dialog Chỉnh sửa tài khoản
     * 
     * @return Danh sách mã nhân viên đã có tài khoản
     */
    public List<String> getNhanVienWithAccount() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT MaNhanVien FROM taikhoan WHERE MaTaiKhoan <> 'TK000' AND MaNhanVien IS NOT NULL";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(rs.getString("MaNhanVien"));
        }
        return list;
    }
    
    /**
     * Lấy mã nhân viên theo mã tài khoản
     * 
     * @param maTaiKhoan Mã tài khoản cần tìm
     * @return Mã nhân viên, null nếu không tìm thấy
     */
    public String getMaNVByMaTaiKhoan(String maTaiKhoan) {
        String sql = "SELECT MaNhanVien FROM taikhoan WHERE MaTaiKhoan = ?";
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, maTaiKhoan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("MaNhanVien");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy
    }
}