package GUI.Panel;

import GUI.Dialog.ChiTietPhieuXuatDialog;
import GUI.Dialog.ThemPhieuXuatDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PhieuXuatPanel extends JPanel {
    private JButton btnThem, btnChiTiet, btnHuyPhieu, btnXuatExcel, btnLamMoi;
    private JComboBox<String> cbbSearchType;
    private JTextField txtSearch;

    private JTable table;
    private DefaultTableModel tableModel;

    // Khu vực lọc bên trái
    private JComboBox<String> cbbKhachHang, cbbNhanVien;
    private JTextField txtTuNgay, txtDenNgay, txtSoTien;

    public PhieuXuatPanel() {
        initComponent();
        addDummyData(); // thêm dữ liệu mẫu
    }

    private void initComponent() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // ==================== Thanh công cụ trên ====================
        JPanel topPanel = new JPanel(new BorderLayout());

        // (A) Panel nút bên trái
        JPanel leftToolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btnThem = new JButton("THÊM");
        btnChiTiet = new JButton("CHI TIẾT");
        btnHuyPhieu = new JButton("HỦY PHIẾU");
        btnXuatExcel = new JButton("XUẤT EXCEL");

        leftToolPanel.add(btnThem);
        leftToolPanel.add(btnChiTiet);
        leftToolPanel.add(btnHuyPhieu);
        leftToolPanel.add(btnXuatExcel);

        // (B) Panel tìm kiếm bên phải
        JPanel rightToolPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        cbbSearchType = new JComboBox<>(new String[]{"Tất cả", "Mã phiếu xuất", "Khách hàng", "Nhân viên xuất"});
        txtSearch = new JTextField("Nhập nội dung tìm kiếm...", 15);
        btnLamMoi = new JButton("Làm mới");

        rightToolPanel.add(cbbSearchType);
        rightToolPanel.add(txtSearch);
        rightToolPanel.add(btnLamMoi);

        topPanel.add(leftToolPanel, BorderLayout.WEST);
        topPanel.add(rightToolPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // ==================== Panel lọc bên trái ====================
        JPanel filterPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        filterPanel.setPreferredSize(new Dimension(200, 0));

        // 1. Khách hàng
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.add(new JLabel("Khách hàng:"));
        cbbKhachHang = new JComboBox<>(new String[]{"Tất cả", "Hoàng Bá Đo", "Nguyễn Văn B", "Trần Thị C"});
        row1.add(cbbKhachHang);
        filterPanel.add(row1);

        // 2. Nhân viên
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.add(new JLabel("Nhân viên:"));
        cbbNhanVien = new JComboBox<>(new String[]{"Tất cả", "NV01", "NV02", "NV03"});
        row2.add(cbbNhanVien);
        filterPanel.add(row2);

        // 3. Từ ngày
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row3.add(new JLabel("Từ ngày:"));
        txtTuNgay = new JTextField(10);
        row3.add(txtTuNgay);
        filterPanel.add(row3);

        // 4. Đến ngày
        JPanel row4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row4.add(new JLabel("Đến ngày:"));
        txtDenNgay = new JTextField(10);
        row4.add(txtDenNgay);
        filterPanel.add(row4);

        // 5. Số tiền (VNĐ)
        JPanel row5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row5.add(new JLabel("Số tiền (VNĐ):"));
        txtSoTien = new JTextField(10);
        row5.add(txtSoTien);
        filterPanel.add(row5);

        add(filterPanel, BorderLayout.WEST);

        // ==================== Bảng trung tâm ====================
        String[] columns = {"STT", "Mã phiếu", "Khách hàng", "Nhân viên xuất", "Ngày xuất", "Tổng tiền"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        // ==================== Sự kiện nút (demo) ====================
        btnThem.addActionListener(e -> {
            // Mở dialog thêm phiếu xuất
            ThemPhieuXuatDialog dlg = new ThemPhieuXuatDialog(SwingUtilities.getWindowAncestor(this));
            dlg.setVisible(true);
        });

        btnChiTiet.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Chưa chọn phiếu xuất!");
                return;
            }
            // Mở dialog chi tiết phiếu xuất
            ChiTietPhieuXuatDialog dlg = new ChiTietPhieuXuatDialog(SwingUtilities.getWindowAncestor(this));
            dlg.setVisible(true);
        });

        btnHuyPhieu.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Hủy phiếu xuất (demo)!");
        });

        btnXuatExcel.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Xuất Excel (demo)!");
        });

        btnLamMoi.addActionListener(e -> {
            // Xoá bảng rồi thêm lại
            tableModel.setRowCount(0);
            addDummyData();
        });
    }

    private void addDummyData() {
        tableModel.addRow(new Object[]{1, "PX001", "Hoàng Bá Đo", "NV01", "2025-03-29 10:00", "1.500.000"});
        tableModel.addRow(new Object[]{2, "PX002", "Nguyễn Văn B", "NV02", "2025-03-29 11:15", "2.000.000"});
        tableModel.addRow(new Object[]{3, "Trần Thị C", "NV03", "PX003", "2025-03-29 14:30", "3.000.000"});
        // Lưu ý: Cột 2-3-4 có thể bị đảo, bạn tùy chỉnh thứ tự cho đúng
        // Chỉnh cho khớp cột: Mã phiếu, Khách hàng, Nhân viên, v.v.
    }
}
