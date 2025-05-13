package GUI.Dialog;

import DTO.KhachHangDTO;
import BUS.KhachHangBUS;
import DAO.KhachHangDAO;
import javax.swing.*;
import java.awt.*;

public class ThemKhachHangDialog extends JDialog {
    private KhachHangBUS kh_bus = new KhachHangBUS();
    private JTextField txtMa, txtTen, txtSdt, txtDiaChi;
    private JButton btnLuu, btnHuy;
    private boolean saved = false;
    
    // Thêm constructor không tham số
    public ThemKhachHangDialog() {
        this(null); // Gọi constructor có tham số với giá trị null
    }

    public ThemKhachHangDialog(String maKh) {
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

        // Nếu maKh được truyền vào, sử dụng nó để điền trước vào trường txtMa
        if (maKh != null && !maKh.isEmpty()) {
            txtMa.setText(maKh);
        }

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
            String maNhap = txtMa.getText().trim();
            
            // Kiểm tra tính hợp lệ của mã khách hàng
            if (!kh_bus.isValidMaKhachHang(maNhap)) {
                JOptionPane.showMessageDialog(this,
                        "Mã khách phải bắt đầu bằng 'KH' và theo sau là số!");
                return;
            }
            
            // Kiểm tra các trường dữ liệu khác
            if (txtTen.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khách hàng!");
                return;
            }
            
            if (txtSdt.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại!");
                return;
            }
            
            // Tạo đối tượng KhachHangDTO và thêm vào CSDL
            KhachHangDTO kh = new KhachHangDTO(maNhap, txtTen.getText().trim(), 
                                              txtSdt.getText().trim(), txtDiaChi.getText().trim(), 0, 1);
            
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