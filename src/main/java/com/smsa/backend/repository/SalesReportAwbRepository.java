package com.smsa.backend.repository;

import com.smsa.backend.model.SalesReportAwb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesReportAwbRepository extends JpaRepository<SalesReportAwb, Long> {


}
