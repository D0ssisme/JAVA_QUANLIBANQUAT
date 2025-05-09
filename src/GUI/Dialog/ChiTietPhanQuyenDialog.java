package GUI.Dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChiTietPhanQuyenDialog extends JDialog implements ActionListener {
    private String maTK;   // Mã tài khoản
    private String tenTK;  // Tên tài khoản

    // Các checkbox (giống ảnh)
    private JCheckBox chkDuyetPhieuNhap, chkQuanLyDanhMuc, chkQuanLyKhuyenMai,
            chkQuanLyPhieuNhap, chkQuanLySanPham, chkQuanLyDonHang,
            chkQuanLyHang, chkQuanLyNguoiDung, chkQuanLyPhanQuyen, chkXemThongKe;

    private JButton btnThucHien;

    public ChiTietPhanQuyenDialog(Window owner, String maTK, String tenTK) {
        super(owner, "Phân quyền cho: " + maTK, ModalityType.APPLICATION_MODAL);
        this.maTK = maTK;
        this.tenTK = tenTK;
        initComponent();
    }

    private void initComponent() {
        setSize(500, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Tiêu đề
        JLabel lblTitle = new JLabel("QUẢN LÝ PHÂN QUYỀN - " + tenTK, SwingConstants.CENTER);
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 16f));
        add(lblTitle, BorderLayout.NORTH);

        // Panel checkbox
        JPanel pnlCheck = new JPanel(new GridLayout(5, 2, 10, 10));
        pnlCheck.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        chkDuyetPhieuNhap = new JCheckBox("Được duyệt phiếu nhập");
        chkQuanLyDanhMuc = new JCheckBox("Quản lý danh mục");
        chkQuanLyKhuyenMai = new JCheckBox("Quản lý khuyến mãi");
        chkQuanLyPhieuNhap = new JCheckBox("Quản lý phiếu nhập");
        chkQuanLySanPham = new JCheckBox("Quản lý sản phẩm");
        chkQuanLyDonHang = new JCheckBox("Quản lý đơn hàng");
        chkQuanLyHang = new JCheckBox("Quản lý hãng");
        chkQuanLyNguoiDung = new JCheckBox("Quản lý người dùng");
        chkQuanLyPhanQuyen = new JCheckBox("Quản lý phân quyền");
        chkXemThongKe = new JCheckBox("Được xem trang thống kê");

        pnlCheck.add(chkDuyetPhieuNhap);
        pnlCheck.add(chkQuanLyDanhMuc);
        pnlCheck.add(chkQuanLyKhuyenMai);
        pnlCheck.add(chkQuanLyPhieuNhap);
        pnlCheck.add(chkQuanLySanPham);
        pnlCheck.add(chkQuanLyDonHang);
        pnlCheck.add(chkQuanLyHang);
        pnlCheck.add(chkQuanLyNguoiDung);
        pnlCheck.add(chkQuanLyPhanQuyen);
        pnlCheck.add(chkXemThongKe);

        add(pnlCheck, BorderLayout.CENTER);

        // Nút "Thực hiện phân quyền"
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnThucHien = new JButton("Thực hiện phân quyền");
        btnThucHien.addActionListener(this);
        pnlBottom.add(btnThucHien);
        add(pnlBottom, BorderLayout.SOUTH);

        // Giả sử bạn muốn load data cũ (nếu có) => setSelected(true/false)...
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnThucHien) {
            // Lấy trạng thái checkbox => Lưu cài đặt
            // Demo: hiển thị message
            JOptionPane.showMessageDialog(this,
                "Đã phân quyền cho tài khoản: " + maTK + "!\n(Đây chỉ là demo GUI).");
            dispose();
        }
    }
}
