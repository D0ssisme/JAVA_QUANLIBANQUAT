package BUS;

import DAO.QLBH_DAO;
import DTO.DBConnection;
import DTO.QLBH_DTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * Lớp tiện ích cung cấp các phương thức hỗ trợ việc quản lý bảo hành
 */
public class QLBH_BUS {
    
    /**
     * Lấy mã bảo hành dựa trên mã quạt
     * @param maQuat Mã quạt cần tìm bảo hành
     * @return Mã bảo hành tương ứng hoặc null nếu không tồn tại
     */
    public static String getMaBaoHanhByMaQuat(String maQuat) {
        if (maQuat == null || maQuat.trim().isEmpty()) {
            return null;
        }
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement("SELECT MaBaoHanh FROM quanlibaohanh WHERE MaQuat = ? ORDER BY ThoiGianBaoHanh DESC LIMIT 1")) {
            
            pst.setString(1, maQuat);
            
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("MaBaoHanh");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Tạo mã bảo hành mới dựa trên mã quạt
     * @param maQuat Mã quạt cần tạo bảo hành
     * @param maKhachHang Mã khách hàng mua quạt
     * @param thoiHanBaoHanh Thời hạn bảo hành tính theo tháng
     * @return Mã bảo hành mới đã được tạo và lưu vào CSDL
     */
    public static String createNewWarranty(String maQuat, String maKhachHang, int thoiHanBaoHanh) {
        // Tạo mã bảo hành
        String maBaoHanh = generateWarrantyCode(maQuat);
        
        // Tính ngày kết thúc bảo hành
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, thoiHanBaoHanh);
        Date thoiGianBaoHanh = calendar.getTime();
        
        // Tạo đối tượng bảo hành
        QLBH_DTO baoHanh = new QLBH_DTO(
            maBaoHanh,
            maQuat,
            maKhachHang,
            thoiGianBaoHanh,
            "Còn hạn" // Trạng thái mặc định
        );
        
        // Lưu vào CSDL
        if (QLBH_DAO.insert(baoHanh) > 0) {
            return maBaoHanh;
        }
        
        return null;
    }
    
    /**
     * Kiểm tra trạng thái bảo hành của một quạt
     * @param maQuat Mã quạt cần kiểm tra
     * @return Trạng thái bảo hành hoặc null nếu không tìm thấy
     */
    public static String getWarrantyStatus(String maQuat) {
        String maBaoHanh = getMaBaoHanhByMaQuat(maQuat);
        if (maBaoHanh != null) {
            QLBH_DTO baoHanh = QLBH_DAO.selectById(maBaoHanh);
            if (baoHanh != null) {
                return baoHanh.getTrangThai();
            }
        }
        return null;
    }
    
    /**
     * Kiểm tra xem quạt còn trong thời gian bảo hành hay không
     * @param maQuat Mã quạt cần kiểm tra
     * @return true nếu còn bảo hành, false nếu hết hạn hoặc không tìm thấy
     */
    public static boolean isInWarranty(String maQuat) {
        String maBaoHanh = getMaBaoHanhByMaQuat(maQuat);
        if (maBaoHanh != null) {
            QLBH_DTO baoHanh = QLBH_DAO.selectById(maBaoHanh);
            if (baoHanh != null) {
                // So sánh ngày kết thúc với ngày hiện tại
                Date thoiGianBaoHanh = baoHanh.getThoiGianBaoHanh();
                Date now = new Date();
                return thoiGianBaoHanh.after(now);
            }
        }
        return false;
    }
    
    /**
     * Tạo mã bảo hành mới với định dạng quy chuẩn
     * @param maQuat Mã quạt để tạo mã bảo hành
     * @return Mã bảo hành mới
     */
    private static String generateWarrantyCode(String maQuat) {
        // Tạo tiền tố cho mã bảo hành
        String prefix = "BH" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        
        // Tìm mã bảo hành lớn nhất có cùng tiền tố
        int maxNumber = 0;
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement pst = conn.prepareStatement("SELECT MAX(MaBaoHanh) FROM quanlibaohanh WHERE MaBaoHanh LIKE ?")) {
            
            pst.setString(1, prefix + "%");
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next() && rs.getString(1) != null) {
                    String maxCode = rs.getString(1);
                    try {
                        // Trích xuất phần số từ mã bảo hành
                        String numPart = maxCode.replaceAll("[^0-9]", "");
                        int startIndex = prefix.replaceAll("[^0-9]", "").length();
                        if (numPart.length() > startIndex) {
                            maxNumber = Integer.parseInt(numPart.substring(startIndex));
                        }
                    } catch (NumberFormatException e) {
                        // Mặc định là 0 nếu không phân tích được
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Tạo mã bảo hành mới kết hợp với mã quạt
        return prefix + String.format("%03d", maxNumber + 1) + "-" + maQuat;
    }
}