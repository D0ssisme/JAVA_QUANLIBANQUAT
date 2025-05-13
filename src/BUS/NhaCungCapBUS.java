package BUS;

import DAO.NhaCungCapDAO;
import DTO.NhaCungCapDTO;

import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

public class NhaCungCapBUS {
    private final NhaCungCapDAO dao = new NhaCungCapDAO();
    
    public String layTenNhaCungCapTheoMa(String maNCC)
    {
        return dao.layTenNhaCungCapTheoMa(maNCC);
    }
     public String layMaNCCTheoTen(String tenNCC) {
        return NhaCungCapDAO.layMaNhaCungCapTheoTen(tenNCC);
    }
    // Lấy tất cả nhà cung cấp
    public List<NhaCungCapDTO> layTatCa() {
        try {
            return dao.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    // Thêm nhà cung cấp
    public boolean them(String maNCC, String tenNCC, String diaChiNCC, String sdtNCC) {
        try {
            dao.insert(new NhaCungCapDTO(maNCC, tenNCC, diaChiNCC, sdtNCC));
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi thêm nhà cung cấp: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Xóa nhà cung cấp theo mã
    public boolean xoa(String maNCC) {
        try {
            dao.delete(maNCC);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật nhà cung cấp
    public boolean sua(String maNCC, String tenNCC, String diaChiNCC, String sdtNCC) {
        try {
            dao.update(new NhaCungCapDTO(maNCC, tenNCC, diaChiNCC, sdtNCC));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Tìm theo mã nhà cung cấp
    public NhaCungCapDTO timTheoMa(String maNCC) {
        try {
            return dao.findByMaNCC(maNCC);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Tìm theo tên gần đúng
    public List<NhaCungCapDTO> timTheoTen(String keyword) {
        try {
            return dao.findByTenNCC(keyword);
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
