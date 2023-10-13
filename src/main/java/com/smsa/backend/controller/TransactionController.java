package com.smsa.backend.controller;

import com.smsa.backend.model.SheetHistory;
import com.smsa.backend.model.Transaction;
import com.smsa.backend.service.SheetHistoryService;
import com.smsa.backend.service.StorageService;
import com.smsa.backend.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @Autowired
    StorageService storageService;
    @GetMapping("/transaction/{id}")
    private ResponseEntity<List<Transaction>> getAllTransaction(Pageable pageable, @PathVariable String id){
        Page<Transaction> sheetHistories= transactionService.getAllTransaction(pageable,id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(sheetHistories.getTotalElements()));
        return ResponseEntity.ok()
                .headers(headers)// Set the headers
                .body(sheetHistories.getContent());
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) {
        byte[] data = storageService.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @DeleteMapping("/transaction/delete/invoiceDetails/{accountNumber}/{sheetId}")
    public ResponseEntity<Void> deleteInvoiceDetails(
            @PathVariable String accountNumber,
            @PathVariable String sheetId
    ) {
        transactionService.deleteInvoiceData(accountNumber, sheetId);
        return ResponseEntity.accepted().build();
    }

}
