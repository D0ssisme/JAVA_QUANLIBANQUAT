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

public class MenuTaskbar extends JPanel {

    private final JFrame parent;
    private final boolean isManager;
    private final String maNhanVien;

    public MenuTaskbar(JFrame parent, boolean isManager, String maNhanVien) {
        this.parent = parent;
        this.isManager = isManager;
        this.maNhanVien = maNhanVien;
        initComponent();
    }
    
    // Constructor original para mantener compatibilidad
    public MenuTaskbar(JFrame parent, boolean isManager) {
        this(parent, isManager, null);
    }

    private void initComponent() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel menu = new JPanel(new GridLayout(0, 1, 0, 5));
        menu.setBackground(Color.WHITE);

        // Tải icon
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

        // Chức năng riêng cho quản lý
        if (isManager) {
            addButton(menu, "Khuyến mãi", promotionIcon, e -> swap(new KhuyenMaiPanel()));
            addButton(menu, "Khách hàng", customerIcon, e -> swap(new KhachHangPanel()));
            addButton(menu, "Nhà cung cấp", supplierIcon, e -> swap(new NhaCungCapPanel()));
            addButton(menu, "Nhân viên", staffIcon, e -> swap(new NhanVienPanel()));
            addButton(menu, "Tài khoản", accountIcon, e -> swap(new TaiKhoanPanel(false))); //mặc định mở từ manager
            addButton(menu, "Thống kê", statisticIcon, e -> swap(new ThongKePanel()));
        }
        
        // Thêm chức năng xem thông tin cá nhân - dùng icon giống tài khoản
        if (maNhanVien != null) {
            addButton(menu, "Thông tin cá nhân", accountIcon, e -> swap(new ThongTinCaNhanPanel(maNhanVien)));
        }

        // Nút đăng xuất
        JButton btnLogout = new JButton("Đăng xuất", logoutIcon);
        btnLogout.setHorizontalAlignment(SwingConstants.LEFT);
        btnLogout.setForeground(Color.RED);
        btnLogout.addActionListener(e -> {
            parent.dispose();
            new LoginFrame().setVisible(true);
        });
        menu.add(btnLogout);

        add(menu, BorderLayout.CENTER);

        for (Component comp : menu.getComponents()) {
            if (comp instanceof JButton) {
                comp.setFont(new Font("Arial", Font.PLAIN, 14));
            }
        }
    }

    // Phiên bản không có icon
    private void addButton(JPanel menu, String text, ActionListener action) {
        JButton btn = new JButton(text);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.addActionListener(action);
        menu.add(btn);
    }

    // Phiên bản thêm icon
    private void addButton(JPanel menu, String text, Icon icon, ActionListener action) {
        JButton btn = new JButton(text, icon);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.addActionListener(action);
        menu.add(btn);
    }

    private void swap(JPanel panel) {
        if (parent instanceof ManagerMainFrame) {
            ((ManagerMainFrame) parent).setPanel(panel);
        } else if (parent instanceof EmployeeMainFrame) {
            ((EmployeeMainFrame) parent).setPanel(panel);
        }
    }
}