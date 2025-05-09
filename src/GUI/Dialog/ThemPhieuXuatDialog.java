package GUI.Dialog;

import javax.swing.*;
import java.awt.*;

public class ThemPhieuXuatDialog extends JDialog {
    private JTextField txtMaPX, txtKhachHang, txtNhanVien, txtNgayXuat, txtTongTien;
    private JButton btnLuu, btnHuy;

    public ThemPhieuXuatDialog(Window owner) {
        super(owner, "Thêm Phiếu Xuất", ModalityType.APPLICATION_MODAL);
        initComponent();
    }

    private void initComponent() {
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel pnlForm = new JPanel(new GridLayout(5, 2, 5, 5));
        pnlForm.add(new JLabel("Mã phiếu xuất:"));
        txtMaPX = new JTextField();
        pnlForm.add(txtMaPX);

        pnlForm.add(new JLabel("Khách hàng:"));
        txtKhachHang = new JTextField();
        pnlForm.add(txtKhachHang);

        pnlForm.add(new JLabel("Nhân viên xuất:"));
        txtNhanVien = new JTextField();
        pnlForm.add(txtNhanVien);

        pnlForm.add(new JLabel("Ngày xuất:"));
        txtNgayXuat = new JTextField();
        pnlForm.add(txtNgayXuat);

        pnlForm.add(new JLabel("Tổng tiền:"));
        txtTongTien = new JTextField();
        pnlForm.add(txtTongTien);

        add(pnlForm, BorderLayout.CENTER);

        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnLuu = new JButton("Lưu");
        btnHuy = new JButton("Hủy");
        pnlButton.add(btnLuu);
        pnlButton.add(btnHuy);

        add(pnlButton, BorderLayout.SOUTH);

        btnLuu.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Đã lưu phiếu xuất (demo).");
            dispose();
        });

        btnHuy.addActionListener(e -> {
            dispose();
        });
    }
}
