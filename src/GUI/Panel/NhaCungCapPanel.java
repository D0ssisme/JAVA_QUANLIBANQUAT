package GUI.Panel;

import DTO.DBConnection;
import BUS.NhaCungCapBUS;
import DTO.NhaCungCapDTO;
import GUI.Dialog.ThemNhaCungCapDialog;
import GUI.Dialog.SuaNhaCungCapDialog;
import java.sql.Statement;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NhaCungCapPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public NhaCungCapPanel() {
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
        JComboBox<String> cbbFilter = new JComboBox<>(new String[]{"Tất cả", "Mã NCC", "Tên NCC"});
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
            ThemNhaCungCapDialog dialog = new ThemNhaCungCapDialog((Window) SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);
            if (dialog.isAdded()) loadDataFromDatabase();
        });

        btnXoa.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhà cung cấp để xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa nhà cung cấp này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            String maNCC = table.getValueAt(selectedRow, 0).toString();
            NhaCungCapBUS bll = new NhaCungCapBUS();
            if (bll.xoa(maNCC)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadDataFromDatabase();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnSua.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhà cung cấp để sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String ma = tableModel.getValueAt(selectedRow, 0).toString();
            String ten = tableModel.getValueAt(selectedRow, 1).toString();
            String diachi = tableModel.getValueAt(selectedRow, 2).toString();
            String sdt = tableModel.getValueAt(selectedRow, 3).toString();
            NhaCungCapDTO ncc = new NhaCungCapDTO(ma, ten, diachi, sdt);

            SuaNhaCungCapDialog dialog = new SuaNhaCungCapDialog((Window) SwingUtilities.getWindowAncestor(this), ncc);
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
                NhaCungCapBUS bll = new NhaCungCapBUS();
                List<NhaCungCapDTO> results = new ArrayList<>();

                switch (filter) {
                    case "Mã NCC":
                        NhaCungCapDTO ncc = bll.timTheoMa(keyword);
                        if (ncc != null) results.add(ncc);
                        break;
                    case "Tên NCC":
                        results = bll.timTheoTen(keyword);
                        break;
                    case "Tất cả":
                        NhaCungCapDTO ncc1 = bll.timTheoMa(keyword);
                        List<NhaCungCapDTO> list2 = bll.timTheoTen(keyword);
                        if (ncc1 != null) results.add(ncc1);
                        for (NhaCungCapDTO item : list2) {
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
        tableModel = new DefaultTableModel(new Object[] {
            "Mã NCC", "Tên NCC", "Địa chỉ", "SĐT"
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
            if (i != 1) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
    }

    private void adjustTableColumnWidth() {
        int w = table.getWidth();
        table.getColumnModel().getColumn(0).setPreferredWidth((int)(w * 0.15));
        table.getColumnModel().getColumn(1).setPreferredWidth((int)(w * 0.35));
        table.getColumnModel().getColumn(2).setPreferredWidth((int)(w * 0.30));
        table.getColumnModel().getColumn(3).setPreferredWidth((int)(w * 0.20));
    }

    private void loadDataToTable(List<NhaCungCapDTO> list) {
        tableModel.setRowCount(0);
        for (NhaCungCapDTO ncc : list) {
            tableModel.addRow(new Object[] {
                ncc.getMaNCC(),
                ncc.getTenNCC(),
                ncc.getDiaChiNCC(),
                ncc.getSdtNCC()
            });
        }
    }

       private void loadDataFromDatabase() {
          tableModel.setRowCount(0); 

          String sql = "SELECT * FROM nha_cung_cap";
          try (Connection conn = DBConnection.getConnection();
               Statement stmt = conn.createStatement();
               ResultSet rs = stmt.executeQuery(sql)) {

              while (rs.next()) {
                  tableModel.addRow(new Object[] {
                      rs.getString("MaNCC"),
                      rs.getString("TenNCC"),
                      rs.getString("DiaChiNCC"),
                      rs.getString("Sdt_NCC")
                  });
              }

          } catch (SQLException e) {
              JOptionPane.showMessageDialog(
                  this,
                  "Lỗi khi tải dữ liệu từ CSDL!\n" + e.getMessage(),
                  "Lỗi", JOptionPane.ERROR_MESSAGE
              );
              e.printStackTrace();
          }
      }

}
