package GUI.Dialog;

import DTO.LoaiSanPhamDTO;
import BUS.LoaiSanPhamBUS;

import javax.swing.*;
import java.awt.*;

public class SuaLoaiSanPhamDialog extends JDialog {
    private JTextField txtMaLoaiSP, txtTenLoai, txtTrangThai, txtMoTa;
    private LoaiSanPhamDTO loaiSanPhamCu;
    private boolean isUpdated = false;

    public boolean isUpdated() {
        return isUpdated;
    }

    public SuaLoaiSanPhamDialog(Window owner, LoaiSanPhamDTO loaiSanPham) {
        super(owner, "Sửa Loại Sản Phẩm", ModalityType.APPLICATION_MODAL);
        this.loaiSanPhamCu = loaiSanPham;
        initComponent();
        loadDataToFields();
    }

    private void initComponent() {
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel pnlForm = new JPanel(new GridLayout(4, 2, 5, 5));
        pnlForm.add(new JLabel("Mã loại sản phẩm:"));
        txtMaLoaiSP = new JTextField();
        pnlForm.add(txtMaLoaiSP);

        pnlForm.add(new JLabel("Tên loại:"));
        txtTenLoai = new JTextField();
        pnlForm.add(txtTenLoai);

        pnlForm.add(new JLabel("Trạng thái:"));
        txtTrangThai = new JTextField();
        pnlForm.add(txtTrangThai);

        pnlForm.add(new JLabel("Mô tả:"));
        txtMoTa = new JTextField();
        pnlForm.add(txtMoTa);

        add(pnlForm, BorderLayout.CENTER);

        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLuu = new JButton("Lưu");
        JButton btnHuy = new JButton("Hủy");
        pnlButton.add(btnLuu);
        pnlButton.add(btnHuy);

        add(pnlButton, BorderLayout.SOUTH);

        // Nút Lưu
        btnLuu.addActionListener(e -> {
            if (saveChanges()) {
                JOptionPane.showMessageDialog(this, "Loại sản phẩm đã được sửa.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi sửa loại sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Nút Hủy
        btnHuy.addActionListener(e -> dispose());
    }

    private void loadDataToFields() {
        txtMaLoaiSP.setText(loaiSanPhamCu.getMaLoaiSanPham());
        txtTenLoai.setText(loaiSanPhamCu.getTenLoai());
        txtTrangThai.setText(loaiSanPhamCu.getTrangThai());
        txtMoTa.setText(loaiSanPhamCu.getMoTa());

        // Không cho sửa mã loại sản phẩm
        txtMaLoaiSP.setEditable(false);
    }

    private boolean saveChanges() {
        try {
            String maLoai = txtMaLoaiSP.getText();
            String tenLoai = txtTenLoai.getText();
            String trangThai = txtTrangThai.getText();
            String moTa = txtMoTa.getText();

            LoaiSanPhamBUS loaiSanPhamBLL = new LoaiSanPhamBUS();
            boolean result = loaiSanPhamBLL.sua(maLoai, tenLoai, trangThai, moTa);

            if (result) {
                isUpdated = true;
            }

            return result;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xử lý dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
