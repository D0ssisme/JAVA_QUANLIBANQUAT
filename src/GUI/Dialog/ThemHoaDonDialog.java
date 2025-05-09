package GUI.Dialog;

import javax.swing.*;
import java.awt.*;

public class ThemHoaDonDialog extends JDialog {
    private JTextField txtMaHD, txtMaNV, txtMaKH, txtNgayLap, txtMaKM, txtTongTien;
    private JButton btnLuu, btnHuy;
    private boolean isSaved = false;

    public ThemHoaDonDialog(Window owner) {
        super(owner, "Thêm Hóa Đơn", ModalityType.APPLICATION_MODAL);
        initComponent();
    }

    private void initComponent() {
        setSize(400, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel pnlForm = new JPanel(new GridLayout(6, 2, 5, 5));
        pnlForm.add(new JLabel("Mã HĐ:"));
        txtMaHD = new JTextField();
        pnlForm.add(txtMaHD);

        pnlForm.add(new JLabel("Mã NV:"));
        txtMaNV = new JTextField();
        pnlForm.add(txtMaNV);

        pnlForm.add(new JLabel("Mã KH:"));
        txtMaKH = new JTextField();
        pnlForm.add(txtMaKH);

        pnlForm.add(new JLabel("Ngày lập:"));
        txtNgayLap = new JTextField();
        pnlForm.add(txtNgayLap);

        pnlForm.add(new JLabel("Mã KM (nếu có):"));
        txtMaKM = new JTextField();
        pnlForm.add(txtMaKM);

        pnlForm.add(new JLabel("Tổng tiền:"));
        txtTongTien = new JTextField();
        pnlForm.add(txtTongTien);

        add(pnlForm, BorderLayout.CENTER);

        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnLuu = new JButton("Lưu");
        btnHuy = new JButton("Hủy");
        pnlButton.add(btnLuu);
        pnlButton.add(btnHuy);
        add(pnlButton, BorderLayout.SOUTH);

        // Sự kiện
        btnLuu.addActionListener(e -> {
            isSaved = true;
            JOptionPane.showMessageDialog(this, "Đã lưu hóa đơn (demo).");
            dispose();
        });
        btnHuy.addActionListener(e -> {
            dispose();
        });
    }

    public boolean isSaved() {
        return isSaved;
    }

    // Lấy dữ liệu mới (nếu muốn chèn vào bảng)
    public Object[] getNewHoaDon() {
        return new Object[]{
            txtMaHD.getText().trim(),
            txtMaNV.getText().trim(),
            txtMaKH.getText().trim(),
            txtNgayLap.getText().trim(),
            txtMaKM.getText().trim(),
            Integer.parseInt(txtTongTien.getText().trim())
        };
    }
}
