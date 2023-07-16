package com.smsa.backend.controller;

import com.itextpdf.text.DocumentException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class SampleController {


    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello from secured endpoint");
    }

    @GetMapping("/export-to-pdf")
    public void generatePdfFile(HttpServletResponse response) throws DocumentException, IOException, DocumentException {
//        response.setContentType("application/pdf");
//        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
//        String currentDateTime = dateFormat.format(new Date());
//        String headerkey = "Content-Disposition";
//        String headervalue = "attachment; filename=Portfolio Health Report" + currentDateTime + ".pdf";
//        response.setHeader(headerkey, headervalue);
//

    }

}
