package GUI.Panel;

import BUS.NhaCungCapBUS;
import DTO.NhaCungCapDTO;
import GUI.Dialog.SuaNhaCungCapDialog;
import GUI.Dialog.ThemNhaCungCapDialog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.EmptyBorder;

public class NhaCungCapPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<String> cbbFilter;
    private final NhaCungCapBUS nccBUS = new NhaCungCapBUS();

    public NhaCungCapPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        add(taoThanhCongCu(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);

        loadDataFromDatabase();
    }
    private JPanel taoThanhCongCu() {
        JPanel thanhCongCu = new JPanel();
        thanhCongCu.setLayout(new GridBagLayout());
        thanhCongCu.setBackground(Color.WHITE);
        thanhCongCu.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 5); 

        JButton btnThem = new JButton("THÊM", new ImageIcon(getClass().getResource("/icon/them.png")));
        btnThem.setHorizontalTextPosition(SwingConstants.CENTER);
        btnThem.setVerticalTextPosition(SwingConstants.BOTTOM);
        thanhCongCu.add(btnThem, gbc);

        gbc.gridx = 1;
        JButton btnSua = new JButton("SỬA", new ImageIcon(getClass().getResource("/icon/sua.png")));
        btnSua.setHorizontalTextPosition(SwingConstants.CENTER);
        btnSua.setVerticalTextPosition(SwingConstants.BOTTOM);
        thanhCongCu.add(btnSua, gbc);

        gbc.gridx = 2;
        JButton btnXoa = new JButton("XÓA", new ImageIcon(getClass().getResource("/icon/xoa.png")));
        btnXoa.setHorizontalTextPosition(SwingConstants.CENTER);
        btnXoa.setVerticalTextPosition(SwingConstants.BOTTOM);
        thanhCongCu.add(btnXoa, gbc);

        gbc.gridx = 3;
        JButton btnExcel = new JButton("XUẤT EXCEL", new ImageIcon(getClass().getResource("/icon/xuatexcel.png")));
        btnExcel.setHorizontalTextPosition(SwingConstants.CENTER);
        btnExcel.setVerticalTextPosition(SwingConstants.BOTTOM);
        thanhCongCu.add(btnExcel, gbc);

        gbc.gridx = 4;
        gbc.weightx = 1.0;
        thanhCongCu.add(Box.createHorizontalGlue(), gbc);

        JPanel panelTimKiem = new JPanel();
        panelTimKiem.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panelTimKiem.setOpaque(false);

        JLabel lblBoLoc = new JLabel("Lọc theo:");
        cbbFilter = new JComboBox<>(new String[]{"Tất cả", "Mã NCC", "Tên NCC"});
        cbbFilter.setPreferredSize(new Dimension(110, 25));

        JLabel lblTimKiem = new JLabel("Tìm kiếm:");
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(180, 25));

        JButton btnLamMoi = new JButton("LÀM MỚI");

        panelTimKiem.add(lblBoLoc);
        panelTimKiem.add(cbbFilter);
        panelTimKiem.add(lblTimKiem);
        panelTimKiem.add(txtSearch);
        panelTimKiem.add(btnLamMoi);

        gbc.gridx = 5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        thanhCongCu.add(panelTimKiem, gbc);

        btnThem.addActionListener(e -> {
            ThemNhaCungCapDialog dialog = new ThemNhaCungCapDialog((Window) SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);
            if (dialog.isAdded()) loadDataFromDatabase();
        });

        // Sự kiện nút SỬA
        btnSua.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhà cung cấp để sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String ma = table.getValueAt(selectedRow, 0).toString();
            String ten = table.getValueAt(selectedRow, 1).toString();
            String diachi = table.getValueAt(selectedRow, 2).toString();
            String sdt = table.getValueAt(selectedRow, 3).toString();
            NhaCungCapDTO ncc = new NhaCungCapDTO(ma, ten, diachi, sdt);

            SuaNhaCungCapDialog dialog = new SuaNhaCungCapDialog((Window) SwingUtilities.getWindowAncestor(this), ncc);
            dialog.setVisible(true);
            if (dialog.isUpdated()) loadDataFromDatabase();
        });

        // Sự kiện nút XÓA
        btnXoa.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhà cung cấp để xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String maNCC = table.getValueAt(selectedRow, 0).toString();
            if (nccBUS.xoa(maNCC)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công.");
                loadDataFromDatabase();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Sự kiện nút XUẤT EXCEL
        btnExcel.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Chức năng xuất Excel đang được phát triển", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        });

        // Sự kiện lọc và tìm kiếm
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { search(); }
            public void removeUpdate(DocumentEvent e) { search(); }
            public void changedUpdate(DocumentEvent e) { search(); }

            private void search() {
                String keyword = txtSearch.getText().trim();
                String filter = cbbFilter.getSelectedItem().toString();
                List<NhaCungCapDTO> results = new ArrayList<>();
                // Áp dụng tìm kiếm theo từ khóa
                // ...
                loadDataToTable(results);
            }
        });

        // Sự kiện nút LÀM MỚI
        btnLamMoi.addActionListener(e -> {
            txtSearch.setText("");
            cbbFilter.setSelectedIndex(0);
            loadDataFromDatabase();
        });

        return thanhCongCu;
    }


    private JScrollPane createTablePanel() {
        tableModel = new DefaultTableModel(new Object[]{"Mã NCC", "Tên NCC", "Địa chỉ", "SĐT"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));

        setTableCellAlignment();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return scrollPane;
    }

    private void loadDataFromDatabase() {
        List<NhaCungCapDTO> list = nccBUS.layTatCa();
        loadDataToTable(list);
    }

    private void loadDataToTable(List<NhaCungCapDTO> list) {
        tableModel.setRowCount(0);
        for (NhaCungCapDTO ncc : list) {
            tableModel.addRow(new Object[]{
                ncc.getMaNCC(), ncc.getTenNCC(), ncc.getDiaChiNCC(), ncc.getSdtNCC()
            });
        }
    }

    private void setTableCellAlignment() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }


}
