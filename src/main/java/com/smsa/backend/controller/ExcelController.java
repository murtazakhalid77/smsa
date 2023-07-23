package com.smsa.backend.controller;

import com.smsa.backend.model.InvoiceDetails;
import com.smsa.backend.security.util.ExcelHelper;
import com.smsa.backend.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


@RestController
@RequestMapping("/api/excel")
@CrossOrigin("*")
public class ExcelController {
    @Autowired
    ExcelService fileService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        Set<String> accountNumbers = new HashSet<>();
        String message = "";

        if (ExcelHelper.hasExcelFormat(file)) {
            try {
                fileService.saveInvoicesToDatabase(file);

                for (InvoiceDetails invoiceDetails : fileService.getInvoicesWithoutAccount()) {
                    accountNumbers.add(invoiceDetails.getInvoiceDetailsId().getAccountNumber());
                }

                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                response.put("message", message);
                response.put("accountNumbers", accountNumbers);

                return ResponseEntity.ok(response);
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                e.printStackTrace();
                response.put("message", message);
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
            }
        }

        message = "Invalid file format! Please upload an Excel file.";
        response.put("message", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
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
