package GUI.Panel.ThongKe;

import BUS.ThongKeBUS;
import DTO.ThongKe.ThongKeKhachHangTheoThangDTO;
import GUI.Component.Chart.BarChart.Chart;
import GUI.Component.Chart.BarChart.ModelChart;
import helper.Formater;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class ThongKeKhachHangTheoThang extends JPanel {

    Color BackgroundColor = new Color(240, 247, 250);
    ThongKeBUS thongkeBUS;
    JTextField txtThang, txtNam;
    Chart barChart;
    JTable table;
    DefaultTableModel model;

    public ThongKeKhachHangTheoThang(ThongKeBUS thongkeBUS) {
        this.thongkeBUS = thongkeBUS;
        initComponent();
    }

    public void initComponent() {
        this.setLayout(new BorderLayout());
        this.setBackground(BackgroundColor);

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

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        barChart = new Chart();
        barChart.setPreferredSize(new Dimension(800, 400));
        centerPanel.add(barChart, BorderLayout.NORTH);
        String[] columnNames = {"Mã khách hàng", "Họ tên", "Số lần mua", "Tổng tiền"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);

        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setIntercellSpacing(new Dimension(10, 10));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(100, 50));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(800, 250));
        centerPanel.add(scroll, BorderLayout.CENTER);

        this.add(centerPanel, BorderLayout.CENTER);
        btnThongKe.addActionListener(e -> thongKe());
        thongKe();
    }

    private void thongKe() {
        try {
            int thang = Integer.parseInt(txtThang.getText().trim());
            int nam = Integer.parseInt(txtNam.getText().trim());

            List<ThongKeKhachHangTheoThangDTO> ds = thongkeBUS.thongKeKHTheoThang(thang, nam);

            barChart.clear();
            for (ThongKeKhachHangTheoThangDTO dto : ds) {
                barChart.addData(new ModelChart(dto.getTenKH(), new double[]{dto.getTongTien()}));
            }

            barChart.revalidate();
            barChart.repaint();
            model.setRowCount(0);
            for (ThongKeKhachHangTheoThangDTO dto : ds) {
                model.addRow(new Object[]{
                        dto.getMaKH(),
                        dto.getTenKH(),
                        dto.getSoLanMua(),
                        Formater.FormatVND(dto.getTongTien())
                });
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng tháng và năm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}