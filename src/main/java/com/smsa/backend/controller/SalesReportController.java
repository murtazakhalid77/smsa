package com.smsa.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smsa.backend.criteria.SearchCriteria;
import com.smsa.backend.dto.SalesReportDto;
import com.smsa.backend.dto.SearchSalesReportDto;
import com.smsa.backend.service.SalesReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    ResponseEntity<List<SalesReportDto>> getSalesReport(@RequestBody SearchSalesReportDto searchSalesReportDto){
        return ResponseEntity.ok(this.salesReportService.getSalesReport(searchSalesReportDto,  PageRequest.of(searchSalesReportDto.getPage(), searchSalesReportDto.getSize())));
    }
}
