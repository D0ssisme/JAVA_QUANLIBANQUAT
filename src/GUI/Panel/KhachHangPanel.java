package GUI.Panel;

import DAO.KhachHangDAO;
import DTO.KhachHangDTO;
import GUI.Dialog.ThemKhachHangDialog;
import GUI.Dialog.SuaKhachHangDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.table.DefaultTableCellRenderer;

public class KhachHangPanel extends JPanel {

    private JButton btnThem, btnXoa, btnSua, btnLamMoi;
    private JTextField txtSearch;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtPriceFrom, txtPriceTo;
    private JButton btnSearch;
    private final TableRowSorter<DefaultTableModel> sorter;
    private KhachHangDAO khachHangDAO = new KhachHangDAO();
    private List<KhachHangDTO> listKH;

    public KhachHangPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // === Toolbar ===
        JPanel toolbar = createButtonPanel();
        add(toolbar, BorderLayout.NORTH);

        // === Table ===
        String[] columns = {"Mã KH", "Tên KH", "SĐT", "Địa chỉ", "Tổng tiền đã mua", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                // Lấy giá trị TrangThai từ model
                String status = (String) table.getModel().getValueAt(row, 5);
                if ("Inactive".equals(status)) {
                    c.setBackground(isSelected ? Color.DARK_GRAY : Color.LIGHT_GRAY);
                    c.setForeground(Color.WHITE);
                } else {
                    // active: màu bình thường
                    c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                    c.setForeground(table.getForeground());
                }
                return c;
            }
        });
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Load dữ liệu
        loadDataFromDB();
    }

    private void applyAdvancedFilter() {
        try {
            // Lấy danh sách tất cả khách hàng
            List<KhachHangDTO> filteredList = new ArrayList<>(listKH);

            // Lọc theo tổng tiền nếu được nhập
            String fromPriceText = txtPriceFrom.getText().trim();
            String toPriceText = txtPriceTo.getText().trim();

            if (!fromPriceText.isEmpty() && !toPriceText.isEmpty()) {
                try {
                    double fromPrice = Double.parseDouble(fromPriceText);
                    double toPrice = Double.parseDouble(toPriceText);

                    // Đảm bảo fromPrice <= toPrice
                    if (fromPrice > toPrice) {
                        JOptionPane.showMessageDialog(this,
                                "Giá bắt đầu phải nhỏ hơn hoặc bằng giá kết thúc!",
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Lọc theo khoảng tổng tiền
                    List<KhachHangDTO> priceFiltered = new ArrayList<>();
                    for (KhachHangDTO kh : filteredList) {
                        double totalSpent = kh.getTongTienDaMua();
                        if (totalSpent >= fromPrice && totalSpent <= toPrice) {
                            priceFiltered.add(kh);
                        }
                    }

                    filteredList = priceFiltered;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Giá phải là số!",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (!fromPriceText.isEmpty() || !toPriceText.isEmpty()) {
                // Only one price field filled
                JOptionPane.showMessageDialog(this,
                        "Vui lòng nhập đầy đủ khoảng giá!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Hiển thị kết quả lọc
            loadFilteredData(filteredList);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi lọc dữ liệu: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createButtonPanel() {
        // Tạo một panel chứa toàn bộ toolbar
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BorderLayout(0, 5)); // Giảm khoảng cách dọc
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Panel trên chứa các buttons và search cơ bản
        JPanel topPanel = new JPanel(new BorderLayout(5, 0));
        topPanel.setBackground(Color.WHITE);

        // Panel chứa các nút chức năng
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        // Tạo các buttons với icons 
        ImageIcon iconThem = new ImageIcon(getClass().getResource("/icon/them.png"));
        btnThem = new JButton("THÊM", iconThem);
        btnThem.setHorizontalTextPosition(SwingConstants.CENTER);
        btnThem.setVerticalTextPosition(SwingConstants.BOTTOM);
        buttonPanel.add(btnThem);

        ImageIcon iconXoa = new ImageIcon(getClass().getResource("/icon/xoa.png"));
        btnXoa = new JButton("XÓA", iconXoa);
        btnXoa.setHorizontalTextPosition(SwingConstants.CENTER);
        btnXoa.setVerticalTextPosition(SwingConstants.BOTTOM);
        buttonPanel.add(btnXoa);

        ImageIcon iconSua = new ImageIcon(getClass().getResource("/icon/sua.png"));
        btnSua = new JButton("SỬA", iconSua);
        btnSua.setHorizontalTextPosition(SwingConstants.CENTER);
        btnSua.setVerticalTextPosition(SwingConstants.BOTTOM);
        buttonPanel.add(btnSua);

        ImageIcon iconToggle = new ImageIcon(getClass().getResource("/icon/Toggle.png"));
        JButton btnToggle = new JButton("TRẠNG THÁI", iconToggle);
        btnToggle.setHorizontalTextPosition(SwingConstants.CENTER);
        btnToggle.setVerticalTextPosition(SwingConstants.BOTTOM);
        buttonPanel.add(btnToggle);

        topPanel.add(buttonPanel, BorderLayout.WEST);

        // Panel chứa các thành phần tìm kiếm - Cải tiến
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        searchPanel.setBackground(Color.WHITE);

        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setPreferredSize(new Dimension(65, 25));

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(180, 25));

        btnLamMoi = new JButton("LÀM MỚI");
        btnLamMoi.setPreferredSize(new Dimension(100, 26));

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnLamMoi);

        topPanel.add(searchPanel, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel(new BorderLayout(0, 0));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        // Panel bên trái (trống, để căn chỉnh)
        JPanel leftPlaceholder = new JPanel();
        leftPlaceholder.setBackground(Color.WHITE);
        leftPlaceholder.setPreferredSize(buttonPanel.getPreferredSize());
        bottomPanel.add(leftPlaceholder, BorderLayout.WEST);

        // Panel tìm kiếm nâng cao - căn chỉnh giống phần search ở trên
        JPanel advancedSearchPanel = new JPanel();
        advancedSearchPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        advancedSearchPanel.setBackground(Color.WHITE);

        // Panel giá
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pricePanel.setBackground(Color.WHITE);

        JLabel lblPriceFrom = new JLabel("Tổng tiền từ:");
        lblPriceFrom.setPreferredSize(new Dimension(80, 25));
        pricePanel.add(lblPriceFrom);

        txtPriceFrom = new JTextField(7);
        pricePanel.add(txtPriceFrom);

        JLabel lblPriceTo = new JLabel("đến:");
        lblPriceTo.setPreferredSize(new Dimension(35, 25));
        pricePanel.add(lblPriceTo);

        txtPriceTo = new JTextField(7);
        pricePanel.add(txtPriceTo);

        advancedSearchPanel.add(pricePanel);

        // Panel nút lọc
        JPanel buttonFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonFilterPanel.setBackground(Color.WHITE);

        btnSearch = new JButton("LỌC");
        btnSearch.setPreferredSize(new Dimension(80, 26));
        btnSearch.addActionListener(e -> applyAdvancedFilter());
        buttonFilterPanel.add(btnSearch);

        advancedSearchPanel.add(buttonFilterPanel);

        bottomPanel.add(advancedSearchPanel, BorderLayout.EAST);

        // Add everything to the main toolbar
        toolbar.add(topPanel, BorderLayout.NORTH);
        toolbar.add(bottomPanel, BorderLayout.CENTER);

        // === Sự kiện nút ===
        btnThem.addActionListener(e -> {
            openThemKhachHangDialog();
        });

        btnXoa.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String maKH = tableModel.getValueAt(
                    table.convertRowIndexToModel(row), 0).toString();

            if (JOptionPane.showConfirmDialog(this,
                    "Xóa khách hàng " + maKH + "?", "Xác nhận",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (khachHangDAO.delete(maKH)) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    loadDataFromDB();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnSua.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String maKH = tableModel.getValueAt(
                    table.convertRowIndexToModel(row), 0).toString();
            KhachHangDTO kh = khachHangDAO.selectById(maKH);
            SuaKhachHangDialog dlg = new SuaKhachHangDialog((JFrame) SwingUtilities.getWindowAncestor(this), kh);
            dlg.setVisible(true);
            if (dlg.isSaved()) {
                loadDataFromDB();
            }
        });

        btnToggle.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String maKH = tableModel.getValueAt(table.convertRowIndexToModel(row), 0).toString();
            String status = tableModel.getValueAt(table.convertRowIndexToModel(row), 5).toString();
            int newStatus = "Active".equals(status) ? 0 : 1;

            // gọi DAO để cập nhật
            if (khachHangDAO.updateStatus(maKH, newStatus)) {
                JOptionPane.showMessageDialog(this, "Thay đổi trạng thái thành công!");
                loadDataFromDB();
            } else {
                JOptionPane.showMessageDialog(this, "Thay đổi trạng thái thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnLamMoi.addActionListener(e -> {
            txtSearch.setText("");
            sorter.setRowFilter(null);

            // Reset các trường tìm kiếm nâng cao
            txtPriceFrom.setText("");
            txtPriceTo.setText("");

            loadDataFromDB(); // Tải lại dữ liệu từ database
        });

        // === Tìm kiếm + lọc ===
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applyFilter();
            }
        });

        return toolbar;
    }

    private void applyFilter() {
        List<RowFilter<DefaultTableModel, Integer>> filters = new ArrayList<>();
        // filter theo text
        String text = txtSearch.getText().trim();
        if (!text.isEmpty()) {
            try {
                filters.add(RowFilter.regexFilter("(?i)" + Pattern.quote(text)));
            } catch (PatternSyntaxException ex) {
            }
        }
        if (filters.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.andFilter(filters));
        }
    }
    // Mở dialog thêm khách hàng
    public void openThemKhachHangDialog() {
        ThemKhachHangDialog dlg = new ThemKhachHangDialog();
        dlg.setVisible(true);
        if (dlg.isSaved()) {
            loadDataFromDB(); // Load lại dữ liệu sau khi thêm
        }
    }
    private void loadDataFromDB() {
        listKH = KhachHangDAO.selectAll();
        tableModel.setRowCount(0);
        for (KhachHangDTO kh : listKH) {
            String status = kh.getTrangThai() == 1 ? "Active" : "Inactive";
            tableModel.addRow(new Object[]{
                kh.getMaKhachHang(),
                kh.getHoTenKH(),
                kh.getSdtKH(),
                kh.getDiaChiKH(),
                kh.getTongTienDaMua(),
                status
            });
        }
    }

    private void loadFilteredData(List<KhachHangDTO> data) {
        tableModel.setRowCount(0);
        for (KhachHangDTO kh : data) {
            String status = kh.getTrangThai() == 1 ? "Active" : "Inactive";
            tableModel.addRow(new Object[]{
                kh.getMaKhachHang(),
                kh.getHoTenKH(),
                kh.getSdtKH(),
                kh.getDiaChiKH(),
                kh.getTongTienDaMua(),
                status
            });
        }
    }
}
