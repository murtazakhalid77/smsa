package com.smsa.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smsa.backend.dto.ExcelImportDto;
import com.smsa.backend.model.SheetHistory;
import com.smsa.backend.repository.SheetHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class HelperService {
    @Autowired
    SheetHistoryRepository sheetHistoryRepository;
    public ExcelImportDto convertExcelImportIntoDto(String excelImport) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        ExcelImportDto excelImportDto1;
        try {
            excelImportDto1 = objectMapper.readValue(excelImport, ExcelImportDto.class);
            excelImportDto1.setStartDate(excelImportDto1.getDate1() != null ? convertInToLocalDate(excelImportDto1.getDate1()) : null);
            excelImportDto1.setEndDate(excelImportDto1.getDate2() != null ? convertInToLocalDate(excelImportDto1.getDate2()) : null);
            return excelImportDto1;
        }catch (Exception e){
            throw new RuntimeException("There was an issue when parsing date");
        }
    }

    private LocalDate convertInToLocalDate(String date){

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDate localDate = LocalDate.parse(date, formatter);
        System.out.println("LocalDate: " + localDate);
        return localDate;
    }
    public String generateInvoiceDate(String sheetUniqueId) {
        SheetHistory history = getSheetHistory(sheetUniqueId);
        String startDate = history.getStartDate() != null ? history.getStartDate().toString() : "";
        String endDate = history.getEndDate() != null ? history.getEndDate().toString() : "";
        return startDate + "-" + endDate;
    }
    public SheetHistory getSheetHistory(String sheetUniqueUUid){
        return  sheetHistoryRepository.findByUniqueUUid(sheetUniqueUUid);
    }
}
