package GUI.Panel;

import DTO.DBConnection;
import DTO.KhuyenMaiDTO;
import DAO.KhachHangDAO;
import DTO.QuatDTO;
import DAO.KhuyenMaiDAO;
import DTO.KhachHangDTO;
import BUS.KhachHangBUS;
import DAO.ChiTietHoaDonDAO;
import DAO.HoaDonDAO;
import DAO.QuatDAO;
import DTO.ChiTietHoaDonDTO;
import DTO.HoaDonDTO;
import java.util.Date;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.*;

public class BanQuatPanel extends JPanel implements ActionListener, Serializable {

    private static final long serialVersionUID = 1L;

    // Map loại → mô tả điều kiện
    private static final Map<Integer, String> dieuKienMap = new HashMap<>();

    static {
        dieuKienMap.put(1, "Đơn hàng ≥ 500.000₫");
        dieuKienMap.put(2, "Mua ≥ 2 sản phẩm");
        dieuKienMap.put(3, "Áp dụng cho mọi hóa đơn");
    }

    private JLabel lblTitle;
    private JLabel lblNguoiLap, lblNgayLap;
    private JLabel lblTrangThai, lblKhachHang, lblKhuyenMai, lblSanPham;
    private JTextField txtNguoiLap, txtNgayLap;
    private String maNhanVien;
    private JRadioButton rdoThanhVien, rdoVangLai;
    private ButtonGroup bgTrangThai;
    private JComboBox<String> cbbKhachHang, cbbKhuyenMai, cbbSanPham;
    private JCheckBox chkApDungKM;
    private JButton btnThemSP, btnThanhToan, btnXuatPDF, btnHuy;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtTong;
    private static final Color BACKGROUND_COLOR = new Color(233, 247, 249);
    private JButton btnThemKhachHang;

    public BanQuatPanel(String maNhanVien) {
        this.maNhanVien = maNhanVien;
        initComponent();
    }

    public BanQuatPanel() {
        this(null);
    }

