package DAO;

import DTO.LoaiSanPhamDTO;
import DTO.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoaiSanPhamDAO {

    public List<LoaiSanPhamDTO> getAll() throws SQLException {
        List<LoaiSanPhamDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM loaisanpham";
        try (Connection c = DBConnection.getConnection(); 
             PreparedStatement ps = c.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new LoaiSanPhamDTO(
                        rs.getString("MaLoaiSanPham"),
                        rs.getString("TenLoai"),
                        rs.getString("TrangThai"),
                        rs.getString("MoTa")
                ));
            }
        }
        return list;
    }

    public void insert(LoaiSanPhamDTO lsp) throws SQLException {
        String sql = "INSERT INTO loaisanpham (MaLoaiSanPham, TenLoai, TrangThai, MoTa) VALUES (?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection(); 
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, lsp.getMaLoaiSanPham());
            ps.setString(2, lsp.getTenLoai());
            ps.setString(3, lsp.getTrangThai());
            ps.setString(4, lsp.getMoTa());
            ps.executeUpdate();
        }
    }

    public void delete(String maLoai) throws SQLException {
        String sql = "DELETE FROM loaisanpham WHERE MaLoaiSanPham = ?";
        try (Connection c = DBConnection.getConnection(); 
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, maLoai);
            ps.executeUpdate();
        }
    }

    public void update(LoaiSanPhamDTO lsp) throws SQLException {
        String sql = "UPDATE loaisanpham SET TenLoai = ?, TrangThai = ?, MoTa = ? WHERE MaLoaiSanPham = ?";
        try (Connection c = DBConnection.getConnection(); 
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, lsp.getTenLoai());
            ps.setString(2, lsp.getTrangThai());
            ps.setString(3, lsp.getMoTa());
            ps.setString(4, lsp.getMaLoaiSanPham());
            ps.executeUpdate();
        }
    }

    public LoaiSanPhamDTO findByMaLoai(String maLoai) throws SQLException {
        String sql = "SELECT * FROM loaisanpham WHERE MaLoaiSanPham = ?";
        try (Connection c = DBConnection.getConnection(); 
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, maLoai);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new LoaiSanPhamDTO(
                            rs.getString("MaLoaiSanPham"),
                            rs.getString("TenLoai"),
                            rs.getString("TrangThai"),
                            rs.getString("MoTa")
                    );
                }
            }
        }
        return null;
    }

    public List<LoaiSanPhamDTO> findByTenLoai(String keyword) throws SQLException {
        List<LoaiSanPhamDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM loaisanpham WHERE TenLoai LIKE ?";
        try (Connection c = DBConnection.getConnection(); 
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new LoaiSanPhamDTO(
                            rs.getString("MaLoaiSanPham"),
                            rs.getString("TenLoai"),
                            rs.getString("TrangThai"),
                            rs.getString("MoTa")
                    ));
                }
            }
        }
        return list;
    }
}
