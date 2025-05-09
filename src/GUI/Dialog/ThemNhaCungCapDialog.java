package GUI.Dialog;

import BUS.NhaCungCapBUS;
import DTO.NhaCungCapDTO;

import javax.swing.*;
import java.awt.*;

public class ThemNhaCungCapDialog extends JDialog {

    private JTextField txtMaNCC, txtTenNCC, txtDiaChi, txtSDT;
    private JButton btnLuu, btnHuy;
    private boolean added = false;

    private NhaCungCapBUS nhaCungCapBLL;

    public ThemNhaCungCapDialog(Window owner) {
        super(owner, "Thêm Nhà Cung Cấp", Dialog.ModalityType.APPLICATION_MODAL);
        nhaCungCapBLL = new NhaCungCapBUS();
        initComponent();
    }

    private void initComponent() {
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(10, 10));

        JPanel pnlForm = new JPanel(new GridLayout(4, 2, 5, 5));

        pnlForm.add(new JLabel("Mã NCC:"));
        txtMaNCC = new JTextField();
        pnlForm.add(txtMaNCC);

        pnlForm.add(new JLabel("Tên NCC:"));
        txtTenNCC = new JTextField();
        pnlForm.add(txtTenNCC);

        pnlForm.add(new JLabel("Địa chỉ:"));
        txtDiaChi = new JTextField();
        pnlForm.add(txtDiaChi);

        pnlForm.add(new JLabel("Số điện thoại:"));
        txtSDT = new JTextField();
        pnlForm.add(txtSDT);

        this.add(pnlForm, BorderLayout.CENTER);

        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnLuu = new JButton("Lưu");
        btnHuy = new JButton("Hủy");
        pnlButton.add(btnLuu);
        pnlButton.add(btnHuy);

        this.add(pnlButton, BorderLayout.SOUTH);

        btnLuu.addActionListener(e -> {
            String ma = txtMaNCC.getText().trim();
            String ten = txtTenNCC.getText().trim();
            String diaChi = txtDiaChi.getText().trim();
            String sdt = txtSDT.getText().trim();

            if (ma.isEmpty() || ten.isEmpty() || diaChi.isEmpty() || sdt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.");
                return;
            }

            if (nhaCungCapBLL.them(ma, ten, diaChi, sdt)) {
                JOptionPane.showMessageDialog(this, "Thêm nhà cung cấp thành công.");
                added = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại.");
            }
        });

        btnHuy.addActionListener(e -> dispose());
    }

    public boolean isAdded() {
        return added;
    }
}
