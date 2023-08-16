package com.smsa.backend.service;

import com.smsa.backend.Exception.RecordNotFoundException;
import com.smsa.backend.dto.CurrencyDto;
import com.smsa.backend.model.Currency;
import com.smsa.backend.model.Custom;
import com.smsa.backend.model.Customer;
import com.smsa.backend.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService {
    @Autowired
    CurrencyRepository currencyRepository;
    @Autowired
    CustomService customService;
    public CurrencyDto addCurrency(CurrencyDto currencyDto) {
        return toDto(this.currencyRepository.save(toDomain(currencyDto)));
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
        Optional<Currency> currency = this.currencyRepository.findById(id);

        if(currency.isPresent()){
            currency.get().setId(id);
            currency.get().setCurrencyFrom(currencyDto.getCurrencyFrom());
            currency.get().setCurrencyTo(currencyDto.getCurrencyTo());
            currency.get().setConversionRate(currencyDto.getConversionRate());
            currency.get().setIsPresent(currencyDto.getIsPresent());
            return toDto(this.currencyRepository.save(currency.get()));
        }
        throw new RecordNotFoundException(String.format("Currency Not Found On this Id => %d",id));
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
                .isPresent(currency.getIsPresent())
                .build();
    }
    private Currency toDomain(CurrencyDto currencyDto){
        return new Currency().builder()
                .id(currencyDto.getId())
                .currencyFrom(currencyDto.getCurrencyFrom())
                .currencyTo(currencyDto.getCurrencyTo())
                .conversionRate(currencyDto.getConversionRate())
                .isPresent(currencyDto.getIsPresent())
                .build();
    }

}
