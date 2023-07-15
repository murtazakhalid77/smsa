package com.smsa.backend.service;


import com.smsa.backend.security.util.ExcelHelper;
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

    public  List<List<String>> filterRowsByValues(MultipartFile multipartFile, List<String> filterValues) {
        List<List<String>> rowsToBeFiltered = excelHelper.parseExcelFile(multipartFile);
        List<List<String>> filteredRows = new ArrayList<>(rowsToBeFiltered); // Start with all rows

        for (String filterValue : filterValues) {
            Set<String> filterSet = new HashSet<>();
            filterSet.add(filterValue);
            List<List<String>> tempFilteredRows = new ArrayList<>();

            for (List<String> row : filteredRows) {
                for (String cellValue : row) {
                    if (filterSet.contains(cellValue)) {
                        tempFilteredRows.add(row);
                        break;
                    }
                }
            }

            filteredRows = tempFilteredRows; // updating the filtered rows for mawb filteration
        }

        return filteredRows;
    }




}
