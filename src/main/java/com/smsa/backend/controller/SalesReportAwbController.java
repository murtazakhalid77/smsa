package com.smsa.backend.controller;

import com.smsa.backend.service.SalesReportAwbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class SalesReportAwbController {

    @Autowired
    SalesReportAwbService salesReportAwbService;

}
