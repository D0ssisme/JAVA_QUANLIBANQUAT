package GUI.Dialog;

import DTO.KhachHangDTO;
import DAO.KhachHangDAO;
import javax.swing.*;
import java.awt.*;

public class SuaKhachHangDialog extends JDialog {
    private JTextField txtMa, txtTen, txtSdt, txtDiaChi;
    private JButton btnLuu, btnHuy;
    private boolean saved = false;

    public SuaKhachHangDialog(JFrame parent, KhachHangDTO kh) {
        super(parent, "Sửa khách hàng", true);
        setSize(350, 300);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(5, 2, 10, 10));

        txtMa = new JTextField(kh.getMaKhachHang());
        txtMa.setEditable(false);
        txtTen = new JTextField(kh.getHoTenKH());
        txtSdt = new JTextField(kh.getSdtKH());
        txtDiaChi = new JTextField(kh.getDiaChiKH());

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
            kh.setHoTenKH(txtTen.getText());
            kh.setSdtKH(txtSdt.getText());
            kh.setDiaChiKH(txtDiaChi.getText());

            if (new KhachHangDAO().update(kh)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                saved = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        });

        btnHuy.addActionListener(e -> dispose());
    }

    public boolean isSaved() {
        return saved;
    }
}
