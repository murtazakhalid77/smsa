package com.smsa.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smsa.backend.dto.ExcelImportDto;
import com.smsa.backend.model.SheetHistory;
import com.smsa.backend.repository.SheetHistoryRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

@Service
public class HelperService {
    @Autowired
    SheetHistoryRepository sheetHistoryRepository;
    public ExcelImportDto convertExcelImportIntoDto(String excelImport) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        try {
            ExcelImportDto excelImportDto = objectMapper.readValue(excelImport, ExcelImportDto.class);

            if (excelImportDto.getDate1() != null) {
                excelImportDto.setFormattedStartDate(convertInToLocalDate(excelImportDto.getDate1()));
            }

            if (excelImportDto.getDate2() != null) {
                excelImportDto.setFormattedEndDate(convertInToLocalDate(excelImportDto.getDate2()));
            }
            if (excelImportDto.getDate3() != null) {
                excelImportDto.setInvoiceDate(convertInToLocalDate(excelImportDto.getDate3()));
            }

            return excelImportDto;
        } catch (Exception e) {
            throw new RuntimeException("There was an issue when parsing date");
        }
    }

    public String convertInToLocalDate(String date){
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDate localDate = LocalDate.parse(date, inputFormatter);

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH);
        return localDate.format(outputFormatter);
    }

    public LocalDate convertStringInToLocalDate(String date){
        if(date.length() > 0){
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(date, inputFormatter);
            return localDate;
        }
        return null;
    }

    public String generateInvoiceDatePeriod(String sheetUniqueId) {
        SheetHistory history = getSheetHistory(sheetUniqueId);
        String startDate = history.getStartDate() != null ? history.getStartDate().toString() : "";
        String endDate = history.getEndDate() != null ? history.getEndDate().toString() : "";
        return startDate + "-" + endDate;
    }
    public SheetHistory getSheetHistory(String sheetUniqueUUid){
        return  sheetHistoryRepository.findByUniqueUUid(sheetUniqueUUid);
    }
    public String generateInvoiceDate(String sheetUniqueId){
        return getSheetHistory(sheetUniqueId).getInvoiceDate();
    }

    public static String getLoggedInUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return currentUserName;
        }else{
            return null;
        }
    }

    public  int findStartingRow(Sheet sheet) {
        DataFormatter dataFormatter = new DataFormatter();
        int lastRowIndex = sheet.getLastRowNum();

        for (int rowIndex = lastRowIndex; rowIndex >= 0; rowIndex--) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                boolean isRowEmpty = true;
                for (Cell cell : row) {
                    String cellValue = dataFormatter.formatCellValue(cell);
                    if (cellValue != null && !cellValue.trim().isEmpty()) {
                        isRowEmpty = false;
                        break;
                    }
                }

                if (!isRowEmpty) {
                    return rowIndex + 1;
                }
            }
        }

        return 0; // If no non-empty row is found, start from the first row
    }
    public static boolean checkValidEmail(String[] emailList) {
        for (String mail : emailList) {
            if (mail == null || !mail.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+)$")) {
                return false; // Return false if any email is invalid or null
            }
        }
        return true; // Return true if all emails are valid
    }

}
