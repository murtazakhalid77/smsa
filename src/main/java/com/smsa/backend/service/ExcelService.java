package com.smsa.backend.service;


import com.smsa.backend.security.util.ExcelHelper;
import org.hibernate.cache.spi.access.CachedDomainDataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



@Service
public class ExcelService {
    @Autowired
    ExcelHelper excelHelper;
    private String origin="";
    private Double awbsCount=0.0;
    private Double weight=0.0;
    private Double devalaredValueSAR=0.0;
    private Double vatAmount=0.0;
    private Double documentation=0.0;
    private Double sar=0.0;
    private Double smsaFeesCharges=0.0;
    private Double usDollar=0.0;
    List<List<String>> pdfRows = new ArrayList<>();
    public  void filterRowsByTwoValues(MultipartFile multipartFile, int firstColumnIndex,String accountNumber) {
        List<List<String>> rowsToBeFiltered = excelHelper.parseExcelFile(multipartFile);

        List<List<String>> accountNumberFilterdRows = new ArrayList<>();

        for (List<String> row : rowsToBeFiltered) {
            if (row.size() > firstColumnIndex) {
                String firstColumnValue = row.get(firstColumnIndex);
                if (accountNumber.equals(firstColumnValue)) {
                    accountNumberFilterdRows.add(row);
                }
            }
        }

        Set<String> mawbNumber = uniqueMawb(accountNumberFilterdRows);
        for (String mawb : mawbNumber){
            fiterValuesbyMawb(accountNumberFilterdRows,0,mawb);
        }
    }

    public  void  fiterValuesbyMawb(List<List<String>> rowsToBeFilteredByMawb, int secondColumnIndex, String mawb) {

        List<String> pdfRow= new ArrayList<>();
        for (List<String> row : rowsToBeFilteredByMawb) {
            if (row.size() > secondColumnIndex) {
                String secondColumnValue = row.get(secondColumnIndex);
                if (mawb.equals(secondColumnValue)) {
                    weight+=Double.parseDouble(row.get(9));
                    awbsCount+=1;
                    devalaredValueSAR+=Double.parseDouble(row.get(10));
                    vatAmount+=Double.parseDouble(row.get(11));
                    documentation+=Double.parseDouble(row.get(12));


                }
            }
        }
        pdfRow.add(mawb);
        pdfRow.add(String.valueOf(weight));
        pdfRow.add(String.valueOf(awbsCount));
        pdfRow.add(String.valueOf(devalaredValueSAR));
        pdfRow.add(String.valueOf(vatAmount));
        pdfRow.add(String.valueOf(documentation));

        pdfRows.add(pdfRow);
        pdfRow.clear();

        awbsCount=0.0;
        weight=0.0;
        devalaredValueSAR=0.0;
        vatAmount=0.0;
        documentation=0.0;

    }

    private Set<String> uniqueMawb(List<List<String>> filteredRows) {
        Set<String> uniqueMawb = new HashSet<>();
        for(List<String> row: filteredRows){
            uniqueMawb.add(row.get(0));
        }
        return uniqueMawb;
    }
    public static void printFilteredRows(List<List<String>> filteredRows) {
        for (List<String> row : filteredRows) {
            for (String cellValue : row) {
                System.out.print(cellValue + "\t");
            }
            System.out.println(); // Move to the next line after printing each row
        }
    }

}
