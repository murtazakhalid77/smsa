package com.smsa.backend.repository;

import com.smsa.backend.controller.CustomController;
import com.smsa.backend.dto.CustomDto;
import com.smsa.backend.model.Custom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomRepository extends JpaRepository<Custom, Long>, JpaSpecificationExecutor<Custom> {
    Optional<Custom> findByCustom(String customName);
    @Query(value = "select new com.smsa.backend.dto.CustomDto(c.id, c.customPort, c.custom, c.smsaFeeVat, c.currency, c.isPresent) from Custom c where c.isPresent = true")
    List<CustomDto> findByIsPresent();

    @Query(value = "SELECT Distinct c.currency " +
            "FROM Custom c " +
            "WHERE c.currency IS NOT NULL " +
            "ORDER BY c.currency")
    List<String> getDistinctCurrencies();

}
