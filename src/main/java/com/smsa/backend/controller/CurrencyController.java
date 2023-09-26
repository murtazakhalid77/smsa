package com.smsa.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smsa.backend.criteria.SearchCriteria;
import com.smsa.backend.dto.CurrencyDto;
import com.smsa.backend.model.Currency;
import com.smsa.backend.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class CurrencyController {
    @Autowired
    CurrencyService currencyService;

    @PostMapping("/currency")
    ResponseEntity<CurrencyDto> addCurrency(@RequestBody CurrencyDto currencyDto){
        return ResponseEntity.ok(this.currencyService.addCurrency(currencyDto));
    }

    @GetMapping("/currency")
    ResponseEntity<List<CurrencyDto>> getAllCurrency(@RequestParam("search") String search, @RequestParam(value = "page", defaultValue = "0") int page,
                                                     @RequestParam(value = "size", defaultValue = "10") int size,
                                                     @RequestParam(value = "sort", defaultValue = "id") String sort) throws JsonProcessingException {
        SearchCriteria searchCriteria = new ObjectMapper().readValue(search, SearchCriteria.class);
        Page<Currency> currencies = this.currencyService.getAllCurrency(searchCriteria, PageRequest.of(page, size,  Sort.by(sort).descending()));
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(currencies.getTotalElements()));
        return ResponseEntity.ok()
                .headers(headers)// Set the headers
                .body(currencyService.toDto(currencies));
    }


    @GetMapping("/currency/{id}")
    ResponseEntity<CurrencyDto> getCurrencyById(@PathVariable Long id){
        return ResponseEntity.ok(this.currencyService.getCurrencyById(id));
    }

    @PatchMapping("/currency/{id}")
    ResponseEntity<CurrencyDto> updateCurrency(@RequestBody CurrencyDto currencyDto, @PathVariable Long id){
        return ResponseEntity.ok(this.currencyService.updateCurrency(currencyDto, id));
    }

    @GetMapping("/currency-distinct")
    ResponseEntity<List<String>> getDistinctCurrencies(){
        return ResponseEntity.ok(this.currencyService.getDistinctCurrencies());
    }
}
