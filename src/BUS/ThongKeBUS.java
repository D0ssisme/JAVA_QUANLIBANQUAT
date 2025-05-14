package BUS;

import DAO.ThongKeDAO;
import DTO.ThongKe.ThongKeKhachHangTheoNamDTO;
import DTO.ThongKe.ThongKeKhachHangTheoNgayDTO;
import DTO.ThongKe.ThongKeKhachHangTheoThangDTO;
import DTO.ThongKe.ThongKeLoaiQuatTheoNamDTO;
import DTO.ThongKe.ThongKeLoaiQuatTheoThangDTO;
import DTO.ThongKe.ThongKeQuatTheoNamDTO;
import DTO.ThongKe.ThongKeQuatTheoNgayDTO;
import DTO.ThongKe.ThongKeQuatTheoThangDTO;
import java.sql.SQLException;
import java.util.List;

public class ThongKeBUS {
    private ThongKeDAO thongKeDAO;

    public ThongKeBUS() {
        try {
            thongKeDAO = new ThongKeDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<ThongKeLoaiQuatTheoNamDTO> thongKeLoaiQuatTheoKhoangNam(int namBatDau, int namKetThuc) {
        return thongKeDAO.thongKeLoaiQuatTheoKhoangNam(namBatDau, namKetThuc);
    }
    public List<ThongKeLoaiQuatTheoThangDTO> thongKeLoaiQuatTheoThang(int thang,int nam){
        return thongKeDAO.thongKeLoaiQuatTheoThang(thang, nam);
    }
    public List<ThongKeQuatTheoNamDTO> thongKeQuatTheoNam(int namBD, int namKT){
        return thongKeDAO.thongKeQuatTheoKhoangNam(namBD, namKT);
    }
    public List<ThongKeQuatTheoThangDTO> thongKeQuatTheoThang(int thang, int nam){
        return thongKeDAO.thongKeQuatTheoThang(thang, nam);
    }
    public List<ThongKeQuatTheoNgayDTO> thongKeQuatTheoNgay(String ngayBD, String ngayKT){
        return thongKeDAO.thongKeQuatTheoNgay(ngayBD, ngayKT);
    }
    public List<ThongKeKhachHangTheoNamDTO> thongKeKHTheoNam(int namBD, int namKT){
        return thongKeDAO.thongKeKhachHangTheoKhoangNam(namBD,namKT);
    }
    public List<ThongKeKhachHangTheoThangDTO> thongKeKHTheoThang(int thang, int nam){
        return thongKeDAO.thongKeKhachHangTheoThang(thang,nam);
    }
    public List<ThongKeKhachHangTheoNgayDTO> thongKeKHTheoNgay(String ngayBD, String ngayKT){
        return thongKeDAO.thongKeKhachHangTheoKhoangNgay(ngayBD,ngayKT);
    }
}
