package GUI.Dialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import DAO.ChiTietHoaDonDAO;
import DTO.ChiTietHoaDonDTO;

public class ChiTietHoaDonDialog extends JDialog {

    private JTable table;
    private DefaultTableModel tableModel;
    private String maHD;

    public ChiTietHoaDonDialog(Window owner, String maHD) {
        super(owner, "Chi Tiết Hóa Đơn - " + maHD, ModalityType.APPLICATION_MODAL);
        this.maHD = maHD;
        initComponent();
    }

    private void initComponent() {
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("CHI TIẾT HÓA ĐƠN " + maHD, SwingConstants.CENTER);
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 16f));
        add(lblTitle, BorderLayout.NORTH);

        String[] columns = {"Mã quạt", "Số lượng", "Đơn giá", "Thành tiền", "Mã bảo hành"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnDong = new JButton("Đóng");
        pnlBottom.add(btnDong);
        add(pnlBottom, BorderLayout.SOUTH);

        // Tải dữ liệu từ cơ sở dữ liệu
        loadDataFromDatabase(maHD);

        btnDong.addActionListener(e -> dispose());
    }

    private void loadDataFromDatabase(String maHD) {
        try {
            List<ChiTietHoaDonDTO> details = ChiTietHoaDonDAO.selectByMaHoaDon(maHD);
            for (ChiTietHoaDonDTO detail : details) {
                tableModel.addRow(new Object[]{
                    detail.getMaQuat(),
                    detail.getSoLuong(),
                    detail.getDonGia(),
                    detail.getThanhTien(),
                    detail.getMaBaoHanh()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isDataUpdated() {
        // Giả sử bạn có biến lưu trữ trạng thái cập nhật
        boolean dataChanged = false; // Thay đổi dựa trên logic thực tế
        return dataChanged;
    }
}
