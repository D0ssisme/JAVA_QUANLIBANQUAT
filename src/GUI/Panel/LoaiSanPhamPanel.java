package GUI.Panel;

import BUS.LoaiSanPhamBUS;
import DTO.LoaiSanPhamDTO;
import GUI.Dialog.SuaLoaiSanPhamDialog;
import GUI.Dialog.ThemLoaiSanPhamDialog;
import helper.XuatExcel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class LoaiSanPhamPanel extends JPanel {
    private final DefaultTableModel tableModel;
    private final JTable table;
    private JComboBox<String> cbbFilter;
    private final LoaiSanPhamBUS bll = new LoaiSanPhamBUS();

    public LoaiSanPhamPanel() {
        setLayout(new BorderLayout(10,10));
        setBorder(new EmptyBorder(10,10,10,10));

        JPanel toolbar = createButtonPanel();
        add(toolbar, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
            new Object[]{"Mã loại", "Tên loại", "Trạng thái", "Mô tả"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 50));

        setTableCellAlignment();

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();
    }

    private JPanel createButtonPanel() {
        JPanel toolbar = new JPanel(new GridBagLayout());
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 5);

        gbc.gridx = 0;
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
        gbc.gridx = 5;
        cbbFilter = new JComboBox<>(new String[]{"Tất cả", "Hoạt động", "Ngừng hoạt động"});
        toolbar.add(new JLabel("Trạng thái:"), gbc);

        gbc.gridx = 6;
        toolbar.add(cbbFilter, gbc);

        gbc.gridx = 4;
        gbc.weightx = 1.0;
        toolbar.add(Box.createHorizontalGlue(), gbc);

        gbc.gridx = 5;
        gbc.weightx = 0.0;

        btnThem.addActionListener(e -> {
            ThemLoaiSanPhamDialog dlg = new ThemLoaiSanPhamDialog(SwingUtilities.getWindowAncestor(this));
            dlg.setVisible(true);
            if (dlg.isAdded()) loadData();
        });

        btnSua.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Chọn dòng cần sửa");
                return;
            }
            String ma = tableModel.getValueAt(row, 0).toString();
            String ten = tableModel.getValueAt(row, 1).toString();
            String tt = tableModel.getValueAt(row, 2).toString();
            String moTa = tableModel.getValueAt(row, 3).toString();
            SuaLoaiSanPhamDialog dlg = new SuaLoaiSanPhamDialog(SwingUtilities.getWindowAncestor(this),
                new LoaiSanPhamDTO(ma, ten, tt, moTa));
            dlg.setVisible(true);
            if (dlg.isUpdated()) loadData();
        });

        btnXoa.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Chọn dòng cần xóa");
                return;
            }
            if (JOptionPane.showConfirmDialog(this, "Xóa loại sản phẩm này?", "Xác nhận", JOptionPane.YES_NO_OPTION)
                    == JOptionPane.YES_OPTION) {
                String ma = tableModel.getValueAt(row, 0).toString();
                if (bll.xoa(ma)) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công");
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại");
                }
            }
        });

        cbbFilter.addActionListener(e -> loadData());
        btnExcel.addActionListener(e -> XuatExcel.xuatExcel(table, "DanhSachQuat"));
        return toolbar;
    }

    private void setTableCellAlignment() {
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }
    }

    private void loadData() {
        List<LoaiSanPhamDTO> list = bll.layTatCa();

        String selected = (String) cbbFilter.getSelectedItem();
        if (!"Tất cả".equals(selected)) {
            list = list.stream()
                       .filter(l -> l.getTrangThai().equalsIgnoreCase(selected))
                       .collect(Collectors.toList());
        }


        tableModel.setRowCount(0);
        for (LoaiSanPhamDTO l : list) {
            tableModel.addRow(new Object[]{
                l.getMaLoaiSanPham(),
                l.getTenLoai(),
                l.getTrangThai(),
                l.getMoTa()
            });
        }
    }
}
