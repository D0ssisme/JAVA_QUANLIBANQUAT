package GUI.Panel;

import BUS.TaiKhoanBUS;
import DTO.TaiKhoanDTO;
import GUI.Dialog.ThemTaiKhoanDialog;
import GUI.Dialog.SuaTaiKhoanDialog;
import GUI.LoginFrame;

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

public class TaiKhoanPanel extends JPanel {
    private final TaiKhoanBUS bll = new TaiKhoanBUS();
    private final DefaultTableModel model;
    private final JTable tbl;
    private final TableRowSorter<DefaultTableModel> sorter;

    private JComboBox<String> cbbFilter;
    private JTextField txtSearch;
    private JButton btnThem, btnSua, btnXoa, btnExcel, btnLamMoi, btnDangXuat;
    
    // Thêm biến để xác định panel được mở từ Admin hay không
    private boolean isAdmin;

    // Thêm tham số constructor để xác định panel được mở từ đâu
    public TaiKhoanPanel(boolean isAdmin) {
        this.isAdmin = isAdmin;
        setLayout(new BorderLayout(10,10));
        setBorder(new EmptyBorder(10,10,10,10));

        // === Toolbar ===
        JPanel toolbar = createButtonPanel();
        add(toolbar, BorderLayout.NORTH);

        // === Table ===
        model = new DefaultTableModel(
            new Object[]{"Mã TK","Mã NV","Tên TK","Mật khẩu","Quyền hạng"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tbl = new JTable(model);
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sorter = new TableRowSorter<>(model);
        tbl.setRowSorter(sorter);
        add(new JScrollPane(tbl), BorderLayout.CENTER);

        // Load dữ liệu
        reloadData();
    }
    
    // Constructor mặc định (không có tham số) - cho tương thích với code cũ
    public TaiKhoanPanel() {
        this(false); // Mặc định không phải Admin
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
        ImageIcon iconSua = new ImageIcon(getClass().getResource("/icon/sua.png"));
        btnSua = new JButton("SỬA", iconSua);
        btnSua.setHorizontalTextPosition(SwingConstants.CENTER);
        btnSua.setVerticalTextPosition(SwingConstants.BOTTOM);
        toolbar.add(btnSua, gbc);
        
        gbc.gridx = 2;
        ImageIcon iconXoa = new ImageIcon(getClass().getResource("/icon/xoa.png"));
        btnXoa = new JButton("XÓA", iconXoa);
        btnXoa.setHorizontalTextPosition(SwingConstants.CENTER);
        btnXoa.setVerticalTextPosition(SwingConstants.BOTTOM);
        toolbar.add(btnXoa, gbc);
        
        // Chỉ hiển thị nút XUẤT EXCEL nếu không phải Admin
        if (!isAdmin) {
            gbc.gridx = 3;
            ImageIcon iconExcel = new ImageIcon(getClass().getResource("/icon/xuatexcel.png"));
            btnExcel = new JButton("XUẤT EXCEL", iconExcel);
            btnExcel.setHorizontalTextPosition(SwingConstants.CENTER);
            btnExcel.setVerticalTextPosition(SwingConstants.BOTTOM);
            toolbar.add(btnExcel, gbc);
        }
        
        // Thêm nút đăng xuất nếu chạy từ Admin
        if (isAdmin) {
            gbc.gridx = 3;
            ImageIcon iconLogout = new ImageIcon(getClass().getResource("/icon/logout.png"));
            btnDangXuat = new JButton("ĐĂNG XUẤT", iconLogout);
            btnDangXuat.setHorizontalTextPosition(SwingConstants.CENTER);
            btnDangXuat.setVerticalTextPosition(SwingConstants.BOTTOM);
            toolbar.add(btnDangXuat, gbc);
        }
        
        // Phần tìm kiếm bên phải
        gbc.gridx = 4;
        gbc.weightx = 1.0; // Phần này sẽ chiếm khoảng trống còn lại
        toolbar.add(Box.createHorizontalGlue(), gbc); // Tạo khoảng trống giữa các nút và phần tìm kiếm
        
        // Panel chứa các thành phần tìm kiếm
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0)); // Căn phải, khoảng cách 5px
        searchPanel.setOpaque(false);
        
        // Thêm các thành phần vào panel tìm kiếm
        JLabel lblFilter = new JLabel("Quyền hạng:");
        cbbFilter = new JComboBox<>(new String[]{"Tất cả", "QuanLy", "NhanVien"});
        cbbFilter.setPreferredSize(new Dimension(110, 25));
        
        JLabel lblSearch = new JLabel("Tìm kiếm:");
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(180, 25));
        
