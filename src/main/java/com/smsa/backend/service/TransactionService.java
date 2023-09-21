package com.smsa.backend.service;

import com.smsa.backend.Exception.RecordNotFoundException;
import com.smsa.backend.model.SheetHistory;
import com.smsa.backend.model.Transaction;
import com.smsa.backend.repository.SheetHistoryRepository;
import com.smsa.backend.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    public Page<Transaction> getAllTransaction(Pageable pageable,String id){
        Page<Transaction> transactions= transactionRepository.findAllBySheetId(pageable,id);
        if (!transactions.isEmpty()) {
            return  transactions;
        }
        throw new RecordNotFoundException(String.format("transaction not found"));
    }
}
