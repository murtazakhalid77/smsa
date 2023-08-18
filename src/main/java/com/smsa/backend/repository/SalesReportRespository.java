package com.smsa.backend.repository;

import com.smsa.backend.model.SalesReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesReportRespository  extends JpaRepository<SalesReport,Long> {

}
