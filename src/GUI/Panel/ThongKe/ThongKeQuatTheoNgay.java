package GUI.Panel.ThongKe;

import BUS.ThongKeBUS;
import DTO.ThongKe.ThongKeQuatTheoNgayDTO;
import GUI.Component.Chart.BarChart.Chart;
import GUI.Component.Chart.BarChart.ModelChart;
import helper.Formater;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class ThongKeQuatTheoNgay extends JPanel {

    Color BackgroundColor = new Color(240, 247, 250);
    ThongKeBUS thongkeBUS;
    JTextField txtNgayBD, txtNgayKT;
    Chart barChart;
    JTable table;
    DefaultTableModel model;

    public ThongKeQuatTheoNgay(ThongKeBUS thongkeBUS) {
        this.thongkeBUS = thongkeBUS;
        initComponent();
    }

    public void initComponent() {
        this.setLayout(new BorderLayout());
        this.setBackground(BackgroundColor);

        // ========== PHẦN NHẬP NGÀY ==========
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
         LocalDate now = LocalDate.now();
        LocalDate oneWeekAgo = now.minusDays(7);  
        String currentDate = now.toString();
        String oneWeekAgoStr = oneWeekAgo.toString();
        JLabel lblNgayBD = new JLabel("Ngày bắt đầu (yyyy-mm-dd):");
        txtNgayBD = new JTextField(oneWeekAgoStr, 10);
        JLabel lblNgayKT = new JLabel("Ngày kết thúc (yyyy-mm-dd):");
        txtNgayKT = new JTextField(currentDate, 10);
        JButton btnThongKe = new JButton("Thống kê");
        topPanel.add(lblNgayBD);
        topPanel.add(txtNgayBD);
        topPanel.add(lblNgayKT);
        topPanel.add(txtNgayKT);
        topPanel.add(btnThongKe);
        this.add(topPanel, BorderLayout.NORTH);

        // ========== PHẦN BIỂU ĐỒ + BẢNG ==========
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        barChart = new Chart();
        barChart.setPreferredSize(new Dimension(800, 400));
        centerPanel.add(barChart, BorderLayout.NORTH);
        String[] columnNames = {"Mã quạt", "Tên quạt", "Số lượng bán", "Tổng tiền"};
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
            String ngayBDStr = txtNgayBD.getText().trim();
            String ngayKTStr = txtNgayKT.getText().trim();

            LocalDate ngayBD = LocalDate.parse(ngayBDStr);
            LocalDate ngayKT = LocalDate.parse(ngayKTStr);

            if (ngayBD.isAfter(ngayKT)) {
                JOptionPane.showMessageDialog(this,
                        "Ngày bắt đầu phải trước hoặc bằng ngày kết thúc!",
                        "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<ThongKeQuatTheoNgayDTO> ds = thongkeBUS.thongKeQuatTheoNgay(ngayBDStr, ngayKTStr);
            barChart.clear();
            for (ThongKeQuatTheoNgayDTO dto : ds) {
                barChart.addData(new ModelChart(dto.getTenQuat(), new double[]{dto.getSoLuongBan()}));
            }

            barChart.revalidate();
            barChart.repaint();
            model.setRowCount(0);
            for (ThongKeQuatTheoNgayDTO dto : ds) {
                model.addRow(new Object[]{
                        dto.getMaQuat(),
                        dto.getTenQuat(),
                        dto.getSoLuongBan(),
                        Formater.FormatVND(dto.getTongTien())
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập đúng định dạng ngày (yyyy-mm-dd)!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

}