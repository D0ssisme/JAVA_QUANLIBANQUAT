/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.Panel.ThongKe;

import BUS.ThongKeBUS;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author Admin
 */
public class ThongKeQuat extends JPanel {
    JTabbedPane tabbedPane;
    ThongKeQuatTheoNam thongKeQuatTheoNam;
    ThongKeQuatTheoThang thongKeQuatTheoThang;
    ThongKeQuatTheoNgay thongKeQuatTheoNgay;
    Color BackgroundColor = new Color(240, 247, 250);
    ThongKeBUS thongkeBUS;

    public ThongKeQuat(ThongKeBUS thongkeBUS) {
        this.thongkeBUS = thongkeBUS;
        initComponent();
    }

    public void initComponent() {
        this.setLayout(new java.awt.BorderLayout());
        thongKeQuatTheoThang = new ThongKeQuatTheoThang(thongkeBUS);
        thongKeQuatTheoNam = new ThongKeQuatTheoNam(thongkeBUS);
        thongKeQuatTheoNgay = new ThongKeQuatTheoNgay(thongkeBUS);
        tabbedPane = new JTabbedPane();
        tabbedPane.setOpaque(false);
        tabbedPane.addTab("Thống kê theo năm", thongKeQuatTheoNam);
        tabbedPane.addTab("Thống kê tháng trong năm", thongKeQuatTheoThang);
        tabbedPane.addTab("Thống kê theo ngày", thongKeQuatTheoNgay);
        
        this.add(tabbedPane, java.awt.BorderLayout.CENTER);
    }
}
