package com.smsa.backend.controller;

import com.smsa.backend.dto.SearchSalesReportDto;
import com.smsa.backend.model.SalesReport;
import com.smsa.backend.service.SalesReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class SalesReportController {

    @Autowired
    SalesReportService salesReportService;

    @PostMapping("/sales-report")
    ResponseEntity<List<SalesReport>> getSalesReport(@RequestBody SearchSalesReportDto searchSalesReportDto){
        List<SalesReport> salesReports = this.salesReportService.getSalesReport(searchSalesReportDto);
        HttpHeaders headers = new HttpHeaders();

        return ResponseEntity.ok()
                .headers(headers)// Set the headers
                .body(salesReports);
    }

//    @GetMapping("/sales-report-awb")
//    ResponseEntity<List<SalesReport>> getSalesReportByAwbs(@RequestParam List<String> awbs){
//        return ResponseEntity.ok(this.salesReportService.getSalesReportByAwbs(awbs));
//    }

}
