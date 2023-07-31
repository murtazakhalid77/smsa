package com.smsa.backend.controller;

import com.itextpdf.text.DocumentException;

import com.smsa.backend.service.ExcelSheetService;
import com.smsa.backend.service.PdfGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class SampleController {
    @Autowired
    PdfGenerator pdfGenerator = new PdfGenerator();
    @Autowired
    ExcelSheetService sheetGenerator = new ExcelSheetService();


    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
        //sheetGenerator.updateExcelFile();
        return ResponseEntity.ok("Hello from secured endpoint");
    }

    @GetMapping("/export-to-pdf")
    public void generatePdfFile(HttpServletResponse response) throws DocumentException, IOException, DocumentException {
     //   pdfGenerator.generatepdf();
    }

}
