package DAO;

import DTO.ChiTietHoaDonDTO;
import DTO.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for managing ChiTietHoaDon (invoice details) entities.
 * Provides methods to create, read, update and delete operations on the
 * database.
 */
public class ChiTietHoaDonDAO {

    /**
     * Inserts a new ChiTietHoaDon record into the database.
     *
     * @param ctHD The ChiTietHoaDonDTO object to insert
     * @return Number of rows affected (1 if successful, 0 if failed)
     */
    public static int insert(ChiTietHoaDonDTO ctHD) {
        int result = 0;

        try (
                Connection conn = DBConnection.getConnection(); PreparedStatement pst = conn.prepareStatement(
                "INSERT INTO chitiet_hoadon (MaHoaDon, MaQuat, SoLuong, DonGia, ThanhTien, MaBaoHanh) VALUES (?, ?, ?, ?, ?, ?)")) {
            pst.setString(1, ctHD.getMaHoaDon());
            pst.setString(2, ctHD.getMaQuat());
            pst.setInt(3, ctHD.getSoLuong());
            pst.setInt(4, ctHD.getDonGia());
            pst.setInt(5, ctHD.getThanhTien());
            pst.setString(6, ctHD.getMaBaoHanh());

            result = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Updates an existing ChiTietHoaDon record in the database.
     *
     * @param ctHD The ChiTietHoaDonDTO object with updated values
     * @return Number of rows affected (1 if successful, 0 if failed)
     */
    public static int update(ChiTietHoaDonDTO ctHD) {
        int result = 0;

        try (
                Connection conn = DBConnection.getConnection(); PreparedStatement pst = conn.prepareStatement(
                "UPDATE chitiet_hoadon SET SoLuong=?, DonGia=?, ThanhTien=?, MaBaoHanh=? WHERE MaHoaDon=? AND MaQuat=?")) {
            pst.setInt(1, ctHD.getSoLuong());
            pst.setInt(2, ctHD.getDonGia());
            pst.setInt(3, ctHD.getThanhTien());
            pst.setString(4, ctHD.getMaBaoHanh());
            pst.setString(5, ctHD.getMaHoaDon());
            pst.setString(6, ctHD.getMaQuat());

            result = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Deletes a ChiTietHoaDon record from the database.
     *
     * @param maHoaDon The invoice ID
     * @param maQuat The fan product ID
     * @return Number of rows affected (1 if successful, 0 if failed)
     */
    public static int delete(String maHoaDon, String maQuat) {
        int result = 0;

        try (
                Connection conn = DBConnection.getConnection(); PreparedStatement pst = conn.prepareStatement(
                "DELETE FROM chitiet_hoadon WHERE MaHoaDon=? AND MaQuat=?")) {
            pst.setString(1, maHoaDon);
            pst.setString(2, maQuat);

            result = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Deletes all ChiTietHoaDon records for a specific invoice.
     *
     * @param maHoaDon The invoice ID
     * @return Number of rows affected
     */
    public static int deleteByMaHoaDon(String maHoaDon) {
        int result = 0;

        try (
                Connection conn = DBConnection.getConnection(); PreparedStatement pst = conn.prepareStatement(
                "DELETE FROM chitiet_hoadon WHERE MaHoaDon=?")) {
            pst.setString(1, maHoaDon);

            result = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Retrieves a ChiTietHoaDon record by its composite primary key.
     *
     * @param maHoaDon The invoice ID
     * @param maQuat The fan product ID
     * @return The ChiTietHoaDonDTO object if found, null otherwise
     */
    public static ChiTietHoaDonDTO selectById(String maHoaDon, String maQuat) {
        ChiTietHoaDonDTO ctHD = null;

        try (
                Connection conn = DBConnection.getConnection(); PreparedStatement pst = conn.prepareStatement(
                "SELECT * FROM chitiet_hoadon WHERE MaHoaDon=? AND MaQuat=?")) {
            pst.setString(1, maHoaDon);
            pst.setString(2, maQuat);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    ctHD = new ChiTietHoaDonDTO(
                            rs.getString("MaHoaDon"),
                            rs.getString("MaQuat"),
                            rs.getInt("SoLuong"),
                            rs.getInt("DonGia"),
                            rs.getInt("ThanhTien"),
                            rs.getString("MaBaoHanh")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ctHD;
    }

    /**
     * Retrieves all ChiTietHoaDon records for a specific invoice.
     *
     * @param maHoaDon The invoice ID
     * @return List of ChiTietHoaDonDTO objects
     */
    public static List<ChiTietHoaDonDTO> selectByMaHoaDon(String maHoaDon) {
        List<ChiTietHoaDonDTO> list = new ArrayList<>();

        try (
                Connection conn = DBConnection.getConnection(); PreparedStatement pst = conn.prepareStatement(
                "SELECT * FROM chitiet_hoadon WHERE MaHoaDon=?")) {
            pst.setString(1, maHoaDon);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    ChiTietHoaDonDTO ctHD = new ChiTietHoaDonDTO(
                            rs.getString("MaHoaDon"),
                            rs.getString("MaQuat"),
                            rs.getInt("SoLuong"),
                            rs.getInt("DonGia"),
                            rs.getInt("ThanhTien"),
                            rs.getString("MaBaoHanh")
                    );
                    list.add(ctHD);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Retrieves all ChiTietHoaDon records from the database.
     *
     * @return List of ChiTietHoaDonDTO objects
     */
    public static List<ChiTietHoaDonDTO> selectAll() {
        List<ChiTietHoaDonDTO> list = new ArrayList<>();

        try (
                Connection conn = DBConnection.getConnection(); PreparedStatement pst = conn.prepareStatement("SELECT * FROM chitiet_hoadon"); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                ChiTietHoaDonDTO ctHD = new ChiTietHoaDonDTO(
                        rs.getString("MaHoaDon"),
                        rs.getString("MaQuat"),
                        rs.getInt("SoLuong"),
                        rs.getInt("DonGia"),
                        rs.getInt("ThanhTien"),
                        rs.getString("MaBaoHanh")
                );
                list.add(ctHD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
