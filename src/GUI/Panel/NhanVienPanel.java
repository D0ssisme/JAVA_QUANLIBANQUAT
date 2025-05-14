
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
    // Các thành phần dữ liệu
    private final DefaultTableModel modelBangDuLieu;
    private final JTable bangDuLieu;
    private JComboBox<String> cbbBoLoc;
    private JTextField txtTimKiem;
    private final NhanVienBUS nhanVienBUS = new NhanVienBUS();

    /**
     * Khởi tạo panel quản lý nhân viên
     */
    public NhanVienPanel() {
        // Thiết lập layout và viền
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Tạo thanh công cụ và thêm vào panel
        JPanel thanhCongCu = taoThanhCongCu();
        add(thanhCongCu, BorderLayout.NORTH);

        // Khởi tạo mô hình dữ liệu cho bảng với các cột
        modelBangDuLieu = new DefaultTableModel(
            new Object[]{"Mã NV", "Họ tên", "Chức vụ", "SĐT", "Địa chỉ"}, 0
        ) {
            /**
             * Ghi đè phương thức để không cho phép chỉnh sửa trực tiếp trên bảng
             */
            @Override 
            public boolean isCellEditable(int row, int column) { 
                return false; 
            }
        };
        
        // Khởi tạo bảng với mô hình dữ liệu vừa tạo
        bangDuLieu = new JTable(modelBangDuLieu);
        bangDuLieu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Chỉ chọn 1 dòng

        // Nạp dữ liệu ban đầu vào bảng
        napDuLieuVaoBang();

        // Thêm bảng vào panel với thanh cuộn
        add(new JScrollPane(bangDuLieu), BorderLayout.CENTER);
    }

    /**
     * Tạo thanh công cụ với các nút chức năng và bộ lọc tìm kiếm
     * @return Panel chứa các điều khiển
     */
    private JPanel taoThanhCongCu() {
        // Tạo panel chính cho thanh công cụ
        JPanel thanhCongCu = new JPanel();
        thanhCongCu.setLayout(new GridBagLayout());
        thanhCongCu.setBackground(Color.WHITE);
        thanhCongCu.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Cấu hình GridBagConstraints để sắp xếp các thành phần
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
        JButton btnThem = new JButton("THÊM", iconThem);
        btnThem.setHorizontalTextPosition(SwingConstants.CENTER);
        btnThem.setVerticalTextPosition(SwingConstants.BOTTOM);
        thanhCongCu.add(btnThem, gbc);
        
        // Nút SỬA
        gbc.gridx = 1;
        ImageIcon iconSua = new ImageIcon(getClass().getResource("/icon/sua.png"));
        JButton btnSua = new JButton("SỬA", iconSua);
        btnSua.setHorizontalTextPosition(SwingConstants.CENTER);
        btnSua.setVerticalTextPosition(SwingConstants.BOTTOM);
        thanhCongCu.add(btnSua, gbc);
        
        // Nút XÓA
        gbc.gridx = 2;
        ImageIcon iconXoa = new ImageIcon(getClass().getResource("/icon/xoa.png"));
        JButton btnXoa = new JButton("XÓA", iconXoa);
        btnXoa.setHorizontalTextPosition(SwingConstants.CENTER);
        btnXoa.setVerticalTextPosition(SwingConstants.BOTTOM);
        thanhCongCu.add(btnXoa, gbc);
        
        // Nút XUẤT EXCEL
        gbc.gridx = 3;
        ImageIcon iconExcel = new ImageIcon(getClass().getResource("/icon/xuatexcel.png"));
        JButton btnExcel = new JButton("XUẤT EXCEL", iconExcel);
        btnExcel.setHorizontalTextPosition(SwingConstants.CENTER);
        btnExcel.setVerticalTextPosition(SwingConstants.BOTTOM);
        thanhCongCu.add(btnExcel, gbc);
        
        // ===== PHẦN 2: Khoảng trống giữa các nút và phần tìm kiếm =====
        gbc.gridx = 4;
        gbc.weightx = 1.0; // Phần này sẽ chiếm khoảng trống còn lại
        thanhCongCu.add(Box.createHorizontalGlue(), gbc);
        
        // ===== PHẦN 3: Panel tìm kiếm và lọc =====
        JPanel panelTimKiem = new JPanel();
        panelTimKiem.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panelTimKiem.setOpaque(false);
        
        // Điều khiển lọc theo chức vụ
        JLabel lblBoLoc = new JLabel("Chức vụ:");
        cbbBoLoc = new JComboBox<>(new String[]{"Tất cả", "Quản lý", "Nhân viên", "Bảo vệ"});
        cbbBoLoc.setPreferredSize(new Dimension(110, 25));
        
        // Điều khiển tìm kiếm
        JLabel lblTimKiem = new JLabel("Tìm kiếm:");
        txtTimKiem = new JTextField();
        txtTimKiem.setPreferredSize(new Dimension(180, 25));
        
        // Nút làm mới
        JButton btnLamMoi = new JButton("LÀM MỚI");
        
        // Thêm các điều khiển vào panel tìm kiếm
        panelTimKiem.add(lblBoLoc);
        panelTimKiem.add(cbbBoLoc);
        panelTimKiem.add(lblTimKiem);
        panelTimKiem.add(txtTimKiem);
        panelTimKiem.add(btnLamMoi);
        
        // Thêm panel tìm kiếm vào thanh công cụ
        gbc.gridx = 5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        thanhCongCu.add(panelTimKiem, gbc);
        
        // ===== PHẦN 4: Gắn các sự kiện =====
        
        // Sự kiện nút THÊM
        btnThem.addActionListener(e -> {
            // Mở hộp thoại thêm nhân viên mới
            ThemNhanVienDialog hopThoai = new ThemNhanVienDialog(
                SwingUtilities.getWindowAncestor(this)
            );
            hopThoai.setVisible(true);
            
            // Nếu người dùng đã lưu, cập nhật lại bảng
            if (hopThoai.isSaved()) {
                napDuLieuVaoBang();
            }
        });

        // Sự kiện nút SỬA
        btnSua.addActionListener(e -> {
            // Kiểm tra xem đã chọn nhân viên nào chưa
            int dongDangChon = bangDuLieu.getSelectedRow();
            if (dongDangChon < 0) {
                JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một nhân viên để chỉnh sửa",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Lấy mã nhân viên của dòng đã chọn
            String maNV = modelBangDuLieu.getValueAt(dongDangChon, 0).toString();
            
            // Mở hộp thoại sửa thông tin nhân viên
            SuaNhanVienDialog hopThoai = new SuaNhanVienDialog(
                SwingUtilities.getWindowAncestor(this), maNV
            );
            hopThoai.setVisible(true);
            
            // Nếu người dùng đã lưu, cập nhật lại bảng
            if (hopThoai.isSaved()) {
                napDuLieuVaoBang();
            }
        });

        // Sự kiện nút XÓA - ĐÃ SỬA
        btnXoa.addActionListener(e -> {
            // Kiểm tra xem đã chọn nhân viên nào chưa
            int dongDangChon = bangDuLieu.getSelectedRow();
            if (dongDangChon < 0) {
                JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một nhân viên để xóa",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Lấy mã nhân viên của dòng đã chọn
            String maNV = modelBangDuLieu.getValueAt(dongDangChon, 0).toString();
            
            // Thực hiện xóa nhân viên - phương thức trả về false nếu:
            // 1. Có lỗi xảy ra trong quá trình xóa
            // 2. Người dùng hủy thao tác khi có xác nhận
            boolean ketQua = nhanVienBUS.xoa(maNV);
            
            // Chỉ hiển thị thông báo và cập nhật bảng khi XÓA THÀNH CÔNG
            if (ketQua) {
                JOptionPane.showMessageDialog(this,
                    "Đã xóa nhân viên thành công",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                napDuLieuVaoBang(); // Cập nhật lại bảng
            }
            // KHÔNG hiển thị thông báo lỗi ở đây nữa
        });

        // Sự kiện nút XUẤT EXCEL
        btnExcel.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Chức năng xuất Excel đang được phát triển",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Thêm sự kiện lọc và tìm kiếm
        ActionListener suKienTimKiem = e -> apDungLocVaTimKiem();
        cbbBoLoc.addActionListener(suKienTimKiem);
        txtTimKiem.addActionListener(suKienTimKiem);
        
        // Sự kiện nút LÀM MỚI
        btnLamMoi.addActionListener(e -> {
            // Đặt lại giá trị mặc định cho các điều khiển
            cbbBoLoc.setSelectedIndex(0);
            txtTimKiem.setText("");
            // Nạp lại toàn bộ dữ liệu
            napDuLieuVaoBang();
        });

        return thanhCongCu;
    }
    
    /**
     * Áp dụng bộ lọc và tìm kiếm dựa trên các tiêu chí người dùng đã chọn
     */
    private void apDungLocVaTimKiem() {
        // Lấy danh sách tất cả nhân viên
        List<NhanVienDTO> danhSachNhanVien = nhanVienBUS.layTatCa();

        // 1) Lọc theo chức vụ nếu không phải "Tất cả"
        String chucVu = (String)cbbBoLoc.getSelectedItem();
        if (!"Tất cả".equals(chucVu)) {
            // Sử dụng Stream API để lọc nhân viên theo chức vụ
            danhSachNhanVien = danhSachNhanVien.stream()
                     .filter(nv -> nv.getChucVu().equalsIgnoreCase(chucVu))
                     .collect(Collectors.toList());
        }

        // 2) Tìm kiếm theo mã hoặc họ tên (nếu có nhập từ khóa)
        String tuKhoa = txtTimKiem.getText().trim().toLowerCase();
        if (!tuKhoa.isEmpty()) {
            // Sử dụng Stream API để lọc nhân viên theo từ khóa
            danhSachNhanVien = danhSachNhanVien.stream()
                     .filter(nv -> 
                         nv.getMaNV().toLowerCase().contains(tuKhoa) || 
                         nv.getHoTen().toLowerCase().contains(tuKhoa)
                     )
                     .collect(Collectors.toList());
        }

        // Cập nhật bảng với kết quả đã lọc
        capNhatBangVoiKetQua(danhSachNhanVien);
    }

    /**
     * Cập nhật bảng với danh sách nhân viên đã lọc
     * @param danhSach Danh sách nhân viên cần hiển thị
     */
    private void capNhatBangVoiKetQua(List<NhanVienDTO> danhSach) {
        // Xóa dữ liệu cũ trong bảng
        modelBangDuLieu.setRowCount(0);
        
        // Thêm từng nhân viên vào bảng
        for (NhanVienDTO nv : danhSach) {
            modelBangDuLieu.addRow(new Object[]{
                nv.getMaNV(),      // Cột 0: Mã nhân viên
                nv.getHoTen(),     // Cột 1: Họ tên
                nv.getChucVu(),    // Cột 2: Chức vụ
                nv.getSdt(),       // Cột 3: Số điện thoại
                nv.getDiaChi()     // Cột 4: Địa chỉ
            });
        }
    }

    /**
     * Nạp lại toàn bộ dữ liệu vào bảng từ cơ sở dữ liệu
     */
    private void napDuLieuVaoBang() {
        // Lấy toàn bộ danh sách nhân viên
        List<NhanVienDTO> danhSach = nhanVienBUS.layTatCa();
        
        // Cập nhật bảng với danh sách đầy đủ
        capNhatBangVoiKetQua(danhSach);
    }
}