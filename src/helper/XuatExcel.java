/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helper;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileOutputStream;

public class XuatExcel {

    public static void xuatExcel(JTable table, String sheetName) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu file Excel");
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().endsWith(".xlsx")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".xlsx");
            }

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet(sheetName != null ? sheetName : "DanhSach");

                TableModel model = table.getModel();

                // Ghi dòng tiêu đề
                Row header = sheet.createRow(0);
                for (int i = 0; i < model.getColumnCount(); i++) {
                    header.createCell(i).setCellValue(model.getColumnName(i));
                }

                // Ghi dữ liệu
                for (int i = 0; i < model.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        Object value = model.getValueAt(i, j);
                        row.createCell(j).setCellValue(value != null ? value.toString() : "");
                    }
                }

                FileOutputStream out = new FileOutputStream(fileToSave);
                workbook.write(out);
                out.close();

                JOptionPane.showMessageDialog(null, "Xuất Excel thành công!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi khi xuất Excel: " + ex.getMessage());
            }
        }
    }
}
