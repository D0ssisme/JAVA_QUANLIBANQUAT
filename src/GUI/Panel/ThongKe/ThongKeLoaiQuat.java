/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.Panel.ThongKe;

import BUS.ThongKeBUS;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author Admin
 */
public class ThongKeLoaiQuat extends JPanel {
    JTabbedPane tabbedPane;
    ThongKeLoaiQuatTungNam thongketungnam;
    ThongKeLoaiQuatTheoThang thongketungthang;
    Color BackgroundColor = new Color(240, 247, 250);
    ThongKeBUS thongkeBUS;

    public ThongKeLoaiQuat(ThongKeBUS thongkeBUS) {
        this.thongkeBUS = thongkeBUS;
        initComponent();
    }

    public void initComponent() {
        this.setLayout(new java.awt.BorderLayout());

          thongketungthang = new ThongKeLoaiQuatTheoThang(thongkeBUS);
          thongketungnam = new ThongKeLoaiQuatTungNam(thongkeBUS);


        tabbedPane = new JTabbedPane();
        tabbedPane.setOpaque(false);
        tabbedPane.addTab("Thống kê theo năm", thongketungnam);
        tabbedPane.addTab("Thống kê từng tháng trong năm", thongketungthang);
        
        this.add(tabbedPane, java.awt.BorderLayout.CENTER);

        this.add(tabbedPane);
    }
}
