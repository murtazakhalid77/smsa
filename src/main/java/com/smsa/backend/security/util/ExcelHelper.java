package com.smsa.backend.security.util;


import com.smsa.backend.dto.InvoiceDetailsDto;
import com.smsa.backend.model.InvoiceDetails;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

@Service
public class ExcelHelper {
    public static boolean hasExcelFormat(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return false;
        }

        // Convert the file name to lowercase to ensure case-insensitivity
        String lowercaseFilename = originalFilename.toLowerCase();

        // Check if the file has an Excel extension
        return lowercaseFilename.endsWith(".xlsx");
    }




    public static List<List<String>> parseExcelFile(MultipartFile file) {
        List<List<String>> rows = new ArrayList<>();

        try (InputStream is = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(is);

            Sheet sheet = workbook.getSheetAt(0); // Assuming you want to parse the first sheet

            for (Row row : sheet) {
                List<String> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    String cellValue = getCellValueAsString(cell);
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

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        } else if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            // Handle numeric values without scientific notation
            if (DateUtil.isCellDateFormatted(cell)) {
                return formatDateCellValue(cell);
            } else {
                return formatNumericCellValue(cell);
            }
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == CellType.FORMULA) {
            return cell.getCellFormula();
        } else {
            return "";
        }
    }

    private static String formatDateCellValue(Cell cell) {
        LocalDate dateValue = cell.getLocalDateTimeCellValue().toLocalDate();
        return dateValue.format(DateTimeFormatter.ofPattern("dd-MMM-yy", Locale.ENGLISH));
    }
    private static String formatNumericCellValue(Cell cell) {
        // Use DecimalFormat to format numeric cell values without scientific notation
        DecimalFormat decimalFormat = new DecimalFormat("#0.#####"); // Adjust the pattern as needed
        return decimalFormat.format(cell.getNumericCellValue());
    }

        }

