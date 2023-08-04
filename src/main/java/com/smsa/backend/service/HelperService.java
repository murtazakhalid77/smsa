package com.smsa.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smsa.backend.dto.ExcelImportDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class HelperService {
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
}
