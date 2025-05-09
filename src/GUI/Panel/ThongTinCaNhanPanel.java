package GUI.Panel;

import BUS.NhanVienBUS;
import BUS.TaiKhoanBUS;
import DTO.NhanVienDTO;
import DTO.TaiKhoanDTO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.awt.Dialog.ModalityType;

public class ThongTinCaNhanPanel extends JPanel {
    private JTextField txtMaNV, txtHoTen, txtChucVu, txtSDT, txtDiaChi;
    private JTextField txtTenDangNhap, txtMatKhau;
    private JButton btnDoiMatKhau, btnHuy;
    private final String maNhanVien;
    private final NhanVienBUS nvBUS = new NhanVienBUS();
    private final TaiKhoanBUS tkBUS = new TaiKhoanBUS();
    
    public ThongTinCaNhanPanel(String maNhanVien) {
        this.maNhanVien = maNhanVien;
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title Panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblTitle = new JLabel("THÔNG TIN CÁ NHÂN");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 102, 204));
        titlePanel.add(lblTitle);
        add(titlePanel, BorderLayout.NORTH);
        
        // Main Content Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10),
            BorderFactory.createLineBorder(Color.LIGHT_GRAY)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Thông tin nhân viên section
        JLabel lblInfoSection = new JLabel("Thông tin nhân viên");
        lblInfoSection.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(lblInfoSection, gbc);
        
        gbc.gridwidth = 1;
        
        // Mã nhân viên
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Mã nhân viên:"), gbc);
        
        gbc.gridx = 1;
        txtMaNV = new JTextField(20);
        txtMaNV.setEditable(false);
        txtMaNV.setBackground(new Color(240, 240, 240));
        mainPanel.add(txtMaNV, gbc);
        
        // Họ tên
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Họ tên:"), gbc);
        
        gbc.gridx = 1;
        txtHoTen = new JTextField(20);
        txtHoTen.setEditable(false);
        txtHoTen.setBackground(new Color(240, 240, 240));
        mainPanel.add(txtHoTen, gbc);
        
        // Chức vụ
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Chức vụ:"), gbc);
        
        gbc.gridx = 1;
        txtChucVu = new JTextField(20);
        txtChucVu.setEditable(false);
        txtChucVu.setBackground(new Color(240, 240, 240));
        mainPanel.add(txtChucVu, gbc);
        
        // Số điện thoại
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("Số điện thoại:"), gbc);
        
        gbc.gridx = 1;
        txtSDT = new JTextField(20);
        txtSDT.setEditable(false);
        txtSDT.setBackground(new Color(240, 240, 240));
        mainPanel.add(txtSDT, gbc);
        
        // Địa chỉ
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(new JLabel("Địa chỉ:"), gbc);
        
        gbc.gridx = 1;
        txtDiaChi = new JTextField(20);
        txtDiaChi.setEditable(false);
        txtDiaChi.setBackground(new Color(240, 240, 240));
        mainPanel.add(txtDiaChi, gbc);
        
        // Separator
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        JSeparator separator = new JSeparator();
        separator.setPreferredSize(new Dimension(400, 2));
        mainPanel.add(separator, gbc);
        
        // Thông tin tài khoản section
        gbc.gridx = 0;
        gbc.gridy = 7;
        JLabel lblAccountSection = new JLabel("Thông tin tài khoản");
        lblAccountSection.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(lblAccountSection, gbc);
        
        gbc.gridwidth = 1;
        
        // Tên đăng nhập
        gbc.gridx = 0;
        gbc.gridy = 8;
        mainPanel.add(new JLabel("Tên đăng nhập:"), gbc);
        
        gbc.gridx = 1;
        txtTenDangNhap = new JTextField(20);
        txtTenDangNhap.setEditable(false);
        txtTenDangNhap.setBackground(new Color(240, 240, 240));
        mainPanel.add(txtTenDangNhap, gbc);
        
        // Mật khẩu
        gbc.gridx = 0;
        gbc.gridy = 9;
        mainPanel.add(new JLabel("Mật khẩu:"), gbc);
        
        gbc.gridx = 1;
        txtMatKhau = new JPasswordField(20);
        txtMatKhau.setEditable(false);
        txtMatKhau.setBackground(new Color(240, 240, 240));
        mainPanel.add(txtMatKhau, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnDoiMatKhau = new JButton("Đổi mật khẩu");
        btnHuy = new JButton("Làm mới");
        
        btnDoiMatKhau.addActionListener(e -> showPasswordChangeDialog());
        btnHuy.addActionListener(e -> resetFields());
        
        buttonPanel.add(btnDoiMatKhau);
        buttonPanel.add(btnHuy);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadData() {
        if (maNhanVien == null || maNhanVien.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Không thể xác định thông tin nhân viên!", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Lấy thông tin nhân viên
        NhanVienDTO nv = null;
        for (NhanVienDTO n : nvBUS.layTatCa()) {
            if (n.getMaNV().equals(maNhanVien)) {
                nv = n;
                break;
            }
        }
        
        if (nv != null) {
            txtMaNV.setText(nv.getMaNV());
            txtHoTen.setText(nv.getHoTen());
            txtChucVu.setText(nv.getChucVu());
            txtSDT.setText(nv.getSdt());
            txtDiaChi.setText(nv.getDiaChi());
        }
        
        // Lấy thông tin tài khoản
        TaiKhoanDTO tk = null;
        for (TaiKhoanDTO t : tkBUS.layTatCa()) {
            if (maNhanVien.equals(t.getMaNhanVien())) {
                tk = t;
                break;
            }
        }
        
        if (tk != null) {
            txtTenDangNhap.setText(tk.getUsername());
            txtMatKhau.setText(tk.getPassword());
        }
    }
    
    private void resetFields() {
        loadData();
    }
    
    private void showPasswordChangeDialog() {
        JDialog passwordDialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Đổi mật khẩu", Dialog.ModalityType.APPLICATION_MODAL);        passwordDialog.setLayout(new BorderLayout(10, 10));
        passwordDialog.setSize(400, 250);
        passwordDialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Mật khẩu hiện tại
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Mật khẩu hiện tại:"), gbc);
        
        gbc.gridx = 1;
        JPasswordField txtCurrentPassword = new JPasswordField(15);
        mainPanel.add(txtCurrentPassword, gbc);
        
        // Mật khẩu mới
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Mật khẩu mới:"), gbc);
        
        gbc.gridx = 1;
        JPasswordField txtNewPassword = new JPasswordField(15);
        mainPanel.add(txtNewPassword, gbc);
        
        // Xác nhận mật khẩu mới
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Xác nhận mật khẩu mới:"), gbc);
        
        gbc.gridx = 1;
        JPasswordField txtConfirmPassword = new JPasswordField(15);
        mainPanel.add(txtConfirmPassword, gbc);
        
        passwordDialog.add(mainPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");
        
        btnSave.addActionListener(e -> {
            // Kiểm tra mật khẩu hiện tại
            String currentPass = new String(txtCurrentPassword.getPassword());
            String newPass = new String(txtNewPassword.getPassword());
            String confirmPass = new String(txtConfirmPassword.getPassword());
            
            if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                JOptionPane.showMessageDialog(passwordDialog, 
                    "Vui lòng nhập đầy đủ thông tin!", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!currentPass.equals(txtMatKhau.getText())) {
                JOptionPane.showMessageDialog(passwordDialog, 
                    "Mật khẩu hiện tại không đúng!", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!newPass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(passwordDialog, 
                    "Mật khẩu mới không khớp!", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Tìm tài khoản và cập nhật mật khẩu
            TaiKhoanDTO tk = null;
            for (TaiKhoanDTO t : tkBUS.layTatCa()) {
                if (maNhanVien.equals(t.getMaNhanVien())) {
                    tk = t;
                    break;
                }
            }
            
            if (tk != null) {
                try {
                    tkBUS.capNhat(tk.getMaTaiKhoan(), tk.getUsername(), newPass, tk.getVaiTro(), tk.getMaNhanVien());
                    JOptionPane.showMessageDialog(passwordDialog, 
                        "Đổi mật khẩu thành công!", 
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    passwordDialog.dispose();
                    loadData(); // Reload data to show new password
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(passwordDialog, 
                        "Lỗi khi đổi mật khẩu: " + ex.getMessage(), 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        btnCancel.addActionListener(e -> passwordDialog.dispose());
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        passwordDialog.add(buttonPanel, BorderLayout.SOUTH);
        passwordDialog.setVisible(true);
    }
}