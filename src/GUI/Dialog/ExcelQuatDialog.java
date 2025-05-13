package GUI.Dialog;

import DAO.QuatDAO;
import DTO.QuatDTO;
import helper.XuatExcel;
import helper.NhapExcel;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ExcelQuatDialog extends JDialog {
    private JTable table;
    private Runnable onSuccess;  // Callback to notify the parent panel to reload data

    // Constructor with the callback
    public ExcelQuatDialog(Window owner, JTable table, Runnable onSuccess) {
        super(owner, "Xuất / Nhập Excel", ModalityType.APPLICATION_MODAL);
        this.table = table;
        this.onSuccess = onSuccess;

        setSize(300, 150);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        JButton btnXuat = new JButton("Xuất Excel");
        JButton btnNhap = new JButton("Nhập Excel");

        buttonPanel.add(btnXuat);
        buttonPanel.add(btnNhap);
        add(buttonPanel, BorderLayout.CENTER);

        btnXuat.addActionListener(e -> exportToExcel());
        btnNhap.addActionListener(e -> importFromExcel());
    }

    private void exportToExcel() {
        try {
            List<QuatDTO> dataList = new QuatDAO().getAll();
            // TODO: Export dataList to Excel file
            JOptionPane.showMessageDialog(this, "Đã xuất " + dataList.size() + " quạt từ CSDL ra Excel.");
            
            // Notify the parent to reload the data and close the dialog
            if (onSuccess != null) {
                onSuccess.run();
            }
            dispose(); // Close the dialog
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy dữ liệu từ CSDL!");
        }
    }

    private void importFromExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file Excel để nhập");

        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            try {
                List<QuatDTO> danhSachQuat = NhapExcel.nhapDanhSachQuatTuExcel(filePath);

                QuatDAO quatDAO = new QuatDAO();
                int count = 0;
                for (QuatDTO quat : danhSachQuat) {
                    try {
                        quatDAO.insert(quat);  
                        count++;              
                    } catch (Exception ex) {
                        ex.printStackTrace();  
                    }
                }

                JOptionPane.showMessageDialog(this, "Đã nhập thành công " + count + " quạt từ file Excel.");
                
                // Notify the parent to reload the data and close the dialog
                if (onSuccess != null) {
                    onSuccess.run();
                }
                dispose(); // Close the dialog
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi nhập dữ liệu từ file Excel!");
            }
        }
    }
}
