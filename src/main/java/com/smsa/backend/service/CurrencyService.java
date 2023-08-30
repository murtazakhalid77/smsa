package com.smsa.backend.service;

import com.smsa.backend.Exception.RecordAlreadyExistException;
import com.smsa.backend.Exception.RecordNotFoundException;
import com.smsa.backend.dto.CurrencyDto;
import com.smsa.backend.model.Currency;
import com.smsa.backend.model.CurrencyAuditLog;
import com.smsa.backend.model.Custom;
import com.smsa.backend.model.Customer;
import com.smsa.backend.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class CurrencyService {
    @Autowired
    CurrencyRepository currencyRepository;
    @Autowired
    CustomService customService;
    @Autowired
    CurrencyAuditLogService currencyAuditLogService;
    public CurrencyDto addCurrency(CurrencyDto currencyDto) {
        Currency currency = this.currencyRepository.findCurrencyBYCurrencyToFrom(currencyDto.getCurrencyTo(), currencyDto.getCurrencyFrom());
        if(currency == null){
            currencyDto.setCreatedBy(HelperService.getLoggedInUserName());
            currencyDto.setCreatedAt(LocalDate.now().toString());
            return toDto(this.currencyRepository.save(toDomain(currencyDto)));
        }else{
            throw new RecordAlreadyExistException("Entry against currency to and currency from exist");
        }
    }

    public List<CurrencyDto> getAllCurrency() {
        List<CurrencyDto> currencyDtos = new ArrayList<>();

        List<Currency> currencies = this.currencyRepository.findAll();

        for(Currency currency: currencies){
            currencyDtos.add(toDto(currency));
        }

        return currencyDtos;
    }

    public List<CurrencyDto> getAllCurrencyForImport() {
        try {
            List<CurrencyDto> currencyDtos = this.currencyRepository.findAllByIsPresent(Boolean.TRUE);
            return currencyDtos;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public CurrencyDto getCurrencyById(Long id) {
        Optional<Currency> currency = this.currencyRepository.findById(id);
        if(currency.isPresent()){
            return toDto(currency.get());
        }
        throw new RecordNotFoundException(String.format("Currency Not Found On this Id => %d",id));
    }



    public CurrencyDto updateCurrency(CurrencyDto currencyDto, Long id) {
        double tolerance = 0.0001;
        Currency currency = this.currencyRepository.findById(id).get();
        if(!(Math.abs(currency.getConversionRate() - currencyDto.getConversionRate()) < tolerance)){
            CurrencyAuditLog currencyAuditLog = new CurrencyAuditLog()
                    .builder()
                    .conversionRate(currency.getConversionRate())
                    .currencyTo(currency.getCurrencyTo())
                    .currencyFrom(currency.getCurrencyFrom())
                    .createdAt(currency.getCreatedAt())
                    .createdBy(currency.getCreatedBy())
                    .updatedAt(currency.getUpdatedAt())
                    .updatedBy(currency.getUpdatedBy())
                    .isPresent(currency.getIsPresent())
                    .currency(currency)
                    .build();

            CurrencyAuditLog currencyAuditLog1 = this.currencyAuditLogService.save(currencyAuditLog);
        }
                currency.setId(id);
                currency.setCurrencyFrom(currencyDto.getCurrencyFrom());
                currency.setCurrencyTo(currencyDto.getCurrencyTo());
                currency.setConversionRate(currencyDto.getConversionRate());
                currency.setIsPresent(currencyDto.getIsPresent());
                currency.setUpdatedAt(LocalDate.now().toString());
                currency.setUpdatedBy(HelperService.getLoggedInUserName());
                return toDto(this.currencyRepository.save(currency));
    }

    public Currency findByCurrencyFromAndCurrencyTo(Custom custom, Customer customer) {
        String currencyFrom=custom.getCurrency();
        String currencyTo=customer.getInvoiceCurrency();
        Optional<Currency> currency = currencyRepository.findByCurrencyFromAndCurrencyTo(currencyFrom,currencyTo);
        if (currency.isPresent()){
            return currency.get();
        }
        throw new RecordNotFoundException("Conversion Rate not Found");
    }

    public List<String> getDistinctCurrencies() {
        return this.customService.getDistinctCurrencies();
    }

    private CurrencyDto toDto(Currency currency){
        return new CurrencyDto().builder()
                .id(currency.getId())
                .currencyFrom(currency.getCurrencyFrom())
                .currencyTo(currency.getCurrencyTo())
                .conversionRate(currency.getConversionRate())
                .createdBy(currency.getCreatedBy())
                .createdAt(currency.getCreatedAt())
                .updatedBy(currency.getUpdatedBy())
                .updatedAt(currency.getUpdatedAt())
                .isPresent(currency.getIsPresent())
                .build();
    }
    private Currency toDomain(CurrencyDto currencyDto){
        return new Currency().builder()
                .id(currencyDto.getId())
                .currencyFrom(currencyDto.getCurrencyFrom())
                .currencyTo(currencyDto.getCurrencyTo())
                .conversionRate(currencyDto.getConversionRate())
                .createdBy(currencyDto.getCreatedBy())
                .createdAt(currencyDto.getCreatedAt())
                .updatedBy(currencyDto.getUpdatedBy())
                .updatedAt(currencyDto.getUpdatedAt())
                .isPresent(currencyDto.getIsPresent())
                .build();
    }

    private LocalDate convertDateInToLocalDate(Date date) {

        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }


}
