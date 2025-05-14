package GUI.Panel;
import GUI.Dialog.ThemQuatDialog;
import BUS.QuatBUS;
import DAO.LoaiSanPhamDAO;
import DTO.LoaiSanPhamDTO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import DTO.QuatDTO;
import GUI.Dialog.ExcelQuatDialog;
import java.awt.Font;
import java.awt.Color;
import GUI.Dialog.SuaQuatDialog;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableRowSorter;


public class QuatPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableQuat;
    private Map<String, String> mapLoaiTen = new HashMap<>();
    private Map<String, String> mapTenToMaLoai = new HashMap<>();

    public QuatPanel() throws SQLException {
        setLayout(new BorderLayout());  
         setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10,10,10,10));

        add(createButtonPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER); 

        loadDataFromDatabase();
    }

    private JPanel createButtonPanel() throws SQLException {
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBackground(Color.WHITE);

        // LEFT TOOL PANEL
        JPanel leftToolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftToolPanel.setOpaque(false);

        JButton btnThem = new JButton("THÊM", new ImageIcon(getClass().getResource("/icon/them.png")));
        JButton btnXoa = new JButton("XÓA", new ImageIcon(getClass().getResource("/icon/xoa.png")));
        JButton btnSua = new JButton("SỬA", new ImageIcon(getClass().getResource("/icon/sua.png")));
        JButton btnExcel = new JButton("EXCEL", new ImageIcon(getClass().getResource("/icon/xuatexcel.png")));

        for (JButton btn : new JButton[]{btnThem, btnXoa, btnSua, btnExcel}) {
            btn.setHorizontalTextPosition(SwingConstants.CENTER);
            btn.setVerticalTextPosition(SwingConstants.BOTTOM);
            leftToolPanel.add(btn);
        }

        JPanel leftWrapper = new JPanel();
        leftWrapper.setLayout(new BoxLayout(leftWrapper, BoxLayout.Y_AXIS));
        leftWrapper.setOpaque(false);
        leftWrapper.add(Box.createVerticalGlue());
        leftWrapper.add(leftToolPanel);
        leftWrapper.add(Box.createVerticalGlue());

        // RIGHT TOOL PANEL (Filter UI)
        JPanel rightToolPanel = new JPanel();
        rightToolPanel.setLayout(new BoxLayout(rightToolPanel, BoxLayout.Y_AXIS));
        rightToolPanel.setOpaque(false);
        JPanel filterPanel = new JPanel(new GridLayout(2, 1, 0, 5)); 
        filterPanel.setOpaque(false);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topPanel.setOpaque(false);
        JComboBox<String> cbbFilter = new JComboBox<>(new String[]{"Tất cả", "Mã Quạt", "Tên Quạt", "Thương Hiệu"});
        JLabel lblSearch = new JLabel("Tìm kiếm:");
        JTextField txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(215, 23));  

        JButton btnLocGia  = new JButton("Lọc");
        topPanel.add(cbbFilter);
        topPanel.add(lblSearch);
        topPanel.add(txtSearch);
        topPanel.add(btnLocGia);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bottomPanel.setOpaque(false);
        JComboBox<String> loaiSP = new JComboBox<>();
        loaiSP.addItem("Tất cả");
        
        LoaiSanPhamDAO dao = new LoaiSanPhamDAO();
        for (LoaiSanPhamDTO lsp : dao.getAll()) {
            loaiSP.addItem(lsp.getTenLoai());
            mapTenToMaLoai.put(lsp.getTenLoai(), lsp.getMaLoaiSanPham());
        }

        JLabel lblGiaFrom = new JLabel("Giá từ:");
        JTextField txtGiaTu = new JTextField(10);
        JLabel lblGiaDen = new JLabel("Đến:");
        JTextField txtGiaDen = new JTextField(10);
        JButton btnLamMoi = new JButton("Làm mới");
        bottomPanel.add(loaiSP);
        bottomPanel.add(lblGiaFrom);
        bottomPanel.add(txtGiaTu);
        bottomPanel.add(lblGiaDen);
        bottomPanel.add(txtGiaDen);
        bottomPanel.add(btnLamMoi);
        filterPanel.add(topPanel);
        filterPanel.add(bottomPanel);
        rightToolPanel.add(filterPanel);

        JPanel rightWrapper = new JPanel();
        rightWrapper.setLayout(new BoxLayout(rightWrapper, BoxLayout.Y_AXIS));
        rightWrapper.setOpaque(false);
        rightWrapper.add(Box.createVerticalGlue());
        rightWrapper.add(rightToolPanel);
        rightWrapper.add(Box.createVerticalGlue());

        toolbar.add(leftWrapper, BorderLayout.WEST);
        toolbar.add(rightWrapper, BorderLayout.EAST);

        btnThem.addActionListener(e -> {
            ThemQuatDialog dialog = new ThemQuatDialog((Window) SwingUtilities.getWindowAncestor(this)); 
            dialog.setVisible(true);
            if (dialog.isAdded()) loadDataFromDatabase(); 
        });

        btnXoa.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một quạt để xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa quạt này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            String maQuat = table.getValueAt(selectedRow, 0).toString();
            QuatBUS bll = new QuatBUS();
            if (bll.xoa(maQuat)) {
                JOptionPane.showMessageDialog(this, "Xóa quạt thành công.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadDataFromDatabase();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa quạt thất bại. Vẫn còn liên kết trong SQL", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnSua.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một quạt để sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String maQuat = tableQuat.getValueAt(selectedRow, 0).toString();
            String tenQuat = tableQuat.getValueAt(selectedRow, 1).toString();
            int gia = Integer.parseInt(tableQuat.getValueAt(selectedRow, 2).toString());
            int soLuongTon = Integer.parseInt(tableQuat.getValueAt(selectedRow, 3).toString());
            String maNSX = tableQuat.getValueAt(selectedRow, 4).toString();
            String ngaySanXuat = tableQuat.getValueAt(selectedRow, 5).toString();
            String chatLieu = tableQuat.getValueAt(selectedRow, 6).toString();
            String thuongHieu = tableQuat.getValueAt(selectedRow, 7).toString();
            String maLoaiSP = tableQuat.getValueAt(selectedRow, 8).toString();

            QuatDTO quat = new QuatDTO(maQuat, tenQuat, gia, soLuongTon, maNSX, ngaySanXuat, chatLieu, thuongHieu, maLoaiSP);
            SuaQuatDialog dialog = new SuaQuatDialog((Window) SwingUtilities.getWindowAncestor(this), quat);
            dialog.setVisible(true);
            if (dialog.isUpdated()) loadDataFromDatabase();
        });
        btnExcel.addActionListener(e -> {
            ExcelQuatDialog dlg = new ExcelQuatDialog(SwingUtilities.getWindowAncestor(this), table, this::loadDataFromDatabase);
            dlg.setVisible(true);
        });




        addSearchFunctionality(txtSearch, cbbFilter, btnLamMoi, loaiSP,txtGiaTu,txtGiaDen,btnLocGia);

        return toolbar;
    }

    
    
    
    
    /////////////////////////////////////////////////////////////////

   private void addSearchFunctionality(JTextField txtSearch, JComboBox<String> cbbFilter,
                                     JButton btnLamMoi, JComboBox<String> loaiSP,
                                     JTextField txtGiaTu, JTextField txtGiaDen, JButton btnLocGia) {

        btnLocGia.addActionListener(e -> filterData(txtSearch, cbbFilter, loaiSP, txtGiaTu, txtGiaDen));


            btnLamMoi.addActionListener(e -> {
             txtSearch.setText("");
             cbbFilter.setSelectedIndex(0);
             loaiSP.setSelectedIndex(0);
             txtGiaTu.setText("");
             txtGiaDen.setText("");
             TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) table.getRowSorter();
             if (sorter != null) {
                 sorter.setRowFilter(null); 
             }

             loadDataFromDatabase(); 
         });

    }

 
 
    ////////////////////////////////////////////////////////////////////
