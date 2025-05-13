package GUI.Dialog;

import BUS.LoaiSanPhamBUS;
import BUS.QuatBUS;
import DTO.LoaiSanPhamDTO;
import DTO.QuatDTO;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class SuaQuatDialog extends JDialog {
    private JTextField txtMaQuat, txtTenQuat, txtGia, txtSoLuongTon, txtMaNSX, txtChatLieu, txtThuongHieu;
    private JComboBox<LoaiSanPhamDTO> cboLoaiSanPham;
    private JDateChooser dateNgaySanXuat;
    private boolean isUpdated = false;

    private final QuatDTO quatCu;
    private final LoaiSanPhamBUS loaiBUS = new LoaiSanPhamBUS();

    public SuaQuatDialog(Window owner, QuatDTO quat) {
        super(owner, "Sửa Quạt", ModalityType.APPLICATION_MODAL);
        this.quatCu = quat;
        initComponent();
        loadDataToFields();
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    private void initComponent() {
        this.setSize(450, 520);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("SỬA QUẠT", SwingConstants.CENTER);
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(255, 193, 7));
        lblTitle.setForeground(Color.BLACK);
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
        txtMaQuat.setEditable(false); 

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
        JButton btnLuu = new JButton("Lưu");
        JButton btnHuy = new JButton("Hủy");
        pnlButton.add(btnLuu);
        pnlButton.add(btnHuy);
        this.add(pnlButton, BorderLayout.SOUTH);

        btnLuu.addActionListener(e -> {
            if (saveChanges()) {
                JOptionPane.showMessageDialog(this, "Đã sửa quạt.");
                isUpdated = true;
                dispose();
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

    private JPanel createDateChooserRow(String labelText, JDateChooser dateChooser, Font labelFont) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        label.setPreferredSize(new Dimension(140, 30));
        row.add(label);
        row.add(dateChooser);
        return row;
    }

    private void loadLoaiSanPham() {
        List<LoaiSanPhamDTO> dsLoai = loaiBUS.layTatCa();
        for (LoaiSanPhamDTO loai : dsLoai) {
            cboLoaiSanPham.addItem(loai);
        }
    }

    private void loadDataToFields() {
        txtMaQuat.setText(quatCu.getMaQuat());
        txtTenQuat.setText(quatCu.getTenQuat());
        txtGia.setText(String.valueOf(quatCu.getGia()));
        txtSoLuongTon.setText(String.valueOf(quatCu.getSoLuongTon()));
        txtMaNSX.setText(quatCu.getMaNSX());
        txtChatLieu.setText(quatCu.getChatLieu());
        txtThuongHieu.setText(quatCu.getThuongHieu());

        // Gán ngày sản xuất
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dateNgaySanXuat.setDate(sdf.parse(quatCu.getNgaySanXuat()));
        } catch (Exception e) {
            dateNgaySanXuat.setDate(null);
        }

        // Chọn loại sản phẩm tương ứng
        for (int i = 0; i < cboLoaiSanPham.getItemCount(); i++) {
            LoaiSanPhamDTO loai = cboLoaiSanPham.getItemAt(i);
            if (loai.getMaLoaiSanPham().equals(quatCu.getMaLoaiSP())) {
                cboLoaiSanPham.setSelectedIndex(i);
                break;
            }
        }
    }

    private boolean saveChanges() {
        try {
            String maQuat = txtMaQuat.getText().trim();
            String tenQuat = txtTenQuat.getText().trim();
            int gia = Integer.parseInt(txtGia.getText().trim());
            int soLuongTon = Integer.parseInt(txtSoLuongTon.getText().trim());
            String maNSX = txtMaNSX.getText().trim();
            String chatLieu = txtChatLieu.getText().trim();
            String thuongHieu = txtThuongHieu.getText().trim();
            java.util.Date selectedDate = dateNgaySanXuat.getDate();
            if (selectedDate == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày sản xuất.");
                return false;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String ngaySanXuatStr = sdf.format(selectedDate);
            LoaiSanPhamDTO selectedLoai = (LoaiSanPhamDTO) cboLoaiSanPham.getSelectedItem();
            String maLoaiSP = selectedLoai.getMaLoaiSanPham();

            return new QuatBUS().sua(maQuat, tenQuat, gia, soLuongTon, maNSX, ngaySanXuatStr, chatLieu, thuongHieu, maLoaiSP);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá và số lượng tồn phải là số nguyên.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
