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

/**
 * Panel quản lý tài khoản - cho phép xem danh sách, thêm, sửa, xóa tài khoản người dùng
 * Có 2 chế độ: 
 * 1. Chế độ admin (khi isAdmin = true) - hiển thị nút đăng xuất thay vì xuất Excel
 * 2. Chế độ quản lý thông thường
 */
public class TaiKhoanPanel extends JPanel {
    // Các thành phần dữ liệu
    private final TaiKhoanBUS taiKhoanBUS = new TaiKhoanBUS();
    private final DefaultTableModel modelBangDuLieu;
    private final JTable bangDuLieu;
    private final TableRowSorter<DefaultTableModel> boLocDuLieu;

    // Các điều khiển giao diện
    private JComboBox<String> cbbQuyenHang;
    private JTextField txtTimKiem;
    private JButton btnThem, btnSua, btnXoa, btnXuatExcel, btnLamMoi, btnDangXuat;
    
    // Biến trạng thái
    private boolean isAdmin; // True nếu panel được mở từ giao diện Admin

    /**
     * Constructor với tham số để xác định nguồn của panel
     * @param isAdmin true nếu panel được mở từ giao diện Admin, ngược lại false
     */
    public TaiKhoanPanel(boolean isAdmin) {
        this.isAdmin = isAdmin;
        
        // Cài đặt layout
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Tạo và thêm thanh công cụ
        JPanel thanhCongCu = taoThanhCongCu();
        add(thanhCongCu, BorderLayout.NORTH);

        // Cài đặt bảng dữ liệu
        modelBangDuLieu = new DefaultTableModel(
            new Object[]{"Mã TK", "Mã NV", "Tên TK", "Mật khẩu", "Quyền hạng"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; } // Không cho phép chỉnh sửa trực tiếp
        };
        
        bangDuLieu = new JTable(modelBangDuLieu);
        bangDuLieu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Chỉ chọn 1 dòng
        
        // Thêm bộ lọc và sắp xếp cho bảng
        boLocDuLieu = new TableRowSorter<>(modelBangDuLieu);
        bangDuLieu.setRowSorter(boLocDuLieu);
        
        // Thêm bảng vào panel với thanh cuộn
        add(new JScrollPane(bangDuLieu), BorderLayout.CENTER);

        // Nạp dữ liệu vào bảng
        napDuLieuVaoBang();
    }
    
    /**
     * Constructor mặc định - cho tương thích với code cũ
     */
    public TaiKhoanPanel() {
        this(false); // Mặc định không phải Admin
    }

    /**
     * Tạo thanh công cụ với các nút chức năng và bộ lọc
     */
    private JPanel taoThanhCongCu() {
        // Panel chính cho thanh công cụ
        JPanel thanh = new JPanel(new GridBagLayout());
        thanh.setBackground(Color.WHITE);
        thanh.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 5); // Khoảng cách giữa các nút

        // ===== PHẦN 1: Các nút chức năng =====
        
        // Nút THÊM
        ImageIcon iconThem = new ImageIcon(getClass().getResource("/icon/them.png"));
        btnThem = new JButton("THÊM", iconThem);
        btnThem.setHorizontalTextPosition(SwingConstants.CENTER);
        btnThem.setVerticalTextPosition(SwingConstants.BOTTOM);
        thanh.add(btnThem, gbc);
        
        // Nút SỬA
        gbc.gridx = 1;
        ImageIcon iconSua = new ImageIcon(getClass().getResource("/icon/sua.png"));
        btnSua = new JButton("SỬA", iconSua);
        btnSua.setHorizontalTextPosition(SwingConstants.CENTER);
        btnSua.setVerticalTextPosition(SwingConstants.BOTTOM);
        thanh.add(btnSua, gbc);
        
        // Nút XÓA
        gbc.gridx = 2;
        ImageIcon iconXoa = new ImageIcon(getClass().getResource("/icon/xoa.png"));
        btnXoa = new JButton("XÓA", iconXoa);
        btnXoa.setHorizontalTextPosition(SwingConstants.CENTER);
        btnXoa.setVerticalTextPosition(SwingConstants.BOTTOM);
        thanh.add(btnXoa, gbc);
        
