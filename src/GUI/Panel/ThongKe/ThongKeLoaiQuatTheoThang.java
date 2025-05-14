package GUI.Panel.ThongKe;

import BUS.ThongKeBUS;
import DTO.ThongKe.ThongKeLoaiQuatTheoThangDTO;
import GUI.Component.Chart.BarChart.Chart;
import GUI.Component.Chart.BarChart.ModelChart;
import helper.Formater;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class ThongKeLoaiQuatTheoThang extends JPanel {

    Color BackgroundColor = new Color(240, 247, 250);
    ThongKeBUS thongkeBUS;
    JTextField txtNam,txtThang;
    Chart barChart;
    JTable table;
    DefaultTableModel model;

    public ThongKeLoaiQuatTheoThang(ThongKeBUS thongkeBUS) {
        this.thongkeBUS = thongkeBUS;
        initComponent();
    }

    public void initComponent() {
        this.setLayout(new BorderLayout());
        this.setBackground(BackgroundColor);

        // ========== PHẦN NHẬP NĂM ==========
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();
        JLabel lblNam = new JLabel("Năm:");
        txtNam = new JTextField(String.valueOf(currentYear), 6);
        JLabel lblThang = new JLabel("Tháng:");
        txtThang = new JTextField(String.valueOf(currentMonth), 4);
        JButton btnThongKe = new JButton("Thống kê");
        topPanel.add(lblNam);
        topPanel.add(txtNam);
        topPanel.add(lblThang);
        topPanel.add(txtThang);
        topPanel.add(btnThongKe);
        this.add(topPanel, BorderLayout.NORTH);


        // ========== PHẦN BIỂU ĐỒ + BẢNG ==========
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        barChart = new Chart();
        barChart.setPreferredSize(new Dimension(800, 400));
        centerPanel.add(barChart, BorderLayout.NORTH);
        String[] columnNames = {"Mã loại", "Tên loại", "Số lượng bán", "Tổng tiền"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setIntercellSpacing(new Dimension(10, 10));
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(100, 50));
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(800, 300));
        centerPanel.add(scroll, BorderLayout.CENTER);
        this.add(centerPanel, BorderLayout.CENTER);
        btnThongKe.addActionListener(e -> thongKe());
        thongKe();
    }

    private void thongKe() {
        try {
            int nam = Integer.parseInt(txtNam.getText().trim());
            int thang = Integer.parseInt(txtThang.getText().trim());
            List<ThongKeLoaiQuatTheoThangDTO> ds = thongkeBUS.thongKeLoaiQuatTheoThang(thang,nam);
            barChart.clear();
              for (ThongKeLoaiQuatTheoThangDTO dto : ds) {
            barChart.addData(new ModelChart(dto.getTenLoaiQuat(), new double[]{dto.getSoLuongBan()}));
            }

            barChart.revalidate();
            barChart.repaint();
            model.setRowCount(0);
            for (ThongKeLoaiQuatTheoThangDTO dto : ds) {
                model.addRow(new Object[]{
                    dto.getMaLoaiQuat(),
                    dto.getTenLoaiQuat(),
                    dto.getSoLuongBan(),
                    Formater.FormatVND(dto.getTongTien())
                });
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng năm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

}