        btnLamMoi = new JButton("LÀM MỚI");
        
        searchPanel.add(lblFilter);
        searchPanel.add(cbbFilter);
        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnLamMoi);
        
        gbc.gridx = 5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        toolbar.add(searchPanel, gbc);
        
        // === Sự kiện nút ===
        btnThem.addActionListener(e -> {
            ThemTaiKhoanDialog dlg = new ThemTaiKhoanDialog(
                SwingUtilities.getWindowAncestor(this)
            );
            dlg.setVisible(true);
            if (dlg.isSaved()) reloadData();
        });
        btnSua.addActionListener(e -> {
            int row = tbl.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản!",
                                              "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String maTK = model.getValueAt(
                tbl.convertRowIndexToModel(row), 0).toString();
            if ("TK000".equals(maTK)) {
                JOptionPane.showMessageDialog(this, "Không thể sửa Admin",
                                              "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            SuaTaiKhoanDialog dlg = new SuaTaiKhoanDialog(
                SwingUtilities.getWindowAncestor(this)
            );
            dlg.loadForEdit(maTK);
            dlg.setVisible(true);
            if (dlg.isSaved()) reloadData();
        });
        btnXoa.addActionListener(e -> {
            int row = tbl.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản!",
                                             "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String maTK = model.getValueAt(
                tbl.convertRowIndexToModel(row), 0).toString();
            if ("TK000".equals(maTK)) {
                JOptionPane.showMessageDialog(this, "Không thể xóa Admin",
                                             "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (JOptionPane.showConfirmDialog(this,
                 "Xóa tài khoản " + maTK + "?", "Xác nhận",
                 JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                bll.xoa(maTK);
                reloadData();
            }
        });
        
        // Chỉ thêm sự kiện nút XUẤT EXCEL nếu không phải Admin
        if (!isAdmin && btnExcel != null) {
            btnExcel.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Chức năng xuất Excel đang phát triển");
            });
        }
        
        // Thêm sự kiện cho nút đăng xuất nếu là Admin
        if (isAdmin && btnDangXuat != null) {
            btnDangXuat.addActionListener(e -> {
                Window owner = SwingUtilities.getWindowAncestor(this);
                owner.dispose();
                new LoginFrame().setVisible(true);
            });
        }
        
        btnLamMoi.addActionListener(e -> {
            txtSearch.setText("");
            cbbFilter.setSelectedIndex(0);
            sorter.setRowFilter(null);
        });

        // === Tìm kiếm + lọc ===
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { applyFilter(); }
            public void removeUpdate(DocumentEvent e) { applyFilter(); }
            public void changedUpdate(DocumentEvent e) { applyFilter(); }
        });
        cbbFilter.addActionListener(e -> applyFilter());
        
        return toolbar;
    }

    private void applyFilter() {
        List<RowFilter<DefaultTableModel, Integer>> filters = new ArrayList<>();
        // filter theo vai trò nếu không phải 'Tất cả'
        String role = (String)cbbFilter.getSelectedItem();
        if (!"Tất cả".equals(role)) {
            filters.add(RowFilter.regexFilter(
                "^" + Pattern.quote(role) + "$", 4));
        }
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

    private void reloadData() {
        model.setRowCount(0);
        for (TaiKhoanDTO t : bll.layTatCa()) {
            model.addRow(new Object[]{
                t.getMaTaiKhoan(),
                t.getMaNhanVien(),
                t.getUsername(),
                t.getPassword(),
                t.getVaiTro()
            });
        }
    }
}