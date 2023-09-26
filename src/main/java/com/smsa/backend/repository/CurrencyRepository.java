package com.smsa.backend.repository;

import com.smsa.backend.dto.CurrencyDto;
import com.smsa.backend.model.Currency;
import com.smsa.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency,Long>, JpaSpecificationExecutor<Currency> {

    List<CurrencyDto> findAllByIsPresent(Boolean isActive);

    Optional<Currency> findByCurrencyFromAndCurrencyTo(String currencyFrom, String currencyTo);

    @Query(value = "Select c from Currency c where c.currencyTo = ?1 and c.currencyFrom = ?2")
    Currency findCurrencyBYCurrencyToFrom(String currencyTo, String currencyFrom);
}
