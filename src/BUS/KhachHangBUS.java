
package BUS;
import DTO.KhachHangDTO;
import DAO.KhachHangDAO;
import DTO.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class KhachHangBUS {
       public KhachHangDTO findBySdt(String sdt) {
        KhachHangDTO kh = null;
        String query = "SELECT * FROM khachhang WHERE Sdt_KH = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, sdt);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                kh = new KhachHangDTO();
                kh.setMaKhachHang(rs.getString("MaKhachHang"));  
                kh.setHoTenKH(rs.getString("HoTenKH"));
                kh.setSdtKH(rs.getString("Sdt_KH"));
                kh.setDiaChiKH(rs.getString("DiaChiKH"));       
                kh.setTongTienDaMua(rs.getInt("TongTienDaMua"));
                kh.setTrangThai(rs.getInt("TrangThai"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return kh;
    }
       public boolean isValidMaKhachHang(String maKh) {
        if (maKh == null) {
            return false;
        }
        // Regex: KH tiếp theo ít nhất 1 chữ số, rồi có thể thêm chữ số nữa
        return maKh.matches("^KH\\d+$");
    }
}
