package DAO;

import java.sql.*;
import java.util.*;
import DTO.ThongKeDTO;
import DTO.DBConnection;
import DTO.ThongKe.ThongKeKhachHangTheoNamDTO;
import DTO.ThongKe.ThongKeKhachHangTheoNgayDTO;
import DTO.ThongKe.ThongKeKhachHangTheoThangDTO;
import DTO.ThongKe.ThongKeLoaiQuatTheoNamDTO;
import DTO.ThongKe.ThongKeLoaiQuatTheoThangDTO;
import DTO.ThongKe.ThongKeQuatTheoNamDTO;
import DTO.ThongKe.ThongKeQuatTheoNgayDTO;
import DTO.ThongKe.ThongKeQuatTheoThangDTO;

public class ThongKeDAO {

    private Connection conn;

    public ThongKeDAO() throws SQLException {
        this.conn = DBConnection.getConnection();
    }
    public List<ThongKeLoaiQuatTheoNamDTO> thongKeLoaiQuatTheoKhoangNam(int namBatDau, int namKetThuc) {
        List<ThongKeLoaiQuatTheoNamDTO> dsThongKe = new ArrayList<>();
        String sql = "SELECT lsp.MaLoaiSanPham, lsp.TenLoai, " +
                     "SUM(cthd.SoLuong) AS SoLuongBan, SUM(cthd.ThanhTien) AS TongTien " +
                     "FROM hoadon hd " +
                     "JOIN chitiet_hoadon cthd ON hd.MaHoaDon = cthd.MaHoaDon " +
                     "JOIN quat q ON q.MaQuat = cthd.MaQuat " +
                     "JOIN loaisanpham lsp ON q.MaLoaiSP = lsp.MaLoaiSanPham " +
                     "WHERE YEAR(hd.NgayLap) BETWEEN ? AND ? " +
                     "GROUP BY lsp.MaLoaiSanPham, lsp.TenLoai";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, namBatDau);
            stmt.setInt(2, namKetThuc);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String maLoai = rs.getString("MaLoaiSanPham");
                String tenLoai = rs.getString("TenLoai");
                int soLuongBan = rs.getInt("SoLuongBan");
                double tongTien = rs.getDouble("TongTien");

