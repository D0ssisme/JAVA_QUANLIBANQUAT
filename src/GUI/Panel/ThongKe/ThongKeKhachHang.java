/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.Panel.ThongKe;

import BUS.ThongKeBUS;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class ThongKeKhachHang extends JPanel {
    JTabbedPane tabbedPane;
    ThongKeKhachHangTheoNam thongKeKhachHangTheoNam;
    ThongKeKhachHangTheoThang thongKeKhachHangTheoThang;
    ThongKeKhachHangTheoNgay thongKeKhachHangTheoNgay;
    Color BackgroundColor = new Color(240, 247, 250);
    ThongKeBUS thongkeBUS;

    public ThongKeKhachHang(ThongKeBUS thongkeBUS) {
        this.thongkeBUS = thongkeBUS;
        initComponent();
    }

    public void initComponent() {
        this.setLayout(new java.awt.BorderLayout());
       thongKeKhachHangTheoThang = new ThongKeKhachHangTheoThang(thongkeBUS);
          thongKeKhachHangTheoNam = new ThongKeKhachHangTheoNam(thongkeBUS);
      thongKeKhachHangTheoNgay = new ThongKeKhachHangTheoNgay(thongkeBUS);
        tabbedPane = new JTabbedPane();
        tabbedPane.setOpaque(false);
          tabbedPane.addTab("Thống kê theo năm", thongKeKhachHangTheoNam);
        tabbedPane.addTab("Thống kê theo tháng", thongKeKhachHangTheoThang);
        tabbedPane.addTab("Thống kê theo ngày", thongKeKhachHangTheoNgay);
        this.add(tabbedPane, java.awt.BorderLayout.CENTER);
    }
}