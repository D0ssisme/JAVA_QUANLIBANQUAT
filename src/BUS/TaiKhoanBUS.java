package BUS;

import DAO.TaiKhoanDAO;
import DTO.TaiKhoanDTO;
import DTO.NhanVienDTO;
import BUS.NhanVienBUS;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class TaiKhoanBUS {
    private final TaiKhoanDAO dao = new TaiKhoanDAO();
    private final NhanVienBUS nvBUS = new NhanVienBUS();
    public String getMaNVByMaTaiKhoan(String mataikhoan){
        return dao.getMaNVByMaTaiKhoan(mataikhoan);
    }
    public List<TaiKhoanDTO> layTatCa() {
        try { return dao.getAll(); }
        catch(SQLException ex) { throw new RuntimeException(ex); }
    }

    public TaiKhoanDTO layTheoMa(String ma) {
        try { return dao.getByMa(ma); }
        catch(SQLException ex) { throw new RuntimeException(ex); }
    }

    public List<String> layDanhSachNhanVienChuaCoTaiKhoan() {
        try { 
            List<String> allNhanVien = dao.getNhanVienWithoutAccount();
            // Filter out employees with "Bảo vệ" role
            List<String> filteredList = new ArrayList<>();
            
            for (String maNV : allNhanVien) {
                // Get employee details to check role
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

    public List<String> layDanhSachNhanVienCoTaiKhoan() {
        try { return dao.getNhanVienWithAccount(); }
        catch(SQLException ex) { throw new RuntimeException(ex); }
    }

    public void them(String user, String pass, String role, String maNV) {
        try {
            if (dao.getByUsername(user) != null)
                throw new IllegalArgumentException("Tên tài khoản đã tồn tại");
            String nextId = genNextId();
            dao.insert(new TaiKhoanDTO(nextId, maNV, user, pass, role));
        } catch(SQLException ex) { throw new RuntimeException(ex); }
    }

    public void capNhat(String maTK, String user, String pass, String role, String maNV) {
        if ("TK000".equals(maTK))
            throw new IllegalArgumentException("Không thể sửa tài khoản Admin");
        try {
            TaiKhoanDTO t = dao.getByUsername(user);
            if (t != null && !t.getMaTaiKhoan().equals(maTK))
                throw new IllegalArgumentException("Tên tài khoản đã tồn tại");
            dao.update(new TaiKhoanDTO(maTK, maNV, user, pass, role));
        } catch(SQLException ex) { throw new RuntimeException(ex); }
    }

    public void xoa(String maTK) {
        if ("TK000".equals(maTK))
            throw new IllegalArgumentException("Không thể xóa tài khoản Admin");
        try { dao.delete(maTK); }
        catch(SQLException ex) { throw new RuntimeException(ex); }
    }

    private String genNextId() throws SQLException {
        int max = 0;
        for (TaiKhoanDTO t : dao.getAll()) {
            String num = t.getMaTaiKhoan().replaceAll("\\D+", "");
            max = Math.max(max, Integer.parseInt(num));
        }
        return String.format("TK%03d", max + 1);
    }
}