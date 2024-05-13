package com.smsa.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smsa.backend.criteria.SearchCriteria;
import com.smsa.backend.dto.RegionDto;
import com.smsa.backend.model.Region;
import com.smsa.backend.service.RegionService;
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
public class RegionController {

    @Autowired
    RegionService regionService;

    @PostMapping("/region")
    ResponseEntity<RegionDto> addRegion(@RequestBody RegionDto regionDto){
        return ResponseEntity.ok(this.regionService.addRegion(regionDto));
    }

    @GetMapping("/region/pagination")
    ResponseEntity<List<RegionDto>> getAllRegions(@RequestParam("search") String search, @RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "10") int size,
                                                  @RequestParam(value = "sort", defaultValue = "id") String sort) throws JsonProcessingException {
        SearchCriteria searchCriteria = new ObjectMapper().readValue(search, SearchCriteria.class);
        Page<Region> regions = this.regionService.getAllRegions(searchCriteria,  PageRequest.of(page, size,  Sort.by(sort).descending()));
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(regions.getTotalElements()));
        return ResponseEntity.ok()
                .headers(headers)// Set the headers
                .body(regionService.toDto(regions.getContent()));

    }


    @GetMapping("/region/{id}")
    ResponseEntity<RegionDto> getRegionById(@PathVariable Long id){
        return ResponseEntity.ok(this.regionService.getRegionById(id));
    }

    @GetMapping("/region")
    ResponseEntity<List<Region>> getRegions(){
        return ResponseEntity.ok(this.regionService.getRegions());
    }

    @PutMapping("/region/{id}")
    ResponseEntity<RegionDto> updateRegion(@PathVariable Long id, @RequestBody RegionDto regionDto){
        return ResponseEntity.ok(this.regionService.updateRegion(id, regionDto));
    }

    @GetMapping("/region-status/{status}")
    ResponseEntity<List<RegionDto>> getAllRegionsByStatus(@PathVariable boolean status){
        return ResponseEntity.ok(this.regionService.getAllRegionsByStatus(status));
    }

}
