
package GUI.Component;

import GUI.Panel.ThongKe.ThongKePanel;
import GUI.LoginFrame;
import GUI.ManagerMainFrame;
import GUI.EmployeeMainFrame;
import GUI.Panel.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Menu chức năng hiển thị bên trái màn hình chính
 * Hiển thị các chức năng khác nhau tùy theo vai trò người dùng (Admin, Quản lý, Nhân viên)
 */
public class MenuTaskbar extends JPanel {
    private final java.util.List<JButton> menuButtons = new java.util.ArrayList<>();
    private JButton selectedButton = null; // nút đang được chọn

    private final JFrame parent;           // Frame cha chứa menu này
    private final boolean isManager;       // Cờ xác định có phải quản lý hay không
    private final String maNhanVien;       // Mã nhân viên đang đăng nhập

    private void addButton(JPanel menu, String text, Icon icon, ActionListener action) {
        JButton btn = new JButton(text, icon);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.addActionListener(e -> {
            updateSelectedButton(btn);  // thêm dòng này
            action.actionPerformed(e);  // giữ nguyên xử lý gốc
        });
        menu.add(btn);
        menuButtons.add(btn); // lưu vào danh sách
    }
    private void updateSelectedButton(JButton selected) {
        for (JButton btn : menuButtons) {
            btn.setBackground(Color.WHITE); // màu gốc
            btn.setForeground(Color.BLACK);
        }
        selected.setBackground(new Color(220, 240, 255)); // màu xanh nhạt
        selected.setForeground(Color.BLUE); // chữ màu xanh
        selectedButton = selected;
    }

    /**
     * Constructor đầy đủ - khởi tạo menu với thông tin người dùng
     * 
     * @param parent Frame cha chứa menu
     * @param isManager true: hiển thị menu cho quản lý, false: hiển thị menu cho nhân viên
     * @param maNhanVien Mã nhân viên đang đăng nhập, null nếu là Admin
     */
    public MenuTaskbar(JFrame parent, boolean isManager, String maNhanVien) {
        this.parent = parent;
        this.isManager = isManager;
        this.maNhanVien = maNhanVien;
        initComponent();
    }
    
    /**
     * Constructor tương thích với mã cũ - không sử dụng mã nhân viên
     * 
     * @param parent Frame cha chứa menu
     * @param isManager true: hiển thị menu cho quản lý, false: hiển thị menu cho nhân viên
     */
    public MenuTaskbar(JFrame parent, boolean isManager) {
        this(parent, isManager, null);
    }


    private void initComponent() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Panel chứa các mục menu theo chiều dọc
        JPanel menu = new JPanel(new GridLayout(0, 1, 0, 5));
        menu.setBackground(Color.WHITE);

        ImageIcon homeIcon = new ImageIcon(getClass().getResource("/icon/trangchu.png"));
        ImageIcon sellIcon = new ImageIcon(getClass().getResource("/icon/ban.png"));
        ImageIcon quatIcon = new ImageIcon(getClass().getResource("/icon/fan.png"));
        ImageIcon billIcon = new ImageIcon(getClass().getResource("/icon/bill.png"));
        ImageIcon importIcon = new ImageIcon(getClass().getResource("/icon/in.png"));
        ImageIcon exportIcon = new ImageIcon(getClass().getResource("/icon/out.png"));
        ImageIcon promotionIcon = new ImageIcon(getClass().getResource("/icon/khuyenmai.png"));
        ImageIcon customerIcon = new ImageIcon(getClass().getResource("/icon/khachhang.png"));
        ImageIcon supplierIcon = new ImageIcon(getClass().getResource("/icon/nhacungcap.png"));
        ImageIcon staffIcon = new ImageIcon(getClass().getResource("/icon/nhanvien.png"));
        ImageIcon accountIcon = new ImageIcon(getClass().getResource("/icon/taikhoan.png"));
        ImageIcon statisticIcon = new ImageIcon(getClass().getResource("/icon/thongke.png"));
        ImageIcon logoutIcon = new ImageIcon(getClass().getResource("/icon/logout.png"));
        ImageIcon loaispIcon = new ImageIcon(getClass().getResource("/icon/loaisp.png"));


