package GUI.Panel;

import javax.swing.*;
import java.awt.*;

public class TrangChuPanel extends JPanel {

    public TrangChuPanel() {
        initComponent();
    }

    private void initComponent() {
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(245, 245, 245)); // Nền xám nhạt nhẹ

        JLabel lblTieuDe = new JLabel("HỆ THỐNG CỬA HÀNG BÁN QUẠT");
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTieuDe.setHorizontalAlignment(SwingConstants.CENTER);
        lblTieuDe.setForeground(new Color(33, 111, 219));
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));

        // Load ảnh mới từ file png sắc nét
        ImageIcon quatIcon = new ImageIcon(getClass().getResource("/img/quat.jpg"));
        Image img = quatIcon.getImage().getScaledInstance(750, 400, Image.SCALE_SMOOTH);
        JLabel lblImage = new JLabel(new ImageIcon(img));
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        lblImage.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));
        centerPanel.add(lblImage, BorderLayout.CENTER);

        this.add(lblTieuDe, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
    }
}
