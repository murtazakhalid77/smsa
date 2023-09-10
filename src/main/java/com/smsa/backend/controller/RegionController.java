package com.smsa.backend.controller;

import com.smsa.backend.dto.RegionDto;
import com.smsa.backend.model.Region;
import com.smsa.backend.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
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

    @GetMapping("/region")
    ResponseEntity<List<RegionDto>> getAllRegions(Pageable pageable){
        Page<Region> regions = this.regionService.getAllRegions(pageable);
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

    @PutMapping("/region/{id}")
    ResponseEntity<RegionDto> updateRegion(@PathVariable Long id, @RequestBody RegionDto regionDto){
        return ResponseEntity.ok(this.regionService.updateRegion(id, regionDto));
    }

    @GetMapping("/region-status/{status}")
    ResponseEntity<List<RegionDto>> getAllRegionsByStatus(@PathVariable boolean status){
        return ResponseEntity.ok(this.regionService.getAllRegionsByStatus(status));
    }

}