        addButton(menu, "Bán quạt", sellIcon, e -> {
            String maNV = null;

            if (parent instanceof EmployeeMainFrame) {
                maNV = ((EmployeeMainFrame) parent).getMaNhanVien();
            } else if (parent instanceof ManagerMainFrame) {
                maNV = ((ManagerMainFrame) parent).getMaNhanVien();
            }

            if (maNV != null) {
                swap(new BanQuatPanel(maNV));
            } else {
                JOptionPane.showMessageDialog(this, "Không xác định được mã nhân viên.");
            }
        });

        addButton(menu, "Quản lý quạt", quatIcon, e -> {
            try {
                swap(new QuatPanel());
            } catch (SQLException ex) {
                Logger.getLogger(MenuTaskbar.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        addButton(menu, "Hóa đơn", billIcon, e -> swap(new HoaDonPanel()));

        addButton(menu, "Phiếu nhập", importIcon, e -> swap(new PhieuNhapPanel(this.maNhanVien)));

        addButton(menu, "Loại sản phẩm", loaispIcon, e -> swap(new LoaiSanPhamPanel()));


        if (isManager) {
            
            addButton(menu, "Khuyến mãi", promotionIcon, e -> swap(new KhuyenMaiPanel()));
            addButton(menu, "Khách hàng", customerIcon, e -> swap(new KhachHangPanel()));

            addButton(menu, "Nhà cung cấp", supplierIcon, e -> swap(new NhaCungCapPanel()));

            addButton(menu, "Nhân viên", staffIcon, e -> swap(new NhanVienPanel()));

            addButton(menu, "Tài khoản", accountIcon, e -> swap(new TaiKhoanPanel(false))); //mặc định mở từ manager

            addButton(menu, "Thống kê", statisticIcon, e -> swap(new ThongKePanel()));
        }
 
        if (maNhanVien != null) {
            addButton(menu, "Thông tin cá nhân", accountIcon, e -> swap(new ThongTinCaNhanPanel(maNhanVien)));
        }

        JButton btnLogout = new JButton("Đăng xuất", logoutIcon);
        btnLogout.setHorizontalAlignment(SwingConstants.LEFT);
        btnLogout.setForeground(Color.RED);
        btnLogout.addActionListener(e -> {
            parent.dispose();
            new LoginFrame().setVisible(true);
        });
        menu.add(btnLogout);

        // Thêm menu vào panel chính
        add(menu, BorderLayout.CENTER);

        // Thiết lập font cho tất cả các nút
        for (Component comp : menu.getComponents()) {
            if (comp instanceof JButton) {
                comp.setFont(new Font("Arial", Font.PLAIN, 14));
            }
        }
    }

    /**
     * Thêm nút menu không có icon
     * 
     * @param menu Panel chứa các nút menu
     * @param text Văn bản hiển thị trên nút
     * @param action Hành động khi nhấn nút
     */
    private void addButton(JPanel menu, String text, ActionListener action) {
        JButton btn = new JButton(text);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.addActionListener(action);
        menu.add(btn);
    }

    /**
     * Thêm nút menu có icon
     * 
     * @param menu Panel chứa các nút menu
     * @param text Văn bản hiển thị trên nút
     * @param icon Icon hiển thị trên nút
     * @param action Hành động khi nhấn nút
     */


    /**
     * Chuyển đổi panel hiển thị trong frame cha
     * 
     * @param panel Panel mới cần hiển thị
     */
    private void swap(JPanel panel) {
        if (parent instanceof ManagerMainFrame) {
            ((ManagerMainFrame) parent).setPanel(panel);
        } else if (parent instanceof EmployeeMainFrame) {
            ((EmployeeMainFrame) parent).setPanel(panel);
        }
    }
}