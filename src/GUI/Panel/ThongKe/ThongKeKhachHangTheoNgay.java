package GUI.Panel.ThongKe;

import BUS.ThongKeBUS;
import DTO.ThongKe.ThongKeKhachHangTheoNgayDTO;
import GUI.Component.Chart.BarChart.Chart;
import GUI.Component.Chart.BarChart.ModelChart;
import helper.Formater;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class ThongKeKhachHangTheoNgay extends JPanel {

    Color BackgroundColor = new Color(240, 247, 250);
    ThongKeBUS thongkeBUS;
    JTextField txtNgayBatDau, txtNgayKetThuc;
    Chart barChart;
    JTable table;
    DefaultTableModel model;

    public ThongKeKhachHangTheoNgay(ThongKeBUS thongkeBUS) {
        this.thongkeBUS = thongkeBUS;
        initComponent();
    }

    public void initComponent() {
        this.setLayout(new BorderLayout());
        this.setBackground(BackgroundColor);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        LocalDate now = LocalDate.now();
        LocalDate oneWeekAgo = now.minusDays(7);  
        String currentDate = now.toString();
        String oneWeekAgoStr = oneWeekAgo.toString();
        JLabel lblNgayBatDau = new JLabel("Ngày bắt đầu (yyyy-mm-dd):");
        txtNgayBatDau = new JTextField(oneWeekAgoStr, 10);
        JLabel lblNgayKetThuc = new JLabel("Ngày kết thúc (yyyy-mm-dd):");
        txtNgayKetThuc = new JTextField(currentDate, 10);

        JButton btnThongKe = new JButton("Thống kê");
        topPanel.add(lblNgayBatDau);
        topPanel.add(txtNgayBatDau);
        topPanel.add(lblNgayKetThuc);
        topPanel.add(txtNgayKetThuc);
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
            String ngayBatDauStr = txtNgayBatDau.getText().trim();
            String ngayKetThucStr = txtNgayKetThuc.getText().trim();

            LocalDate ngayBatDau = LocalDate.parse(ngayBatDauStr);
            LocalDate ngayKetThuc = LocalDate.parse(ngayKetThucStr);

            if (ngayBatDau.isAfter(ngayKetThuc)) {
                JOptionPane.showMessageDialog(this,
                        "Ngày bắt đầu phải trước hoặc bằng ngày kết thúc!",
                        "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
                return;
            }
            List<ThongKeKhachHangTheoNgayDTO> ds = thongkeBUS.thongKeKHTheoNgay(ngayBatDauStr, ngayKetThucStr);
            List<ThongKeKhachHangTheoNgayDTO> top5 = ds.stream()
                    .sorted(Comparator.comparingDouble(ThongKeKhachHangTheoNgayDTO::getTongTien).reversed())
                    .limit(5)
                    .toList();

            barChart.clear();
            barChart.setChartTitle("Top 5 khách hàng có tổng tiền mua hàng cao nhất từ năm " + ngayBatDauStr + " đến " + ngayKetThucStr);
            for (ThongKeKhachHangTheoNgayDTO dto : top5) {
                barChart.addData(new ModelChart(dto.getTenKH(), new double[]{dto.getTongTien()}));
            }
            barChart.revalidate();
            barChart.repaint();

            // ==== HIỂN THỊ TOÀN BỘ DỮ LIỆU TRONG BẢNG ====
            ds.sort(Comparator.comparingDouble(ThongKeKhachHangTheoNgayDTO::getTongTien).reversed());
            model.setRowCount(0);
            for (ThongKeKhachHangTheoNgayDTO dto : ds) {
                model.addRow(new Object[]{
                        dto.getMaKH(),
                        dto.getTenKH(),
                        dto.getSoLanMua(),
                        Formater.FormatVND(dto.getTongTien())
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập đúng định dạng ngày! (YYYY-MM-DD)",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }


}
