package GUI.Panel;

import DAO.KhuyenMaiDAO;
import DTO.KhuyenMaiDTO;
import GUI.Dialog.ThemKhuyenMaiDialog;
import GUI.Dialog.SuaKhuyenMaiDialog;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class KhuyenMaiPanel extends JPanel implements ItemListener, KeyListener {
    private JButton btnThem, btnXoa, btnSua, btnExcel, btnLamMoi;
    private JComboBox<String> cbbPhanTram;
    private JTextField txtSearch;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<KhuyenMaiDTO> listKM;

    // Map loại → mô tả điều kiện
    private static final Map<Integer, String> dieuKienMap = new HashMap<>();
    static {
        dieuKienMap.put(1, "Đơn hàng ≥ 500k");
        dieuKienMap.put(2, "Mua ≥ 2 sản phẩm");
        dieuKienMap.put(3, "Áp dụng cho mọi hóa đơn");
    }

    public KhuyenMaiPanel() {
        initComponent();
        loadDataFromDatabase();
    }

    private void initComponent() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // Toolbar
        JPanel toolbar = new JPanel(new BorderLayout());

        JPanel leftTool = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btnThem = new JButton("THÊM");
        btnXoa = new JButton("XÓA");
        btnSua = new JButton("SỬA");
        btnExcel = new JButton("XUẤT EXCEL");
        leftTool.add(btnThem);
        leftTool.add(btnXoa);
        leftTool.add(btnSua);
        leftTool.add(btnExcel);

        JPanel rightTool = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        cbbPhanTram = new JComboBox<>(new String[]{"Tất cả", "Dưới 10%", "Từ 10% - 20%", "Trên 20%"});
        cbbPhanTram.addItemListener(this);
        txtSearch = new JTextField(15);
        setupSearchPlaceholder();
        txtSearch.addKeyListener(this);
        btnLamMoi = new JButton("LÀM MỚI");
        rightTool.add(cbbPhanTram);
        rightTool.add(txtSearch);
        rightTool.add(btnLamMoi);

        toolbar.add(leftTool, BorderLayout.WEST);
        toolbar.add(rightTool, BorderLayout.EAST);
        add(toolbar, BorderLayout.NORTH);

        // Table
        String[] cols = {"Mã KM", "Tên khuyến mãi", "Phần trăm giảm", "Ngày bắt đầu", "Ngày kết thúc", "Điều kiện"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Events
        btnThem.addActionListener(e -> themKhuyenMai());
        btnXoa.addActionListener(e -> xoaKhuyenMai());
        btnSua.addActionListener(e -> suaKhuyenMai());
        btnLamMoi.addActionListener(e -> refreshData());
    }

    private void setupSearchPlaceholder() {
        txtSearch.setText("Nhập nội dung tìm kiếm...");
        txtSearch.setForeground(Color.GRAY);
        txtSearch.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (txtSearch.getText().equals("Nhập nội dung tìm kiếm...")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setForeground(Color.GRAY);
                    txtSearch.setText("Nhập nội dung tìm kiếm...");
                }
            }
        });
    }

    private void loadDataFromDatabase() {
        try {
            listKM = KhuyenMaiDAO.selectAll();
            loadDataToTable(listKM);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + ex.getMessage());
        }
    }

    private void loadDataToTable(List<KhuyenMaiDTO> data) {
        tableModel.setRowCount(0);
        for (KhuyenMaiDTO km : data) {
            tableModel.addRow(new Object[]{
                km.getMaSKKhuyenMai(),
                km.getTenKhuyenMai(),
                km.getPhanTramGiam() + "%",
                km.getNgayBatDau(),
                km.getNgayKetThuc(),
                dieuKienMap.get(km.getLoai())
            });
        }
    }

    private void themKhuyenMai() {
        ThemKhuyenMaiDialog dlg = new ThemKhuyenMaiDialog(SwingUtilities.getWindowAncestor(this));
        dlg.setVisible(true);
        if (dlg.isSaved()) refreshData();
    }

    private void xoaKhuyenMai() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi cần xóa!"); return; }
        String ma = (String) tableModel.getValueAt(row, 0);
        int c = JOptionPane.showConfirmDialog(this, "Xóa khuyến mãi " + ma + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            try {
                if (KhuyenMaiDAO.delete(ma)) JOptionPane.showMessageDialog(this, "Xóa thành công!");
                else JOptionPane.showMessageDialog(this, "Xóa thất bại!");
                refreshData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + ex.getMessage());
            }
        }
    }

    private void suaKhuyenMai() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi cần sửa!"); return; }
        String ma = (String) tableModel.getValueAt(row, 0);
        KhuyenMaiDTO km = KhuyenMaiDAO.selectById(ma);
        if (km != null) {
            SuaKhuyenMaiDialog dlg = new SuaKhuyenMaiDialog((Frame)SwingUtilities.getWindowAncestor(this), km);
            dlg.setVisible(true);
            if (dlg.isUpdated()) refreshData();
        }
    }

    private void refreshData() {
        loadDataFromDatabase();
        txtSearch.setText("Nhập nội dung tìm kiếm...");
        txtSearch.setForeground(Color.GRAY);
        cbbPhanTram.setSelectedIndex(0);
    }

    private void filterData() {
        String search = txtSearch.getText().trim().toLowerCase();
        if (search.equals("nhập nội dung tìm kiếm...")) search = "";
        String filter = (String) cbbPhanTram.getSelectedItem();
        List<KhuyenMaiDTO> out = new ArrayList<>();
        for (KhuyenMaiDTO km : listKM) {
            int pt = km.getPhanTramGiam();
            boolean okPT = filter.equals("Tất cả") ||
                (filter.equals("Dưới 10%") && pt < 10) ||
                (filter.equals("Từ 10% - 20%") && pt >= 10 && pt <= 20) ||
                (filter.equals("Trên 20%") && pt > 20);
            boolean okSearch = search.isEmpty()
                || km.getMaSKKhuyenMai().toLowerCase().contains(search)
                || km.getTenKhuyenMai().toLowerCase().contains(search)
                || dieuKienMap.get(km.getLoai()).toLowerCase().contains(search);
            if (okPT && okSearch) out.add(km);
        }
        loadDataToTable(out);
    }

    @Override public void itemStateChanged(ItemEvent e) { if (e.getStateChange()==ItemEvent.SELECTED) filterData(); }
    @Override public void keyReleased(KeyEvent e) { filterData(); }
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyPressed(KeyEvent e) {}
}
