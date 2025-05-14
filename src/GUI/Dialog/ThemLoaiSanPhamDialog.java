package GUI.Dialog;

import BUS.LoaiSanPhamBUS;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThemLoaiSanPhamDialog extends JDialog {

    private JTextField txtMaLoai, txtTenLoai, txtMoTa;
    private JComboBox<String> cboTrangThai;
    private JButton btnLuu, btnHuy;
    private boolean added = false;

    private final LoaiSanPhamBUS loaiSanPhamBUS = new LoaiSanPhamBUS();

    public ThemLoaiSanPhamDialog(Window owner) {
        super(owner, "Thêm Loại Sản Phẩm", Dialog.ModalityType.APPLICATION_MODAL);
        initComponent();
    }

    private void initComponent() {
        this.setSize(450, 350);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("THÊM LOẠI SẢN PHẨM", SwingConstants.CENTER);
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

        pnlForm.add(createInputRow("Mã loại:", txtMaLoai = new JTextField(), labelFont, textFont, textFieldSize));
        pnlForm.add(createInputRow("Tên loại:", txtTenLoai = new JTextField(), labelFont, textFont, textFieldSize));
        cboTrangThai = new JComboBox<>(new String[]{"Hoạt động", "Ngừng kinh doanh"});
        pnlForm.add(createComboBoxRow("Trạng thái:", cboTrangThai, labelFont));
        pnlForm.add(createInputRow("Mô tả:", txtMoTa = new JTextField(), labelFont, textFont, textFieldSize));

        this.add(pnlForm, BorderLayout.CENTER);

        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnLuu = new JButton("Lưu");
        btnHuy = new JButton("Hủy");
        pnlButton.add(btnLuu);
        pnlButton.add(btnHuy);
        this.add(pnlButton, BorderLayout.SOUTH);

        btnLuu.addActionListener(e -> {
            try {
                handleSave();
            } catch (SQLException ex) {
                Logger.getLogger(ThemLoaiSanPhamDialog.class.getName()).log(Level.SEVERE, null, ex);
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

    private void handleSave() throws SQLException {
        String ma = txtMaLoai.getText().trim();
        String ten = txtTenLoai.getText().trim();
        String trangThai = (String) cboTrangThai.getSelectedItem();
        String moTa = txtMoTa.getText().trim();

        if (ma.isEmpty() || ten.isEmpty() || trangThai.isEmpty() || moTa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.");
            return;
        }
         if (loaiSanPhamBUS.daTonTaiMaLoai(ma)) {
            JOptionPane.showMessageDialog(this, "Mã loại sản phẩm đã tồn tại. Vui lòng nhập mã khác.");
            return;
            }
        if (loaiSanPhamBUS.them(ma, ten, trangThai, moTa)) {
            JOptionPane.showMessageDialog(this, "Thêm loại sản phẩm thành công.");
            added = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại. Vui lòng kiểm tra lại.");
        }
    }

    public boolean isAdded() {
        return added;
    }
}
