package com.smsa.backend.repository;

import com.smsa.backend.dto.SalesReportDto;
import com.smsa.backend.model.SalesReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalesReportRepository extends JpaRepository<SalesReport,Long>, JpaSpecificationExecutor<SalesReport> {

    List<SalesReport> findByIdBetween(Long saleIdStart, Long saleIdEnd);
    Page<SalesReport> findByInvoiceNumberBetween(String invoiceTo, String invoiceFrom, Pageable pageable);

    Page<SalesReport> findAllByCreatedAtBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    List<SalesReport> findAllByIdIn(List<Long> salesReportIds);
}
