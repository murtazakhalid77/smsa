package com.smsa.backend.repository;

import com.smsa.backend.dto.CurrencyDto;
import com.smsa.backend.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency,Long> {

    List<CurrencyDto> findAllByIsPresent(Boolean isActive);

    Optional<Currency> findByCurrencyFromAndCurrencyTo(String currencyFrom, String currencyTo);
}
