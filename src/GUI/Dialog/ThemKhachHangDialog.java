package GUI.Dialog;

import DTO.KhachHangDTO;
import BUS.KhachHangBUS;
import DAO.KhachHangDAO;
import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

public class ThemKhachHangDialog extends JDialog {
    private KhachHangBUS kh_bus = new KhachHangBUS();
    private KhachHangDAO kh_dao = new KhachHangDAO();
    private JTextField txtMa, txtTen, txtSdt, txtDiaChi;
    private JButton btnLuu, btnHuy;
    private boolean saved = false;
    
    public ThemKhachHangDialog() {
        this(null);
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

        if (maKh != null && !maKh.isEmpty()) {
            txtMa.setText(maKh);
            txtMa.setEditable(false);
        }

        add(new JLabel("Mã KH:")); add(txtMa);
        add(new JLabel("Tên KH:")); add(txtTen);
        add(new JLabel("SĐT:")); add(txtSdt);
        add(new JLabel("Địa chỉ:")); add(txtDiaChi);
        add(btnLuu); add(btnHuy);

        btnLuu.addActionListener(e -> {
            String maNhap = txtMa.getText().trim();
            String tenNhap = txtTen.getText().trim();
            String sdtNhap = txtSdt.getText().trim();
            String diaChiNhap = txtDiaChi.getText().trim();
            
            // Kiểm tra mã khách hàng
            if (!kh_bus.isValidMaKhachHang(maNhap)) {
                JOptionPane.showMessageDialog(this,
                    "Mã khách phải bắt đầu bằng 'KH' và theo sau là số!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Kiểm tra trùng mã
            if (kh_dao.exists(maNhap)) {
                JOptionPane.showMessageDialog(this,
                    "Mã khách hàng đã tồn tại!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Kiểm tra tên
            if (tenNhap.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập tên khách hàng!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Kiểm tra SĐT
            if (sdtNhap.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Số điện thoại không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtSdt.requestFocus();
                return;
            }
            
            // Kiểm tra định dạng số điện thoại (10 số, bắt đầu bằng 0)
            Pattern pattern = Pattern.compile("^0\\d{9}$");
            if (!pattern.matcher(sdtNhap).matches()) {
                JOptionPane.showMessageDialog(this, 
                    "Số điện thoại phải có 10 số và bắt đầu bằng số 0!", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtSdt.requestFocus();
                return;
            }
            
            // Tạo DTO và lưu
            KhachHangDTO kh = new KhachHangDTO(maNhap, tenNhap, sdtNhap, diaChiNhap, 0, 1);
            if (kh_dao.insert(kh)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                saved = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnHuy.addActionListener(e -> dispose());
    }

    public boolean isSaved() {
        return saved;
    }
}