package GUI.Dialog;

import BUS.NhanVienBUS;
import BUS.TaiKhoanBUS;
import DTO.NhanVienDTO;
import DTO.TaiKhoanDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class SuaNhanVienDialog extends JDialog {
    private final JTextField txtMa, txtTen, txtSdt, txtDiaChi;
    private final JComboBox<String> cbbChucVu;
    private final JButton btnLuu, btnHuy;
    private final NhanVienBUS bll = new NhanVienBUS();
    private final TaiKhoanBUS tkBUS = new TaiKhoanBUS();
    private boolean saved = false;
    private String oldChucVu; // Para guardar el cargo original

    public SuaNhanVienDialog(Window owner, String maNV) {
        super(owner, "Chỉnh sửa nhân viên", ModalityType.APPLICATION_MODAL);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        // Load dữ liệu
        NhanVienDTO n = bll.layTatCa().stream()
                        .filter(x -> x.getMaNV().equals(maNV))
                        .findFirst()
                        .orElseThrow();
        
        oldChucVu = n.getChucVu(); // Guardar el cargo original

        gbc.gridx=0; gbc.gridy=0;
        add(new JLabel("Mã NV:"), gbc);
        gbc.gridx=1;
        txtMa = new JTextField(n.getMaNV(), 15);
        txtMa.setEditable(false);
        txtMa.setBackground(new Color(240, 240, 240));
        add(txtMa, gbc);

        gbc.gridy=1; gbc.gridx=0;
        add(new JLabel("Họ tên:"), gbc);
        gbc.gridx=1;
        txtTen = new JTextField(n.getHoTen(), 15);
        add(txtTen, gbc);

        gbc.gridy=2; gbc.gridx=0;
        add(new JLabel("Chức vụ:"), gbc);
        gbc.gridx=1;
        cbbChucVu = new JComboBox<>(new String[]{"Nhân viên", "Quản lý", "Bảo vệ"});
        cbbChucVu.setSelectedItem(n.getChucVu().equals("Quản lý") ? "Quản lý" : 
                                n.getChucVu().equals("Bảo vệ") ? "Bảo vệ" : "Nhân viên");
        add(cbbChucVu, gbc);

        // Evento al cambiar el cargo
        cbbChucVu.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String newChucVu = (String) cbbChucVu.getSelectedItem();
                    if (!newChucVu.equals(oldChucVu)) {
                        // Verificar si tiene cuenta
                        Optional<TaiKhoanDTO> taikhoan = tkBUS.layTatCa().stream()
                                                      .filter(tk -> maNV.equals(tk.getMaNhanVien()))
                                                      .findFirst();
                        
                        if (taikhoan.isPresent()) {
                            String message;
                            String title;
                            int messageType;
                            
                            if (newChucVu.equals("Bảo vệ")) {
                                message = "Nhân viên này đã có tài khoản với quyền hạng là " + 
                                          taikhoan.get().getVaiTro() + ".\n" +
                                          "Nếu chỉnh sửa thành Bảo vệ, tài khoản hiện tại sẽ bị xóa.\n" +
                                          "Bạn có muốn tiếp tục?";
                                title = "Cảnh báo xóa tài khoản";
                                messageType = JOptionPane.WARNING_MESSAGE;
                            } else {
                                message = "Cập nhật chức vụ của nhân viên sẽ cũng sẽ cập nhật quyền hạng của tài khoản.\n" +
                                         "Bạn có muốn tiếp tục?";
                                title = "Xác nhận";
                                messageType = JOptionPane.INFORMATION_MESSAGE;
                            }
                            
                            int response = JOptionPane.showOptionDialog(
                                SuaNhanVienDialog.this,
                                message,
                                title,
                                JOptionPane.YES_NO_OPTION,
                                messageType,
                                null,
                                new Object[]{"Có", "Hủy"},
                                "Có"
                            );
                            
                            if (response != 0) { // No es "Có"
                                cbbChucVu.setSelectedItem(oldChucVu); // Revertir cambio
                            }
                        }
                    }
                }
            }
        });

        gbc.gridy=3; gbc.gridx=0;
        add(new JLabel("SĐT:"), gbc);
        gbc.gridx=1;
        txtSdt = new JTextField(n.getSdt(), 15);
        add(txtSdt, gbc);

        gbc.gridy=4; gbc.gridx=0;
        add(new JLabel("Địa chỉ:"), gbc);
        gbc.gridx=1;
        txtDiaChi = new JTextField(n.getDiaChi(), 15);
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
        
        SwingUtilities.invokeLater(() -> txtTen.requestFocus());

        btnHuy.addActionListener(e -> dispose());
        btnLuu.addActionListener(e -> {
            if (validateInput()) {
                String oldRole = oldChucVu;
                String newRole = cbbChucVu.getSelectedItem().toString();
                
                // Si hay cambio de cargo
                if (!newRole.equals(oldRole)) {
                    // Ver si tiene cuenta
                    String maNhanVien = txtMa.getText().trim();
                    Optional<TaiKhoanDTO> taikhoan = tkBUS.layTatCa().stream()
                                                 .filter(tk -> maNhanVien.equals(tk.getMaNhanVien()))
                                                 .findFirst();
                    
                    if (taikhoan.isPresent()) {
                        if (newRole.equals("Bảo vệ")) {
                            // Eliminar cuenta si cambia a Bảo vệ
                            tkBUS.xoa(taikhoan.get().getMaTaiKhoan());
                        } else {
                            // Actualizar rol de la cuenta
                            String newAccountRole = newRole.equals("Quản lý") ? "QuanLy" : "NhanVien";
                            TaiKhoanDTO tk = taikhoan.get();
                            tkBUS.capNhat(tk.getMaTaiKhoan(), tk.getUsername(), tk.getPassword(), 
                                        newAccountRole, tk.getMaNhanVien());
                        }
                    }
                }
                
                // Guardar cambios al empleado
                NhanVienDTO nv = new NhanVienDTO(
                    txtMa.getText().trim(),
                    txtTen.getText().trim(),
                    newRole,
                    txtSdt.getText().trim(),
                    txtDiaChi.getText().trim()
                );
                
                if (bll.sua(nv)) {
                    saved = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
                }
            }
        });
    }
    
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

    public boolean isSaved() { return saved; }
}