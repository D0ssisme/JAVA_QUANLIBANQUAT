package BUS;

import DAO.NhanVienDAO;
import DTO.NhanVienDTO;
import java.sql.SQLException;
import java.util.List;

public class NhanVienBUS {
    private final NhanVienDAO dao = new NhanVienDAO();
    public String getNameNVByMaNV(String manv){
        return dao.getTenNhanVienByManv(manv);
    }
    
    public List<NhanVienDTO> layTatCa() {
        try {
            return dao.getAll();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return List.of();
        }
    }

    public boolean them(NhanVienDTO nv) {
        try {
            dao.insert(nv);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean sua(NhanVienDTO nv) {
        try {
            dao.update(nv);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean xoa(String maNV) {
        try {
            dao.delete(maNV);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
