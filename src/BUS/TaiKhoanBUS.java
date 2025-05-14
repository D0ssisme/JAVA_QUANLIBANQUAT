
package BUS;

import DAO.TaiKhoanDAO;
import BUS.NhanVienBUS;
import DTO.TaiKhoanDTO;
import DTO.NhanVienDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public class TaiKhoanBUS {
    private final TaiKhoanDAO dao = new TaiKhoanDAO();
    private final NhanVienBUS nvBUS = new NhanVienBUS();
    
    /**
     * Lấy mã nhân viên từ mã tài khoản
     * @param mataikhoan Mã tài khoản cần tìm
     * @return Mã nhân viên tương ứng
     */
    public String getMaNVByMaTaiKhoan(String mataikhoan){
        return dao.getMaNVByMaTaiKhoan(mataikhoan);
    }
    
    /**
     * Lấy tất cả tài khoản từ CSDL
     * @return Danh sách tất cả tài khoản
     */
    public List<TaiKhoanDTO> layTatCa() {
        try { return dao.getAll(); }
        catch(SQLException ex) { throw new RuntimeException(ex); }
    }

    /**
     * Lấy tài khoản theo mã
     * @param ma Mã tài khoản cần tìm
     * @return Đối tượng tài khoản nếu tìm thấy, null nếu không tìm thấy
     */
    public TaiKhoanDTO layTheoMa(String ma) {
        try { return dao.getByMa(ma); }
        catch(SQLException ex) { throw new RuntimeException(ex); }
    }

    /**
     * Lấy danh sách mã nhân viên chưa có tài khoản
     * Lọc bỏ nhân viên có chức vụ "Bảo vệ" (không cần tạo tài khoản)
     * 
     * @return Danh sách mã nhân viên chưa có tài khoản và không phải bảo vệ
     */
    public List<String> layDanhSachNhanVienChuaCoTaiKhoan() {
        try { 
            // Lấy danh sách mã nhân viên chưa có tài khoản từ DAO
            List<String> allNhanVien = dao.getNhanVienWithoutAccount();
            // Lọc bỏ nhân viên có chức vụ "Bảo vệ"
            List<String> filteredList = new ArrayList<>();
            
            for (String maNV : allNhanVien) {
                // Lấy thông tin nhân viên để kiểm tra chức vụ
                NhanVienDTO nv = nvBUS.layTatCa().stream()
                                  .filter(n -> n.getMaNV().equals(maNV))
                                  .findFirst()
                                  .orElse(null);
                                    
                if (nv != null && !nv.getChucVu().equals("Bảo vệ")) {
                    filteredList.add(maNV);
                }
            }
            
            return filteredList;
        }
        catch(SQLException ex) { throw new RuntimeException(ex); }
    }

    /**
     * Lấy danh sách mã nhân viên đã có tài khoản
     * 
     * @return Danh sách mã nhân viên đã có tài khoản
     */
    public List<String> layDanhSachNhanVienCoTaiKhoan() {
        try { return dao.getNhanVienWithAccount(); }
        catch(SQLException ex) { throw new RuntimeException(ex); }
    }

    /**
     * Thêm tài khoản mới cho nhân viên
     * Kiểm tra trùng tên tài khoản và tạo mã tài khoản tự động
     * 
     * @param user Tên đăng nhập
     * @param pass Mật khẩu
     * @param role Vai trò (quyền hạng)
     * @param maNV Mã nhân viên
     * @throws IllegalArgumentException Nếu tên tài khoản đã tồn tại

* 
     */
    public void them(String user, String pass, String role, String maNV) {
        try {
            // Kiểm tra tên tài khoản đã tồn tại chưa
            if (dao.getByUsername(user) != null)
                throw new IllegalArgumentException("Tên tài khoản đã tồn tại");
            
            // Tạo mã tài khoản mới
            String nextId = genNextId();
            
            // Thêm tài khoản mới vào CSDL
            dao.insert(new TaiKhoanDTO(nextId, maNV, user, pass, role));
        } catch(SQLException ex) { throw new RuntimeException(ex); }
    }

    /**
     * Cập nhật thông tin tài khoản
     * Không cho phép cập nhật tài khoản Admin
     * 
     * @param maTK Mã tài khoản cần cập nhật
     * @param user Tên đăng nhập mới
     * @param pass Mật khẩu mới
     * @param role Vai trò mới
     * @param maNV Mã nhân viên mới
     * @throws IllegalArgumentException Nếu là tài khoản Admin hoặc tên đăng nhập đã tồn tại
     */
    public void capNhat(String maTK, String user, String pass, String role, String maNV) {
        // Không cho phép sửa tài khoản Admin
        if ("TK000".equals(maTK))
            throw new IllegalArgumentException("Không thể sửa tài khoản Admin");
        
        try {
            // Kiểm tra tên đăng nhập có bị trùng không (loại trừ chính nó)
            TaiKhoanDTO t = dao.getByUsername(user);
            if (t != null && !t.getMaTaiKhoan().equals(maTK))
                throw new IllegalArgumentException("Tên tài khoản đã tồn tại");
            
            // Cập nhật tài khoản
            dao.update(new TaiKhoanDTO(maTK, maNV, user, pass, role));
        } catch(SQLException ex) { throw new RuntimeException(ex); }
    }

    /**
     * Xóa tài khoản
     * Không cho phép xóa tài khoản Admin
     * 
     * @param maTK Mã tài khoản cần xóa
     * @throws IllegalArgumentException Nếu là tài khoản Admin
     */
    public void xoa(String maTK) {
        // Không cho phép xóa tài khoản Admin
        if ("TK000".equals(maTK))
            throw new IllegalArgumentException("Không thể xóa tài khoản Admin");
        
        try { dao.delete(maTK); }
        catch(SQLException ex) { throw new RuntimeException(ex); }
    }

    /**
     * Tự động tạo mã tài khoản mới
     * Tìm số nhỏ nhất chưa được sử dụng thay vì lấy max+1
     * 
     * @return Mã tài khoản mới theo định dạng TKxxx
     */
    private String genNextId() throws SQLException {
        // Lấy tất cả các mã tài khoản hiện có
        List<TaiKhoanDTO> dsTaiKhoan = dao.getAll();
        
        // Tạo một tập hợp các số đã được sử dụng
        Set<Integer> daSuDung = new HashSet<>();
        for (TaiKhoanDTO tk : dsTaiKhoan) {
            // Tách lấy phần số từ mã tài khoản (bỏ qua ký tự không phải số)
            String maTK = tk.getMaTaiKhoan();
            if (maTK.startsWith("TK")) {
                try {
                    String phanSo = maTK.substring(2); // Bỏ "TK" ở đầu
                    int so = Integer.parseInt(phanSo);
                    daSuDung.add(so);
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    // Bỏ qua nếu định dạng không đúng
                }
            }
        }
        
        // Tìm số nhỏ nhất chưa được sử dụng
        int nextId = 1; // Bắt đầu từ 1
        while (daSuDung.contains(nextId)) {
            nextId++;
        }
        
        // Trả về mã với định dạng TKxxx (ví dụ: TK001, TK015, v.v.)
        return String.format("TK%03d", nextId);
    }
}