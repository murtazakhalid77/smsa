package com.smsa.backend.security.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
@Component
public class ExcelMaker {
    public static void cleanExcel(String filePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        fileInputStream.close();

        // Process the first sheet
        Sheet firstSheet = workbook.getSheetAt(0);
        overwriteSpecificCells(firstSheet, 2, 9);
        overwriteSpecificCells(firstSheet, 3, 1);
        overwriteSpecificCells(firstSheet, 3, 9);
        overwriteSpecificCells(firstSheet, 4, 1);
        overwriteSpecificCells(firstSheet, 4, 9);
        clearRows(firstSheet, 6);


            Sheet secondSheet = workbook.getSheetAt(1);
            clearRows(secondSheet, 1);


        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        workbook.close();
    }

    private static void overwriteSpecificCells(Sheet sheet, int rowIndex, int cellIndex) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        Cell cell = row.getCell(cellIndex);
        if (cell == null) {
            cell = row.createCell(cellIndex);
        }
        // Set the value you want to overwrite here (e.g., "xxxx")
        cell.setCellValue("XXXX");
    }

    private static void clearRows(Sheet sheet, int startRowToClear) {
        // Clear rows after the specified row index
        for (int rowIndex = startRowToClear; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                sheet.removeRow(row);
            }
        }
    }
}
