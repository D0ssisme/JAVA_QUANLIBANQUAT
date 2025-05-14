package GUI.Panel;

import BUS.PhieuNhapBUS;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import DTO.PhieuNhapDTO;
import DTO.PhieuNhapDTO;
import java.util.List;
import java.util.Map;
import GUI.Dialog.ThemPhieuNhapDialog;
import org.jdesktop.swingx.prompt.PromptSupport;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.toedter.calendar.JDateChooser;
import java.util.Locale;
import javax.swing.JOptionPane;
import GUI.Dialog.ThemPhieuNhapDialog;
import GUI.Dialog.ChiTietPhieuNhapDialog;
import GUI.Dialog.SuaPhieuNhapDialog;
import BUS.NhaCungCapBUS;
import DTO.NhaCungCapDTO;
import DTO.NhanVienDTO;
import BUS.NhanVienBUS;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import com.toedter.calendar.JDateChooser;


// Apache POI
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.awt.Desktop;
import java.util.ArrayList;




public class PhieuNhapPanel extends JPanel {
    private String manv;
    private JButton btnThem, btnChiTiet, btnSuaPhieu, btnXuatExcel, btnLamMoi,btnTimKiem,btnTimKiem2;
    private JComboBox<String> cbbSearchType;
    private JTextField txtSearch;
    private JDateChooser dateChooserTuNgay;
    private JDateChooser dateChooserDenNgay;
    private JTable table;
    private DefaultTableModel tableModel;

    // Bộ lọc bên trái
    private JComboBox<String> cbbNhaCungCap, cbbNhanVien;
    private JTextField txtTuNgay, txtDenNgay, txtTuSoTien,txtDenSotien; 
    // (Có thể dùng JDateChooser, v.v. tuỳ bạn)

    public PhieuNhapPanel(String manv) {
       this.manv=manv;
       initComponent();
      loadData();
    }

    private void initComponent() {
        setLayout(new BorderLayout(10, 10));

        
        
        // ==================== Thanh công cụ trên ====================
        // Gồm 2 phần: 
        // (A) Nút lớn bên trái
        // (B) Combobox + ô tìm kiếm + refreszh btn
        JPanel topPanel = new JPanel(new BorderLayout());

        
        
        
        
        //(A) Panel chứa các nút lớn (FlowLayout trái)
        JPanel leftToolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btnThem = new JButton("THÊM");
        btnThem.setIcon(new ImageIcon(getClass().getResource("/icon/them.png")));
        btnThem.setHorizontalTextPosition(SwingConstants.CENTER);
        btnThem.setVerticalTextPosition(SwingConstants.BOTTOM);

        btnChiTiet = new JButton("CHI TIẾT");
        btnChiTiet.setIcon(new ImageIcon(getClass().getResource("/icon/sua.png")));
        btnChiTiet.setHorizontalTextPosition(SwingConstants.CENTER);
        btnChiTiet.setVerticalTextPosition(SwingConstants.BOTTOM);

        btnSuaPhieu = new JButton("SỬA PHIẾU");
        btnSuaPhieu.setIcon(new ImageIcon(getClass().getResource("/icon/xoa.png")));
        btnSuaPhieu.setHorizontalTextPosition(SwingConstants.CENTER);
        btnSuaPhieu.setVerticalTextPosition(SwingConstants.BOTTOM);
        
        btnXuatExcel = new JButton("XUẤT EXCEL");
        btnXuatExcel.setIcon(new ImageIcon(getClass().getResource("/icon/xuatexcel.png")));
        btnXuatExcel.setHorizontalTextPosition(SwingConstants.CENTER);
        btnXuatExcel.setVerticalTextPosition(SwingConstants.BOTTOM);
        
        leftToolPanel.add(btnThem);
        leftToolPanel.add(btnChiTiet);
        leftToolPanel.add(btnSuaPhieu);
        leftToolPanel.add(btnXuatExcel);

        //(B) Panel chứa combo + ô tìm kiếm + nút làm mới (FlowLayout phải)
        JPanel rightToolPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        cbbSearchType = new JComboBox<>(new String[]{"Tất cả", "Mã phiếu nhập", "Nhà cung cấp", "Nhân viên nhập"});
        txtSearch = new JTextField(15);
        PromptSupport.setPrompt("Nhập nội dung tìm kiếm...", txtSearch);
        PromptSupport.setForeground(Color.GRAY, txtSearch);
        btnLamMoi = new JButton("Làm mới");
        btnTimKiem2=new JButton("Tìm Kiếm");

        rightToolPanel.add(cbbSearchType);
        rightToolPanel.add(txtSearch);
        rightToolPanel.add(btnTimKiem2);
        rightToolPanel.add(btnLamMoi);

        // Ghép 2 panel vào topPanel
        topPanel.add(leftToolPanel, BorderLayout.WEST);
        topPanel.add(rightToolPanel, BorderLayout.EAST);

        // Thêm topPanel lên khu vực north
        add(topPanel, BorderLayout.NORTH);

        
        
        
        // ==================== Panel lọc bên trái ====================
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new GridLayout(7, 1, 0, 0)); 
        // Hoặc BoxLayout, tuỳ ý
        filterPanel.setPreferredSize(new Dimension(200, 0)); 
        // Để chiều rộng cố định, chiều cao flexy
     