private void filterData(JTextField txtSearch, JComboBox<String> cbbFilter, JComboBox<String> loaiSP, JTextField txtGiaTu, JTextField txtGiaDen) {
    String tuKhoa = txtSearch.getText().trim().toLowerCase();
    String loaiQuat = cbbFilter.getSelectedItem().toString();
    String loaiSanPham = loaiSP.getSelectedItem().toString();
    String giaTuStr = txtGiaTu.getText().trim();
    String giaDenStr = txtGiaDen.getText().trim();
    
    int giaTu = 0;
    int giaDen = Integer.MAX_VALUE;

    if (!giaTuStr.isEmpty()) {
        try {
            giaTu = Integer.parseInt(giaTuStr);
            if (giaTu < 0) {
                JOptionPane.showMessageDialog(null, "Giá từ không thể là số âm!");
                txtGiaTu.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Giá từ phải là số nguyên!");
            txtGiaTu.requestFocus();
            return;
        }
    }

    if (!giaDenStr.isEmpty()) {
        try {
            giaDen = Integer.parseInt(giaDenStr);
            if (giaDen < 0) {
                JOptionPane.showMessageDialog(null, "Giá đến không thể là số âm!");
                txtGiaDen.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Giá đến phải là số nguyên!");
            txtGiaDen.requestFocus();
            return;
        }
    }

    if (!giaTuStr.isEmpty() && !giaDenStr.isEmpty() && giaTu > giaDen) {
        JOptionPane.showMessageDialog(null, "Giá từ phải nhỏ hơn hoặc bằng giá đến!");
        txtGiaTu.requestFocus();
        return;
    }
    
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    table.setRowSorter(sorter);
    
    List<RowFilter<Object, Object>> filters = new ArrayList<>();

    if (!tuKhoa.isEmpty()) {
        filters.add(RowFilter.regexFilter("(?i)" + tuKhoa, 1));
    }

    if (!loaiQuat.equals("Tất cả")) {
        filters.add(RowFilter.regexFilter("(?i)" + loaiQuat, 9)); 
    }

    if (!loaiSanPham.equals("Tất cả")) {
        filters.add(RowFilter.regexFilter("(?i)" + loaiSanPham, 8)); 
    }

    final int finalGiaTu = giaTu;
    final int finalGiaDen = giaDen;
    
    filters.add(new RowFilter<Object, Object>() {
        @Override
        public boolean include(Entry<? extends Object, ? extends Object> entry) {
            try {
                String giaText = entry.getStringValue(2); 
               
                String giaClean = giaText.replaceAll("[^0-9]", "");
                if (giaClean.isEmpty()) {
                    return false;
                }
                
                int gia = Integer.parseInt(giaClean);
                return finalGiaTu <= gia && gia <= finalGiaDen;
            } catch (Exception e) {
                System.out.println("Lỗi xử lý giá: " + e.getMessage());
                return false;
            }
        }
    });
    
    if (filters.isEmpty()) {
        sorter.setRowFilter(null); 
    } else {
        sorter.setRowFilter(RowFilter.andFilter(filters));
    }
}


    

    private JScrollPane createTablePanel() {
         tableQuat = new DefaultTableModel(new Object[] {
             "Mã Quạt", "Tên Quạt", "Giá","SL Tồn", "Mã NSX", "Ngày SX", "Chất liệu", "Thương Hiệu", "Tên Loại"
         }, 0) {
             @Override
             public boolean isCellEditable(int row, int column) {
                 return false;
             }
         };

         table = new JTable(tableQuat);
         setTableCellAlignment();
         table.setFont(new Font("Arial", Font.PLAIN, 14)); 
         table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
         table.setRowHeight(30);

         JTableHeader header = table.getTableHeader();
         header.setFont(new Font("Arial", Font.BOLD, 14)); 
         header.setPreferredSize(new Dimension(header.getPreferredSize().width, 50));

         table.addComponentListener(new java.awt.event.ComponentAdapter() {
             @Override
             public void componentResized(java.awt.event.ComponentEvent evt) {
                 adjustTableColumnWidth();
             }
         });

         return new JScrollPane(table);
    }


    private void setTableCellAlignment() {
        DefaultTableCellRenderer cangiua = new DefaultTableCellRenderer();
        cangiua.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i != 1) { 
                table.getColumnModel().getColumn(i).setCellRenderer(cangiua);
            }
        }
    }

    private void adjustTableColumnWidth() {
        int w = table.getWidth();
        table.getColumnModel().getColumn(0).setPreferredWidth((int)(w * 0.08));
        table.getColumnModel().getColumn(1).setPreferredWidth((int)(w * 0.20));
        table.getColumnModel().getColumn(2).setPreferredWidth((int)(w * 0.08));

        table.getColumnModel().getColumn(3).setPreferredWidth((int)(w * 0.08));
        table.getColumnModel().getColumn(4).setPreferredWidth((int)(w * 0.10));
        table.getColumnModel().getColumn(5).setPreferredWidth((int)(w * 0.10));
        table.getColumnModel().getColumn(6).setPreferredWidth((int)(w * 0.13));
        table.getColumnModel().getColumn(7).setPreferredWidth((int)(w * 0.13));
        table.getColumnModel().getColumn(8).setPreferredWidth((int)(w * 0.10));
    }

    private void loadDataToTable(List<QuatDTO> list) {
        
        tableQuat.setRowCount(0);  // Xóa dữ liệu cũ
        loadLoaiMap(); 
        for (QuatDTO q : list) {
            tableQuat.addRow(new Object[]{
                q.getMaQuat(),
                q.getTenQuat(),
                q.getGia(),
                q.getSoLuongTon(),
                q.getMaNSX(),
                q.getNgaySanXuat(),
                q.getChatLieu(),
                q.getThuongHieu(),
                mapLoaiTen.getOrDefault(q.getMaLoaiSP(), "Không rõ")
            });
        }
    }

    private QuatBUS quatBUS = new QuatBUS(); 
    

    private void loadLoaiMap() {
        try {
            LoaiSanPhamDAO dao = new LoaiSanPhamDAO();
            for (LoaiSanPhamDTO loai : dao.getAll()) {
                mapLoaiTen.put(loai.getMaLoaiSanPham(), loai.getTenLoai());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void loadDataFromDatabase() {
        
        tableQuat.setRowCount(0);
        loadLoaiMap(); 
        List<QuatDTO> danhSachQuat = quatBUS.layTatCa();
        for (QuatDTO quat : danhSachQuat) {
            tableQuat.addRow(new Object[] {
                quat.getMaQuat(),
                quat.getTenQuat(),
                quat.getGia(),
                quat.getSoLuongTon(),
                quat.getMaNSX(),
                quat.getNgaySanXuat(),
                quat.getChatLieu(),
                quat.getThuongHieu(),
                mapLoaiTen.getOrDefault(quat.getMaLoaiSP(), "Không rõ")
            });
        }
    }

  

}