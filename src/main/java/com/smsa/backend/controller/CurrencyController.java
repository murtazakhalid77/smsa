package com.smsa.backend.controller;

import com.smsa.backend.dto.CurrencyDto;
import com.smsa.backend.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
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
    ResponseEntity<List<CurrencyDto>> getAllCurrency(){
        return ResponseEntity.ok(this.currencyService.getAllCurrency());
    }


    @GetMapping("/currency/{id}")
    ResponseEntity<CurrencyDto> getCurrencyById(@PathVariable Long id){
        return ResponseEntity.ok(this.currencyService.getCurrencyById(id));
    }

    @PatchMapping("/currency/{id}")
    ResponseEntity<CurrencyDto> updateCurrency(@RequestBody CurrencyDto currencyDto, @PathVariable Long id){
        return ResponseEntity.ok(this.currencyService.updateCurrency(currencyDto, id));
    }
}
