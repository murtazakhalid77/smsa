package com.smsa.backend.controller;

import com.smsa.backend.model.SheetHistory;
import com.smsa.backend.service.SheetHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class SheetHistoryController {
    @Autowired
    SheetHistoryService sheetHistoryService;
    @GetMapping("/sheetHistory")
    private ResponseEntity<List<SheetHistory>> getAllSheetHistory(Pageable pageable){
       Page<SheetHistory> sheetHistories= sheetHistoryService.getAllSheetHistory(pageable);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(sheetHistories.getTotalElements()));
        return ResponseEntity.ok()
                .headers(headers)// Set the headers
                .body(sheetHistories.getContent());
    }
}