        // chọn nhà cung cấp
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.add(new JLabel("Nhà cung cấp:"));
        NhaCungCapBUS nccbus=new NhaCungCapBUS();
        // 1. Lấy danh sách NCC từ cơ sở dữ liệu
        List<NhaCungCapDTO> danhSachNCC = new ArrayList<>();
        danhSachNCC=nccbus.layTatCa();

        // 2. Tạo model cho ComboBox và thêm "Tất cả"
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("Tất cả");

        // 3. Thêm mã NCC vào model
        for (NhaCungCapDTO ncc : danhSachNCC) {
            model.addElement(ncc.getTenNCC());
        }

        // 4. Gán model cho ComboBox
        cbbNhaCungCap = new JComboBox<>(model);

        
        
        
        row1.add(cbbNhaCungCap);
        filterPanel.add(row1);

        // chọn nhân viên
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.add(new JLabel("Nhân viên:"));
        NhanVienBUS nvbus=new NhanVienBUS();
        List<NhanVienDTO> listnv=new ArrayList<>();
        listnv=nvbus.layTatCa();
        DefaultComboBoxModel<String> modelcomboboxnv=new DefaultComboBoxModel<>();
        modelcomboboxnv.addElement("Tất cả");
        for(NhanVienDTO nvdto : listnv)
        {
            modelcomboboxnv.addElement(nvdto.getMaNV());
        }
        
        cbbNhanVien = new JComboBox<>(modelcomboboxnv);
        row2.add(cbbNhanVien);
        filterPanel.add(row2);
        
        Locale.setDefault(new Locale("vi", "VN"));

        // Từ ngày
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row3.add(new JLabel("Từ ngày:"));
         dateChooserTuNgay = new JDateChooser();
        dateChooserTuNgay.setLocale(new Locale("vi", "VN")); // hiển thị tiếng Việt
        dateChooserTuNgay.setDateFormatString("dd/MM/yyyy");
        row3.add(dateChooserTuNgay);
        filterPanel.add(row3);

        // Đến ngày
        JPanel row4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row4.add(new JLabel("Đến ngày:"));
         dateChooserDenNgay = new JDateChooser();
        dateChooserDenNgay.setLocale(new Locale("vi", "VN")); // hiển thị tiếng Việt
        dateChooserDenNgay.setDateFormatString("dd/MM/yyyy");
        row4.add(dateChooserDenNgay);
        filterPanel.add(row4);

        //Số tiền
        JPanel row5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row5.add(new JLabel("Từ Số tiền (VNĐ):"));
        txtTuSoTien = new JTextField(10);
        row5.add(txtTuSoTien);
        filterPanel.add(row5);
        
        JPanel row7= new JPanel(new FlowLayout(FlowLayout.LEFT));
        row7.add(new JLabel("Đến Số tiền (VNĐ):"));
        txtDenSotien = new JTextField(10);
        row7.add(txtDenSotien);
        filterPanel.add(row7);
        
        
        
