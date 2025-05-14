package GUI.Panel;

import DAO.HoaDonDAO;
import DTO.HoaDonDTO;
import GUI.Dialog.ChiTietHoaDonDialog;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.border.EmptyBorder;

public class HoaDonPanel extends JPanel implements ItemListener {

    private JButton btnXoa, btnChiTiet, btnExcel, btnLamMoi;
    private JComboBox<String> cbbFilter;
    private JTextField txtSearch;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<HoaDonDTO> listHD;
    private JCheckBox chkDateFilter, chkPriceFilter;
    private JDateChooser dateFrom, dateTo;
    private JTextField txtPriceFrom, txtPriceTo;
    private JButton btnSearch;

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

    private void applyAdvancedFilter() {
        try {
            // Lấy danh sách tất cả hóa đơn
            List<HoaDonDTO> filteredList = new ArrayList<>(listHD);

            // Lọc theo ngày nếu có ngày được chọn
            Date fromDate = dateFrom.getDate();
            Date toDate = dateTo.getDate();

            if (fromDate != null && toDate != null) {
                if (fromDate.after(toDate)) {
                    JOptionPane.showMessageDialog(this,
                            "Ngày bắt đầu phải trước hoặc bằng ngày kết thúc!",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Lọc theo khoảng ngày
                List<HoaDonDTO> dateFiltered = new ArrayList<>();
                for (HoaDonDTO hd : filteredList) {
                    Date orderDate = new Date(hd.getNgayLap().getTime());
                    if ((orderDate.equals(fromDate) || orderDate.after(fromDate))
                            && (orderDate.equals(toDate) || orderDate.before(toDate))) {
                        dateFiltered.add(hd);
                    }
                }
                filteredList = dateFiltered;
            } else if (fromDate != null || toDate != null) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn đầy đủ khoảng ngày!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Lọc theo giá nếu có giá được nhập
            String fromPriceText = txtPriceFrom.getText().trim();
            String toPriceText = txtPriceTo.getText().trim();

            if (!fromPriceText.isEmpty() && !toPriceText.isEmpty()) {
                try {
                    int fromPrice = Integer.parseInt(fromPriceText);
                    int toPrice = Integer.parseInt(toPriceText);
                    if (fromPrice > toPrice) {
                        JOptionPane.showMessageDialog(this,
                                "Giá bắt đầu phải nhỏ hơn hoặc bằng giá kết thúc!",
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Lọc theo khoảng giá
                    List<HoaDonDTO> priceFiltered = new ArrayList<>();
                    for (HoaDonDTO hd : filteredList) {
                        int price = hd.getTongTien();
                        if (price >= fromPrice && price <= toPrice) {
                            priceFiltered.add(hd);
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
                JOptionPane.showMessageDialog(this,
                        "Vui lòng nhập đầy đủ khoảng giá!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Hiển thị kết quả lọc
            loadDataToTable(filteredList);
        } catch (HeadlessException ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi lọc dữ liệu: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createButtonPanel() {
        // Tạo panel chính cho toolbar
        JPanel toolbar = new JPanel(new BorderLayout(0, 5));
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Panel trên cho các nút và tìm kiếm cơ bản
        JPanel topPanel = new JPanel(new BorderLayout(5, 0));
        topPanel.setBackground(Color.WHITE);

        // Panel cho các nút chức năng
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        ImageIcon iconXoa = new ImageIcon(getClass().getResource("/icon/xoa.png"));
        btnXoa = new JButton("XÓA", iconXoa);
        btnXoa.setHorizontalTextPosition(SwingConstants.CENTER);
        btnXoa.setVerticalTextPosition(SwingConstants.BOTTOM);
        buttonPanel.add(btnXoa);

        ImageIcon iconChiTiet = new ImageIcon(getClass().getResource("/icon/chitiet.png"));
        btnChiTiet = new JButton("CHI TIẾT", iconChiTiet);
        btnChiTiet.setHorizontalTextPosition(SwingConstants.CENTER);
        btnChiTiet.setVerticalTextPosition(SwingConstants.BOTTOM);
        buttonPanel.add(btnChiTiet);

        ImageIcon iconExcel = new ImageIcon(getClass().getResource("/icon/xuatexcel.png"));
        btnExcel = new JButton("XUẤT EXCEL", iconExcel);
        btnExcel.setHorizontalTextPosition(SwingConstants.CENTER);
        btnExcel.setVerticalTextPosition(SwingConstants.BOTTOM);
        buttonPanel.add(btnExcel);

        topPanel.add(buttonPanel, BorderLayout.WEST);

        // Panel cho tìm kiếm cơ bản
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        searchPanel.setBackground(Color.WHITE);

        JLabel lblFilter = new JLabel("Lọc:");
        cbbFilter = new JComboBox<>(new String[]{"Tất cả", "Mã HĐ", "Mã NV", "Mã KH"});
        cbbFilter.setPreferredSize(new Dimension(100, 25));
        cbbFilter.addItemListener(this);

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(180, 25));
        setupSearchPlaceholder();

        btnLamMoi = new JButton("LÀM MỚI");
        btnLamMoi.setPreferredSize(new Dimension(100, 26));

        searchPanel.add(lblFilter);
        searchPanel.add(cbbFilter);
        searchPanel.add(txtSearch);
        searchPanel.add(btnLamMoi);

        topPanel.add(searchPanel, BorderLayout.EAST);

        // Panel dưới cho tìm kiếm nâng cao
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm nâng cao"));

        // Thêm khoảng trống bên trái để căn chỉnh với tìm kiếm cơ bản
        bottomPanel.add(Box.createHorizontalStrut(150)); // Điều chỉnh giá trị này để thẳng hàng với txtSearch

        // Tìm kiếm theo giá
        JLabel lblPriceFrom = new JLabel("Giá từ:");
        lblPriceFrom.setPreferredSize(new Dimension(60, 25));
        bottomPanel.add(lblPriceFrom);

        txtPriceFrom = new JTextField(10);
        txtPriceFrom.setPreferredSize(new Dimension(100, 25));
        bottomPanel.add(txtPriceFrom);

        JLabel lblPriceTo = new JLabel("đến:");
        lblPriceTo.setPreferredSize(new Dimension(40, 25));
        bottomPanel.add(lblPriceTo);

        txtPriceTo = new JTextField(10);
        txtPriceTo.setPreferredSize(new Dimension(100, 25));
        bottomPanel.add(txtPriceTo);

        bottomPanel.add(Box.createHorizontalStrut(10)); // Khoảng cách giữa giá và ngày

        // Tìm kiếm theo ngày
        JLabel lblDateFrom = new JLabel("Từ ngày:");
        lblDateFrom.setPreferredSize(new Dimension(60, 25));
        bottomPanel.add(lblDateFrom);

        dateFrom = new JDateChooser();
        dateFrom.setPreferredSize(new Dimension(100, 25));
        dateFrom.setDateFormatString("dd/MM/yyyy");
        bottomPanel.add(dateFrom);

        JLabel lblDateTo = new JLabel("Đến ngày:");
        lblDateTo.setPreferredSize(new Dimension(60, 25));
        bottomPanel.add(lblDateTo);

        dateTo = new JDateChooser();
        dateTo.setPreferredSize(new Dimension(100, 25));
        dateTo.setDateFormatString("dd/MM/yyyy");
        bottomPanel.add(dateTo);

        bottomPanel.add(Box.createHorizontalStrut(10)); // Khoảng cách trước nút LỌC

        // Nút LỌC
        btnSearch = new JButton("LỌC");
        btnSearch.setPreferredSize(new Dimension(80, 26));
        btnSearch.addActionListener(e -> applyAdvancedFilter());
        bottomPanel.add(btnSearch);

        // Thêm các panel vào toolbar
        toolbar.add(topPanel, BorderLayout.NORTH);
        toolbar.add(bottomPanel, BorderLayout.CENTER);

        // Thêm sự kiện cho các nút
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
            listHD = HoaDonDAO.selectAll();
            loadDataToTable(listHD);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    private void loadDataToTable(List<HoaDonDTO> data) {
        tableModel.setRowCount(0);
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
        dateFrom.setDate(null);
        dateTo.setDate(null);
        txtPriceFrom.setText("");
        txtPriceTo.setText("");
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

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            filterData();
        }
    }
}
