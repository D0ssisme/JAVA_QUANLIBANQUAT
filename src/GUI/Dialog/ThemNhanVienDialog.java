// src/GUI/Dialog/ThemNhanVienDialog.java
package GUI.Dialog;

import BUS.NhanVienBUS;
import DTO.NhanVienDTO;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;
import DTO.DBConnection; // Đổi từ import dto.DBConnection thành đúng package

public class ThemNhanVienDialog extends JDialog {
    private final JTextField txtMa, txtTen, txtSdt, txtDiaChi;
    private final JComboBox<String> cbbChucVu;
    private final JButton btnLuu, btnHuy;
    private final NhanVienBUS bll = new NhanVienBUS();
    private boolean saved = false;

    public ThemNhanVienDialog(Window owner) {
        super(owner, "Thêm nhân viên", ModalityType.APPLICATION_MODAL);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        // Generate next employee ID
        String nextMaNV = generateNextMaNV();

        gbc.gridx=0; gbc.gridy=0;
        add(new JLabel("Mã NV:"), gbc);
        gbc.gridx=1;
        txtMa = new JTextField(nextMaNV, 15);
        txtMa.setEditable(false); // Ko cho chỉnh sửa
        txtMa.setBackground(new Color(240, 240, 240)); // Nền xám
        add(txtMa, gbc);

        gbc.gridy=1; gbc.gridx=0;
        add(new JLabel("Họ tên:"), gbc);
        gbc.gridx=1;
        txtTen = new JTextField(15);
        add(txtTen, gbc);

        gbc.gridy=2; gbc.gridx=0;
        add(new JLabel("Chức vụ:"), gbc);
        gbc.gridx=1;
        cbbChucVu = new JComboBox<>(new String[]{"Nhân viên", "Quản lý", "Bảo vệ"});
        add(cbbChucVu, gbc);

        gbc.gridy=3; gbc.gridx=0;
        add(new JLabel("SĐT:"), gbc);
        gbc.gridx=1;
        txtSdt = new JTextField(15);
        add(txtSdt, gbc);

        gbc.gridy=4; gbc.gridx=0;
        add(new JLabel("Địa chỉ:"), gbc);
        gbc.gridx=1;
        txtDiaChi = new JTextField(15);
        add(txtDiaChi, gbc);

        gbc.gridy=5; gbc.gridx=0; gbc.gridwidth=2; gbc.anchor = GridBagConstraints.CENTER;
        JPanel pnl = new JPanel();
        btnLuu = new JButton("Lưu");
        btnHuy = new JButton("Hủy");
        pnl.add(btnLuu);
        pnl.add(btnHuy);
        add(pnl, gbc);

        pack();
        setLocationRelativeTo(owner);
        
        // Focus vào trường Họ tên sau khi hiển thị
        SwingUtilities.invokeLater(() -> txtTen.requestFocus());

        btnHuy.addActionListener(e -> dispose());
        btnLuu.addActionListener(e -> {
            if (validateInput()) {
                NhanVienDTO nv = new NhanVienDTO(
                    txtMa.getText().trim(),
                    txtTen.getText().trim(),
                    cbbChucVu.getSelectedItem().toString(),
                    txtSdt.getText().trim(),
                    txtDiaChi.getText().trim()
                );
                if (bll.them(nv)) {
                    saved = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại!");
                }
            }
        });
    }
    
    // Các phương thức khác giữ nguyên
    private boolean validateInput() {
        // Kiểm tra tên nhân viên
        if (txtTen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ tên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtTen.requestFocus();
            return false;
        }
        
        // Kiểm tra số điện thoại
        String sdt = txtSdt.getText().trim();
        if (sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSdt.requestFocus();
            return false;
        }
        
        // Kiểm tra định dạng số điện thoại (10 số, bắt đầu bằng 0)
        Pattern pattern = Pattern.compile("^0\\d{9}$");
        if (!pattern.matcher(sdt).matches()) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải có 10 số và bắt đầu bằng số 0!", 
                                         "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSdt.requestFocus();
            return false;
        }
        
        // Kiểm tra địa chỉ
        if (txtDiaChi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Địa chỉ không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtDiaChi.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private String generateNextMaNV() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT MaNhanVien FROM nhanvien ORDER BY MaNhanVien DESC")) {
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String lastID = rs.getString("MaNhanVien");
                    if (lastID.startsWith("NV")) {
                        int number = Integer.parseInt(lastID.substring(2));
                        return String.format("NV%03d", number + 1);
                    }
                }
            }
            // Nếu không có NhanVien nào hoặc có lỗi, trả về mã mặc định
            return "NV001";
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "NV001"; // Mã mặc định nếu có lỗi
        }
    }

    public boolean isSaved() { return saved; }
}