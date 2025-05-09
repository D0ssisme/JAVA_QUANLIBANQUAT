package GUI.Dialog;

import DAO.KhuyenMaiDAO;
import DTO.KhuyenMaiDTO;
import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SuaKhuyenMaiDialog extends JDialog {

    private JTextField txtMaKM, txtTenKM, txtPhanTram, txtNgayBD, txtNgayKT;
    private JComboBox<String> cbbLoai;
    private JButton btnSave, btnCancel;
    private boolean updated = false;
    private KhuyenMaiDTO km;

    public SuaKhuyenMaiDialog(Frame parent, KhuyenMaiDTO km) {
        super(parent, "Sửa Khuyến Mãi", true);
        this.km = km;
        initUI();
        loadData();
        setSize(400, 300);
        setLocationRelativeTo(parent);
    }

    private void initUI() {
        JPanel pnl = new JPanel(new GridLayout(6, 2, 5, 5));
        pnl.add(new JLabel("Mã KM:"));
        txtMaKM = new JTextField();
        txtMaKM.setEditable(false);
        pnl.add(txtMaKM);

        pnl.add(new JLabel("Tên KM:"));
        txtTenKM = new JTextField();
        pnl.add(txtTenKM);

        pnl.add(new JLabel("Phần trăm giảm:"));
        txtPhanTram = new JTextField();
        pnl.add(txtPhanTram);

        pnl.add(new JLabel("Ngày bắt đầu (yyyy-MM-dd):"));
        txtNgayBD = new JTextField();
        pnl.add(txtNgayBD);

        pnl.add(new JLabel("Ngày kết thúc (yyyy-MM-dd):"));
        txtNgayKT = new JTextField();
        pnl.add(txtNgayKT);

        pnl.add(new JLabel("Loại khuyến mãi:"));
        cbbLoai = new JComboBox<>(new String[]{"Đơn hàng ≥500k", "Mua ≥2 sp", "Mọi hóa đơn"});
        pnl.add(cbbLoai);

        add(pnl, BorderLayout.CENTER);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> dispose());
    }

    private void loadData() {
        txtMaKM.setText(km.getMaSKKhuyenMai());
        txtTenKM.setText(km.getTenKhuyenMai());
        txtPhanTram.setText(String.valueOf(km.getPhanTramGiam()));
        txtNgayBD.setText(format(km.getNgayBatDau()));
        txtNgayKT.setText(format(km.getNgayKetThuc()));
        cbbLoai.setSelectedIndex(km.getLoai() - 1);
    }

    private void save() {
        try {
            km.setTenKhuyenMai(txtTenKM.getText().trim());
            km.setPhanTramGiam(Integer.parseInt(txtPhanTram.getText().trim()));
            km.setNgayBatDau(parse(txtNgayBD.getText().trim()));
            km.setNgayKetThuc(parse(txtNgayKT.getText().trim()));
            km.setLoai(cbbLoai.getSelectedIndex() + 1);

            boolean ok = KhuyenMaiDAO.update(km);
            if (ok) {
                updated = true;
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private Date parse(String s) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        return sdf.parse(s);
    }

    private String format(Date d) {
        return new SimpleDateFormat("yyyy-MM-dd").format(d);
    }

    public boolean isUpdated() {
        return updated;
    }
}
