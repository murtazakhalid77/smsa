package com.smsa.backend.service;

import com.smsa.backend.repository.RecordManifestFolderRepository;
import org.apache.poi.ss.usermodel.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.smsa.backend.model.ManifestData;
import com.smsa.backend.repository.DemoRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Slf4j
@Service
public class DemoService {
    private static final Logger logger = LoggerFactory.getLogger(StorageService.class);
    public static final String BUCKET_NAME="cdvinv";
    private static final String MANIFEST_FOLDER="manifestFolder/";

    @Autowired
    private AmazonS3 s3Client;
    @Autowired
    private DemoRepository demoRepository;
    @Autowired
    private RecordManifestFolderRepository manifestFolderRepository;
    @Autowired
    private StorageService storageService;

    public void readFilesInManifestFolder() {
        List<String> csvContents = new ArrayList<>();
        try {
//            List<String> filesNamesDone = manifestFolderRepository.findAllfileName();
            List<String> fileNames = storageService.getFileNamesInManifestFolder();
            for (String fileName : fileNames) {
//                boolean found = filesNamesDone.stream().anyMatch(e -> e.equals(fileName));
                if (fileName.endsWith(".csv")) {
                    List<ManifestData> csvContent = readCSVFile(fileName);
                    demoRepository.saveAll(csvContent);

                    //manifestFolderRepository is used to record manifest folder which has been done
//                    manifestFolderRepository.saveFileName(fileName);
                }
//                else if(fileName.endsWith(".xlsx") &&!found){
//                    List<ManifestData> csvContent = readXLSXFile(fileName);
////                    demoRepository.saveAll(csvContent);
//                }
            }
        } catch (Exception e) {
            logger.error("Failed to read CSV files from manifest folder", e);
            throw new RuntimeException("Failed to read CSV files from manifest folder", e);
        }
    }

    private List<ManifestData> readCSVFile(String fileName) throws IOException {
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(BUCKET_NAME, MANIFEST_FOLDER + fileName));
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        List<ManifestData> manifestDataList = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null && !line.startsWith("company_name")) {
            String[] fields = line.split(",");
            if (fields.length != 23) {
                // Skip lines with invalid number of fields
                continue;
            }
            ManifestData manifestData = ManifestData.builder()
                    .companyName(fields[0])
                    .mode(fields[1])
                    .shipmentMode(fields[2])
                    .encodeDesc(fields[3])
                    .loadingPortCode(fields[4])
                    .encodeDescSec(fields[5])
                    .destinationPort(fields[6])
                    .carrierCode(fields[7])
                    .flightNumber(fields[8])
                    .departureDate(fields[9])
                    .arrivalDate(fields[10])
                    .actualWeight(fields[11])
                    .dimWeight(fields[12])
                    .prefix(fields[13])
                    .manifestNumber(fields[14])
                    .blDate(fields[15])
                    .awb(fields[16])
                    .orderNumber(fields[17])
                    .customShipDate(fields[18])
                    .accountNumber(fields[19])
                    .weight(fields[20])
                    .amount(fields[21])
                    .shipmentCountry(fields[22])
                    .build();

            manifestDataList.add(manifestData);
        }

        reader.close();
        inputStream.close();

        return manifestDataList;
    }

    private List<ManifestData> readXLSXFile(String fileName) throws IOException {
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(BUCKET_NAME, MANIFEST_FOLDER + fileName));
        S3ObjectInputStream inputStream = s3Object.getObjectContent();

        List<ManifestData> manifestDataList = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);// Assuming you want to read from the first sheet

            int startRow;
            if(sheet.getRow(0).getCell(0).equals("company_name")){
                startRow = 1;
            }
            else {startRow=0;}

            for (int rowIndex = startRow; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }
                ManifestData manifestData = ManifestData.builder()
                        .companyName(String.valueOf(row.getCell(0)))
                        .mode(String.valueOf(row.getCell(1)))
                        .shipmentMode(String.valueOf(row.getCell(2)))
                        .encodeDesc(String.valueOf(row.getCell(3)))
                        .loadingPortCode(String.valueOf(row.getCell(4)))
                        .encodeDescSec(String.valueOf(row.getCell(5)))
                        .destinationPort(String.valueOf(row.getCell(6)))
                        .carrierCode(String.valueOf(row.getCell(7)))
                        .flightNumber(String.valueOf(row.getCell(8)))
                        .departureDate(String.valueOf(row.getCell(9)))
                        .arrivalDate(String.valueOf(row.getCell(10)))
                        .actualWeight(String.valueOf(row.getCell(11)))
                        .dimWeight(String.valueOf(row.getCell(12)))
                        .prefix(String.valueOf(row.getCell(13)))
                        .manifestNumber(String.valueOf(row.getCell(14)))
                        .blDate(String.valueOf(row.getCell(15)))
                        .awb(String.valueOf(row.getCell(16)))
                        .orderNumber(String.valueOf(row.getCell(17)))
                        .customShipDate(String.valueOf(row.getCell(18)))
                        .accountNumber(String.valueOf(row.getCell(19)))
                        .weight(String.valueOf(row.getCell(20)))
                        .amount(String.valueOf(row.getCell(21)))
                        .shipmentCountry(String.valueOf(row.getCell(22)))
                        .build();

                manifestDataList.add(manifestData);
            }
        } finally {
            inputStream.close();
        }

        return manifestDataList;
    }

}
