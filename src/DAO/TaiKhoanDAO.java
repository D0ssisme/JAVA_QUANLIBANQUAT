package DAO;

import DTO.DBConnection;
import DTO.TaiKhoanDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanDAO {
    // Lấy tất cả TK
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

    // Lấy 1 TK theo mã
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

    // Lấy TK theo username (để check trùng)
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

    // Thêm TK
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

    // Cập nhật TK
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

    // Xóa TK
    public boolean delete(String ma) throws SQLException {
        String sql = "DELETE FROM taikhoan WHERE MaTaiKhoan=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ma);
            return ps.executeUpdate() > 0;
        }
    }

    // DS nhân viên chưa có TK (cho dialog Thêm)
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

    // DS nhân viên đã có TK (cho dialog Chỉnh sửa)
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
