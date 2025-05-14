package BUS;

import DAO.QuatDAO;
import DTO.QuatDTO;

import java.sql.SQLException;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;

public class QuatBUS {
    private final QuatDAO dao = new QuatDAO();

    public List<QuatDTO> layTatCa() {
        try {
            return dao.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    private String convertDateFormat(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            return outputFormat.format(inputFormat.parse(inputDate));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Thêm quạt
 public boolean them(String maQuat, String tenQuat, int gia, int soLuongTon, String maNSX, java.sql.Date ngaySanXuat, String chatLieu, String thuongHieu, String maLoaiSP) {
    try {
        dao.insert(new QuatDTO(maQuat, tenQuat, gia, soLuongTon, maNSX, ngaySanXuat.toString(), chatLieu, thuongHieu, maLoaiSP));
        return true;
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Lỗi khi lưu quạt: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        return false;
    }
}

    // Xóa quạt
    public boolean xoa(String maQuat) {
        try {
            dao.delete(maQuat);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sua(String maQuat, String tenQuat, int gia, int soLuongTon, String maNSX, String ngaySanXuat, String chatLieu, String thuongHieu, String maLoaiSP) {
        try {
            dao.update(new QuatDTO(maQuat, tenQuat, gia,soLuongTon, maNSX, ngaySanXuat, chatLieu, thuongHieu, maLoaiSP));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public QuatDTO timTheoMaQuat(String maQuat) {
        try {
            return dao.findByMaQuat(maQuat);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<QuatDTO> timTheoTenQuat(String keyword) {
        try {
            return dao.findByTenQuat(keyword);
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }
    public List<QuatDTO> timTheoThuongHieu(String keyword) {
        try {
            return dao.findByThuongHieu(keyword);
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }
    public boolean  updatesoluongquat(String maquat,int soluongquat)
    {
        return dao.updateSoLuongQuat(maquat, soluongquat);
    }
    
    public List<QuatDTO> layTheoMaLoai(String maLoai) throws SQLException {
        return new QuatDAO().findByMaLoai(maLoai);
    }
    public boolean daTonTaiMaQuat(String maQuat) throws SQLException {
        return dao.findByMaQuat(maQuat) != null;
    }

}
