package com.smsa.backend.repository;

import com.smsa.backend.model.SalesReport;
import com.smsa.backend.model.SalesReportAwb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesReportAwbRepository extends JpaRepository<SalesReportAwb, Long> {


}
