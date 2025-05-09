package GUI.Dialog;

import BUS.NhaCungCapBUS;
import DTO.NhaCungCapDTO;

import javax.swing.*;
import java.awt.*;

public class SuaNhaCungCapDialog extends JDialog {
    private JTextField txtMaNCC, txtTenNCC, txtDiaChi, txtSDT;
    private NhaCungCapDTO nhaCungCapCu;
    private boolean isUpdated = false;

    public boolean isUpdated() {
        return isUpdated;
    }

    public SuaNhaCungCapDialog(Window owner, NhaCungCapDTO nhaCungCap) {
        super(owner, "Sửa Nhà Cung Cấp", ModalityType.APPLICATION_MODAL);
        this.nhaCungCapCu = nhaCungCap;
        initComponent();
        loadDataToFields();
    }

    private void initComponent() {
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

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
                JOptionPane.showMessageDialog(this, "Nhà cung cấp đã được sửa.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi sửa nhà cung cấp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Nút Hủy
        btnHuy.addActionListener(e -> dispose());
    }

    private void loadDataToFields() {
        // Hiển thị thông tin nhà cung cấp cũ lên các trường nhập liệu
        txtMaNCC.setText(nhaCungCapCu.getMaNCC());
        txtTenNCC.setText(nhaCungCapCu.getTenNCC());
        txtDiaChi.setText(nhaCungCapCu.getDiaChiNCC());
        txtSDT.setText(nhaCungCapCu.getSdtNCC());

        // Không cho phép sửa Mã NCC
        txtMaNCC.setEditable(false);
    }

    private boolean saveChanges() {
        try {
            // Lấy dữ liệu từ các trường nhập liệu
            String maNCC = txtMaNCC.getText();
            String tenNCC = txtTenNCC.getText();
            String diaChi = txtDiaChi.getText();
            String sdt = txtSDT.getText();
            
            NhaCungCapBUS nhaCungCapBLL = new NhaCungCapBUS();
            boolean result = nhaCungCapBLL.sua(maNCC, tenNCC, diaChi, sdt);

            if (result) {
                isUpdated = true;
            }

            return result;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu thông tin nhà cung cấp.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
