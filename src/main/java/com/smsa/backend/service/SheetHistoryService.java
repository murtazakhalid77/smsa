package com.smsa.backend.service;

import com.smsa.backend.Exception.RecordNotFoundException;
import com.smsa.backend.model.Region;
import com.smsa.backend.model.SheetHistory;
import com.smsa.backend.repository.InvoiceDetailsRepository;
import com.smsa.backend.repository.SheetHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class SheetHistoryService {
    @Autowired
    SheetHistoryRepository sheetHistoryRespository;

    @Autowired
    InvoiceDetailsRepository invoiceDetailsRepository;

    public Page<SheetHistory> getAllSheetHistory(Pageable pageable){
        Page<SheetHistory> sheetHistory= sheetHistoryRespository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),  Sort.by("id").descending()));
     return sheetHistory;
    }
    public  void deleteSheetHistory(String sheetId){
        sheetHistoryRespository.deleteByUniqueUUid(sheetId);
        invoiceDetailsRepository.deleteBySheetUniqueId(sheetId);
    }


}
