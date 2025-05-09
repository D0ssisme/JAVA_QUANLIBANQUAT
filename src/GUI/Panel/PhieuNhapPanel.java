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
import GUI.Dialog.ChiTietPhieuNhapDialog;
import org.jdesktop.swingx.prompt.PromptSupport;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.toedter.calendar.JDateChooser;
import java.util.Locale;
import javax.swing.JOptionPane;



public class PhieuNhapPanel extends JPanel {
    private String manv;
    private JButton btnThem, btnChiTiet, btnHuyPhieu, btnXuatExcel, btnLamMoi,btnTimKiem;
    private JComboBox<String> cbbSearchType;
    private JTextField txtSearch;

    private JTable table;
    private DefaultTableModel tableModel;

    // Bộ lọc bên trái
    private JComboBox<String> cbbNhaCungCap, cbbNhanVien;
    private JTextField txtTuNgay, txtDenNgay, txtSoTien; 
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

        btnHuyPhieu = new JButton("HỦY PHIẾU");
        btnHuyPhieu.setIcon(new ImageIcon(getClass().getResource("/icon/xoa.png")));
        btnHuyPhieu.setHorizontalTextPosition(SwingConstants.CENTER);
        btnHuyPhieu.setVerticalTextPosition(SwingConstants.BOTTOM);
        
        btnXuatExcel = new JButton("XUẤT EXCEL");
        btnXuatExcel.setIcon(new ImageIcon(getClass().getResource("/icon/xuatexcel.png")));
        btnXuatExcel.setHorizontalTextPosition(SwingConstants.CENTER);
        btnXuatExcel.setVerticalTextPosition(SwingConstants.BOTTOM);
        
        leftToolPanel.add(btnThem);
        leftToolPanel.add(btnChiTiet);
        leftToolPanel.add(btnHuyPhieu);
        leftToolPanel.add(btnXuatExcel);

        //(B) Panel chứa combo + ô tìm kiếm + nút làm mới (FlowLayout phải)
        JPanel rightToolPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        cbbSearchType = new JComboBox<>(new String[]{"Tất cả", "Mã phiếu nhập", "Nhà cung cấp", "Nhân viên nhập"});
        txtSearch = new JTextField(15);
        PromptSupport.setPrompt("Nhập nội dung tìm kiếm...", txtSearch);
        PromptSupport.setForeground(Color.GRAY, txtSearch);
        btnLamMoi = new JButton("Làm mới");

        rightToolPanel.add(cbbSearchType);
        rightToolPanel.add(txtSearch);
        rightToolPanel.add(btnLamMoi);

        // Ghép 2 panel vào topPanel
        topPanel.add(leftToolPanel, BorderLayout.WEST);
        topPanel.add(rightToolPanel, BorderLayout.EAST);

