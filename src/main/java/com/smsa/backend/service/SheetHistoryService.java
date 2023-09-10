package com.smsa.backend.service;

import com.smsa.backend.Exception.RecordNotFoundException;
import com.smsa.backend.model.Region;
import com.smsa.backend.model.SheetHistory;
import com.smsa.backend.repository.SheetHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class SheetHistoryService {
    @Autowired
    SheetHistoryRepository sheetHistoryRespository;

    public Page<SheetHistory> getAllSheetHistory(Pageable pageable){
        Page<SheetHistory> sheetHistory= sheetHistoryRespository.findAll(pageable);
        if (!sheetHistory.isEmpty()) {
            return  sheetHistory;
        }
        throw new RecordNotFoundException(String.format("sheetHistory not found"));
    }


}
