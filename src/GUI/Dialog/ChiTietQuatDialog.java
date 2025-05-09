package GUI.Dialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


public class ChiTietQuatDialog extends JDialog {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSoLuong;

    public ChiTietQuatDialog(Window owner) {
        super(owner, "Chi Tiết Quạt", Dialog.ModalityType.APPLICATION_MODAL);
        initComponent();
    }

    private void initComponent() {
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        // Tiêu đề
        JLabel lblTitle = new JLabel("CHI TIẾT QUẠT", SwingConstants.CENTER);
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 16f));
        this.add(lblTitle, BorderLayout.NORTH);

        // Bảng
        tableModel = new DefaultTableModel(new Object[]{"Serial", "Tình trạng"}, 0);
        table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        this.add(scroll, BorderLayout.CENTER);

        // Panel chứa textfield Số lượng
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        txtSoLuong = new JTextField(10);
        txtSoLuong.setEditable(false);
        pnlBottom.add(new JLabel("Số lượng serial:"));
        pnlBottom.add(txtSoLuong);
        this.add(pnlBottom, BorderLayout.SOUTH);

        // Thêm dữ liệu mẫu
        addDummyData();
    }

    private void addDummyData() {
        // Thêm một vài serial minh hoạ
        tableModel.addRow(new Object[]{"SR-001", "Đang bán"});
        tableModel.addRow(new Object[]{"SR-002", "Đã bán"});
        tableModel.addRow(new Object[]{"SR-003", "Đang bảo hành"});

        txtSoLuong.setText(String.valueOf(tableModel.getRowCount()));
    }
}
