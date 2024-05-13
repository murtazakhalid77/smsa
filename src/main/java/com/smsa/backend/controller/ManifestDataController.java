package com.smsa.backend.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.smsa.backend.dto.ManifestDataDto;
import com.smsa.backend.model.ManifestData;
import com.smsa.backend.service.ManifestDataService;
import com.smsa.backend.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class ManifestDataController {

    @Autowired
    private AmazonS3 s3Client;
    //    @Autowired
    //    private JobLauncher jobLauncher;
// @Autowired
//    private Job job;
    @Autowired
    StorageService storageService;
    @Autowired
    ManifestDataService manifestDataService;

    @PostMapping("/manifest-data")
    ResponseEntity<List<ManifestData>> getManifestData(@RequestBody ManifestDataDto manifestDataDto){
        List<ManifestData> manifestDataList = this.manifestDataService.getManifestData(manifestDataDto);
        HttpHeaders headers = new HttpHeaders();

        return ResponseEntity.ok()
                .headers(headers)
                .body(manifestDataList);
    }

    @GetMapping("/manifest-data/{id}")
    ResponseEntity<ManifestData> getManifestDataById(@PathVariable Long id){
        return ResponseEntity.ok(this.manifestDataService.getManifestDataById(id));
    }

    @PatchMapping("/manifest-data/{id}")
    ResponseEntity<ManifestData> updateManifestData(@RequestBody ManifestData manifestData, @PathVariable Long id){
        return ResponseEntity.ok(this.manifestDataService.updateManifestData(manifestData, id));
    }

    @GetMapping("/download/getAllFilesName")
    public ResponseEntity<List<String>> getAllFilesName(){
        return ResponseEntity.ok(storageService.getFileNamesInManifestFolder());
    }

    @GetMapping("/download/getAllFilesData")
    public ResponseEntity<Void> getAllFilesData(){
        manifestDataService.readFilesInManifestFolder();
        return ResponseEntity.ok().build();
    }

//    @PostMapping("/download/getAllFiles")
//    public void getAllFiles(){
//        JobParameters jobParameter = new JobParametersBuilder()
//                .addLong("Start At",System.currentTimeMillis()).toJobParameters();
//
//        try {
//            jobLauncher.run(job, jobParameter);
//        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
//                 JobParametersInvalidException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
