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

    private JButton btnThem, btnXoa, btnSua, btnExcel, btnLamMoi;
    private JTextField txtSearch;
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
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

    private JPanel createButtonPanel() {
        // Tạo một panel chứa toàn bộ toolbar
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new GridBagLayout()); // Sử dụng GridBagLayout để căn chỉnh chính xác
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 5); // Khoảng cách giữa các nút

        // Tạo các buttons với icons 
        ImageIcon iconThem = new ImageIcon(getClass().getResource("/icon/them.png"));
        btnThem = new JButton("THÊM", iconThem);
        btnThem.setHorizontalTextPosition(SwingConstants.CENTER);
        btnThem.setVerticalTextPosition(SwingConstants.BOTTOM);
        toolbar.add(btnThem, gbc);

        gbc.gridx = 1;
        ImageIcon iconXoa = new ImageIcon(getClass().getResource("/icon/xoa.png"));
        btnXoa = new JButton("XÓA", iconXoa);
        btnXoa.setHorizontalTextPosition(SwingConstants.CENTER);
        btnXoa.setVerticalTextPosition(SwingConstants.BOTTOM);
        toolbar.add(btnXoa, gbc);

        gbc.gridx = 2;
        ImageIcon iconSua = new ImageIcon(getClass().getResource("/icon/sua.png"));
        btnSua = new JButton("SỬA", iconSua);
        btnSua.setHorizontalTextPosition(SwingConstants.CENTER);
        btnSua.setVerticalTextPosition(SwingConstants.BOTTOM);
        toolbar.add(btnSua, gbc);

        gbc.gridx = 3;
        ImageIcon iconExcel = new ImageIcon(getClass().getResource("/icon/xuatexcel.png"));
        btnExcel = new JButton("XUẤT EXCEL", iconExcel);
        btnExcel.setHorizontalTextPosition(SwingConstants.CENTER);
        btnExcel.setVerticalTextPosition(SwingConstants.BOTTOM);
        toolbar.add(btnExcel, gbc);

        gbc.gridx = 4;
        ImageIcon iconToggle = new ImageIcon(getClass().getResource("/icon/Toggle.png"));
        JButton btnToggle = new JButton("Chuyển TT", iconToggle);
        btnToggle.setHorizontalTextPosition(SwingConstants.CENTER);
        btnToggle.setVerticalTextPosition(SwingConstants.BOTTOM);
        toolbar.add(btnToggle, gbc);

        // Phần tìm kiếm bên phải
        gbc.gridx = 4;
        gbc.weightx = 1.0; // Phần này sẽ chiếm khoảng trống còn lại
        toolbar.add(Box.createHorizontalGlue(), gbc); // Tạo khoảng trống giữa các nút và phần tìm kiếm

        // Panel chứa các thành phần tìm kiếm
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0)); // Căn phải, khoảng cách 5px
        searchPanel.setOpaque(false);

        // Thêm các thành phần vào panel tìm kiếm
        JLabel lblSearch = new JLabel("Tìm kiếm:");
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(180, 25));

        btnLamMoi = new JButton("LÀM MỚI");

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnLamMoi);

        gbc.gridx = 5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        toolbar.add(searchPanel, gbc);

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
                JOptionPane.showMessageDialog(this, "Chuyển trạng thái thành công!");
                loadDataFromDB();
            } else {
                JOptionPane.showMessageDialog(this, "Chuyển trạng thái thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnExcel.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Chức năng xuất Excel đang phát triển");
        });

        btnLamMoi.addActionListener(e -> {
            txtSearch.setText("");;
            sorter.setRowFilter(null);
        });

        // === Tìm kiếm + lọc ===
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                applyFilter();
            }

            public void removeUpdate(DocumentEvent e) {
                applyFilter();
            }

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
                // bỏ qua nếu không hợp lệ
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

}
