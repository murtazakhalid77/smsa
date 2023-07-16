package com.smsa.backend.security.util;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = {"Id", "Title", "Description", "Published"};
    static String SHEET = "Tutorials";

    public static boolean hasExcelFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static List<String> exceltoList(InputStream is) {
//        try {
//            Workbook workbook = new XSSFWorkbook(is);
//
//            Sheet sheet = workbook.getSheet(SHEET);
//            Iterator<Row> rows = sheet.iterator();
//
//            List<String> allRows = new ArrayList<String>();
//
//            int rowNumber = 0;
//            while (rows.hasNext()) {
//                Row currentRow = rows.next();
//
//                // skip header
//                if (rowNumber == 0) {
//                    rowNumber++;
//                    continue;
//                }
//
//                Iterator<Cell> cellsInRow = currentRow.iterator();
//
//                int cellIdx = 0;
//                while (cellsInRow.hasNext()) {
//                    Cell currentCell = cellsInRow.next();
//                    allRows.add(currentCell);
//
//                    cellIdx++;
//                }
//
//
//            }
//
//            workbook.close();
//        } catch (IOException e) {
//            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());

        return null;
    }


    public static List<List<String>> parseExcelFile(MultipartFile file) {
        List<List<String>> rows = new ArrayList<>();

        try (InputStream is = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheetAt(0); // Assuming you want to parse the first sheet

            DataFormatter dataFormatter = new DataFormatter();
            for (Row row : sheet) {
                List<String> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    String cellValue = dataFormatter.formatCellValue(cell);
                    rowData.add(cellValue);
                }
                rows.add(rowData);
            }

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rows;
    }

            private static String getCellValueAsString (Cell cell){
                if (cell.getCellType() == CellType.STRING) {
                    return cell.getStringCellValue();
                } else if (cell.getCellType() == CellType.NUMERIC) {
                    return String.valueOf(cell.getNumericCellValue());
                } else if (cell.getCellType() == CellType.BOOLEAN) {
                    return String.valueOf(cell.getBooleanCellValue());
                } else if (cell.getCellType() == CellType.FORMULA) {
                    return cell.getCellFormula();
                } else {
                    return "";
                }
            }
        }

