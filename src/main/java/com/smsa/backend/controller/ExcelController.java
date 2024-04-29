package com.smsa.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smsa.backend.dto.ExcelImportDto;
import com.smsa.backend.model.InvoiceDetails;

import com.smsa.backend.repository.SalesReportRepository;
import com.smsa.backend.security.util.ExcelImportHelper;
import com.smsa.backend.service.ExcelService;
import com.smsa.backend.service.HelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    SalesReportRepository salesReportRepository;
    @Autowired
    HelperService helperService;

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("excelImport") String excelImport,@RequestParam("userInput") String userInput) throws Exception {

        Map<String, Object> response = new HashMap<>();
        Set<String> accountNumbers = new HashSet<>();
        String message = "";

            ExcelImportDto excelImportDto1 = this.helperService.convertExcelImportIntoDto(excelImport);
        Map<String, String> userInputMap=null;
        if (ExcelImportHelper.hasExcelFormat(file)) {
            if(excelImportDto1.isCustomPlusExcel()){
                if("null".equals(userInput)){
                  return  ResponseEntity.ok(excelService.extractUniqueFromTheExcel(file));
                }
                ObjectMapper objectMapper = new ObjectMapper();
                userInputMap = objectMapper.readValue(userInput, Map.class);


                HashMap<String,List<InvoiceDetails>> invoices= excelService.saveInvoicesToDatabase(file,excelImportDto1,userInputMap);

                for (InvoiceDetails invoiceDetails : invoices.get("invoicesWithoutAccount")) {
                    accountNumbers.add(invoiceDetails.getInvoiceDetailsId().getAccountNumber());
                }


                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                response.put("message", message);
                response.put("accountNumbers", accountNumbers);

                return ResponseEntity.ok(response);
            }

             HashMap<String,List<InvoiceDetails>> invoices= excelService.saveInvoicesToDatabase(file,excelImportDto1,userInputMap);

            for (InvoiceDetails invoiceDetails : invoices.get("invoicesWithoutAccount")) {
                accountNumbers.add(invoiceDetails.getInvoiceDetailsId().getAccountNumber());
            }


            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            response.put("message", message);
            response.put("accountNumbers", accountNumbers);

            return ResponseEntity.ok(response);


        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @GetMapping("/export/excel")
    public ResponseEntity<Resource> exportToExcel(@RequestParam List<Long> salesReportIds) throws IOException {

             Resource file = excelService.salesReportExcel(salesReportIds);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping("/export/manifest-data")
    public ResponseEntity<Resource> exportToExcelManifestData(@RequestParam List<Long> manifestDataIds) throws IOException {

        Resource file = excelService.manifestDataExcel(manifestDataIds);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping("/customer")
    public ResponseEntity<Resource> customerExcelDownload() throws IOException {

        Resource file = excelService.customerExcelDownload();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping("/currency")
    public ResponseEntity<Resource> currencyExcelDownload() throws IOException {

        Resource file = excelService.currencyExcelDownload();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping("/user")
    public ResponseEntity<Resource> userExcelDownload() throws IOException {

        Resource file = excelService.userExcelDownload();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping("/custom")
    public ResponseEntity<Resource> customExcelDownload() throws IOException {

        Resource file = excelService.customExcelDownload();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping("/region")
    public ResponseEntity<Resource> regionExcelDownload() throws IOException {

        Resource file = excelService.regionExcelDownload();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping("/currency-audit/{id}")
    public ResponseEntity<Resource> currencyAuditDownload(@PathVariable Long id) throws IOException {

        Resource file = excelService.currencyAuditDownload(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

}
