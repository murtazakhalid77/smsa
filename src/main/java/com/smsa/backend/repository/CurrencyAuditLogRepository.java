package com.smsa.backend.repository;

import com.smsa.backend.dto.CurrencyAuditLogDto;
import com.smsa.backend.model.Currency;
import com.smsa.backend.model.CurrencyAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyAuditLogRepository extends JpaRepository<CurrencyAuditLog,Long> {
    List<CurrencyAuditLog> findCurrencyAuditByCurrencyId(Long currencyId);
}