        JPanel row6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnTimKiem = new JButton("Tìm Kiếm");  // Tạo một nút mới với tên "Click Me"
        row6.add(btnTimKiem);  // Thêm button vào panel
        filterPanel.add(row6);  // Thêm panel chứa button vào filterPanel


        // Thêm filterPanel vào west
        add(filterPanel, BorderLayout.WEST);

        // ==================== Bảng hiển thị trung tâm ====================
        String[] columns = {"STT", "Mã phiếu", "Tên nhà cung cấp", "Nhân viên nhập", "Thời gian nhập", "Tổng tiền"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        // Cho bảng vào JScrollPane
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        // ==================== Sự kiện mẫu này nọ ====================
        
        
        btnLamMoi.addActionListener(e -> {
                loadData();
                txtSearch.setText("");
                cbbSearchType.setSelectedIndex(0);
                cbbNhaCungCap.setSelectedIndex(0);
                cbbNhanVien.setSelectedIndex(0);
             
                txtDenSotien.setText("");
                txtTuSoTien.setText("");
                dateChooserTuNgay.setDate(null);
                dateChooserDenNgay.setDate(null);
        }
        
        );
        btnTimKiem.addActionListener(e -> {
        
            String mancc=(String) cbbNhaCungCap.getSelectedItem();
            String manv=(String ) cbbNhanVien.getSelectedItem();
            Date ngaystar=dateChooserTuNgay.getDate();
            Date ngayend=dateChooserDenNgay.getDate();
            int tongtienstar=0;
            int tongtienend=0;
            String text = txtTuSoTien.getText(); 
            try {
                 tongtienstar = Integer.parseInt(text);  
                System.out.println("Số đã nhập: " + tongtienstar);
            } catch (NumberFormatException ex) { 
                System.out.println("GIA TRI NHAP VAO KHONG HOP LE .");
            }
            String text2=txtDenSotien.getText();
            try {
                tongtienend = Integer.parseInt(text2);  
                System.out.println("Số đã nhập: " + tongtienend);
            } catch (NumberFormatException ex) { 
                System.out.println("GIA TRI NHAP VAO KHONG HOP LE .");
            }
            System.out.println("MA NHAN VIEN "+manv+"MA NHA CUNG CAP :"+mancc+"NGAY STAR :"+ngaystar+"NGAY END :"+ngayend+"TONGTIENSTAR:"+tongtienstar+"TONG TIEN END "+tongtienend);
            
           
            loadDataFromFilter(manv, mancc, ngaystar, ngayend, tongtienstar, tongtienend);
            
            
    
            
            
          
            
            
        });
        btnTimKiem2.addActionListener(e-> load_datatimkiem());
        
        btnThem.addActionListener(e -> {
        ThemPhieuNhapDialog dialog = new ThemPhieuNhapDialog((Frame) SwingUtilities.getWindowAncestor(this), true,this.manv);
        dialog.setLocationRelativeTo(this); // Căn giữa với frame cha
        dialog.setVisible(true);
        tableModel.setRowCount(0);
        txtSearch.setText("");
        loadData();
    });

        btnChiTiet.addActionListener(e -> {   
        int row = table.getSelectedRow();
        if (row == -1) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu nhập để xem chi tiết.");
        return;
        }
        String maPhieuNhap = table.getValueAt(row, 1).toString();
        ChiTietPhieuNhapDialog dialog = new ChiTietPhieuNhapDialog(new JFrame(), true, maPhieuNhap);
        dialog.setVisible(true);

            
        });
        
