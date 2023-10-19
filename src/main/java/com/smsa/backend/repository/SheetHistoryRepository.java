package com.smsa.backend.repository;

import com.smsa.backend.model.SheetHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SheetHistoryRepository extends JpaRepository<SheetHistory,Long> {
    boolean existsByName(String originalFilename);
    @Query("SELECT sh FROM SheetHistory sh WHERE sh.isEmailSent = :isEmailSent")
    List<SheetHistory> findAllByIsEmail(@Param("isEmailSent") Boolean isEmailSent);


    SheetHistory findByUniqueUUid(String sheetUniqueId);

    @Transactional
    @Modifying
    void deleteByUniqueUUid(String uuid);
}
