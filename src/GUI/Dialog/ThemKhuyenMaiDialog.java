package GUI.Dialog;

import DTO.KhuyenMaiDTO;
import DAO.KhuyenMaiDAO;
import java.util.Date;
import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ThemKhuyenMaiDialog extends JDialog {

    private JTextField txtMaKM, txtTenKM, txtPhanTram, txtNgayBD, txtNgayKT;
    private JComboBox<String> cbbLoai;
    private JButton btnLuu, btnHuy;
    private boolean isSaved = false;

    public ThemKhuyenMaiDialog(Window owner) {
        super(owner, "Thêm Khuyến Mãi", ModalityType.APPLICATION_MODAL);
        initComponent();
    }

    private void initComponent() {
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel pnlForm = new JPanel(new GridLayout(6, 2, 5, 5));

        pnlForm.add(new JLabel("Mã KM:"));
        txtMaKM = new JTextField();
        pnlForm.add(txtMaKM);

        pnlForm.add(new JLabel("Tên KM:"));
        txtTenKM = new JTextField();
        pnlForm.add(txtTenKM);

        pnlForm.add(new JLabel("Phần trăm giảm:"));
        txtPhanTram = new JTextField();
        pnlForm.add(txtPhanTram);

        pnlForm.add(new JLabel("Ngày bắt đầu (yyyy-MM-dd):"));
        txtNgayBD = new JTextField();
        pnlForm.add(txtNgayBD);

        pnlForm.add(new JLabel("Ngày kết thúc (yyyy-MM-dd):"));
        txtNgayKT = new JTextField();
        pnlForm.add(txtNgayKT);

        pnlForm.add(new JLabel("Loại khuyến mãi:"));
        cbbLoai = new JComboBox<>(new String[]{"Đơn hàng ≥500k", "Mua ≥2 sp", "Mọi hóa đơn"});
        pnlForm.add(cbbLoai);

        add(pnlForm, BorderLayout.CENTER);

        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnLuu = new JButton("Lưu");
        btnHuy = new JButton("Hủy");
        pnlBtn.add(btnLuu);
        pnlBtn.add(btnHuy);
        add(pnlBtn, BorderLayout.SOUTH);

        btnLuu.addActionListener(e -> save());
        btnHuy.addActionListener(e -> dispose());
    }

    private void save() {
        try {
            String ma = txtMaKM.getText().trim();
            String ten = txtTenKM.getText().trim();
            int pt = Integer.parseInt(txtPhanTram.getText().trim());
            Date bd = parseDate(txtNgayBD.getText().trim());
            Date kt = parseDate(txtNgayKT.getText().trim());
            int loai = cbbLoai.getSelectedIndex() + 1;

            KhuyenMaiDTO km = new KhuyenMaiDTO(ma, ten, pt, bd, kt, loai);
            boolean success = KhuyenMaiDAO.insert(km);
            if (success) {
                isSaved = true;
                JOptionPane.showMessageDialog(this, "Thêm khuyến mãi thành công!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại, mã có thể đã tồn tại.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private Date parseDate(String s) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        return sdf.parse(s);
    }

    public boolean isSaved() {
        return isSaved;
    }
}
