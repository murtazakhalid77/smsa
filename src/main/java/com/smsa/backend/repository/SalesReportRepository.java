package com.smsa.backend.repository;

import com.smsa.backend.dto.SalesReportDto;
import com.smsa.backend.model.SalesReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalesReportRepository extends JpaRepository<SalesReport,Long>, JpaSpecificationExecutor<SalesReport> {

    List<SalesReport> findByIdBetween(Long saleIdStart, Long saleIdEnd);
    List<SalesReport> findByInvoiceNumberBetween(String invoiceTo, String invoiceFrom);

    List<SalesReport> findAllByCreatedAtBetween(LocalDate startDate, LocalDate endDate);

    List<SalesReport> findAllByIdIn(List<Long> salesReportIds);
    @Query(value = "SELECT sr.* " +
            "FROM sales_report sr " +
            "INNER JOIN sales_report_awb sra ON sr.id = sra.sales_report_id " +
            "WHERE sra.awb IN :awbs", nativeQuery = true)
    List<SalesReport> getSalesReportByAwbs(@Param("awbs") List<String> awbs);
}
