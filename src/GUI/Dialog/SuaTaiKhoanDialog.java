package GUI.Dialog;

import BUS.TaiKhoanBUS;
import BUS.NhanVienBUS;
import DTO.TaiKhoanDTO;
import DTO.NhanVienDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

public class SuaTaiKhoanDialog extends JDialog {
    private final JComboBox<String> cbbNhanVien;
    private final JTextField txtUsername, txtPassword;
    private final JComboBox<String> cbbQuyenHang;
    private final JButton btnLuu, btnHuy;
    private boolean saved = false;
    private String editingMaTK;
    private final NhanVienBUS nvBUS = new NhanVienBUS();
    private String oldRole; // Para guardar el rol original

    public SuaTaiKhoanDialog(Window owner) {
        super(owner, "Chỉnh sửa tài khoản", ModalityType.APPLICATION_MODAL);

        JPanel pnl = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        // Mã NV
        gbc.gridx = 0; gbc.gridy = 0;
        pnl.add(new JLabel("Nhân viên:"), gbc);
        gbc.gridx = 1;
        cbbNhanVien = new JComboBox<>();
        // Nạp danh sách NV đã có TK (trừ Admin)
        new TaiKhoanBUS().layDanhSachNhanVienCoTaiKhoan()
                         .forEach(cbbNhanVien::addItem);
        pnl.add(cbbNhanVien, gbc);

        // Tên đăng nhập
        gbc.gridy = 1; gbc.gridx = 0;
        pnl.add(new JLabel("Tên đăng nhập:"), gbc);
        gbc.gridx = 1;
        txtUsername = new JTextField(15);
        pnl.add(txtUsername, gbc);

        // Mật khẩu
        gbc.gridy = 2; gbc.gridx = 0;
        pnl.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1;
        txtPassword = new JTextField(15);
        pnl.add(txtPassword, gbc);

        // Quyền hạng
        gbc.gridy = 3; gbc.gridx = 0;
        pnl.add(new JLabel("Quyền hạng:"), gbc);
        gbc.gridx = 1;
        cbbQuyenHang = new JComboBox<>(new String[]{"QuanLy", "NhanVien"});
        pnl.add(cbbQuyenHang, gbc);

        // Nút Lưu / Hủy
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel btnPnl = new JPanel();
        btnLuu = new JButton("Lưu");
        btnHuy = new JButton("Hủy");
        btnPnl.add(btnLuu);
        btnPnl.add(btnHuy);
        pnl.add(btnPnl, gbc);

        getContentPane().add(pnl);
        pack();
        setLocationRelativeTo(owner);

        // Sự kiện khi chọn nhân viên khác
        cbbNhanVien.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    setDefaultQuyenHang();
                }
            }
        });

        // Sự kiện khi thay đổi quyền hạng
        cbbQuyenHang.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED && oldRole != null) {
                    String newRole = (String) cbbQuyenHang.getSelectedItem();
                    if (!newRole.equals(oldRole)) {
                        // Mostrar diálogo de confirmación
                        int response = JOptionPane.showOptionDialog(
                            SuaTaiKhoanDialog.this,
                            "Cập nhật quyền hạng của tài khoản sẽ cũng sẽ cập nhật chức vụ của nhân viên.\n" +
                            "Bạn có muốn tiếp tục?",
                            "Xác nhận",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE,
                            null,
                            new Object[]{"Có", "Hủy"},
                            "Có"
                        );
                        
                        if (response != 0) { // No es "Có"
                            cbbQuyenHang.setSelectedItem(oldRole); // Revertir cambio
                        }
                    }
                }
            }
        });

        btnHuy.addActionListener(e -> dispose());
        btnLuu.addActionListener(e -> {
            String maNV = (String) cbbNhanVien.getSelectedItem();
            String user = txtUsername.getText().trim();
            String pass = txtPassword.getText().trim();
            String role = (String) cbbQuyenHang.getSelectedItem();
            
            if (!role.equals(oldRole)) {
                // Buscar el empleado
                NhanVienDTO nv = nvBUS.layTatCa().stream()
                              .filter(n -> n.getMaNV().equals(maNV))
                              .findFirst()
                              .orElse(null);
                              
                if (nv != null) {
                    // Actualizar el cargo del empleado según el rol
                    String newChucVu = role.equals("QuanLy") ? "Quản lý" : "Nhân viên";
                    if (!nv.getChucVu().equals(newChucVu)) {
                        // Actualizar el cargo del empleado
                        nv.setChucVu(newChucVu);
                        nvBUS.sua(nv);
                    }
                }
            }

            new TaiKhoanBUS().capNhat(editingMaTK, user, pass, role, maNV);
            saved = true;
            dispose();
        });
    }

    // Phương thức cập nhật quyền hạng mặc định dựa trên chức vụ của nhân viên
    private void setDefaultQuyenHang() {
        String maNV = (String)cbbNhanVien.getSelectedItem();
        if (maNV != null && !maNV.isEmpty()) {
            // Truy vấn thông tin nhân viên từ BUS
            List<NhanVienDTO> listNV = nvBUS.layTatCa();
            for (NhanVienDTO nv : listNV) {
                if (nv.getMaNV().equals(maNV)) {
                    // Kiểm tra chức vụ và chọn đúng quyền hạng tương ứng
                    if ("Quản lý".equals(nv.getChucVu())) {
                        cbbQuyenHang.setSelectedItem("QuanLy");
                    } else {
                        cbbQuyenHang.setSelectedItem("NhanVien");
                    }
                    break;
                }
            }
        }
    }

    /** Gọi trước khi chỉnh sửa để nạp data và khoá cbbNhanVien */
    public void loadForEdit(String maTK) {
        this.editingMaTK = maTK;
        setTitle("Chỉnh sửa tài khoản");
        TaiKhoanDTO tk = new TaiKhoanBUS().layTheoMa(maTK);
        cbbNhanVien.setSelectedItem(tk.getMaNhanVien());
        cbbNhanVien.setEnabled(false);  // KHÓA không cho đổi nv
        txtUsername.setText(tk.getUsername());
        txtPassword.setText(tk.getPassword());
        cbbQuyenHang.setSelectedItem(tk.getVaiTro()); // Hiển thị quyền hạng hiện tại
        oldRole = tk.getVaiTro(); // Guardar el rol original
    }

    /** Đánh dấu xem ng dùng có lưu thành công hay ko */
    public boolean isSaved() {
        return saved;
    }
}