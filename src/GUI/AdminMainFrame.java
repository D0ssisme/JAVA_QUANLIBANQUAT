package GUI;

import GUI.Panel.TaiKhoanPanel;
import javax.swing.*;
import java.awt.*;

public class AdminMainFrame extends JFrame {
    public AdminMainFrame() {
        initUI();
    }
    private void initUI() {
        setTitle("Quản trị hệ thống");
        setSize(1000,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Tạo TaiKhoanPanel với tham số true để chỉ ra rằng đây là Admin
        add(new TaiKhoanPanel(true), BorderLayout.CENTER);
    }
}