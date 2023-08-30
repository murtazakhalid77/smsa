package com.smsa.backend.controller;

import com.smsa.backend.dto.CurrencyAuditLogDto;
import com.smsa.backend.dto.CurrencyDto;
import com.smsa.backend.model.CurrencyAuditLog;
import com.smsa.backend.service.CurrencyAuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
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
    ResponseEntity<List<CurrencyAuditLogDto>> getAllCurrency(@PathVariable Long currencyId){
        return ResponseEntity.ok(this.currencyAuditLogService.getCurrencyAuditLogById(currencyId));
    }
}
