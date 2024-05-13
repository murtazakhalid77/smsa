package com.smsa.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smsa.backend.criteria.SearchCriteria;
import com.smsa.backend.dto.CustomDto;
import com.smsa.backend.model.Custom;
import com.smsa.backend.service.CustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class CustomController {

    @Autowired
    CustomService customService;

    @PostMapping("/custom")
    ResponseEntity<CustomDto> addCustom(@RequestBody CustomDto customDto){
        return ResponseEntity.ok(this.customService.addCustom(customDto));
    }

    @GetMapping("/custom")
    ResponseEntity<List<CustomDto>> getAllCustoms(@RequestParam("search") String search,
                                                  @RequestParam(value = "page") int page,
                                                  @RequestParam(value = "size") int size,
                                                  @RequestParam(value = "sort", defaultValue = "id") String sort) throws JsonProcessingException {
        SearchCriteria searchCriteria = new ObjectMapper().readValue(search, SearchCriteria.class);
        Page<Custom> customs = this.customService.getAllCustoms(searchCriteria, PageRequest.of(page, size,  Sort.by(sort).descending()));
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(customs.getTotalElements()));
        return ResponseEntity.ok()
                .headers(headers)
                .body(customService.toDto(customs.getContent()));
    }

    @GetMapping("/custom-import")
    ResponseEntity<List<CustomDto>> getAllCustomForImport(){
        return ResponseEntity.ok(this.customService.getAllCustomForImport());
    }

    @GetMapping("/custom/{id}")
    ResponseEntity<CustomDto> getCustomById(@PathVariable Long id){
        return ResponseEntity.ok(this.customService.getCustomById(id));
    }

    @PatchMapping("/custom/{id}")
    ResponseEntity<CustomDto> updateCustom(@RequestBody CustomDto customDto, @PathVariable Long id){
        return ResponseEntity.ok(this.customService.updateCustom(customDto, id));
    }
}
