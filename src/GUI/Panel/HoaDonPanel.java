package GUI.Panel;

import DAO.HoaDonDAO;
import DTO.HoaDonDTO;
import GUI.Dialog.ChiTietHoaDonDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.EmptyBorder;

public class HoaDonPanel extends JPanel implements ItemListener, KeyListener {

    private JButton btnXoa, btnChiTiet, btnExcel, btnLamMoi;
    private JComboBox<String> cbbFilter;
    private JTextField txtSearch;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<HoaDonDTO> listHD;

    public HoaDonPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // === Toolbar ===
        JPanel toolbar = createButtonPanel();
        add(toolbar, BorderLayout.NORTH);

        // === Table ===
        String[] columns = {"Mã HĐ", "Mã NV", "Mã KH", "Ngày lập", "Mã KM", "Tổng tiền"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Load dữ liệu
        loadDataFromDAO();
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

        // Button "Xóa" with icon
        ImageIcon iconXoa = new ImageIcon(getClass().getResource("/icon/xoa.png"));
        btnXoa = new JButton("XÓA", iconXoa);
        btnXoa.setHorizontalTextPosition(SwingConstants.CENTER);
        btnXoa.setVerticalTextPosition(SwingConstants.BOTTOM);
        toolbar.add(btnXoa, gbc);

        // Button "Chi tiết" with icon
        gbc.gridx = 1;
        ImageIcon iconChiTiet = new ImageIcon(getClass().getResource("/icon/chitiet.png"));
        btnChiTiet = new JButton("CHI TIẾT", iconChiTiet);
        btnChiTiet.setHorizontalTextPosition(SwingConstants.CENTER);
        btnChiTiet.setVerticalTextPosition(SwingConstants.BOTTOM);
        toolbar.add(btnChiTiet, gbc);

        // Button "Xuất Excel" with icon
        gbc.gridx = 2;
        ImageIcon iconExcel = new ImageIcon(getClass().getResource("/icon/xuatexcel.png"));
        btnExcel = new JButton("XUẤT EXCEL", iconExcel);
        btnExcel.setHorizontalTextPosition(SwingConstants.CENTER);
        btnExcel.setVerticalTextPosition(SwingConstants.BOTTOM);
        toolbar.add(btnExcel, gbc);

        // Phần tìm kiếm bên phải
        gbc.gridx = 3;
        gbc.weightx = 1.0; // Phần này sẽ chiếm khoảng trống còn lại
        toolbar.add(Box.createHorizontalGlue(), gbc); // Tạo khoảng trống giữa các nút và phần tìm kiếm

        // Panel chứa các thành phần tìm kiếm
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0)); // Căn phải, khoảng cách 5px
        searchPanel.setOpaque(false);

        // Thêm các thành phần vào panel tìm kiếm
        JLabel lblFilter = new JLabel("Lọc:");
        cbbFilter = new JComboBox<>(new String[]{"Tất cả", "Mã HĐ", "Mã NV", "Mã KH"});
        cbbFilter.addItemListener(this);
        
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(180, 25));
        setupSearchPlaceholder();
        txtSearch.addKeyListener(this);
        
        btnLamMoi = new JButton("LÀM MỚI");

        searchPanel.add(lblFilter);
        searchPanel.add(cbbFilter);
        searchPanel.add(txtSearch);
        searchPanel.add(btnLamMoi);

        gbc.gridx = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        toolbar.add(searchPanel, gbc);

        // === Sự kiện nút ===
        btnXoa.addActionListener(e -> deleteHoaDon());
        btnChiTiet.addActionListener(e -> openChiTietDialog());
        btnExcel.addActionListener(e -> JOptionPane.showMessageDialog(this, "Chức năng xuất Excel đang phát triển"));
        btnLamMoi.addActionListener(e -> refreshData());

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

    private void loadDataFromDAO() {
        try {
            listHD = HoaDonDAO.selectAll(); // Lấy toàn bộ data từ database
            loadDataToTable(listHD);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    private void loadDataToTable(List<HoaDonDTO> data) {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ
        for (HoaDonDTO hd : data) {
            tableModel.addRow(new Object[]{
                hd.getMaHoaDon(),
                hd.getMaNhanVien(),
                hd.getMaKhachHang(),
                hd.getNgayLap(),
                hd.getMaSuKienKM() != null ? hd.getMaSuKienKM() : "",
                String.format("%,d VND", hd.getTongTien())
            });
        }
    }

    private void deleteHoaDon() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần xóa!");
            return;
        }

        String maHD = (String) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Xóa hóa đơn " + maHD + "?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int result = HoaDonDAO.delete(maHD);
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    refreshData();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + e.getMessage());
            }
        }
    }

    private void openChiTietDialog() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn!");
            return;
        }

        String maHD = (String) tableModel.getValueAt(row, 0);
        ChiTietHoaDonDialog dlg = new ChiTietHoaDonDialog(
                SwingUtilities.getWindowAncestor(this),
                maHD
        );
        dlg.setVisible(true);

        // Refresh data sau khi dialog đóng
        if (dlg.isDataUpdated()) {
            refreshData();
        }
    }

    private void refreshData() {
        loadDataFromDAO();
        txtSearch.setText("Nhập nội dung tìm kiếm...");
        txtSearch.setForeground(Color.GRAY);
        cbbFilter.setSelectedIndex(0);
    }

    private void filterData() {
        String searchText = txtSearch.getText().trim().toLowerCase();
        if (searchText.equals("nhập nội dung tìm kiếm...")) {
            searchText = "";
        }

        String filterType = (String) cbbFilter.getSelectedItem();
        List<HoaDonDTO> filteredList = new ArrayList<>();

        for (HoaDonDTO hd : listHD) {
            boolean match = false;
            String maKM = hd.getMaSuKienKM() != null ? hd.getMaSuKienKM().toLowerCase() : "";

            switch (filterType) {
                case "Mã HĐ":
                    match = hd.getMaHoaDon().toLowerCase().contains(searchText);
                    break;
                case "Mã NV":
                    match = hd.getMaNhanVien().toLowerCase().contains(searchText);
                    break;
                case "Mã KH":
                    match = hd.getMaKhachHang().toLowerCase().contains(searchText);
                    break;
                case "Tất cả":
                default:
                    match = hd.getMaHoaDon().toLowerCase().contains(searchText)
                            || hd.getMaNhanVien().toLowerCase().contains(searchText)
                            || hd.getMaKhachHang().toLowerCase().contains(searchText)
                            || maKM.contains(searchText)
                            || hd.getNgayLap().toString().contains(searchText);
                    break;
            }

            if (match) {
                filteredList.add(hd);
            }
        }

        loadDataToTable(filteredList);
    }

    // ======= Implement interfaces ========
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