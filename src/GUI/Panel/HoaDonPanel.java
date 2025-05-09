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

public class HoaDonPanel extends JPanel implements ItemListener, KeyListener {
    private JButton btnXoa, btnChiTiet, btnExcel, btnLamMoi;
    private JComboBox<String> cbbFilter;
    private JTextField txtSearch;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<HoaDonDTO> listHD;

    public HoaDonPanel() {
        initComponent();
        loadDataFromDAO(); // Load data từ database
    }

    private void initComponent() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // ======= Toolbar ========
        JPanel toolbar = new JPanel(new BorderLayout());
        
        // Left tools
        JPanel leftToolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btnXoa = new JButton("XÓA");
        btnChiTiet = new JButton("CHI TIẾT");
        btnExcel = new JButton("XUẤT EXCEL");
        leftToolPanel.add(btnXoa);
        leftToolPanel.add(btnChiTiet);
        leftToolPanel.add(btnExcel);

        // Right tools
        JPanel rightToolPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        cbbFilter = new JComboBox<>(new String[]{"Tất cả", "Mã HĐ", "Mã NV", "Mã KH"});
        cbbFilter.addItemListener(this);
        
        txtSearch = new JTextField(15);
        txtSearch.addKeyListener(this);
        setupSearchPlaceholder(); // Xử lý placeholder
        
        btnLamMoi = new JButton("LÀM MỚI");
        
        rightToolPanel.add(cbbFilter);
        rightToolPanel.add(txtSearch);
        rightToolPanel.add(btnLamMoi);

        toolbar.add(leftToolPanel, BorderLayout.WEST);
        toolbar.add(rightToolPanel, BorderLayout.EAST);
        add(toolbar, BorderLayout.NORTH);

        // ======= Table ========
        String[] columns = {"Mã HĐ", "Mã NV", "Mã KH", "Ngày lập", "Mã KM", "Tổng tiền"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho edit trực tiếp
            }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ======= Events ========
        btnXoa.addActionListener(e -> deleteHoaDon());
        btnChiTiet.addActionListener(e -> openChiTietDialog());
        btnLamMoi.addActionListener(e -> refreshData());
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
                    match = hd.getMaHoaDon().toLowerCase().contains(searchText) ||
                            hd.getMaNhanVien().toLowerCase().contains(searchText) ||
                            hd.getMaKhachHang().toLowerCase().contains(searchText) ||
                            maKM.contains(searchText) ||
                            hd.getNgayLap().toString().contains(searchText);
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

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyPressed(KeyEvent e) {}
}