    private void initComponent() {
        setLayout(new BorderLayout(10, 10));
        setBackground(BACKGROUND_COLOR);

        // NORTH: Title
        lblTitle = new JLabel("Bán Quạt", SwingConstants.CENTER);
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 24f));
        add(lblTitle, BorderLayout.NORTH);

        // CENTER: Form + Table
        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setOpaque(false);
        add(center, BorderLayout.CENTER);

        // Form panel
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Người lập
        lblNguoiLap = new JLabel("Người lập:");
        txtNguoiLap = new JTextField(loadTenNhanVien(), 15);
        txtNguoiLap.setEditable(false);
        lblNgayLap = new JLabel("Ngày lập:");
        txtNgayLap = new JTextField(LocalDate.now().format(DateTimeFormatter.ISO_DATE), 15);
        txtNgayLap.setEditable(false);
        gbc.gridy = 0;
        gbc.gridx = 0;
        form.add(lblNguoiLap, gbc);
        gbc.gridx = 1;
        form.add(txtNguoiLap, gbc);
        gbc.gridx = 2;
        form.add(lblNgayLap, gbc);
        gbc.gridx = 3;
        form.add(txtNgayLap, gbc);

        // Trạng thái
        lblTrangThai = new JLabel("Trạng thái khách:");
        rdoThanhVien = new JRadioButton("Thành viên", true);
        rdoVangLai = new JRadioButton("Vãng lai");
        bgTrangThai = new ButtonGroup();
        bgTrangThai.add(rdoThanhVien);
        bgTrangThai.add(rdoVangLai);
        rdoThanhVien.addActionListener(this);
        rdoVangLai.addActionListener(this);
        JPanel pTrangThai = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pTrangThai.setOpaque(false);
        pTrangThai.add(rdoThanhVien);
        pTrangThai.add(rdoVangLai);
        gbc.gridy = 1;
        gbc.gridx = 0;
        form.add(lblTrangThai, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        form.add(pTrangThai, gbc);
        gbc.gridwidth = 1;

        // Khách hàng
        lblKhachHang = new JLabel("Khách hàng:");
        cbbKhachHang = new JComboBox<>();
        loadCustomersToCombo();
        btnThemKhachHang = new JButton("Thêm");
        btnThemKhachHang.addActionListener(this);
        JPanel pKhachHang = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pKhachHang.setOpaque(false);
        pKhachHang.add(cbbKhachHang);  // Thêm combobox vào panel
        pKhachHang.add(btnThemKhachHang);  // Thêm button vào panel
        gbc.gridy = 2;
        gbc.gridx = 0;
        form.add(lblKhachHang, gbc);
        gbc.gridx = 1;
        form.add(pKhachHang, gbc);

        // Khuyến mãi
        lblKhuyenMai = new JLabel("Khuyến mãi:");
        cbbKhuyenMai = new JComboBox<>();
        chkApDungKM = new JCheckBox("Áp dụng");
        loadPromotionsToCombo();
        gbc.gridy = 3;
        gbc.gridx = 0;
        form.add(lblKhuyenMai, gbc);
        gbc.gridx = 1;
        form.add(cbbKhuyenMai, gbc);
        gbc.gridx = 2;
        form.add(chkApDungKM, gbc);

        // Sản phẩm
        lblSanPham = new JLabel("Chọn quạt:");
        cbbSanPham = new JComboBox<>();
        loadProductsToCombo();
        btnThemSP = new JButton("Thêm");
        btnThemSP.addActionListener(this);
        gbc.gridy = 4;
        gbc.gridx = 0;
        form.add(lblSanPham, gbc);
        gbc.gridx = 1;
        form.add(cbbSanPham, gbc);
        gbc.gridx = 2;
        form.add(btnThemSP, gbc);

        center.add(form, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(new String[]{"Mã", "Tên", "Giá", "Thương hiệu", "Số lượng"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 4;
            }

            @Override
            public Class<?> getColumnClass(int c) {
                return (c == 2 || c == 4) ? Integer.class : String.class;
            }
        };
        tableModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                updateTotal();
            }
        });
        table = new JTable(tableModel);
        DefaultTableCellRenderer ctr = new DefaultTableCellRenderer();
        ctr.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(ctr);
        }
        table.getColumnModel().getColumn(4).setCellEditor(new SpinnerEditor());
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Danh sách sản phẩm"));
        center.add(scroll, BorderLayout.CENTER);

        // SOUTH: Tổng & các nút
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        south.setOpaque(false);
        south.add(new JLabel("Tổng:"));
        txtTong = new JTextField("0", 8);
        txtTong.setEditable(false);
        south.add(txtTong);
        btnThanhToan = new JButton("Thanh toán");
        btnThanhToan.addActionListener(this);
        south.add(btnThanhToan);
        btnXuatPDF = new JButton("Xuất PDF");
        btnXuatPDF.addActionListener(this);
        south.add(btnXuatPDF);
        btnHuy = new JButton("Xóa");
        btnHuy.addActionListener(this);
        south.add(btnHuy);
        add(south, BorderLayout.SOUTH);
    }

    // ActionListener override
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == rdoThanhVien) {
            cbbKhachHang.setEnabled(true);
        } else if (src == rdoVangLai) {
            cbbKhachHang.setEnabled(false);
        } else if (src == btnThemSP) {
            addProduct();
        } else if (src == chkApDungKM) {
            updateTotal();
        } else if (src == btnThanhToan) {
            processPayment();
        } else if (src == btnXuatPDF) {
            JOptionPane.showMessageDialog(this, "Chức năng đang phát triển.");
        } else if (src == btnHuy) {
            tableModel.setRowCount(0);
            updateTotal();
        } else if (src == btnThemKhachHang) {
            KhachHangPanel khPanel = new KhachHangPanel();  // Tạo một đối tượng KhachHangPanel
            khPanel.openThemKhachHangDialog(); // Gọi phương thức openThemKhachHangDialog
        }
    }

    // Load methods
    private void loadProductsToCombo() {
        cbbSanPham.removeAllItems();
        try (Connection c = DBConnection.getConnection(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT MaQuat,TenQuat FROM quat")) {
            while (rs.next()) {
                cbbSanPham.addItem(rs.getString(1) + " - " + rs.getString(2));
            }
        } catch (SQLException ex) {
            showError(ex);
        }
    }

    private void loadCustomersToCombo() {
        cbbKhachHang.removeAllItems();
        try (Connection c = DBConnection.getConnection(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT Sdt_KH,HoTenKH FROM khachhang WHERE TrangThai = 1")) {
            while (rs.next()) {
                cbbKhachHang.addItem(rs.getString(1) + " – " + rs.getString(2));
            }
        } catch (SQLException ex) {
            showError(ex);
        }
    }

    private void loadPromotionsToCombo() {
        cbbKhuyenMai.removeAllItems();
        for (KhuyenMaiDTO km : KhuyenMaiDAO.selectAll()) {
            cbbKhuyenMai.addItem(km.getMaSKKhuyenMai() + " – " + dieuKienMap.get(km.getLoai()) + " – " + km.getTenKhuyenMai());
        }
    }

    // Helpers
    private void addProduct() {
        String sel = (String) cbbSanPham.getSelectedItem();
        if (sel != null) {
            String code = sel.split(" - ")[0];
            try {
                QuatDTO q = new QuatDAO().findByMaQuat(code);
                if (q != null) {
                    tableModel.addRow(new Object[]{q.getMaQuat(), q.getTenQuat(), q.getGia(), q.getThuongHieu(), 1});
                }
            } catch (SQLException ex) {
                showError(ex);
            }
            updateTotal();
        }
    }

    private void updateTotal() {
        int sum = 0, qty = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int price = (int) tableModel.getValueAt(i, 2);
            int count = (int) tableModel.getValueAt(i, 4);
            sum += price * count;
            qty += count;
        }
        if (chkApDungKM.isSelected() && cbbKhuyenMai.getSelectedItem() != null) {
            String ma = ((String) cbbKhuyenMai.getSelectedItem()).split(" – ")[0];
            PromotionResult r = checkPromotion(ma, sum, qty);
            if (r.isValid) {
                sum -= sum * r.discount / 100;
            }
        }
        txtTong.setText(String.valueOf(sum));
        btnThanhToan.setEnabled(tableModel.getRowCount() > 0);
        btnXuatPDF.setEnabled(tableModel.getRowCount() > 0);
    }

//    private void processPayment() {
//        // Kiểm tra xem có sản phẩm trong giỏ hàng không
//        if (tableModel.getRowCount() == 0) {
//            JOptionPane.showMessageDialog(this, "Chưa có sản phẩm nào để thanh toán!", "Thông báo", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//
//        // Tính tổng tiền trước khi áp dụng khuyến mãi
//        int sum = 0, qty = 0;
//        for (int i = 0; i < tableModel.getRowCount(); i++) {
//            int price = (int) tableModel.getValueAt(i, 2);
//            int count = (int) tableModel.getValueAt(i, 4);
//            sum += price * count;
//            qty += count;
//        }
//
//        // Kiểm tra khuyến mãi nếu có
//        boolean khuyenMaiHopLe = true;
//        String thongBaoKM = "";
//        int totalAfterDiscount = sum;
//
//        if (chkApDungKM.isSelected() && cbbKhuyenMai.getSelectedItem() != null) {
//            String ma = ((String) cbbKhuyenMai.getSelectedItem()).split(" – ")[0];
//            PromotionResult r = checkPromotion(ma, sum, qty);
//
//            if (!r.isValid) {
//                khuyenMaiHopLe = false;
//                thongBaoKM = "Khuyến mãi không hợp lệ: " + r.msg;
//            } else {
//                totalAfterDiscount = sum - (sum * r.discount / 100);
//            }
//        }
//
//        // Nếu khuyến mãi không hợp lệ, hiển thị thông báo và không cho thanh toán
//        if (!khuyenMaiHopLe) {
//            JOptionPane.showMessageDialog(this, thongBaoKM, "Lỗi khuyến mãi", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        // Hiển thị xác nhận thanh toán
//        int choice = JOptionPane.showConfirmDialog(this,
//                "Xác nhận thanh toán: " + totalAfterDiscount + " VND?",
//                "Thanh toán", JOptionPane.OK_CANCEL_OPTION);
//
//        if (choice == JOptionPane.OK_OPTION) {
//            boolean capNhatThanhCong = true;
//
//            // Nếu là khách hàng thành viên, cập nhật tổng tiền đã mua
//            if (rdoThanhVien.isSelected() && cbbKhachHang.getSelectedItem() != null) {
//                String selectedItem = (String) cbbKhachHang.getSelectedItem();
//                String sdtKH = selectedItem.split(" – ")[0];
//
//                // Truy vấn thông tin khách hàng từ cơ sở dữ liệu
//                KhachHangDTO kh = new KhachHangBUS().findBySdt(sdtKH);
//                if (kh == null) {
//                    JOptionPane.showMessageDialog(this, "Khách hàng không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//
//                // Cập nhật tổng tiền cho khách hàng
//                int tongTienMoi = kh.getTongTienDaMua() + totalAfterDiscount;
//                kh.setTongTienDaMua(tongTienMoi);
//
//                // Cập nhật vào cơ sở dữ liệu
//                KhachHangDAO dao = new KhachHangDAO();
//                capNhatThanhCong = dao.update(kh);
//            }
//
//            // Hiển thị thông báo phù hợp
//            if (capNhatThanhCong) {
//                JOptionPane.showMessageDialog(this, "Thanh toán thành công!");
//                tableModel.setRowCount(0);
//                updateTotal();
//            } else {
//                JOptionPane.showMessageDialog(this, "Thanh toán thành công nhưng cập nhật thông tin khách hàng thất bại!",
//                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
//                tableModel.setRowCount(0);
//                updateTotal();
//            }
//        }
//    }
    private void processPayment() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            System.out.println("Row " + i + ": Mã Quạt=" + tableModel.getValueAt(i, 0) + ", Giá=" + tableModel.getValueAt(i, 2) + ", Số lượng=" + tableModel.getValueAt(i, 4));
        }
        // Kiểm tra xem có sản phẩm trong giỏ hàng không
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Chưa có sản phẩm nào để thanh toán!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Tính tổng tiền trước khi áp dụng khuyến mãi
        int sum = 0, qty = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int price = (int) tableModel.getValueAt(i, 2);
            int count = (int) tableModel.getValueAt(i, 4);
            sum += price * count;
            qty += count;
        }

        // Kiểm tra khuyến mãi nếu có
        boolean khuyenMaiHopLe = true;
        String maKM = null;
        String thongBaoKM = "";
        int totalAfterDiscount = sum;

        if (chkApDungKM.isSelected() && cbbKhuyenMai.getSelectedItem() != null) {
            maKM = ((String) cbbKhuyenMai.getSelectedItem()).split(" – ")[0];
            PromotionResult r = checkPromotion(maKM, sum, qty);

            if (!r.isValid) {
                khuyenMaiHopLe = false;
                thongBaoKM = "Khuyến mãi không hợp lệ: " + r.msg;
            } else {
                totalAfterDiscount = sum - (sum * r.discount / 100);
            }
        }

        // Nếu khuyến mãi không hợp lệ, hiển thị thông báo và không cho thanh toán
        if (!khuyenMaiHopLe) {
            JOptionPane.showMessageDialog(this, thongBaoKM, "Lỗi khuyến mãi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Hiển thị xác nhận thanh toán
        int choice = JOptionPane.showConfirmDialog(this,
                "Xác nhận thanh toán: " + totalAfterDiscount + " VND?",
                "Thanh toán", JOptionPane.OK_CANCEL_OPTION);

        if (choice == JOptionPane.OK_OPTION) {
            try {
                // Tạo mã hóa đơn mới (format: HDyyyyMMdd + số thứ tự)
                String maHD = generateNewInvoiceCode();

                // Lấy mã khách hàng từ ComboBox (nếu là thành viên)
                String maKH = "KH000"; // Default for non-member customers
                if (rdoThanhVien.isSelected() && cbbKhachHang.getSelectedItem() != null) {
                    String sdtKH = ((String) cbbKhachHang.getSelectedItem()).split(" – ")[0];
                    KhachHangDTO kh = new KhachHangBUS().findBySdt(sdtKH);
                    if (kh != null) {
                        maKH = kh.getMaKhachHang();
                    }
                }

                // Tạo đối tượng HoaDon
                java.sql.Date ngayLap = java.sql.Date.valueOf(java.time.LocalDate.now());
                HoaDonDTO hoaDon = new HoaDonDTO(maHD, maKH, maNhanVien, ngayLap, maKM, totalAfterDiscount);

                // Lưu hóa đơn vào cơ sở dữ liệu
                int resultHD = HoaDonDAO.insert(hoaDon);

                if (resultHD > 0) {
                    // Lưu chi tiết hóa đơn
                    boolean allDetailsSaved = true;
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        System.out.println("Row " + i + ": " + tableModel.getValueAt(i, 0) + ", Price: " + tableModel.getValueAt(i, 2) + ", Quantity: " + tableModel.getValueAt(i, 4));
                        // Add debug output
                        System.out.println("Row " + i + " data:");
                        for (int j = 0; j < tableModel.getColumnCount(); j++) {
                            System.out.println("Column " + j + ": " + tableModel.getValueAt(i, j));
                        }

                        String maQuat = (String) tableModel.getValueAt(i, 0);
                        int donGia = (int) tableModel.getValueAt(i, 2);
                        int soLuong = (int) tableModel.getValueAt(i, 4);
                        int thanhTien = donGia * soLuong;

                        System.out.println("Creating detail with: MaQuat=" + maQuat
                                + ", SoLuong=" + soLuong
                                + ", DonGia=" + donGia
                                + ", ThanhTien=" + thanhTien);

                        // Create warranty code
                        String maBaoHanh = "BH" + maHD + maQuat;

                        ChiTietHoaDonDTO ctHD = new ChiTietHoaDonDTO(maHD, maQuat, soLuong, donGia, thanhTien, maBaoHanh);
                        int resultCTHD = ChiTietHoaDonDAO.insert(ctHD);

                        System.out.println("Insert result: " + resultCTHD);
                    }

                    // Cập nhật tổng tiền cho khách hàng nếu là thành viên
                    if (rdoThanhVien.isSelected() && cbbKhachHang.getSelectedItem() != null) {
                        String sdtKH = ((String) cbbKhachHang.getSelectedItem()).split(" – ")[0];
                        KhachHangDTO kh = new KhachHangBUS().findBySdt(sdtKH);
                        if (kh != null) {
                            kh.setTongTienDaMua(kh.getTongTienDaMua() + totalAfterDiscount);
                            new KhachHangDAO().update(kh);
                        }
                    }

                    // Hiển thị thông báo thành công
                    if (allDetailsSaved) {
                        JOptionPane.showMessageDialog(this,
                                "Thanh toán thành công!\nMã hóa đơn: " + maHD,
                                "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Thanh toán thành công nhưng có lỗi khi lưu chi tiết hóa đơn!",
                                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    }

                    // Xóa giỏ hàng
                    tableModel.setRowCount(0);
                    updateTotal();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Thanh toán thất bại! Không thể lưu hóa đơn.",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi xảy ra khi thanh toán: " + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private String loadTenNhanVien() {
        if (maNhanVien == null) {
            return "";
        }
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement("SELECT HoTenNV FROM nhanvien WHERE MaNhanVien=?")) {
            p.setString(1, maNhanVien);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException ex) {
            showError(ex);
        }
        return maNhanVien;
    }

    // Sửa phương thức updateTongTien để nhận tổng tiền thay vì ActionEvent
    public void updateTongTien(int soTienMua) {
        // Kiểm tra xem có phải là khách hàng thành viên hay không
        if (!rdoThanhVien.isSelected()) {
            // Nếu là khách vãng lai, không cần cập nhật tổng tiền
            return;
        }

        // Lấy khách hàng từ ComboBox
        String selectedItem = (String) cbbKhachHang.getSelectedItem();
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng!");
            return;
        }

        // Lấy số điện thoại của khách hàng (Dựa vào thông tin từ ComboBox)
        String sdtKH = selectedItem.split(" – ")[0];

        // Truy vấn thông tin khách hàng từ cơ sở dữ liệu
        KhachHangDTO kh = new KhachHangBUS().findBySdt(sdtKH);
        if (kh == null) {
            JOptionPane.showMessageDialog(this, "Khách hàng không tồn tại!");
            return;
        }

        // Cập nhật tổng tiền cho khách hàng
        int tongTienMoi = kh.getTongTienDaMua() + soTienMua;
        kh.setTongTienDaMua(tongTienMoi);

        // Cập nhật vào cơ sở dữ liệu
        KhachHangDAO dao = new KhachHangDAO();
        boolean isUpdated = dao.update(kh);
        if (isUpdated) {
            JOptionPane.showMessageDialog(this, "Cập nhật tổng tiền thành công!");
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật tổng tiền thất bại!");
        }
    }

    private PromotionResult checkPromotion(String ma, int sum, int qty) {
        KhuyenMaiDTO km = KhuyenMaiDAO.selectById(ma);
        if (km == null) {
            return new PromotionResult(false, "Không tìm KM", 0);
        }
        Date now = new Date();
        if (now.before(km.getNgayBatDau()) || now.after(km.getNgayKetThuc())) {
            return new PromotionResult(false, "KM không hiệu lực", 0);
        }
        switch (km.getLoai()) {
            case 1:
                if (sum < 500000) {
                    return new PromotionResult(false, "Đơn <500k", 0);
                }
                break;
            case 2:
                if (qty < 2) {
                    return new PromotionResult(false, "Số lượng <2", 0);
                }
                break;
        }
        return new PromotionResult(true, "OK", km.getPhanTramGiam());
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    // Inner helper classes
    private static class PromotionResult {

        boolean isValid;
        String msg;
        int discount;

        PromotionResult(boolean v, String m, int d) {
            isValid = v;
            msg = m;
            discount = d;
        }
    }

    private String generateNewInvoiceCode() {
        // Lấy ngày hiện tại
        LocalDate currentDate = LocalDate.now();
        String dateStr = currentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "HD" + dateStr;

        // Tìm mã hóa đơn lớn nhất trong ngày
        int maxNumber = 0;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement pst = conn.prepareStatement(
                "SELECT MAX(MaHoaDon) FROM hoadon WHERE MaHoaDon LIKE ?")) {

            pst.setString(1, prefix + "%");
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next() && rs.getString(1) != null) {
                    String maxCode = rs.getString(1);
                    // Extract the numeric part
                    try {
                        maxNumber = Integer.parseInt(maxCode.substring(prefix.length()));
                    } catch (NumberFormatException e) {
                        // Default to 0 if parsing fails
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Tăng số thứ tự lên 1 và định dạng với 3 chữ số
        return prefix + String.format("%03d", maxNumber + 1);
    }

    private static class SpinnerEditor extends AbstractCellEditor implements TableCellEditor {

        private final JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));

        @Override
        public Component getTableCellEditorComponent(JTable table, Object val, boolean sel, int r, int c) {
            spinner.setValue(val);
            return spinner;
        }

        @Override
        public Object getCellEditorValue() {
            return spinner.getValue();
        }
    }
}
