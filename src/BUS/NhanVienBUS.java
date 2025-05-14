
package BUS;

import DAO.NhanVienDAO;
import DTO.NhanVienDTO;
import DTO.TaiKhoanDTO;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;


public class NhanVienBUS {
    private final NhanVienDAO dao = new NhanVienDAO();
    
    /**
     * Lấy tên nhân viên theo mã nhân viên
     * 
     * @param manv Mã nhân viên cần tìm
     * @return Tên nhân viên, null nếu không tìm thấy
     */
    public String getNameNVByMaNV(String manv){
        return dao.getTenNhanVienByManv(manv);
    }
    
    /**
     * Lấy tất cả nhân viên từ CSDL
     * 
     * @return Danh sách tất cả nhân viên, danh sách rỗng nếu có lỗi
     */
    public List<NhanVienDTO> layTatCa() {
        try {
            return dao.getAll();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return List.of(); // Java 9+ syntax for empty list
        }
    }

    /**
     * Thêm nhân viên mới vào CSDL
     * 
     * @param nv Đối tượng nhân viên cần thêm
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean them(NhanVienDTO nv) {
        try {
            dao.insert(nv);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật thông tin nhân viên
     * 
     * @param nv Đối tượng nhân viên với thông tin đã cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean sua(NhanVienDTO nv) {
        try {
            dao.update(nv);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa nhân viên khỏi CSDL
     * Kiểm tra xem nhân viên có tài khoản hay không trước khi xóa
     * 
     * @param maNV Mã nhân viên cần xóa
     * @return true nếu xóa thành công, false nếu thất bại hoặc người dùng hủy thao tác
     */
    public boolean xoa(String maNV) {
        try {
            // Kiểm tra xem nhân viên này có tài khoản hay không
            TaiKhoanBUS taiKhoanBUS = new TaiKhoanBUS();
            boolean coTaiKhoan = false;
            String maTaiKhoan = null;
            
            // Duyệt qua tất cả tài khoản để tìm tài khoản của nhân viên này
            for (TaiKhoanDTO tk : taiKhoanBUS.layTatCa()) {
                if (maNV.equals(tk.getMaNhanVien())) {
                    coTaiKhoan = true;
                    maTaiKhoan = tk.getMaTaiKhoan();
                    break;
                }
            }
            
            // Nếu nhân viên có tài khoản, hiển thị thông báo xác nhận
            if (coTaiKhoan) {
                int luaChon = JOptionPane.showConfirmDialog(null,
                    "Nhân viên này đã có tài khoản (" + maTaiKhoan + "). " +
                    "Nếu xóa nhân viên, tài khoản cũng sẽ bị xóa.\n" +
                    "Bạn có chắc chắn muốn xóa?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                
                if (luaChon != JOptionPane.YES_OPTION) {
                    // Người dùng đã chọn "Không" hoặc đóng hộp thoại
                    // Trả về false để báo hiệu việc xóa đã bị hủy bởi người dùng
                    // KHÔNG HIỂN THỊ THÔNG BÁO LỖI Ở ĐÂY vì đây không phải lỗi thực sự
                    return false;
                }
                
                // Người dùng đã chọn "Có" -> Xóa tài khoản trước
                try {
                    taiKhoanBUS.xoa(maTaiKhoan);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                        "Không thể xóa tài khoản: " + e.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            
            // Sau khi đã xóa tài khoản (nếu có), tiến hành xóa nhân viên
            dao.delete(maNV);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Xóa nhân viên thất bại: " + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}