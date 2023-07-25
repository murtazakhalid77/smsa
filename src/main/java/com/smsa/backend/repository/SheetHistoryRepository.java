package com.smsa.backend.repository;

import com.smsa.backend.model.SheetHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SheetHistoryRepository extends JpaRepository<SheetHistory,Long> {
    boolean existsByUniqueUUidAndName(String sheetId, String originalFilename);
}
