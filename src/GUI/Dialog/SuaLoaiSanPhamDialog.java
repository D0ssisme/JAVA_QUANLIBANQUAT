package GUI.Dialog;

import BUS.LoaiSanPhamBUS;
import DTO.LoaiSanPhamDTO;

import javax.swing.*;
import java.awt.*;

public class SuaLoaiSanPhamDialog extends JDialog {
    private JTextField txtMaLoai, txtTenLoai, txtMoTa;
    private JComboBox<String> cboTrangThai;
    private boolean isUpdated = false;

    private final LoaiSanPhamDTO loaiSanPhamCu;
    private final LoaiSanPhamBUS loaiSanPhamBUS = new LoaiSanPhamBUS();

    public SuaLoaiSanPhamDialog(Window owner, LoaiSanPhamDTO loaiSanPham) {
        super(owner, "Sửa Loại Sản Phẩm", ModalityType.APPLICATION_MODAL);
        this.loaiSanPhamCu = loaiSanPham;
        initComponent();
        loadDataToFields();
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    private void initComponent() {
        this.setSize(450, 350);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("SỬA LOẠI SẢN PHẨM", SwingConstants.CENTER);
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

        txtMaLoai = new JTextField();
        txtMaLoai.setEditable(false);
        pnlForm.add(createInputRow("Mã loại:", txtMaLoai, labelFont, textFont, textFieldSize));

        txtTenLoai = new JTextField();
        pnlForm.add(createInputRow("Tên loại:", txtTenLoai, labelFont, textFont, textFieldSize));

        cboTrangThai = new JComboBox<>(new String[]{"Hoạt động", "Ngừng kinh doanh"});
        pnlForm.add(createComboBoxRow("Trạng thái:", cboTrangThai, labelFont));

        txtMoTa = new JTextField();
        pnlForm.add(createInputRow("Mô tả:", txtMoTa, labelFont, textFont, textFieldSize));

        this.add(pnlForm, BorderLayout.CENTER);

        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLuu = new JButton("Lưu");
        JButton btnHuy = new JButton("Hủy");
        pnlButton.add(btnLuu);
        pnlButton.add(btnHuy);
        this.add(pnlButton, BorderLayout.SOUTH);

        btnLuu.addActionListener(e -> {
            if (saveChanges()) {
                JOptionPane.showMessageDialog(this, "Loại sản phẩm đã được cập nhật.");
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

    private void loadDataToFields() {
        txtMaLoai.setText(loaiSanPhamCu.getMaLoaiSanPham());
        txtTenLoai.setText(loaiSanPhamCu.getTenLoai());
        cboTrangThai.setSelectedItem(loaiSanPhamCu.getTrangThai());
        txtMoTa.setText(loaiSanPhamCu.getMoTa());
    }

    private boolean saveChanges() {
        String maLoai = txtMaLoai.getText().trim();
        String tenLoai = txtTenLoai.getText().trim();
        String trangThai = (String) cboTrangThai.getSelectedItem();
        String moTa = txtMoTa.getText().trim();

        if (tenLoai.isEmpty() || moTa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.");
            return false;
        }

        return loaiSanPhamBUS.sua(maLoai, tenLoai, trangThai, moTa);
    }
}
