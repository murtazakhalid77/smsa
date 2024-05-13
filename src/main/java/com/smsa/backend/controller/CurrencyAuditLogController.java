package com.smsa.backend.controller;

import com.smsa.backend.model.CurrencyAuditLog;
import com.smsa.backend.service.CurrencyAuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class CurrencyAuditLogController {

    @Autowired
    CurrencyAuditLogService currencyAuditLogService;

    @GetMapping("/currency-audit-log/{currencyId}")
    ResponseEntity<List<CurrencyAuditLog>> getAllCurrency(@PathVariable Long currencyId, Pageable pageable){
        Page<CurrencyAuditLog> currencyAuditLogs = this.currencyAuditLogService.getCurrencyAuditLogById(currencyId, pageable);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(currencyAuditLogs.getTotalElements()));
        return ResponseEntity.ok()
                .headers(headers)// Set the headers
                .body(currencyAuditLogs.getContent());
    }
}