                ThongKeLoaiQuatTheoNamDTO tk = new ThongKeLoaiQuatTheoNamDTO(maLoai, tenLoai, soLuongBan, tongTien);
                dsThongKe.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsThongKe;
    }
    public List<ThongKeLoaiQuatTheoThangDTO> thongKeLoaiQuatTheoThang(int thang, int nam) {
        List<ThongKeLoaiQuatTheoThangDTO> dsThongKe = new ArrayList<>();
        String sql = "SELECT lsp.MaLoaiSanPham, lsp.TenLoai, " +
                     "SUM(cthd.SoLuong) AS SoLuongBan, SUM(cthd.ThanhTien) AS TongTien " +
                     "FROM hoadon hd " +
                     "JOIN chitiet_hoadon cthd ON hd.MaHoaDon = cthd.MaHoaDon " +
                     "JOIN quat q ON q.MaQuat = cthd.MaQuat " +
                     "JOIN loaisanpham lsp ON q.MaLoaiSP = lsp.MaLoaiSanPham " +
                     "WHERE YEAR(hd.NgayLap) = ? AND MONTH(hd.NgayLap) = ? " +
                     "GROUP BY lsp.MaLoaiSanPham, lsp.TenLoai " +
                     "ORDER BY lsp.MaLoaiSanPham";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, nam);
            stmt.setInt(2, thang);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String maLoai = rs.getString("MaLoaiSanPham");
                String tenLoai = rs.getString("TenLoai");
                int soLuongBan = rs.getInt("SoLuongBan");
                double tongTien = rs.getDouble("TongTien");

                ThongKeLoaiQuatTheoThangDTO tk = new ThongKeLoaiQuatTheoThangDTO(maLoai, tenLoai, thang, soLuongBan, tongTien);
                dsThongKe.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsThongKe;
    }
    public List<ThongKeQuatTheoNamDTO> thongKeQuatTheoKhoangNam(int namBatDau, int namKetThuc) {
        List<ThongKeQuatTheoNamDTO> dsThongKe = new ArrayList<>();
        String sql = "SELECT q.MaQuat, q.TenQuat, " +
                     "SUM(cthd.SoLuong) AS SoLuongBan, SUM(cthd.ThanhTien) AS TongTien " +
                     "FROM hoadon hd " +
                     "JOIN chitiet_hoadon cthd ON hd.MaHoaDon = cthd.MaHoaDon " +
                     "JOIN quat q ON q.MaQuat = cthd.MaQuat " +
                     "WHERE YEAR(hd.NgayLap) BETWEEN ? AND ? " +
                     "GROUP BY q.MaQuat, q.TenQuat";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, namBatDau);
            stmt.setInt(2, namKetThuc);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String maQuat = rs.getString("MaQuat");
                String tenQuat = rs.getString("TenQuat");
                int soLuongBan = rs.getInt("SoLuongBan");
                double tongTien = rs.getDouble("TongTien");

                ThongKeQuatTheoNamDTO tk = new ThongKeQuatTheoNamDTO(maQuat, tenQuat, soLuongBan, tongTien);
                dsThongKe.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsThongKe;
    }
    public List<ThongKeQuatTheoThangDTO> thongKeQuatTheoThang(int thang, int nam) {
        List<ThongKeQuatTheoThangDTO> dsThongKe = new ArrayList<>();
        String sql = "SELECT q.MaQuat, q.TenQuat, " +
                     "SUM(cthd.SoLuong) AS SoLuongBan, SUM(cthd.ThanhTien) AS TongTien " +
                     "FROM hoadon hd " +
                     "JOIN chitiet_hoadon cthd ON hd.MaHoaDon = cthd.MaHoaDon " +
                     "JOIN quat q ON q.MaQuat = cthd.MaQuat " +
                     "WHERE YEAR(hd.NgayLap) = ? AND MONTH(hd.NgayLap) = ? " +
                     "GROUP BY q.MaQuat, q.TenQuat " +
                     "ORDER BY q.MaQuat";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, nam);
            stmt.setInt(2, thang);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String maQuat = rs.getString("MaQuat");
                String tenQuat = rs.getString("TenQuat");
                int soLuongBan = rs.getInt("SoLuongBan");
                double tongTien = rs.getDouble("TongTien");

                ThongKeQuatTheoThangDTO tk = new ThongKeQuatTheoThangDTO(maQuat, tenQuat, thang, soLuongBan, tongTien);
                dsThongKe.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsThongKe;
    }
    public List<ThongKeQuatTheoNgayDTO> thongKeQuatTheoNgay(String ngayBD, String ngayKT) {
        List<ThongKeQuatTheoNgayDTO> dsThongKe = new ArrayList<>();
        String sql = "SELECT q.MaQuat, q.TenQuat, " +
                     "SUM(cthd.SoLuong) AS SoLuongBan, SUM(cthd.ThanhTien) AS TongTien " +
                     "FROM hoadon hd " +
                     "JOIN chitiet_hoadon cthd ON hd.MaHoaDon = cthd.MaHoaDon " +
                     "JOIN quat q ON q.MaQuat = cthd.MaQuat " +
                     "WHERE hd.NgayLap BETWEEN ? AND ? " +
                     "GROUP BY q.MaQuat, q.TenQuat " +
                     "ORDER BY q.MaQuat";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ngayBD);
            stmt.setString(2, ngayKT);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String maQuat = rs.getString("MaQuat");
                String tenQuat = rs.getString("TenQuat");
                String ngay = ngayBD + " đến " + ngayKT; 
                int soLuongBan = rs.getInt("SoLuongBan");
                double tongTien = rs.getDouble("TongTien");
                ThongKeQuatTheoNgayDTO tk = new ThongKeQuatTheoNgayDTO(maQuat, tenQuat, ngay, soLuongBan, tongTien);
                dsThongKe.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsThongKe;
    }
     public List<ThongKeKhachHangTheoNamDTO> thongKeKhachHangTheoKhoangNam(int namBatDau, int namKetThuc) {
        List<ThongKeKhachHangTheoNamDTO> dsThongKe = new ArrayList<>();
        String sql = "SELECT kh.MaKhachHang, kh.HoTenKH, COUNT(hd.MaHoaDon) AS SoLanMua, " +
                     "SUM(cthd.ThanhTien) AS TongTienDaMua " +
                     "FROM khachhang kh " +
                     "JOIN hoadon hd ON kh.MaKhachHang = hd.MaKhachHang " +
                     "JOIN chitiet_hoadon cthd ON hd.MaHoaDon = cthd.MaHoaDon " +
                     "WHERE YEAR(hd.NgayLap) BETWEEN ? AND ? " +
                     "GROUP BY kh.MaKhachHang, kh.HoTenKH";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, namBatDau);
            stmt.setInt(2, namKetThuc);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String maKhachHang = rs.getString("MaKhachHang");
                String hoTenKH = rs.getString("HoTenKH");
                int soLanMua = rs.getInt("SoLanMua");
                double tongTienDaMua = rs.getDouble("TongTienDaMua");

                ThongKeKhachHangTheoNamDTO tk = new ThongKeKhachHangTheoNamDTO(maKhachHang, hoTenKH, soLanMua, tongTienDaMua);
                dsThongKe.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsThongKe;
    }
      public List<ThongKeKhachHangTheoThangDTO> thongKeKhachHangTheoThang(int thang, int nam) {
        List<ThongKeKhachHangTheoThangDTO> dsThongKe = new ArrayList<>();
        String sql = "SELECT kh.MaKhachHang, kh.HoTenKH, COUNT(hd.MaHoaDon) AS SoLanMua, " +
                     "SUM(cthd.ThanhTien) AS TongTienDaMua " +
                     "FROM khachhang kh " +
                     "JOIN hoadon hd ON kh.MaKhachHang = hd.MaKhachHang " +
                     "JOIN chitiet_hoadon cthd ON hd.MaHoaDon = cthd.MaHoaDon " +
                     "WHERE MONTH(hd.NgayLap) = ? AND YEAR(hd.NgayLap) = ? " +
                     "GROUP BY kh.MaKhachHang, kh.HoTenKH";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, thang);
            stmt.setInt(2, nam);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String maKhachHang = rs.getString("MaKhachHang");
                String hoTenKH = rs.getString("HoTenKH");
                int soLanMua = rs.getInt("SoLanMua");
                double tongTienDaMua = rs.getDouble("TongTienDaMua");

                ThongKeKhachHangTheoThangDTO tk = new ThongKeKhachHangTheoThangDTO(maKhachHang, hoTenKH, soLanMua, tongTienDaMua);
                dsThongKe.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsThongKe;
    }


    public List<ThongKeKhachHangTheoNgayDTO> thongKeKhachHangTheoKhoangNgay(String ngayBatDau, String ngayKetThuc) {
        List<ThongKeKhachHangTheoNgayDTO> dsThongKe = new ArrayList<>();
        String sql = "SELECT kh.MaKhachHang, kh.HoTenKH, COUNT(hd.MaHoaDon) AS SoLanMua, " +
                     "SUM(cthd.ThanhTien) AS TongTienDaMua " +
                     "FROM khachhang kh " +
                     "JOIN hoadon hd ON kh.MaKhachHang = hd.MaKhachHang " +
                     "JOIN chitiet_hoadon cthd ON hd.MaHoaDon = cthd.MaHoaDon " +
                     "WHERE hd.NgayLap BETWEEN ? AND ? " +
                     "GROUP BY kh.MaKhachHang, kh.HoTenKH";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ngayBatDau);
            stmt.setString(2, ngayKetThuc);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String maKhachHang = rs.getString("MaKhachHang");
                String hoTenKH = rs.getString("HoTenKH");
                int soLanMua = rs.getInt("SoLanMua");
                double tongTienDaMua = rs.getDouble("TongTienDaMua");

                ThongKeKhachHangTheoNgayDTO tk = new ThongKeKhachHangTheoNgayDTO(maKhachHang, hoTenKH, soLanMua, tongTienDaMua);
                dsThongKe.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsThongKe;
    }


}
