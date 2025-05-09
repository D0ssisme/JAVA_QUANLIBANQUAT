package GUI.Dialog;

import DAO.QuatDAO;
import DTO.QuatDTO;
import BUS.QuatBUS;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class ThemQuatDialog extends JDialog {

    private JTextField txtMaQuat, txtTenQuat, txtGia, txtMaNSX, txtNgaySanXuat, txtChatLieu, txtThuongHieu, txtMaLoaiSP, txtSoLuongTon;
    private JButton btnLuu, btnHuy;
    private boolean added = false;

    private QuatBUS quatBLL;

    public ThemQuatDialog(Window owner) {
        super(owner, "Thêm Quạt", Dialog.ModalityType.APPLICATION_MODAL);
        quatBLL = new QuatBUS(); // Khởi tạo QuatBUS
        initComponent();
    }

    private void initComponent() {
        this.setSize(500, 450); // Điều chỉnh kích thước để chứa thêm trường
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(10, 10));

        JPanel pnlForm = new JPanel(new GridLayout(10, 2, 5, 5)); // Tăng số dòng lên 10
        pnlForm.add(new JLabel("Mã quạt:"));
        txtMaQuat = new JTextField();
        pnlForm.add(txtMaQuat);

        pnlForm.add(new JLabel("Tên quạt:"));
        txtTenQuat = new JTextField();
        pnlForm.add(txtTenQuat);

        pnlForm.add(new JLabel("Giá:"));
        txtGia = new JTextField();
        pnlForm.add(txtGia);
        
        pnlForm.add(new JLabel("Số lượng tồn:"));
        txtSoLuongTon = new JTextField();
        pnlForm.add(txtSoLuongTon);

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

        pnlForm.add(new JLabel("Mã loại sản phẩm:"));
        txtMaLoaiSP = new JTextField();
        pnlForm.add(txtMaLoaiSP);

        pnlForm.add(new JLabel("Số lượng tồn:"));
        txtSoLuongTon = new JTextField();
        pnlForm.add(txtSoLuongTon);

        this.add(pnlForm, BorderLayout.CENTER);

        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnLuu = new JButton("Lưu");
        btnHuy = new JButton("Hủy");
        pnlButton.add(btnLuu);
        pnlButton.add(btnHuy);

        this.add(pnlButton, BorderLayout.SOUTH);

        // Nút Lưu
        btnLuu.addActionListener(e -> {
            String maQuat = txtMaQuat.getText();
            String tenQuat = txtTenQuat.getText();
            int gia = Integer.parseInt(txtGia.getText());
            String maNSX = txtMaNSX.getText();
            String ngaySanXuat = txtNgaySanXuat.getText();
            String chatLieu = txtChatLieu.getText();
            String thuongHieu = txtThuongHieu.getText();
            String maLoaiSP = txtMaLoaiSP.getText();
            int soLuongTon = Integer.parseInt(txtSoLuongTon.getText());  // Lấy giá trị số lượng tồn

            // Gọi hàm them trong QuatBUS
            if (quatBLL.them(maQuat, tenQuat, gia,soLuongTon, maNSX, ngaySanXuat, chatLieu, thuongHieu, maLoaiSP)) {
                JOptionPane.showMessageDialog(this, "Đã lưu quạt.");
                added = true;  // Đánh dấu là quạt đã được thêm
                dispose();  // Đóng dialog
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi lưu quạt.");
            }
        });

        // Nút Hủy
        btnHuy.addActionListener(e -> {
            dispose(); // Đóng dialog mà không lưu
        });
    }

    public boolean isAdded() {
        return added;
    }
}
