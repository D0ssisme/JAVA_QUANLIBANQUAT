package DAO;

import DTO.NhaCungCapDTO;
import DTO.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhaCungCapDAO {

    public List<NhaCungCapDTO> getAll() throws SQLException {
        List<NhaCungCapDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM nha_cung_cap";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new NhaCungCapDTO(
                        rs.getString("MaNCC"),
                        rs.getString("TenNCC"),
                        rs.getString("DiaChiNCC"),
                        rs.getString("Sdt_NCC")
                ));
            }
        }
        return list;
    }

    public void insert(NhaCungCapDTO ncc) throws SQLException {
        String sql = "INSERT INTO nha_cung_cap (MaNCC, TenNCC, DiaChiNCC, Sdt_NCC) VALUES (?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, ncc.getMaNCC());
            ps.setString(2, ncc.getTenNCC());
            ps.setString(3, ncc.getDiaChiNCC());
            ps.setString(4, ncc.getSdtNCC());
            ps.executeUpdate();
        }
    }

    public void delete(String maNCC) throws SQLException {
        String sql = "DELETE FROM nha_cung_cap WHERE MaNCC = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, maNCC);
            ps.executeUpdate();
        }
    }

    public void update(NhaCungCapDTO ncc) throws SQLException {
        String sql = "UPDATE nha_cung_cap SET TenNCC = ?, DiaChiNCC = ?, Sdt_NCC = ? WHERE MaNCC = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, ncc.getTenNCC());
            ps.setString(2, ncc.getDiaChiNCC());
            ps.setString(3, ncc.getSdtNCC());
            ps.setString(4, ncc.getMaNCC());
            ps.executeUpdate();
        }
    }

    public NhaCungCapDTO findByMaNCC(String maNCC) throws SQLException {
        String sql = "SELECT * FROM nha_cung_cap WHERE MaNCC = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, maNCC);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new NhaCungCapDTO(
                            rs.getString("MaNCC"),
                            rs.getString("TenNCC"),
                            rs.getString("DiaChiNCC"),
                            rs.getString("Sdt_NCC")
                    );
                }
            }
        }
        return null;
    }
    
    public String layTenNhaCungCapTheoMa(String maNCC)
    {
        String tenncc="";
        String sql="SELECT TenNCC From nha_cung_cap WHERE MaNCC=?";
        try(Connection conn=DBConnection.getConnection();
        PreparedStatement ps =conn.prepareStatement(sql))
        {
            ps.setString(1,maNCC);
            ResultSet rs=ps.executeQuery();
            while(rs.next())
            {
                tenncc=rs.getString("TenNCC");
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return tenncc;
       
            
        }
        
    
    public static String layMaNhaCungCapTheoTen(String tenNCC) {
    String maNCC = null;
    String sql = "SELECT MaNCC FROM nha_cung_cap WHERE TenNCC = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, tenNCC);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            maNCC = rs.getString("MaNCC");
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return maNCC;
}

    public List<NhaCungCapDTO> findByTenNCC(String keyword) throws SQLException {
        List<NhaCungCapDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM nha_cung_cap WHERE TenNCC LIKE ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new NhaCungCapDTO(
                            rs.getString("MaNCC"),
                            rs.getString("TenNCC"),
                            rs.getString("DiaChiNCC"),
                            rs.getString("Sdt_NCC")
                    ));
                }
            }
        }
        return list;
    }
}