        btnSuaPhieu.addActionListener(e -> {
               int row = table.getSelectedRow();
 
        if (row == -1) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu nhập để xem chi tiết.");
        return;
        }
        String maPhieuNhap = table.getValueAt(row, 1).toString();
        SuaPhieuNhapDialog dialog=new SuaPhieuNhapDialog(new JFrame(), true, maPhieuNhap);
        dialog.setVisible(true);
            
        });
        
       
      btnXuatExcel.addActionListener(e -> {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getAbsolutePath().endsWith(".xlsx")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".xlsx");
            }

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Danh sách phiếu nhập");

                // Tạo header
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(tableModel.getColumnName(i));
                }

                // Dữ liệu
                for (int row = 0; row < tableModel.getRowCount(); row++) {
                    Row excelRow = sheet.createRow(row + 1);
                    for (int col = 0; col < tableModel.getColumnCount(); col++) {
                        Cell cell = excelRow.createCell(col);
                        Object value = tableModel.getValueAt(row, col);
                        cell.setCellValue(value != null ? value.toString() : "");
                    }
                }

            
                FileOutputStream fileOut = new FileOutputStream(fileToSave);
                workbook.write(fileOut);
                fileOut.close();


                Desktop.getDesktop().open(fileToSave);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi khi xuất Excel: " + ex.getMessage());
            }
        }
    });


        
        
        

        
     
   
    
    }
private void load_datatimkiem()
{
      String type = cbbSearchType.getSelectedItem().toString();
            String keyword = txtSearch.getText().trim();
            load(type, keyword);
}
    
private void loadData() {
 
    tableModel.setRowCount(0);
        NhaCungCapBUS nccbus =new NhaCungCapBUS();

    List<PhieuNhapDTO> danhSach = new PhieuNhapBUS().getAllPhieuNhap();
  

    int stt = 1;
    for (PhieuNhapDTO pn : danhSach) {
        String tenNCC=nccbus.layTenNhaCungCapTheoMa(pn.getMaNCC());
        tableModel.addRow(new Object[]{
            stt++,
            pn.getMaPhieuNhap(),
            tenNCC,
            pn.getMaNhanVien(), 
            pn.getNgayNhap(),
            pn.getTongTien()
        });
    }
}

    


private void loadDataFromFilter(String tenncc,String manv,Date ngaystar,Date ngayend,int tongtienstar,int tongtienend) {
        // Xóa dữ liệu cũ
        tableModel.setRowCount(0);
        NhaCungCapBUS nccbus =new NhaCungCapBUS();

     
        List<PhieuNhapDTO> danhSach = new PhieuNhapBUS().get_filter(manv, tenncc, ngaystar, ngayend, tongtienstar, tongtienend);


        int stt = 1;
        for (PhieuNhapDTO pn : danhSach) {
            String tenNCC=nccbus.layTenNhaCungCapTheoMa(pn.getMaNCC());
            tableModel.addRow(new Object[]{
                stt++,
                pn.getMaPhieuNhap(),
                tenNCC,
                pn.getMaNhanVien(), 
                pn.getNgayNhap(),
                pn.getTongTien()
            });
        }
    }





private void load(String type,String keyword)
{
    int stt=1;
 
    tableModel.setRowCount(0);
    List<PhieuNhapDTO> ds =new ArrayList<>();
    ds=new PhieuNhapBUS().getAllPhieuNhap();
    NhaCungCapBUS nccbus=new NhaCungCapBUS();
    for (PhieuNhapDTO pn : ds)
    {
        boolean check=false;
        switch (type)
        {
            case "Tất cả":
                check=true;
                break;
            case "Mã phiếu nhập":
                if(pn.getMaPhieuNhap().toLowerCase().contains(keyword.toLowerCase()))
                {
                    check=true;
                    
                }
                break;
            case "Nhà cung cấp" :
                if(nccbus.layTenNhaCungCapTheoMa(pn.getMaNCC()).toLowerCase().contains(keyword.toLowerCase()))
                {
                    
                    check=true;;
                    
                }
                break;
            case "Nhân viên nhập":
            {
                if(pn.getMaNhanVien().toLowerCase().contains(keyword.toLowerCase()))
                {
                    check=true;
                     
                }
                break;
            }
                
        }
        if(check)
        {
            tableModel.addRow(new Object[]{
                stt++,
                pn.getMaPhieuNhap(),
                nccbus.layTenNhaCungCapTheoMa(pn.MaNCC),
                pn.getMaNhanVien(),
                pn.getNgayNhap(),
                pn.getTongTien()
                
            
            });
            
        }
        
    }
}












}
