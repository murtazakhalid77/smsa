package com.smsa.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smsa.backend.dto.CustomDto;
import com.smsa.backend.dto.ExcelImportDto;
import com.smsa.backend.model.Custom;
import com.smsa.backend.model.InvoiceDetails;

import com.smsa.backend.security.util.ExcelImportHelper;
import com.smsa.backend.service.ExcelService;
import com.smsa.backend.service.HelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.jws.Oneway;
import java.io.IOException;
import java.util.*;


@RestController
@RequestMapping("/api/excel")
@CrossOrigin("*")
public class ExcelController {
    @Autowired
    ExcelImportHelper excelImportHelper;
    @Autowired
    ExcelService excelService;
    @Autowired
    HelperService helperService;

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("excelImport") String excelImport) {
        Map<String, Object> response = new HashMap<>();
        Set<String> accountNumbers = new HashSet<>();
        String message = "";

        ExcelImportDto excelImportDto1 = this.helperService.convertExcelImportIntoDto(excelImport);


        if (excelImportHelper.hasExcelFormat(file)) {
            try {
                excelService.saveInvoicesToDatabase(file, excelImportDto1.getCustom());

                for (InvoiceDetails invoiceDetails : excelService.getInvoicesWithoutAccount()) {
                    accountNumbers.add(invoiceDetails.getInvoiceDetailsId().getAccountNumber());
                }

                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                response.put("message", message);
                response.put("accountNumbers", accountNumbers);

                return ResponseEntity.ok(response);
            } catch (Exception e) {
                message = e.getMessage();
                response.put("message", message);
                return ResponseEntity.ok(response);
            }

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
