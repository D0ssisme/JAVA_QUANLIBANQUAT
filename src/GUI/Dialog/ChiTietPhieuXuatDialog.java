package GUI.Dialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ChiTietPhieuXuatDialog extends JDialog {
    private JTable table;
    private DefaultTableModel tableModel;

    public ChiTietPhieuXuatDialog(Window owner) {
        super(owner, "Chi Tiết Phiếu Xuất", ModalityType.APPLICATION_MODAL);
        initComponent();
    }

    private void initComponent() {
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("CHI TIẾT PHIẾU XUẤT", SwingConstants.CENTER);
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 16f));
        add(lblTitle, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Mã quạt", "Số lượng", "Đơn giá"}, 0);
        table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnDong = new JButton("Đóng");
        pnlBottom.add(btnDong);
        add(pnlBottom, BorderLayout.SOUTH);

        addDummyData();

        btnDong.addActionListener(e -> dispose());
    }

    private void addDummyData() {
        // Dữ liệu mẫu
        tableModel.addRow(new Object[]{"Q001", 2, "300.000"});
        tableModel.addRow(new Object[]{"Q002", 1, "500.000"});
    }
}
