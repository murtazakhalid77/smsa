package com.smsa.backend.repository;

import com.smsa.backend.model.SheetHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SheetHistoryRepository extends JpaRepository<SheetHistory,Long> {
    boolean existsByName(String originalFilename);
    List<SheetHistory> findAllByIsEmailSentFalse();

    SheetHistory findByUniqueUUid(String sheetUniqueId);
}
