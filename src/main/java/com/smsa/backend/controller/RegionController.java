package com.smsa.backend.controller;

import com.smsa.backend.dto.RegionDto;
import com.smsa.backend.model.Region;
import com.smsa.backend.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
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
    ResponseEntity<List<RegionDto>> getAllRegions(){
        return ResponseEntity.ok(this.regionService.getAllRegions());
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
