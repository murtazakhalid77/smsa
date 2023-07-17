package com.smsa.backend.service;


import com.smsa.backend.security.util.ExcelHelper;
import org.hibernate.cache.spi.access.CachedDomainDataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


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
    public  Map<String, List<List<String>>> filterRowsByCommonKey(MultipartFile multipartFile) {
        List<List<String>> rowsToBeFiltered = excelHelper.parseExcelFile(multipartFile);

        Map<String, List<List<String>>> filteredRowsMap = new HashMap<>();

        List<String> uniqueKeys = new ArrayList<>();

        for (List<String> row : rowsToBeFiltered) {
            if (row.size() > 23) {
                String commonKey = row.get(23);
                if (!commonKey.isEmpty() && !uniqueKeys.contains(commonKey)) {
                    uniqueKeys.add(commonKey);
                }
            }
        }

        for (String key : uniqueKeys) {
            List<List<String>> rowsForKey = new ArrayList<>();
            for (List<String> row : rowsToBeFiltered) {
                if (row.size() > 23 && key.equals(row.get(23))) {
                    rowsForKey.add(row);
                }
            }
            filteredRowsMap.put(key, rowsForKey);
        }

        return filteredRowsMap;
    }
    public  Map<String, Map<String, List<List<String>>>> filterRowsByCommonKeyAndMawbNumber(MultipartFile multipartFile) {

        List<List<String>> rowsToBeFiltered = excelHelper.parseExcelFile(multipartFile);
        Map<String, Map<String, List<List<String>>>> filteredRowsMap = new HashMap<>();

        for (List<String> row : rowsToBeFiltered) {
            if (row.size() > 23 && row.size() > 0) {
                String commonKey = row.get(23);
                String mawbNumber = row.get(0);

                if (!commonKey.isEmpty()) {
                    filteredRowsMap.putIfAbsent(commonKey, new HashMap<>());

                    Map<String, List<List<String>>> mawbFilteredRowsMap = filteredRowsMap.get(commonKey);
                    mawbFilteredRowsMap.putIfAbsent(mawbNumber, new ArrayList<>());
                    mawbFilteredRowsMap.get(mawbNumber).add(row);
                }
            }
        }
        printHashMap(filteredRowsMap);
        return filteredRowsMap;
    }


    public void printHashMap(Map<String, Map<String, List<List<String>>>> filteredRowsMap){
        for (Map.Entry<String, Map<String, List<List<String>>>> accountEntry : filteredRowsMap.entrySet()) {
            String accountNumber = accountEntry.getKey();
            Map<String, List<List<String>>> mawbFilteredRowsMap = accountEntry.getValue();

            System.out.println("Account Number: " + accountNumber);

            for (Map.Entry<String, List<List<String>>> mawbEntry : mawbFilteredRowsMap.entrySet()) {
                String mawbNumber = mawbEntry.getKey();
                List<List<String>> rowsForMawb = mawbEntry.getValue();

                System.out.println("MAWB Number: " + mawbNumber);

                for (List<String> row : rowsForMawb) {
                    // Print each row
                    for (String cellValue : row) {
                        System.out.print(cellValue + "\t");
                    }
                    System.out.println(); // Newline after printing each row
                }
                System.out.println(); // Empty line between each group of rows with the same MAWB number
            }
            System.out.println(); // Empty line between each group of rows with the same account number
        }
    }
}



