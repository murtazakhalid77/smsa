package com.smsa.backend.controller;

import com.smsa.backend.model.SalesReportAwb;
import com.smsa.backend.service.SalesReportAwbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class SalesReportAwbController {

    @Autowired
    SalesReportAwbService salesReportAwbService;

}
