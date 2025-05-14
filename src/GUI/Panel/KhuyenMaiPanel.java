package GUI.Panel;

import DAO.KhuyenMaiDAO;
import DTO.KhuyenMaiDTO;
import GUI.Dialog.ThemKhuyenMaiDialog;
import GUI.Dialog.SuaKhuyenMaiDialog;
import com.toedter.calendar.JDateChooser;
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
    private JDateChooser dateFrom, dateTo;
    private JButton btnSearchDate;
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
        setLayout(new BorderLayout(10, 10));
        setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // === Toolbar ===
        JPanel toolbar = createButtonPanel();
        add(toolbar, BorderLayout.NORTH);

        // Table
        String[] cols = {"Mã KM", "Tên khuyến mãi", "Phần trăm giảm", "Ngày bắt đầu", "Ngày kết thúc", "Điều kiện"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Load data
        loadDataFromDatabase();
    }

    private JPanel createButtonPanel() {
        JPanel toolbar = new JPanel(new GridBagLayout());
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0;

        // Buttons THÊM, XÓA, SỬA, EXCEL
        gbc.gridx = 0;
        gbc.gridy = 0;
        btnThem = new JButton("THÊM", new ImageIcon(getClass().getResource("/icon/them.png")));
        toolbar.add(btnThem, gbc);
        gbc.gridx = 1;
        btnXoa = new JButton("XÓA", new ImageIcon(getClass().getResource("/icon/xoa.png")));
        toolbar.add(btnXoa, gbc);
        gbc.gridx = 2;
        btnSua = new JButton("SỬA", new ImageIcon(getClass().getResource("/icon/sua.png")));
        toolbar.add(btnSua, gbc);
        gbc.gridx = 3;
        btnExcel = new JButton("XUẤT EXCEL", new ImageIcon(getClass().getResource("/icon/xuatexcel.png")));
        toolbar.add(btnExcel, gbc);

        // Spacer
        gbc.gridx = 4;
        gbc.weightx = 1;
        toolbar.add(Box.createHorizontalGlue(), gbc);

        // Basic Search (row 0)
        cbbPhanTram = new JComboBox<>(new String[]{"Tất cả", "Dưới 10%", "Từ 10% - 20%", "Trên 20%"});
        txtSearch = new JTextField(15);
        btnLamMoi = new JButton("LÀM MỚI");
        JPanel basicSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        basicSearch.setOpaque(false);
        basicSearch.add(new JLabel("Lọc:"));
        basicSearch.add(cbbPhanTram);
        basicSearch.add(txtSearch);
        basicSearch.add(btnLamMoi);
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        toolbar.add(basicSearch, gbc);

       // Date Search (row 1)
        dateFrom = new JDateChooser();
        dateFrom.setDateFormatString("dd/MM/yyyy");
        ((JTextField)dateFrom.getDateEditor().getUiComponent()).setEditable(true);
        dateFrom.setPreferredSize(new Dimension(100, 25));
        
        dateTo = new JDateChooser();
        dateTo.setDateFormatString("dd/MM/yyyy");
        ((JTextField)dateTo.getDateEditor().getUiComponent()).setEditable(true);
        dateTo.setPreferredSize(new Dimension(100, 25));
        
        btnSearchDate = new JButton("LỌC NGÀY");
        JPanel dateSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        dateSearch.setOpaque(false);
        dateSearch.add(new JLabel("Từ ngày:"));
        dateSearch.add(dateFrom);
        dateSearch.add(new JLabel("Đến ngày:"));
        dateSearch.add(dateTo);
        dateSearch.add(btnSearchDate);
        gbc.gridx = 5; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        toolbar.add(dateSearch, gbc);

        // Listeners
        btnLamMoi.addActionListener(e -> refreshData());
        btnSearchDate.addActionListener(e -> filterByDate());
        cbbPhanTram.addItemListener(this);
        txtSearch.addKeyListener(this);

        return toolbar;
    }

    private void setupSearchPlaceholder() {
        txtSearch.setText("Nhập nội dung tìm kiếm...");
        txtSearch.setForeground(Color.GRAY);
        txtSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtSearch.getText().equals("Nhập nội dung tìm kiếm...")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setForeground(Color.GRAY);
                    txtSearch.setText("Nhập nội dung tìm kiếm...");
                }
            }
        });
    }

      private void filterByDate() {
        Date from = dateFrom.getDate();
        Date to = dateTo.getDate();
        if (from == null || to == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn cả ngày bắt đầu và ngày kết thúc.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Đảm bảo from <= to
        if (from.after(to)) {
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Xóa dữ liệu table hiện tại
        tableModel.setRowCount(0);
        // Lọc trong danh sách listKM
        for (KhuyenMaiDTO km : listKM) {
            Date start = km.getNgayBatDau();
            Date end = km.getNgayKetThuc();
            // Kiểm tra giao nhau khoảng
            if (!start.after(to) && !end.before(from)) {
                tableModel.addRow(new Object[]{
                    km.getMaSKKhuyenMai(), km.getTenKhuyenMai(), km.getPhanTramGiam(),
                    km.getNgayBatDau(), km.getNgayKetThuc(),
                    dieuKienMap.get(km.getLoai())
                });
            }
        }
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
        if (dlg.isSaved()) {
            refreshData();
        }
    }

    private void xoaKhuyenMai() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi cần xóa!");
            return;
        }
        String ma = (String) tableModel.getValueAt(row, 0);
        int c = JOptionPane.showConfirmDialog(this, "Xóa khuyến mãi " + ma + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            try {
                if (KhuyenMaiDAO.delete(ma)) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!");
                }
                refreshData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + ex.getMessage());
            }
        }
    }

    private void suaKhuyenMai() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi cần sửa!");
            return;
        }
        String ma = (String) tableModel.getValueAt(row, 0);
        KhuyenMaiDTO km = KhuyenMaiDAO.selectById(ma);
        if (km != null) {
            SuaKhuyenMaiDialog dlg = new SuaKhuyenMaiDialog((Frame) SwingUtilities.getWindowAncestor(this), km);
            dlg.setVisible(true);
            if (dlg.isUpdated()) {
                refreshData();
            }
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
        if (search.equals("nhập nội dung tìm kiếm...")) {
            search = "";
        }
        String filter = (String) cbbPhanTram.getSelectedItem();
        List<KhuyenMaiDTO> out = new ArrayList<>();
        for (KhuyenMaiDTO km : listKM) {
            int pt = km.getPhanTramGiam();
            boolean okPT = filter.equals("Tất cả")
                    || (filter.equals("Dưới 10%") && pt < 10)
                    || (filter.equals("Từ 10% - 20%") && pt >= 10 && pt <= 20)
                    || (filter.equals("Trên 20%") && pt > 20);
            boolean okSearch = search.isEmpty()
                    || km.getMaSKKhuyenMai().toLowerCase().contains(search)
                    || km.getTenKhuyenMai().toLowerCase().contains(search)
                    || dieuKienMap.get(km.getLoai()).toLowerCase().contains(search);
            if (okPT && okSearch) {
                out.add(km);
            }
        }
        loadDataToTable(out);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            filterData();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        filterData();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }
}
