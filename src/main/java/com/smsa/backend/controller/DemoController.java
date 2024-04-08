package com.smsa.backend.controller;

import com.smsa.backend.service.DemoService;
import com.smsa.backend.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class DemoController {
    @Autowired
    StorageService storageService;
    @Autowired
    DemoService demoService;

    @GetMapping("/download/getAllFilesName")
    public ResponseEntity<List<String>> getAllFilesName(){
        return ResponseEntity.ok(storageService.getFileNamesInManifestFolder());
    }

    @GetMapping("/download/getAllFilesData")
    public ResponseEntity<Void> getAllFilesData(){
        demoService.readFilesInManifestFolder();
        return ResponseEntity.ok().build();
    }
}