        // Nút thứ 4: XUẤT EXCEL hoặc ĐĂNG XUẤT (tùy chế độ)
        gbc.gridx = 3;
        if (!isAdmin) {
            // Nếu không phải Admin -> hiển thị nút XUẤT EXCEL
            ImageIcon iconExcel = new ImageIcon(getClass().getResource("/icon/xuatexcel.png"));
            btnXuatExcel = new JButton("XUẤT EXCEL", iconExcel);
            btnXuatExcel.setHorizontalTextPosition(SwingConstants.CENTER);
            btnXuatExcel.setVerticalTextPosition(SwingConstants.BOTTOM);
            thanh.add(btnXuatExcel, gbc);
        } else {
            // Nếu là Admin -> hiển thị nút ĐĂNG XUẤT
            ImageIcon iconLogout = new ImageIcon(getClass().getResource("/icon/logout.png"));
            btnDangXuat = new JButton("ĐĂNG XUẤT", iconLogout);
            btnDangXuat.setHorizontalTextPosition(SwingConstants.CENTER);
            btnDangXuat.setVerticalTextPosition(SwingConstants.BOTTOM);
            thanh.add(btnDangXuat, gbc);
        }
        
        // ===== PHẦN 2: Khoảng trống giữa các nút và phần tìm kiếm =====
        gbc.gridx = 4;
        gbc.weightx = 1.0; // Phần này sẽ chiếm khoảng trống còn lại
        thanh.add(Box.createHorizontalGlue(), gbc);
        
        // ===== PHẦN 3: Panel tìm kiếm và lọc =====
        JPanel panelTimKiem = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panelTimKiem.setOpaque(false);
        
        // Điều khiển lọc theo quyền hạng
        JLabel lblQuyenHang = new JLabel("Quyền hạng:");
        cbbQuyenHang = new JComboBox<>(new String[]{"Tất cả", "QuanLy", "NhanVien"});
        cbbQuyenHang.setPreferredSize(new Dimension(110, 25));
        
        // Điều khiển tìm kiếm
        JLabel lblTimKiem = new JLabel("Tìm kiếm:");
        txtTimKiem = new JTextField();
        txtTimKiem.setPreferredSize(new Dimension(180, 25));
        
        // Nút làm mới
        btnLamMoi = new JButton("LÀM MỚI");
        
        // Thêm các điều khiển vào panel tìm kiếm
        panelTimKiem.add(lblQuyenHang);
        panelTimKiem.add(cbbQuyenHang);
        panelTimKiem.add(lblTimKiem);
        panelTimKiem.add(txtTimKiem);
        panelTimKiem.add(btnLamMoi);
        
        // Thêm panel tìm kiếm vào thanh công cụ
        gbc.gridx = 5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        thanh.add(panelTimKiem, gbc);
        
        // ===== PHẦN 4: Gắn các sự kiện =====
        
        // Sự kiện nút THÊM
        btnThem.addActionListener(e -> {
            ThemTaiKhoanDialog hopThoai = new ThemTaiKhoanDialog(
                SwingUtilities.getWindowAncestor(this)
            );
            hopThoai.setVisible(true);
            // Nếu người dùng đã lưu dữ liệu, nạp lại bảng
            if (hopThoai.isSaved()) {
                napDuLieuVaoBang();
            }
        });
        