        // Thêm topPanel lên khu vực north
        add(topPanel, BorderLayout.NORTH);

        
        
        
        // ==================== Panel lọc bên trái ====================
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new GridLayout(6, 1, 0, 0)); 
        // Hoặc BoxLayout, tuỳ ý
        filterPanel.setPreferredSize(new Dimension(200, 0)); 
        // Để chiều rộng cố định, chiều cao flexy
     

        // chọn nhà cung cấp
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.add(new JLabel("Nhà cung cấp:"));
        cbbNhaCungCap = new JComboBox<>(new String[]{"Tất cả", "NCC01", "NCC02", "NCC03"});
        row1.add(cbbNhaCungCap);
        filterPanel.add(row1);

        // chọn nhân viên
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.add(new JLabel("Nhân viên:"));
        cbbNhanVien = new JComboBox<>(new String[]{"Tất cả", "NV01", "NV02", "NV03"});
        row2.add(cbbNhanVien);
        filterPanel.add(row2);
        
        Locale.setDefault(new Locale("vi", "VN"));

        // Từ ngày
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row3.add(new JLabel("Từ ngày:"));
        JDateChooser dateChooserTuNgay = new JDateChooser();
        dateChooserTuNgay.setLocale(new Locale("vi", "VN")); // hiển thị tiếng Việt
        dateChooserTuNgay.setDateFormatString("dd/MM/yyyy");
        row3.add(dateChooserTuNgay);
        filterPanel.add(row3);

        // Đến ngày
        JPanel row4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row4.add(new JLabel("Đến ngày:"));
        JDateChooser dateChooserDenNgay = new JDateChooser();
        dateChooserDenNgay.setLocale(new Locale("vi", "VN")); // hiển thị tiếng Việt
        dateChooserDenNgay.setDateFormatString("dd/MM/yyyy");
        row4.add(dateChooserDenNgay);
        filterPanel.add(row4);

        //Số tiền
        JPanel row5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row5.add(new JLabel("Số tiền (VNĐ):"));
        txtSoTien = new JTextField(10);
        row5.add(txtSoTien);
        filterPanel.add(row5);
        
        JPanel row6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnTimKiem = new JButton("Tìm Kiếm");  // Tạo một nút mới với tên "Click Me"
        row6.add(btnTimKiem);  // Thêm button vào panel
        filterPanel.add(row6);  // Thêm panel chứa button vào filterPanel


        // Thêm filterPanel vào west
        add(filterPanel, BorderLayout.WEST);

        // ==================== Bảng hiển thị trung tâm ====================
        String[] columns = {"STT", "Mã phiếu", "Tên nhà cung cấp", "Nhân viên nhập", "Thời gian", "Tổng tiền"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        // Cho bảng vào JScrollPane
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        // ==================== Sự kiện mẫu này nọ ====================
        btnTimKiem.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Thông báo của bạn ở đây", "Tiêu đề thông báo", JOptionPane.INFORMATION_MESSAGE);
        });
        btnThem.addActionListener(e -> {
        ThemPhieuNhapDialog dialog = new ThemPhieuNhapDialog((Frame) SwingUtilities.getWindowAncestor(this), true,this.manv);
        dialog.setLocationRelativeTo(this); // Căn giữa với frame cha
        dialog.setVisible(true);
    });

        btnChiTiet.addActionListener(e -> {   
        int row = table.getSelectedRow();
    // Nếu không có dòng nào được chọn
        if (row == -1) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu nhập để xem chi tiết.");
        return;
        }
        // Lấy mã phiếu nhập từ cột thứ 2 (giả sử cột mã phiếu nhập là cột thứ 2)
        String maPhieuNhap = table.getValueAt(row, 1).toString();
        // Mở dialog hiển thị chi tiết phiếu nhập
       // Mở dialog hiển thị chi tiết phiếu nhập và truyền mã phiếu nhập vào
        ChiTietPhieuNhapDialog dialog = new ChiTietPhieuNhapDialog(new JFrame(), true, maPhieuNhap);
        dialog.setVisible(true);

            
        });
        btnHuyPhieu.addActionListener(e -> {
            
        });
        btnXuatExcel.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Xuất Excel!");
        });
        btnLamMoi.addActionListener(e -> {
            // Xoá bảng rồi thêm lại
        tableModel.setRowCount(0);
        txtSearch.setText("");
        loadData();
        });
        
        
        
        
        
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
        public void insertUpdate(DocumentEvent e) {
            search();
        }

        public void removeUpdate(DocumentEvent e) {
            search();
        }

        public void changedUpdate(DocumentEvent e) {
            search();
        }

        private void search() {
            String type = cbbSearchType.getSelectedItem().toString();
            String keyword = txtSearch.getText().trim();
            loadData(type, keyword);
        }
        
});

        
        
        
   
    
    }

private void loadData() {
    // Xóa dữ liệu cũ
    tableModel.setRowCount(0);

    // Lấy danh sách phiếu nhập và map MaNCC -> TenNCC
    List<PhieuNhapDTO> danhSach = new PhieuNhapBUS().getAllPhieuNhap();
    Map<String, String> nhaCungCapMap = PhieuNhapBUS.getTenNhaCungCapMap();

    int stt = 1;
    for (PhieuNhapDTO pn : danhSach) {
        String tenNCC = nhaCungCapMap.getOrDefault(pn.getMaNCC(), "Không rõ");
        tableModel.addRow(new Object[]{
            stt++,
            pn.getMaPhieuNhap(),
            tenNCC,
            pn.getMaNhanVien(), // hoặc map tên nhân viên nếu cần
            pn.getNgayNhap(),
            pn.getTongTien()
        });
    }
}
private void loadData(String type, String keyword) {
    tableModel.setRowCount(0);

    List<PhieuNhapDTO> danhSach = new PhieuNhapBUS().getAllPhieuNhap();
    Map<String, String> nhaCungCapMap = PhieuNhapBUS.getTenNhaCungCapMap();

    int stt = 1;
    for (PhieuNhapDTO pn : danhSach) {
        String tenNCC = nhaCungCapMap.getOrDefault(pn.getMaNCC(), "Không rõ");

        boolean match = switch (type) {
            case "Tất cả" -> true;
            case "Mã phiếu nhập" -> pn.getMaPhieuNhap().toLowerCase().contains(keyword.toLowerCase());
            case "Nhà cung cấp" -> tenNCC.toLowerCase().contains(keyword.toLowerCase());
            case "Nhân viên nhập" -> pn.getMaNhanVien().toLowerCase().contains(keyword.toLowerCase()); // Hoặc map tên nhân viên nếu cần
            default -> true;
        };

        if (match) {
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
}












}
