package GUI;

import DTO.DBConnection;
import GUI.Component.MenuTaskbar;
import GUI.Panel.BanQuatPanel;




import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;

public class EmployeeMainFrame extends JFrame {
    public  final String maNhanVien;
    private MenuTaskbar menuTaskbar;
    private JPanel mainContent;
    public String getMaNhanVien(){
        return maNhanVien;
    }

    public EmployeeMainFrame(String maNhanVien) {
        this.maNhanVien = maNhanVien;
        initLookAndFeel();
        initComponent();
    }   
         

    private void initLookAndFeel() {
      
        UIManager.put("PasswordField.showRevealButton", true);
    }

    private void initComponent() {
        setTitle("Giao diện Nhân viên");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0,0));

        // Load tên nhân viên từ CSDL
        String tenNV = loadTenNhanVien(maNhanVien);

        // Header hiển thị tên NV
        JLabel lblInfo = new JLabel("Nhân viên: " + tenNV);
        lblInfo.setBorder(new EmptyBorder(10,10,10,10));
        add(lblInfo, BorderLayout.NORTH);

        // Menu (chỉ 4 chức năng cho NV)
        menuTaskbar = new MenuTaskbar(this, false, maNhanVien);
        menuTaskbar.setPreferredSize(new Dimension(250, 800));
        add(menuTaskbar, BorderLayout.WEST);

        // Vùng nội dung chính
        mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);
        add(mainContent, BorderLayout.CENTER);

        // Mặc định hiển thị TrangChuPanel
        setPanel(new BanQuatPanel());
    }

    /**
     * Truy vấn CSDL để lấy họ tên nhân viên. 
     * Lưu ý: bảng trong CSDL là `nhanvien`, không có dấu cách.
     */
    private String loadTenNhanVien(String maNV) {
        String sql = "SELECT HoTenNV FROM `nhanvien` WHERE MaNhanVien = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, maNV);
            try (ResultSet r = p.executeQuery()) {
                if (r.next()) {
                    return r.getString("HoTenNV");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Nếu có lỗi, trả về mã NV tạm thời
        return maNV;
    }

    /**
     * Phương thức cho phép các panel con (TrangChuPanel, Bán quạt, ...) được swap vào vùng chính.
     */
    public void setPanel(JPanel panel) {
        mainContent.removeAll();
        mainContent.add(panel, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }
}
