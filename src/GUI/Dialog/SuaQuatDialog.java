package GUI.Dialog;

import DTO.QuatDTO;
import BUS.QuatBUS;

import javax.swing.*;
import java.awt.*;

public class SuaQuatDialog extends JDialog {
    private JTextField txtMaQuat, txtTenQuat, txtGia, txtsoLuongTon, txtMaNSX, txtNgaySanXuat, txtChatLieu, txtThuongHieu, txtMaLoaiSP;
    private QuatDTO quatCu;
    private boolean isUpdated = false;

    public boolean isUpdated() {
        return isUpdated;
    }

    public SuaQuatDialog(Window owner, QuatDTO quat) {
        super(owner, "Sửa Quạt", ModalityType.APPLICATION_MODAL);
        this.quatCu = quat;
        initComponent();
        loadDataToFields();
    }

    private void initComponent() {
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel pnlForm = new JPanel(new GridLayout(9, 2, 5, 5));  // Sửa lại thành 9 dòng
        pnlForm.add(new JLabel("Mã quạt:"));
        txtMaQuat = new JTextField();
        pnlForm.add(txtMaQuat);

        pnlForm.add(new JLabel("Tên quạt:"));
        txtTenQuat = new JTextField();
        pnlForm.add(txtTenQuat);

        pnlForm.add(new JLabel("Giá:"));
        txtGia = new JTextField();
        pnlForm.add(txtGia);
        
        pnlForm.add(new JLabel("Số Lượng Tồn:"));
        txtsoLuongTon = new JTextField();  // Đảm bảo đúng tên
        pnlForm.add(txtsoLuongTon);

        pnlForm.add(new JLabel("Mã NSX:"));
        txtMaNSX = new JTextField();
        pnlForm.add(txtMaNSX);

        pnlForm.add(new JLabel("Ngày sản xuất:"));
        txtNgaySanXuat = new JTextField();
        pnlForm.add(txtNgaySanXuat);

        pnlForm.add(new JLabel("Chất liệu:"));
        txtChatLieu = new JTextField();
        pnlForm.add(txtChatLieu);

        pnlForm.add(new JLabel("Thương hiệu:"));
        txtThuongHieu = new JTextField();
        pnlForm.add(txtThuongHieu);

        pnlForm.add(new JLabel("Mã loại SP:"));
        txtMaLoaiSP = new JTextField();
        pnlForm.add(txtMaLoaiSP);

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
                JOptionPane.showMessageDialog(this, "Quạt đã được sửa.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi sửa quạt!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Nút Hủy
        btnHuy.addActionListener(e -> dispose());
    }

    private void loadDataToFields() {
        // Hiển thị thông tin quạt cũ lên các trường nhập liệu
        txtMaQuat.setText(quatCu.getMaQuat());
        txtTenQuat.setText(quatCu.getTenQuat());
        txtGia.setText(String.valueOf(quatCu.getGia()));
        txtsoLuongTon.setText(String.valueOf(quatCu.getSoLuongTon()));  // Sửa lại giá trị này
        txtMaNSX.setText(quatCu.getMaNSX());
        txtNgaySanXuat.setText(quatCu.getNgaySanXuat());
        txtChatLieu.setText(quatCu.getChatLieu());
        txtThuongHieu.setText(quatCu.getThuongHieu());
        txtMaLoaiSP.setText(quatCu.getMaLoaiSP());

        // Không cho phép sửa Mã quạt
        txtMaQuat.setEditable(false);
    }

   private boolean saveChanges() {
        try {
            // Lấy dữ liệu từ các trường nhập liệu
            String maQuat = txtMaQuat.getText();
            String tenQuat = txtTenQuat.getText();
            int gia = Integer.parseInt(txtGia.getText());
            int soLuongTon = Integer.parseInt(txtsoLuongTon.getText());  // Lấy số lượng tồn
            String maNSX = txtMaNSX.getText();
            String ngaySanXuat = txtNgaySanXuat.getText();
            String chatLieu = txtChatLieu.getText();
            String thuongHieu = txtThuongHieu.getText();
            String maLoaiSP = txtMaLoaiSP.getText();

            QuatBUS quatBLL = new QuatBUS();
            boolean result = quatBLL.sua(maQuat, tenQuat, gia, soLuongTon, maNSX, ngaySanXuat, chatLieu, thuongHieu, maLoaiSP);

            if (result) {
                isUpdated = true;
            }

            return result;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
