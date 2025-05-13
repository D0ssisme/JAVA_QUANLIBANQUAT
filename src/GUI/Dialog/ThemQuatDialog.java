package GUI.Dialog;

import BUS.LoaiSanPhamBUS;
import BUS.QuatBUS;
import DTO.LoaiSanPhamDTO;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import com.toedter.calendar.JDateChooser;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThemQuatDialog extends JDialog {

    private JTextField txtMaQuat, txtTenQuat, txtGia, txtMaNSX, txtChatLieu, txtThuongHieu, txtSoLuongTon;
    private JComboBox<LoaiSanPhamDTO> cboLoaiSanPham;
    private JDateChooser dateNgaySanXuat;
    private JButton btnLuu, btnHuy;
    private boolean added = false;

    private final QuatBUS quatBUS = new QuatBUS();
    private final LoaiSanPhamBUS loaiBUS = new LoaiSanPhamBUS();

    public ThemQuatDialog(Window owner) {
        super(owner, "Thêm Quạt", Dialog.ModalityType.APPLICATION_MODAL);
        initComponent();
    }

    private void initComponent() {
        this.setSize(450, 520);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("THÊM QUẠT", SwingConstants.CENTER);
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(0, 123, 255));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setPreferredSize(new Dimension(this.getWidth(), 50));
        this.add(lblTitle, BorderLayout.NORTH);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font textFont = new Font("Segoe UI", Font.PLAIN, 14);
        Dimension textFieldSize = new Dimension(200, 30);

        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
        pnlForm.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        pnlForm.add(createInputRow("Mã quạt:", txtMaQuat = new JTextField(), labelFont, textFont, textFieldSize));
        pnlForm.add(createInputRow("Tên quạt:", txtTenQuat = new JTextField(), labelFont, textFont, textFieldSize));
        pnlForm.add(createInputRow("Giá:", txtGia = new JTextField(), labelFont, textFont, textFieldSize));
        pnlForm.add(createInputRow("Số lượng tồn:", txtSoLuongTon = new JTextField(), labelFont, textFont, textFieldSize));
        pnlForm.add(createInputRow("Mã NSX:", txtMaNSX = new JTextField(), labelFont, textFont, textFieldSize));
         dateNgaySanXuat = new JDateChooser();
        dateNgaySanXuat.setDateFormatString("yyyy-MM-dd");
        dateNgaySanXuat.setPreferredSize(new Dimension(200, 30));
        pnlForm.add(createDateChooserRow("Ngày sản xuất:", dateNgaySanXuat, labelFont));

        pnlForm.add(createInputRow("Chất liệu:", txtChatLieu = new JTextField(), labelFont, textFont, textFieldSize));
        pnlForm.add(createInputRow("Thương hiệu:", txtThuongHieu = new JTextField(), labelFont, textFont, textFieldSize));

        cboLoaiSanPham = new JComboBox<>();
        loadLoaiSanPham();
        pnlForm.add(createComboBoxRow("Loại:", cboLoaiSanPham, labelFont));

        this.add(pnlForm, BorderLayout.CENTER);

        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnLuu = new JButton("Lưu");
        btnHuy = new JButton("Hủy");
        pnlButton.add(btnLuu);
        pnlButton.add(btnHuy);
        this.add(pnlButton, BorderLayout.SOUTH);

        btnLuu.addActionListener(e -> {
            try {
                kiemtra();
            } catch (SQLException ex) {
                Logger.getLogger(ThemQuatDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btnHuy.addActionListener(e -> dispose());
    }

    private JPanel createInputRow(String labelText, JTextField textField, Font labelFont, Font textFont, Dimension textFieldSize) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        label.setPreferredSize(new Dimension(140, 30));
        textField.setFont(textFont);
        textField.setPreferredSize(textFieldSize);
        row.add(label);
        row.add(textField);
        return row;
    }

    private JPanel createComboBoxRow(String labelText, JComboBox<?> comboBox, Font labelFont) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        label.setPreferredSize(new Dimension(140, 30));
        comboBox.setPreferredSize(new Dimension(200, 30));
        row.add(label);
        row.add(comboBox);
        return row;
    }

    private void loadLoaiSanPham() {
        List<LoaiSanPhamDTO> dsLoai = loaiBUS.layTatCa();
        for (LoaiSanPhamDTO loai : dsLoai) {
            cboLoaiSanPham.addItem(loai);
        }
    }

    private void kiemtra() throws SQLException {
        try {
            String maQuat = txtMaQuat.getText().trim();
            String tenQuat = txtTenQuat.getText().trim();
            String chatLieu = txtChatLieu.getText().trim();
            String thuongHieu = txtThuongHieu.getText().trim();
            String maNSX = txtMaNSX.getText().trim();
            if (maQuat.isEmpty() || tenQuat.isEmpty() || chatLieu.isEmpty() || thuongHieu.isEmpty() || maNSX.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.");
                return;
            }
            if (quatBUS.daTonTaiMaQuat(maQuat)) {
                JOptionPane.showMessageDialog(this, "Mã quạt đã tồn tại. Vui lòng nhập mã khác.");
                return;
            }
            int gia = Integer.parseInt(txtGia.getText().trim());
            int soLuongTon = Integer.parseInt(txtSoLuongTon.getText().trim());

            if (gia < 1000) {
                JOptionPane.showMessageDialog(this, "Giá phải lớn hơn hoặc bằng 1000.");
                return;
            }

            if (soLuongTon < 0) {
                JOptionPane.showMessageDialog(this, "Số lượng tồn không được âm.");
                return;
            }
            java.util.Date selectedDate = dateNgaySanXuat.getDate();
            if (selectedDate == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày sản xuất.");
                return;
            }
            java.sql.Date ngaySanXuat = new java.sql.Date(selectedDate.getTime());
            LoaiSanPhamDTO selectedLoai = (LoaiSanPhamDTO) cboLoaiSanPham.getSelectedItem();
            if (selectedLoai == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn loại sản phẩm.");
                return;
            }
            String maLoaiSP = selectedLoai.getMaLoaiSanPham();
            if (quatBUS.them(maQuat, tenQuat, gia, soLuongTon, maNSX, ngaySanXuat, chatLieu, thuongHieu, maLoaiSP)) {
                JOptionPane.showMessageDialog(this, "Đã lưu quạt.");
                added = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi lưu quạt.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Giá và số lượng tồn phải là số nguyên.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createDateChooserRow(String labelText, JDateChooser dateChooser, Font labelFont) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        label.setPreferredSize(new Dimension(140, 30));
        row.add(label);
        row.add(dateChooser);
        return row;
    }

    public boolean isAdded() {
        return added;
    }
}
