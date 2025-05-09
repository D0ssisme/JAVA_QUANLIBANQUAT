package GUI.Panel;
import GUI.Dialog.ThemQuatDialog;
import DTO.DBConnection;
import BUS.QuatBUS;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import DTO.QuatDTO;
import java.awt.Font;
import java.awt.Color;
import GUI.Dialog.SuaQuatDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.poi.ss.usermodel.Sheet;  
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class QuatPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public QuatPanel() {
        setBackground(Color.LIGHT_GRAY);
        setLayout(new BorderLayout());  

        add(createButtonPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER); 

        loadDataFromDatabase();
    }

    private JPanel createButtonPanel() {
        // Panel chính dùng BorderLayout
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // ---- LEFT TOOL PANEL (THÊM XÓA SỬA XUẤT EXCEL) ----
        JPanel leftToolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftToolPanel.setOpaque(false);

        ImageIcon iconThem = new ImageIcon(getClass().getResource("/icon/them.png"));
        JButton btnThem = new JButton("THÊM", iconThem);
        btnThem.setHorizontalTextPosition(SwingConstants.CENTER);
        btnThem.setVerticalTextPosition(SwingConstants.BOTTOM);

        ImageIcon iconXoa = new ImageIcon(getClass().getResource("/icon/xoa.png"));
        JButton btnXoa = new JButton("XÓA", iconXoa);
        btnXoa.setHorizontalTextPosition(SwingConstants.CENTER);
        btnXoa.setVerticalTextPosition(SwingConstants.BOTTOM);

        ImageIcon iconSua = new ImageIcon(getClass().getResource("/icon/sua.png"));
        JButton btnSua = new JButton("SỬA", iconSua);
        btnSua.setHorizontalTextPosition(SwingConstants.CENTER);
        btnSua.setVerticalTextPosition(SwingConstants.BOTTOM);

        ImageIcon iconExcel = new ImageIcon(getClass().getResource("/icon/xuatexcel.png"));
        JButton btnExcel = new JButton("XUẤT EXCEL", iconExcel);
        btnExcel.setHorizontalTextPosition(SwingConstants.CENTER);
        btnExcel.setVerticalTextPosition(SwingConstants.BOTTOM);

        leftToolPanel.add(btnThem);
        leftToolPanel.add(btnXoa);
        leftToolPanel.add(btnSua);
        leftToolPanel.add(btnExcel);

        // Wrapper để căn giữa theo chiều dọc
        JPanel leftWrapper = new JPanel();
        leftWrapper.setLayout(new BoxLayout(leftWrapper, BoxLayout.Y_AXIS));
        leftWrapper.setOpaque(false);
        leftWrapper.add(Box.createVerticalGlue());
        leftWrapper.add(leftToolPanel);
        leftWrapper.add(Box.createVerticalGlue());
        

       // ---- RIGHT TOOL PANEL (FILTER + SEARCH + LÀM MỚI) ----
        JPanel rightToolPanel = new JPanel();
        rightToolPanel.setLayout(new BoxLayout(rightToolPanel, BoxLayout.Y_AXIS));
        rightToolPanel.setOpaque(false);

        // Panel trên chứa combobox, tìm kiếm và button làm mới
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        topPanel.setOpaque(false);

        JComboBox<String> cbbFilter = new JComboBox<>(new String[]{"Tất cả", "Mã Quạt", "Tên Quạt", "Thương Hiệu"});
        JLabel lblSearch = new JLabel("Tìm kiếm:");
        JTextField txtSearch = new JTextField(15);
        JButton btnLamMoi = new JButton("LÀM MỚI");

        topPanel.add(cbbFilter);
        topPanel.add(lblSearch);
        topPanel.add(txtSearch);
        topPanel.add(btnLamMoi);

        // Panel dưới chứa phần giá từ và đến (ngang hàng)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        bottomPanel.setOpaque(false);

        JLabel lblGiaFrom = new JLabel("Giá từ:");
        JTextField txtGiaFrom = new JTextField(10);
        JLabel lblGiaTo = new JLabel("Đến:");
        JTextField txtGiaTo = new JTextField(10);

        bottomPanel.add(lblGiaFrom);
        bottomPanel.add(txtGiaFrom);
        bottomPanel.add(lblGiaTo);
        bottomPanel.add(txtGiaTo);

        // Thêm cả hai panel vào rightToolPanel
        rightToolPanel.add(topPanel);
        rightToolPanel.add(bottomPanel);

        // Wrapper để căn giữa theo chiều dọc
        JPanel rightWrapper = new JPanel();
        rightWrapper.setLayout(new BoxLayout(rightWrapper, BoxLayout.Y_AXIS));
        rightWrapper.setOpaque(false);
        rightWrapper.add(Box.createVerticalGlue());
        rightWrapper.add(rightToolPanel);
        rightWrapper.add(Box.createVerticalGlue());

        // Đưa vào toolbar
        toolbar.add(leftWrapper, BorderLayout.WEST);
        toolbar.add(rightWrapper, BorderLayout.EAST);
        toolbar.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 5, true));
        btnThem.addActionListener(e -> {
            ThemQuatDialog dialog = new ThemQuatDialog((Window) SwingUtilities.getWindowAncestor(this)); // Khởi tạo dialog
            dialog.setVisible(true);
            
            if (dialog.isAdded()) {
                loadDataFromDatabase(); 
            }
        });
        
        
        btnXoa.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một quạt để xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa quạt này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            String maQuat = table.getValueAt(selectedRow, 0).toString();
            QuatBUS bll = new QuatBUS();
            if (bll.xoa(maQuat)) {
                JOptionPane.showMessageDialog(this, "Xóa quạt thành công.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadDataFromDatabase();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa quạt thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnSua.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một quạt để sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String maQuat = tableModel.getValueAt(selectedRow, 0).toString();
            String tenQuat = tableModel.getValueAt(selectedRow, 1).toString();
            int gia = Integer.parseInt(tableModel.getValueAt(selectedRow, 2).toString());
            int soLuongTon= Integer.parseInt(tableModel.getValueAt(selectedRow, 3).toString());
            String maNSX = tableModel.getValueAt(selectedRow, 4).toString();
            String ngaySanXuat = tableModel.getValueAt(selectedRow, 5).toString();
            String chatLieu = tableModel.getValueAt(selectedRow, 6).toString();
            String thuongHieu = tableModel.getValueAt(selectedRow, 7).toString();
            String maLoaiSP = tableModel.getValueAt(selectedRow, 8).toString();

            QuatDTO quat = new QuatDTO(maQuat, tenQuat, gia,soLuongTon, maNSX, ngaySanXuat, chatLieu, thuongHieu, maLoaiSP);
            SuaQuatDialog dialog = new SuaQuatDialog((Window) SwingUtilities.getWindowAncestor(this), quat);
            dialog.setVisible(true);

            // Chỉ load lại nếu có thay đổi
            if (dialog.isUpdated()) {
                loadDataFromDatabase();
            }
        });
        btnExcel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xuatExcel();
            }
        });
        
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search();
            }

            private void search() {
                String keyword = txtSearch.getText().trim();
                String filter = cbbFilter.getSelectedItem().toString();
                QuatBUS quatBLL = new QuatBUS();

                List<QuatDTO> results = new ArrayList<>();
                    if (keyword.isEmpty()) {
                   
                        results = quatBLL.layTatCa(); 
               } else {
                   switch (filter) {
                       case "Mã Quạt":
                           QuatDTO q = quatBLL.timTheoMaQuat(keyword);
                           if (q != null) results.add(q);
                           break;
                       case "Tên Quạt":
                           results = quatBLL.timTheoTenQuat(keyword);
                           break;
                       case "Thương Hiệu":
                           results = quatBLL.timTheoThuongHieu(keyword);
                           break;
                      case "Tất cả":
                        Set<QuatDTO> resultSet = new LinkedHashSet<>(); 

                        QuatDTO q1 = quatBLL.timTheoMaQuat(keyword);
                        if (q1 != null) resultSet.add(q1);

                        List<QuatDTO> q2 = quatBLL.timTheoTenQuat(keyword);
                        List<QuatDTO> q3 = quatBLL.timTheoThuongHieu(keyword);

                        resultSet.addAll(q2);
                        resultSet.addAll(q3);

                        results = new ArrayList<>(resultSet); 
                        break;

                   }
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
             "Mã Quạt", "Tên Quạt", "Giá","SL Tồn", "Mã NSX", "Ngày SX", "Chất liệu", "Thương Hiệu", "Mã Loại"
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
        table.getColumnModel().getColumn(0).setPreferredWidth((int)(w * 0.07));
        table.getColumnModel().getColumn(1).setPreferredWidth((int)(w * 0.25));
        table.getColumnModel().getColumn(2).setPreferredWidth((int)(w * 0.08));

        table.getColumnModel().getColumn(3).setPreferredWidth((int)(w * 0.08));
        table.getColumnModel().getColumn(4).setPreferredWidth((int)(w * 0.10));
        table.getColumnModel().getColumn(5).setPreferredWidth((int)(w * 0.10));
        table.getColumnModel().getColumn(6).setPreferredWidth((int)(w * 0.13));
        table.getColumnModel().getColumn(7).setPreferredWidth((int)(w * 0.13));
        table.getColumnModel().getColumn(8).setPreferredWidth((int)(w * 0.08));
    }

    private void loadDataToTable(List<QuatDTO> list) {
        tableModel.setRowCount(0);  // Xóa dữ liệu cũ

        for (QuatDTO q : list) {
            tableModel.addRow(new Object[]{
                q.getMaQuat(),
                q.getTenQuat(),
                q.getGia(),
                q.getSoLuongTon(),
                q.getMaNSX(),
                q.getNgaySanXuat(),
                q.getChatLieu(),
                q.getThuongHieu(),
                q.getMaLoaiSP()
            });
        }
    }

    private QuatBUS quatBUS = new QuatBUS(); // Thêm dòng này ở đầu class

    private void loadDataFromDatabase() {
        tableModel.setRowCount(0);

        List<QuatDTO> danhSachQuat = quatBUS.layTatCa(); // Gọi qua đối tượng

        for (QuatDTO quat : danhSachQuat) {
            tableModel.addRow(new Object[] {
                quat.getMaQuat(),
                quat.getTenQuat(),
                quat.getGia(),
                quat.getSoLuongTon(),
                quat.getMaNSX(),
                quat.getNgaySanXuat(),
                quat.getChatLieu(),
                quat.getThuongHieu(),
                quat.getMaLoaiSP()
            });
        }
    }
    public void xuatExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu file Excel");
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().endsWith(".xlsx")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".xlsx");
            }

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("DanhSach");

                // Tạo dòng tiêu đề từ tableModel
                Row header = sheet.createRow(0);
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    header.createCell(i).setCellValue(tableModel.getColumnName(i));
                }

                // Ghi dữ liệu từ tableModel
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        Object value = tableModel.getValueAt(i, j);
                        row.createCell(j).setCellValue(value != null ? value.toString() : "");
                    }
                }

                // Ghi ra file
                FileOutputStream out = new FileOutputStream(fileToSave);
                workbook.write(out);
                out.close();

                JOptionPane.showMessageDialog(null, "Xuất Excel thành công!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi khi xuất Excel: " + ex.getMessage());
            }
        }
    }


}