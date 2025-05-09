package GUI.Panel;

import BUS.NhanVienBUS;
import DTO.NhanVienDTO;
import GUI.Dialog.ThemNhanVienDialog;
import GUI.Dialog.SuaNhanVienDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

public class NhanVienPanel extends JPanel {
    private final DefaultTableModel model;
    private final JTable tbl;
    private JComboBox<String> cbbFilter;
    private JTextField txtSearch;
    private final NhanVienBUS bll = new NhanVienBUS();

    public NhanVienPanel() {
        setLayout(new BorderLayout(10,10));
        setBorder(new EmptyBorder(10,10,10,10));

        // Tạo toolbar (thiết lập cbbFilter và txtSearch trong đó)
        JPanel toolbar = createButtonPanel();
        add(toolbar, BorderLayout.NORTH);

        // Tạo table model
        model = new DefaultTableModel(
            new Object[]{"Mã NV","Họ tên","Chức vụ","SĐT","Địa chỉ"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tbl = new JTable(model);
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Lần đầu load dữ liệu
        reloadData();

        add(new JScrollPane(tbl), BorderLayout.CENTER);
    }

    private JPanel createButtonPanel() {
        // Tạo một panel chứa toàn bộ toolbar
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new GridBagLayout()); // Sử dụng GridBagLayout 
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
        JButton btnThem = new JButton("THÊM", iconThem);
        btnThem.setHorizontalTextPosition(SwingConstants.CENTER);
        btnThem.setVerticalTextPosition(SwingConstants.BOTTOM);
        toolbar.add(btnThem, gbc);
        
        gbc.gridx = 1;
        ImageIcon iconSua = new ImageIcon(getClass().getResource("/icon/sua.png"));
        JButton btnSua = new JButton("SỬA", iconSua);
        btnSua.setHorizontalTextPosition(SwingConstants.CENTER);
        btnSua.setVerticalTextPosition(SwingConstants.BOTTOM);
        toolbar.add(btnSua, gbc);
        
        gbc.gridx = 2;
        ImageIcon iconXoa = new ImageIcon(getClass().getResource("/icon/xoa.png"));
        JButton btnXoa = new JButton("XÓA", iconXoa);
        btnXoa.setHorizontalTextPosition(SwingConstants.CENTER);
        btnXoa.setVerticalTextPosition(SwingConstants.BOTTOM);
        toolbar.add(btnXoa, gbc);
        
        gbc.gridx = 3;
        ImageIcon iconExcel = new ImageIcon(getClass().getResource("/icon/xuatexcel.png"));
        JButton btnExcel = new JButton("XUẤT EXCEL", iconExcel);
        btnExcel.setHorizontalTextPosition(SwingConstants.CENTER);
        btnExcel.setVerticalTextPosition(SwingConstants.BOTTOM);
        toolbar.add(btnExcel, gbc);
        
        // Phần tìm kiếm bên phải
        gbc.gridx = 4;
        gbc.weightx = 1.0; // Phần này sẽ chiếm khoảng trống còn lại
        toolbar.add(Box.createHorizontalGlue(), gbc); // Tạo khoảng trống giữa các nút và phần tìm kiếm
        
        // Panel chứa các thành phần tìm kiếm
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0)); // Căn phải, khoảng cách 5px
        searchPanel.setOpaque(false);
        
        // Thêm các thành phần vào panel tìm kiếm
        JLabel lblFilter = new JLabel("Chức vụ:");
        cbbFilter = new JComboBox<>(new String[]{"Tất cả", "Quản lý", "Nhân viên", "Bảo vệ"});
        cbbFilter.setPreferredSize(new Dimension(110, 25));
        
        JLabel lblSearch = new JLabel("Tìm kiếm:");
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(180, 25));
        
        JButton btnLamMoi = new JButton("LÀM MỚI");
        
        searchPanel.add(lblFilter);
        searchPanel.add(cbbFilter);
        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnLamMoi);
        
        gbc.gridx = 5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        toolbar.add(searchPanel, gbc);
        
        // Thêm sự kiện cho các nút
        btnThem.addActionListener(e -> {
            ThemNhanVienDialog dlg = new ThemNhanVienDialog(
                SwingUtilities.getWindowAncestor(this)
            );
            dlg.setVisible(true);
            if (dlg.isSaved()) reloadData();
        });

        btnSua.addActionListener(e -> {
            int r = tbl.getSelectedRow();
            if (r < 0) {
                JOptionPane.showMessageDialog(this,
                    "Chọn một nhân viên để chỉnh sửa",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String maNV = model.getValueAt(r,0).toString();
            SuaNhanVienDialog dlg = new SuaNhanVienDialog(
                SwingUtilities.getWindowAncestor(this), maNV
            );
            dlg.setVisible(true);
            if (dlg.isSaved()) reloadData();
        });

        btnXoa.addActionListener(e -> {
            int r = tbl.getSelectedRow();
            if (r < 0) {
                JOptionPane.showMessageDialog(this,
                    "Chọn một nhân viên để xóa",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (JOptionPane.showConfirmDialog(this,
                "Xóa nhân viên này?") == JOptionPane.YES_OPTION) {
                String maNV = model.getValueAt(r,0).toString();
                bll.xoa(maNV);
                reloadData();
            }
        });

        // Kịch bản lọc & tìm kiếm
        ActionListener doFilter = e -> reloadData();
        cbbFilter.addActionListener(doFilter);
        txtSearch.addActionListener(doFilter);
        
        btnLamMoi.addActionListener(e -> {
            cbbFilter.setSelectedIndex(0);
            txtSearch.setText("");
            reloadData();
        });

        return toolbar;
    }

    // Các phương thức khác giữ nguyên
    private void reloadData() {
        List<NhanVienDTO> all = bll.layTatCa();

        // 1) Lọc theo chức vụ
        String cv = (String)cbbFilter.getSelectedItem();
        if (!"Tất cả".equals(cv)) {
            all = all.stream()
                     .filter(n -> n.getChucVu().equalsIgnoreCase(cv))
                     .collect(Collectors.toList());
        }

        // 2) Tìm kiếm theo mã hoặc họ tên
        String kw = txtSearch.getText().trim().toLowerCase();
        if (!kw.isEmpty()) {
            all = all.stream()
                     .filter(n -> n.getMaNV().toLowerCase().contains(kw)
                               || n.getHoTen().toLowerCase().contains(kw))
                     .collect(Collectors.toList());
        }

        // Đổ lên bảng
        model.setRowCount(0);
        for (NhanVienDTO n : all) {
            model.addRow(new Object[]{
                n.getMaNV(),
                n.getHoTen(),
                n.getChucVu(),
                n.getSdt(),
                n.getDiaChi()
            });
        }
    }
}