package GUI.Dialog;

import BUS.LoaiSanPhamBUS;

import javax.swing.*;
import java.awt.*;

public class ThemLoaiSanPhamDialog extends JDialog {

    private JTextField txtMaLoai, txtTenLoai, txtTrangThai, txtMoTa;
    private JButton btnLuu, btnHuy;
    private boolean added = false;

    private LoaiSanPhamBUS loaiSanPhamBLL;

    public ThemLoaiSanPhamDialog(Window owner) {
        super(owner, "Thêm Loại Sản Phẩm", Dialog.ModalityType.APPLICATION_MODAL);
        loaiSanPhamBLL = new LoaiSanPhamBUS();
        initComponent();
    }

    private void initComponent() {
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(10, 10));

        JPanel pnlForm = new JPanel(new GridLayout(4, 2, 5, 5));

        pnlForm.add(new JLabel("Mã loại:"));
        txtMaLoai = new JTextField();
        pnlForm.add(txtMaLoai);

        pnlForm.add(new JLabel("Tên loại:"));
        txtTenLoai = new JTextField();
        pnlForm.add(txtTenLoai);

        pnlForm.add(new JLabel("Trạng thái:"));
        txtTrangThai = new JTextField();
        pnlForm.add(txtTrangThai);

        pnlForm.add(new JLabel("Mô tả:"));
        txtMoTa = new JTextField();
        pnlForm.add(txtMoTa);

        this.add(pnlForm, BorderLayout.CENTER);

        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnLuu = new JButton("Lưu");
        btnHuy = new JButton("Hủy");
        pnlButton.add(btnLuu);
        pnlButton.add(btnHuy);

        this.add(pnlButton, BorderLayout.SOUTH);

        btnLuu.addActionListener(e -> {
            String ma = txtMaLoai.getText().trim();
            String ten = txtTenLoai.getText().trim();
            String trangThai = txtTrangThai.getText().trim();
            String moTa = txtMoTa.getText().trim();

            if (ma.isEmpty() || ten.isEmpty() || trangThai.isEmpty() || moTa.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.");
                return;
            }

            if (loaiSanPhamBLL.them(ma, ten, trangThai, moTa)) {
                JOptionPane.showMessageDialog(this, "Thêm loại sản phẩm thành công.");
                added = true;
                dispose();
            } else {
                // Thông báo lỗi đã được xử lý trong BLL
            }
        });

        btnHuy.addActionListener(e -> dispose());
    }

    public boolean isAdded() {
        return added;
    }
}
