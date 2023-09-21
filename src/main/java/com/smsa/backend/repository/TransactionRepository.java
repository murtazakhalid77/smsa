package com.smsa.backend.repository;

import com.smsa.backend.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    Optional<Transaction> findByAccountNumberAndSheetId(String accountNumber,String sheetId);
    Page<Transaction> findAllBySheetId(Pageable pageable,String sheetId);
}
