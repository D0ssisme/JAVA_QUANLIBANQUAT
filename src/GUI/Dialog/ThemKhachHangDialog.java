package GUI.Dialog;

import DTO.KhachHangDTO;
import DAO.KhachHangDAO;
import javax.swing.*;
import java.awt.*;

public class ThemKhachHangDialog extends JDialog {
    private JTextField txtMa, txtTen, txtSdt, txtDiaChi;
    private JButton btnLuu, btnHuy;
    private boolean saved = false;

    public ThemKhachHangDialog() {
        setTitle("Thêm khách hàng");
        setModal(true);
        setSize(350, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        txtMa = new JTextField();
        txtTen = new JTextField();
        txtSdt = new JTextField();
        txtDiaChi = new JTextField();

        btnLuu = new JButton("Lưu");
        btnHuy = new JButton("Hủy");

        add(new JLabel("Mã KH:"));
        add(txtMa);
        add(new JLabel("Tên KH:"));
        add(txtTen);
        add(new JLabel("SĐT:"));
        add(txtSdt);
        add(new JLabel("Địa chỉ:"));
        add(txtDiaChi);
        add(btnLuu);
        add(btnHuy);

        btnLuu.addActionListener(e -> {
            KhachHangDTO kh = new KhachHangDTO(txtMa.getText(), txtTen.getText(), txtSdt.getText(), txtDiaChi.getText(), 0,1);
            if (new KhachHangDAO().insert(kh)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                saved = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!");
            }
        });

        btnHuy.addActionListener(e -> dispose());
    }

    public boolean isSaved() {
        return saved;
    }
}
