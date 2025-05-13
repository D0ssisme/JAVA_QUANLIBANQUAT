package GUI.Panel.ThongKe;

import BUS.ThongKeBUS;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ThongKePanel extends JPanel {

   JTabbedPane tabbedPane;
    JPanel loaiquat, quat, khachhang;

    Color BackgroundColor = new Color(240, 247, 250);
    ThongKeBUS thongkeBUS = new ThongKeBUS();

    public ThongKePanel() {
        initComponent();
    }

    public void initComponent() {
        this.setLayout(new GridLayout(1, 1));
        this.setBackground(BackgroundColor);

        loaiquat = new ThongKeLoaiQuat(thongkeBUS);
         quat = new ThongKeQuat(thongkeBUS);
        khachhang = new ThongKeKhachHang(thongkeBUS);

        tabbedPane = new JTabbedPane();
        tabbedPane.setOpaque(false);
        tabbedPane.addTab("Loại Quạt", loaiquat);
        tabbedPane.addTab("  Quạt  ", quat);
        tabbedPane.addTab("Khách hàng", khachhang);

        this.add(tabbedPane);
    }
}