        // Sự kiện nút SỬA
        btnSua.addActionListener(e -> {
            // Kiểm tra xem đã chọn dòng nào chưa
            int dongDangChon = bangDuLieu.getSelectedRow();
            if (dongDangChon < 0) {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn tài khoản cần sửa!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Lấy mã tài khoản từ dòng đã chọn
            String maTK = modelBangDuLieu.getValueAt(
                bangDuLieu.convertRowIndexToModel(dongDangChon), 0).toString();
            
            // Kiểm tra nếu là Admin (không cho phép sửa)
            if ("TK000".equals(maTK)) {
                JOptionPane.showMessageDialog(this, 
                    "Không thể sửa tài khoản Admin",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Mở hộp thoại sửa tài khoản
            SuaTaiKhoanDialog hopThoai = new SuaTaiKhoanDialog(
                SwingUtilities.getWindowAncestor(this)
            );
            hopThoai.loadForEdit(maTK); // Nạp dữ liệu tài khoản cần sửa
            hopThoai.setVisible(true);
            
            // Nếu người dùng đã lưu, nạp lại bảng
            if (hopThoai.isSaved()) {
                napDuLieuVaoBang();
            }
        });
        
        // Sự kiện nút XÓA
        btnXoa.addActionListener(e -> {
            // Kiểm tra xem đã chọn dòng nào chưa
            int dongDangChon = bangDuLieu.getSelectedRow();
            if (dongDangChon < 0) {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn tài khoản cần xóa!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Lấy mã tài khoản từ dòng đã chọn
            String maTK = modelBangDuLieu.getValueAt(
                bangDuLieu.convertRowIndexToModel(dongDangChon), 0).toString();
            
            // Kiểm tra nếu là Admin (không cho phép xóa)
            if ("TK000".equals(maTK)) {
                JOptionPane.showMessageDialog(this, 
                    "Không thể xóa tài khoản Admin",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Hiển thị hộp thoại xác nhận xóa
            int luaChon = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa tài khoản " + maTK + "?", 
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                
            // Nếu người dùng chọn Yes, tiến hành xóa
            if (luaChon == JOptionPane.YES_OPTION) {
                taiKhoanBUS.xoa(maTK);
                napDuLieuVaoBang(); // Nạp lại bảng sau khi xóa
            }
        });
        
        // Gắn sự kiện tùy theo nút thứ 4
        if (!isAdmin && btnXuatExcel != null) {
            // Sự kiện nút xuất excel (chỉ cho chế độ không phải Admin)
            btnXuatExcel.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, 
                    "Chức năng xuất Excel đang được phát triển",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            });
        } else if (isAdmin && btnDangXuat != null) {
            // Sự kiện nút ĐĂNG XUẤT (chỉ cho chế độ Admin)
            btnDangXuat.addActionListener(e -> {
                // Đóng cửa sổ hiện tại
                Window cauSo = SwingUtilities.getWindowAncestor(this);
                cauSo.dispose();
                
                // Mở lại màn hình đăng nhập
                new LoginFrame().setVisible(true);
            });
        }
        
        // Sự kiện nút LÀM MỚI
        btnLamMoi.addActionListener(e -> {
            // Xóa nội dung tìm kiếm và đặt lại bộ lọc
            txtTimKiem.setText("");
            cbbQuyenHang.setSelectedIndex(0);
            boLocDuLieu.setRowFilter(null);
        });

        // Thêm sự kiện lọc khi người dùng nhập vào ô tìm kiếm
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { apDungBoLoc(); }
            public void removeUpdate(DocumentEvent e) { apDungBoLoc(); }
            public void changedUpdate(DocumentEvent e) { apDungBoLoc(); }
        });
        
        // Thêm sự kiện lọc khi người dùng chọn quyền hạng
        cbbQuyenHang.addActionListener(e -> apDungBoLoc());
        
        return thanh;
    }

    /**
     * Áp dụng bộ lọc cho bảng dựa trên giá trị tìm kiếm và quyền hạng đã chọn
     */
    private void apDungBoLoc() {
        List<RowFilter<DefaultTableModel, Integer>> cacBoLoc = new ArrayList<>();
        
        // 1. Lọc theo quyền hạng nếu không phải "Tất cả"
        String quyenHang = (String)cbbQuyenHang.getSelectedItem();
        if (!"Tất cả".equals(quyenHang)) {
            // Tạo bộ lọc chính xác cho cột quyền hạng (cột số 4)
            RowFilter<DefaultTableModel, Integer> locQuyenHang = RowFilter.regexFilter(
                "^" + Pattern.quote(quyenHang) + "$", 4);
            cacBoLoc.add(locQuyenHang);
        }
        
        // 2. Lọc theo nội dung tìm kiếm (tìm trong tất cả các cột)
        String tuKhoa = txtTimKiem.getText().trim();
        if (!tuKhoa.isEmpty()) {
            try {
                // Tạo bộ lọc chứa từ khóa (không phân biệt chữ hoa/thường)
                RowFilter<DefaultTableModel, Integer> locTuKhoa = RowFilter.regexFilter(
                    "(?i)" + Pattern.quote(tuKhoa));
                cacBoLoc.add(locTuKhoa);
            } catch (PatternSyntaxException ex) {
                // Bỏ qua nếu cú pháp regex không hợp lệ
            }
        }
        
        // Áp dụng bộ lọc vào bảng
        if (cacBoLoc.isEmpty()) {
            // Nếu không có bộ lọc nào -> hiển thị tất cả
            boLocDuLieu.setRowFilter(null);
        } else {
            // Nếu có bộ lọc -> áp dụng tất cả các bộ lọc (AND)
            boLocDuLieu.setRowFilter(RowFilter.andFilter(cacBoLoc));
        }
    }

    /**
     * Nạp dữ liệu từ cơ sở dữ liệu vào bảng
     */
    private void napDuLieuVaoBang() {
        // Xóa hết dữ liệu cũ
        modelBangDuLieu.setRowCount(0);
        
        // Lấy danh sách tài khoản từ BUS
        List<TaiKhoanDTO> danhSachTaiKhoan = taiKhoanBUS.layTatCa();
        
        // Thêm từng tài khoản vào bảng
        for (TaiKhoanDTO taiKhoan : danhSachTaiKhoan) {
            modelBangDuLieu.addRow(new Object[]{
                taiKhoan.getMaTaiKhoan(),       // Cột 0: Mã tài khoản
                taiKhoan.getMaNhanVien(),       // Cột 1: Mã nhân viên
                taiKhoan.getUsername(),         // Cột 2: Tên đăng nhập
                taiKhoan.getPassword(),         // Cột 3: Mật khẩu
                taiKhoan.getVaiTro()            // Cột 4: Vai trò (quyền hạng)
            });
        }
    }
}