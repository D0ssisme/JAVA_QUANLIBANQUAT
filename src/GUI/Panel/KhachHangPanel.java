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
import javax.swing.table.JTableHeader;

public class KhachHangPanel extends JPanel {

    private JButton btnThem, btnXoa, btnSua, btnToggle;
    private JTextField txtSearch;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtPriceFrom, txtPriceTo;
    private JButton btnSearch, btnLamMoi;
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
        
        // Thiết lập và styling của table
        table.setFont(new Font("Arial", Font.PLAIN, 14)); 
        table.setRowHeight(30);
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14)); 
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 50));
        
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
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBackground(Color.WHITE);

        // LEFT TOOL PANEL - Chứa các nút chức năng (Thêm, Xóa, Sửa, Trạng thái)
        JPanel leftToolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftToolPanel.setOpaque(false);

        ImageIcon iconThem = new ImageIcon(getClass().getResource("/icon/them.png"));
        btnThem = new JButton("THÊM", iconThem);
        btnThem.setHorizontalTextPosition(SwingConstants.CENTER);
        btnThem.setVerticalTextPosition(SwingConstants.BOTTOM);

        ImageIcon iconXoa = new ImageIcon(getClass().getResource("/icon/xoa.png"));
        btnXoa = new JButton("XÓA", iconXoa);
        btnXoa.setHorizontalTextPosition(SwingConstants.CENTER);
        btnXoa.setVerticalTextPosition(SwingConstants.BOTTOM);

        ImageIcon iconSua = new ImageIcon(getClass().getResource("/icon/sua.png"));
        btnSua = new JButton("SỬA", iconSua);
        btnSua.setHorizontalTextPosition(SwingConstants.CENTER);
        btnSua.setVerticalTextPosition(SwingConstants.BOTTOM);

        ImageIcon iconToggle = new ImageIcon(getClass().getResource("/icon/Toggle.png"));
        btnToggle = new JButton("TRẠNG THÁI", iconToggle);
        btnToggle.setHorizontalTextPosition(SwingConstants.CENTER);
        btnToggle.setVerticalTextPosition(SwingConstants.BOTTOM);

        leftToolPanel.add(btnThem);
        leftToolPanel.add(btnXoa);
        leftToolPanel.add(btnSua);
        leftToolPanel.add(btnToggle);

        JPanel leftWrapper = new JPanel();
        leftWrapper.setLayout(new BoxLayout(leftWrapper, BoxLayout.Y_AXIS));
        leftWrapper.setOpaque(false);
        leftWrapper.add(Box.createVerticalGlue());
        leftWrapper.add(leftToolPanel);
        leftWrapper.add(Box.createVerticalGlue());

        // RIGHT TOOL PANEL - Chứa tìm kiếm và lọc
        JPanel rightToolPanel = new JPanel();
        rightToolPanel.setLayout(new BoxLayout(rightToolPanel, BoxLayout.Y_AXIS));
        rightToolPanel.setOpaque(false);
        
        JPanel filterPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        filterPanel.setOpaque(false);

        // Hàng 1: Tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        searchPanel.setOpaque(false);
        
        JLabel lblSearch = new JLabel("Tìm kiếm:");
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(215, 25));
        
        btnLamMoi = new JButton("LÀM MỚI");
        
        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnLamMoi);

        // Hàng 2: Lọc theo tổng tiền
        JPanel priceFilterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        priceFilterPanel.setOpaque(false);
        
        JLabel lblPriceFrom = new JLabel("Tổng tiền từ:");
        txtPriceFrom = new JTextField(7);
        
        JLabel lblPriceTo = new JLabel("đến:");
        txtPriceTo = new JTextField(7);
        
        btnSearch = new JButton("LỌC");
        btnSearch.setPreferredSize(new Dimension(80, 26));
        
        priceFilterPanel.add(lblPriceFrom);
        priceFilterPanel.add(txtPriceFrom);
        priceFilterPanel.add(lblPriceTo);
        priceFilterPanel.add(txtPriceTo);
        priceFilterPanel.add(btnSearch);

        filterPanel.add(searchPanel);
        filterPanel.add(priceFilterPanel);
        rightToolPanel.add(filterPanel);

        JPanel rightWrapper = new JPanel();
        rightWrapper.setLayout(new BoxLayout(rightWrapper, BoxLayout.Y_AXIS));
        rightWrapper.setOpaque(false);
        rightWrapper.add(Box.createVerticalGlue());
        rightWrapper.add(rightToolPanel);
        rightWrapper.add(Box.createVerticalGlue());

        toolbar.add(leftWrapper, BorderLayout.WEST);
        toolbar.add(rightWrapper, BorderLayout.EAST);

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
        
        btnSearch.addActionListener(e -> applyAdvancedFilter());

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