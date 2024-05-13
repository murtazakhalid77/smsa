package com.smsa.backend.controller;

import com.smsa.backend.service.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/download")
public class TextController {

    @Autowired
    TextService textService;

    @GetMapping("/awb-to-text-file/")
    ResponseEntity<ByteArrayResource> getAwbs(){
        ByteArrayResource fileContents = this.textService.getAwbs(); // Assuming getAwbs() returns byte[]
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("filename", "awb.txt"); // Set the filename

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=awbs.txt")
                .body(fileContents);
    }
}
