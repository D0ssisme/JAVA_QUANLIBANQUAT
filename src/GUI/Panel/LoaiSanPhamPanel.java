package GUI.Panel;

import BUS.LoaiSanPhamBUS;
import DTO.LoaiSanPhamDTO;
import GUI.Dialog.ThemLoaiSanPhamDialog;
import GUI.Dialog.SuaLoaiSanPhamDialog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LoaiSanPhamPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public LoaiSanPhamPanel() {
        setBackground(Color.LIGHT_GRAY);
        setLayout(new BorderLayout());

        add(createButtonPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);

        loadDataFromDatabase();
    }

    private JPanel createButtonPanel() {
        JPanel toolbar = new JPanel(new BorderLayout());

        JPanel leftToolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton btnThem = new JButton("THÊM");
        JButton btnXoa = new JButton("XÓA");
        JButton btnSua = new JButton("SỬA");
        JButton btnExcel = new JButton("XUẤT EXCEL");
        leftToolPanel.add(btnThem);
        leftToolPanel.add(btnXoa);
        leftToolPanel.add(btnSua);
        leftToolPanel.add(btnExcel);

        JPanel rightToolPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JComboBox<String> cbbFilter = new JComboBox<>(new String[]{"Tất cả", "Mã loại", "Tên loại"});
        JLabel lblSearch = new JLabel("Tìm kiếm:");
        JTextField txtSearch = new JTextField(15);
        JButton btnLamMoi = new JButton("LÀM MỚI");

        rightToolPanel.add(cbbFilter);
        rightToolPanel.add(lblSearch);
        rightToolPanel.add(txtSearch);
        rightToolPanel.add(btnLamMoi);

        toolbar.add(leftToolPanel, BorderLayout.WEST);
        toolbar.add(rightToolPanel, BorderLayout.EAST);

        btnThem.addActionListener(e -> {
            ThemLoaiSanPhamDialog dialog = new ThemLoaiSanPhamDialog((Window) SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);
            if (dialog.isAdded()) loadDataFromDatabase();
        });

        btnXoa.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một loại sản phẩm để xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa loại sản phẩm này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            String maLoai = table.getValueAt(selectedRow, 0).toString();
            LoaiSanPhamBUS bll = new LoaiSanPhamBUS();
            if (bll.xoa(maLoai)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadDataFromDatabase();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnSua.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một loại sản phẩm để sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String ma = tableModel.getValueAt(selectedRow, 0).toString();
            String ten = tableModel.getValueAt(selectedRow, 1).toString();
            String trangthai = tableModel.getValueAt(selectedRow, 2).toString();
            String mota = tableModel.getValueAt(selectedRow, 3).toString();
            LoaiSanPhamDTO lsp = new LoaiSanPhamDTO(ma, ten, trangthai, mota);

            SuaLoaiSanPhamDialog dialog = new SuaLoaiSanPhamDialog((Window) SwingUtilities.getWindowAncestor(this), lsp);
            dialog.setVisible(true);
            if (dialog.isUpdated()) loadDataFromDatabase();
        });

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { search(); }
            public void removeUpdate(DocumentEvent e) { search(); }
            public void changedUpdate(DocumentEvent e) { search(); }

            private void search() {
                String keyword = txtSearch.getText().trim();
                String filter = cbbFilter.getSelectedItem().toString();
                LoaiSanPhamBUS bll = new LoaiSanPhamBUS();
                List<LoaiSanPhamDTO> results = new ArrayList<>();

                switch (filter) {
                    case "Mã loại":
                        LoaiSanPhamDTO lsp = bll.timTheoMa(keyword);
                        if (lsp != null) results.add(lsp);
                        break;
                    case "Tên loại":
                        results = bll.timTheoTen(keyword);
                        break;
                    case "Tất cả":
                        LoaiSanPhamDTO lsp1 = bll.timTheoMa(keyword);
                        List<LoaiSanPhamDTO> list2 = bll.timTheoTen(keyword);
                        if (lsp1 != null) results.add(lsp1);
                        for (LoaiSanPhamDTO item : list2) {
                            if (!results.contains(item)) results.add(item);
                        }
                        break;
                }

                loadDataToTable(results);
            }
        });

        btnLamMoi.addActionListener(e -> {
            txtSearch.setText("");
            cbbFilter.setSelectedIndex(0);
            loadDataFromDatabase();
        });

        return toolbar;
    }

    private JScrollPane createTablePanel() {
        tableModel = new DefaultTableModel(new Object[]{
            "Mã loại", "Tên loại", "Trạng thái", "Mô tả"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        setTableCellAlignment();
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(30);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 50));

        table.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                adjustTableColumnWidth();
            }
        });

        return new JScrollPane(table);
    }

    private void setTableCellAlignment() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void adjustTableColumnWidth() {
        int w = table.getWidth();
        table.getColumnModel().getColumn(0).setPreferredWidth((int)(w * 0.15));
        table.getColumnModel().getColumn(1).setPreferredWidth((int)(w * 0.25));
        table.getColumnModel().getColumn(2).setPreferredWidth((int)(w * 0.20));
        table.getColumnModel().getColumn(3).setPreferredWidth((int)(w * 0.40));
    }

    private void loadDataToTable(List<LoaiSanPhamDTO> list) {
        tableModel.setRowCount(0);
        for (LoaiSanPhamDTO lsp : list) {
            tableModel.addRow(new Object[]{
                lsp.getMaLoaiSanPham(),
                lsp.getTenLoai(),
                lsp.getTrangThai(),
                lsp.getMoTa()
            });
        }
    }

    private void loadDataFromDatabase() {
        LoaiSanPhamBUS bll = new LoaiSanPhamBUS();
        List<LoaiSanPhamDTO> list = bll.layTatCa();
        loadDataToTable(list);
    }
}
