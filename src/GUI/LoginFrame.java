package GUI;

import DTO.DBConnection;
import GUI.AdminMainFrame;
import GUI.ManagerMainFrame;
import GUI.EmployeeMainFrame;
import GUI.Component.InputForm;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFrame extends JFrame implements KeyListener {

    private JPanel pnlMain, pnlLogIn;
    private JLabel lblImage, lblTitle, lblLoginBtn;
    private InputForm txtUsername, txtPassword;
    private final Color hoverColor = new Color(96, 125, 139);

    public LoginFrame() {
        initLookAndFeel();
        initComponent();
        txtUsername.setText("nv1");
        txtPassword.setPass("123");
    }

    private void initLookAndFeel() {
 
        UIManager.put("PasswordField.showRevealButton", true);
    }

    private void initComponent() {
        setTitle("Đăng nhập");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        imgIntro();

        // Panel chính chứa form
        pnlMain = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setBorder(new EmptyBorder(20, 0, 0, 0));
        pnlMain.setPreferredSize(new Dimension(500, 500));

        // Tiêu đề
        lblTitle = new JLabel("ĐĂNG NHẬP VÀO HỆ THỐNG");
  
        pnlMain.add(lblTitle);

        // Form Username / Password
        JPanel formPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        formPanel.setBackground(Color.white);
        formPanel.setPreferredSize(new Dimension(400, 200));

        txtUsername = new InputForm("Tên đăng nhập");
        txtPassword = new InputForm("Mật khẩu", "password");
        formPanel.add(txtUsername);
        formPanel.add(txtPassword);

        txtUsername.getTxtForm().addKeyListener(this);
        txtPassword.getTxtPass().addKeyListener(this);

        pnlMain.add(formPanel);

        // Nút Đăng nhập
        lblLoginBtn = new JLabel("ĐĂNG NHẬP", SwingConstants.CENTER);
  
        lblLoginBtn.setForeground(Color.WHITE);

        pnlLogIn = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 12));
 
        pnlLogIn.setBackground(Color.BLACK);
        pnlLogIn.setPreferredSize(new Dimension(380, 45));
        pnlLogIn.add(lblLoginBtn);
        pnlLogIn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                pnlLogIn.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                pnlLogIn.setBackground(Color.BLACK);
            }
            @Override
            public void mousePressed(MouseEvent e) {
                checkLogin();
            }
        });
        pnlMain.add(pnlLogIn);

        // Đã loại bỏ phần "Quên mật khẩu"

        add(pnlMain, BorderLayout.EAST);
    }

    private void imgIntro() {
        JPanel imgPanel = new JPanel();
        imgPanel.setBackground(Color.WHITE);
        imgPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        imgPanel.setPreferredSize(new Dimension(500, 500));

        lblImage = new JLabel();
        ImageIcon icon = new ImageIcon(getClass().getResource("/img/login-image2.png"));
        Image img = icon.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
        lblImage.setIcon(new ImageIcon(img));
        imgPanel.add(lblImage);

        add(imgPanel, BorderLayout.WEST);
    }

    /**
     * Thực hiện xác thực với CSDL. Nếu đúng:
     *  Vai trò = Admin → mở AdminMainFrame
     *  Vai trò = QuanLy → mở ManagerMainFrame
     *  Vai trò = NhanVien → mở EmployeeMainFrame(maNhanVien)
     */
    private void checkLogin() {
    String user = txtUsername.getText().trim();
    String pass = txtPassword.getPass().trim();
    
    //Kiểm tra dữ liệu đầu vào
    if (user.isEmpty() || pass.isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "Vui lòng nhập đầy đủ thông tin",
            "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    //Truy vấn CSDL để xác thuc
    String sql = "SELECT * FROM taikhoan WHERE TenTaiKhoan = ? AND MatKhau = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, user);
        ps.setString(2, pass);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String role   = rs.getString("VaiTro");
                String maNV   = rs.getString("MaNhanVien"); // có thể null nếu admin
                
                //Chuyển hướng theo vai trò
                if ("Admin".equalsIgnoreCase(role)) {
                    // chỉ có admin 
                    new AdminMainFrame().setVisible(true);

                } else if ("QuanLy".equalsIgnoreCase(role)) {
                    // Quản lý
                    new ManagerMainFrame(maNV).setVisible(true);

                } else {
                    // Nhân viên thường
                    new EmployeeMainFrame(maNV).setVisible(true);
                }
                dispose();

            } else {
                JOptionPane.showMessageDialog(this,
                    "Đăng nhập thất bại: sai user hoặc mật khẩu",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this,
            "Lỗi kết nối CSDL", "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}


    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            checkLogin();
        }
    }
    @Override public void keyTyped(KeyEvent e)  { }
    @Override public void keyReleased(KeyEvent e) { }
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Không thể đặt LookAndFeel Nimbus: " + e);
        }

        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }

}