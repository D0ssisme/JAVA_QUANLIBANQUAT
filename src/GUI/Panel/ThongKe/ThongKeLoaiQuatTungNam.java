package GUI.Panel.ThongKe;

import BUS.ThongKeBUS;
import DTO.ThongKe.ThongKeLoaiQuatTheoNamDTO;
import GUI.Component.Chart.BarChart.Chart;
import GUI.Component.Chart.BarChart.ModelChart;
import helper.Formater;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class ThongKeLoaiQuatTungNam extends JPanel {

    Color BackgroundColor = new Color(240, 247, 250);
    ThongKeBUS thongkeBUS;
    JTextField txtNamTu, txtNamDen;
    Chart barChart;
    JTable table;
    DefaultTableModel model;

    public ThongKeLoaiQuatTungNam(ThongKeBUS thongkeBUS) {
        this.thongkeBUS = thongkeBUS;
        initComponent();
    }

    public void initComponent() {
        this.setLayout(new BorderLayout());
        this.setBackground(BackgroundColor);

        // ========== PHẦN NHẬP NĂM ==========
        int currentYear = LocalDate.now().getYear();
        int startYear = currentYear - 2;
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        JLabel lblNamTu = new JLabel("Năm từ:");
        JLabel lblNamDen = new JLabel("Đến:");
        txtNamTu = new JTextField(String.valueOf(startYear),6);
        txtNamDen = new JTextField(String.valueOf(currentYear),6);
        JButton btnThongKe = new JButton("Thống kê");

        topPanel.add(lblNamTu);
        topPanel.add(txtNamTu);
        topPanel.add(lblNamDen);
        topPanel.add(txtNamDen);
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
            int namTu = Integer.parseInt(txtNamTu.getText().trim());
            int namDen = Integer.parseInt(txtNamDen.getText().trim());

            List<ThongKeLoaiQuatTheoNamDTO> ds = thongkeBUS.thongKeLoaiQuatTheoKhoangNam(namTu, namDen);
            barChart.clear();
            for (ThongKeLoaiQuatTheoNamDTO dto : ds) {
                barChart.addData(new ModelChart(dto.getTenLoaiQuat(), new double[]{dto.getSoLuongBan()}));
            }
            barChart.revalidate();
            barChart.repaint();  

            model.setRowCount(0);
            for (ThongKeLoaiQuatTheoNamDTO dto : ds) {
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
