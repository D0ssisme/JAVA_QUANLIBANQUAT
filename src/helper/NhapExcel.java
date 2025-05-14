package helper;
import DTO.QuatDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NhapExcel {
    public static List<QuatDTO> nhapDanhSachQuatTuExcel(String excelFilePath) {
        List<QuatDTO> danhSachQuat = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(excelFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Sheet đầu tiên
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

               String maQuat = getCellValueAsString(row.getCell(0));
                String tenQuat = getCellValueAsString(row.getCell(1));
                int gia = Integer.parseInt(getCellValueAsString(row.getCell(2)));
                int soLuongTon = Integer.parseInt(getCellValueAsString(row.getCell(3)));
                String maNSX = getCellValueAsString(row.getCell(4));
                String ngaySanXuat = getCellValueAsString(row.getCell(5));
                String chatLieu = getCellValueAsString(row.getCell(6));
                String thuongHieu = getCellValueAsString(row.getCell(7));
                String maLoaiSP = getCellValueAsString(row.getCell(8));


                QuatDTO quat = new QuatDTO(maQuat, tenQuat, gia, soLuongTon, maNSX,
                        ngaySanXuat, chatLieu, thuongHieu, maLoaiSP);
                danhSachQuat.add(quat);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return danhSachQuat;
    }
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new java.text.SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
                } else {
                    return String.valueOf((long) cell.getNumericCellValue()); // dùng (long) để loại bỏ .0
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return cell.toString().trim();
        }
    }

}
