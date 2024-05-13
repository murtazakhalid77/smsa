package com.smsa.backend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.smsa.backend.Exception.RecordNotFoundException;
import com.smsa.backend.criteria.SearchCriteria;
import com.smsa.backend.dto.ManifestDataDto;
import com.smsa.backend.model.ManifestData;
import com.smsa.backend.model.RecordManifestFolder;
import com.smsa.backend.repository.ManifestDataRepository;
import com.smsa.backend.repository.RecordManifestFolderRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ManifestDataService {
    private static final Logger logger = LoggerFactory.getLogger(ManifestDataService.class);
    @Autowired
    private AmazonS3 s3Client;
    @Autowired
    private ManifestDataRepository manifestDataRepository;
    @Autowired
    private RecordManifestFolderRepository manifestFolderRepository;
    @Autowired
    private StorageService storageService;

    public List<ManifestData> getManifestData(ManifestDataDto manifestDataDto){
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setMapper(manifestDataDto.getMapper());

        Optional<List<ManifestData>> manifestDataList =null;
        if (manifestDataDto.getAwbs()!=null){
            manifestDataList = this.getManifestDataByAwbs(manifestDataDto.getAwbs());
        }
        else if(manifestDataDto.getManifestNo()!=null){
            if(manifestDataDto.getPrefix()==null){
                manifestDataList = this.getManifestDataByManifestNoOnly(manifestDataDto.getManifestNo());
            }
            else if(manifestDataDto.getPrefix().isEmpty()){
                manifestDataList = this.getManifestDataByManifestNoOnly(manifestDataDto.getManifestNo());
            }
            else {
                manifestDataList = this.getManifestDataByManifestNoAndPrefix(manifestDataDto.getPrefix(), manifestDataDto.getManifestNo());
            }
        }

        for (ManifestData manifestData: manifestDataList.get()){
            manifestData.setId(manifestData.getId());
            manifestData.setActualWeight(manifestData.getActualWeight());
            manifestData.setWeight(manifestData.getWeight());
            manifestData.setDimWeight(manifestData.getDimWeight());
        }
        return manifestDataList.get();
    }
    public void readFilesInManifestFolder() {
        try {
            List<String> filesNamesDone = manifestFolderRepository.findAll()
                    .stream()
                    .map(RecordManifestFolder::getFileName) // Assuming getFileName() returns the file name
                    .collect(Collectors.toList());;
            List<String> fileNames = storageService.getFileNamesInManifestFolder();
            for (String fileName : fileNames) {
                boolean found = filesNamesDone.stream().anyMatch(e -> e.equals(fileName));
                if (fileName.endsWith(".csv") && !found) {
                    List<ManifestData> csvContent = readCSVFile(fileName);
                    manifestDataRepository.saveAll(csvContent);

                    //manifestFolderRepository is used to record manifest folder which has been done
                    manifestFolderRepository.save(RecordManifestFolder.builder()
                            .fileName(fileName)
                            .build());
                }
                else if(fileName.endsWith(".xlsx") && !found){
                    List<ManifestData> xlsxContent = readXLSXFile(fileName);
                    manifestDataRepository.saveAll(xlsxContent);

                    manifestFolderRepository.save(RecordManifestFolder.builder()
                            .fileName(fileName)
                            .build());
                }
            }
        } catch (Exception e) {
            logger.error("Failed to read CSV files from manifest folder", e);
            throw new RuntimeException("Failed to read CSV files from manifest folder", e);
        }
    }

    private List<ManifestData> readCSVFile(String fileName) throws IOException {
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(StorageService.BUCKET_NAME, StorageService.MANIFEST_FOLDER + fileName));
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
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(StorageService.BUCKET_NAME, StorageService.MANIFEST_FOLDER + fileName));
        S3ObjectInputStream inputStream = s3Object.getObjectContent();

        List<ManifestData> manifestDataList = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);// Assuming you want to read from the first sheet

            int startRow = sheet.getRow(0).getCell(0).toString().equals("company_name") ? 1 : 0;

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

    public ManifestData getManifestDataById(Long id) {
        Optional<ManifestData> manifestData = this.manifestDataRepository.findById(id);
        if(manifestData.isPresent()){
            return manifestData.get();
        }
        throw new RecordNotFoundException(String.format("Custom Not Found On this Id => %d",id));
    }

    public ManifestData updateManifestData(ManifestData manifestData, Long id) {
        Optional<ManifestData> optionalManifestData = this.manifestDataRepository.findById(id);

        if(optionalManifestData.isPresent()){
            optionalManifestData.get().setId(id);
            optionalManifestData.get().setCompanyName(manifestData.getCompanyName());
            optionalManifestData.get().setMode(manifestData.getMode());
            optionalManifestData.get().setShipmentMode(manifestData.getShipmentMode());
            optionalManifestData.get().setEncodeDesc(manifestData.getEncodeDesc());
            optionalManifestData.get().setLoadingPortCode(manifestData.getLoadingPortCode());
            optionalManifestData.get().setEncodeDescSec(manifestData.getEncodeDescSec());
            optionalManifestData.get().setDestinationPort(manifestData.getDestinationPort());
            optionalManifestData.get().setCarrierCode(manifestData.getCarrierCode());
            optionalManifestData.get().setFlightNumber(manifestData.getFlightNumber());
            optionalManifestData.get().setDepartureDate(manifestData.getDepartureDate());
            optionalManifestData.get().setArrivalDate(manifestData.getArrivalDate());
            optionalManifestData.get().setActualWeight(manifestData.getActualWeight());
            optionalManifestData.get().setDimWeight(manifestData.getDimWeight());
            optionalManifestData.get().setPrefix(manifestData.getPrefix());
            optionalManifestData.get().setManifestNumber(manifestData.getManifestNumber());
            optionalManifestData.get().setBlDate(manifestData.getBlDate());
            optionalManifestData.get().setAwb(manifestData.getAwb());
            optionalManifestData.get().setOrderNumber(manifestData.getOrderNumber());
            optionalManifestData.get().setCustomShipDate(manifestData.getCustomShipDate());
            optionalManifestData.get().setAccountNumber(manifestData.getAccountNumber());
            optionalManifestData.get().setWeight(manifestData.getWeight());
            optionalManifestData.get().setAmount(manifestData.getAmount());
            optionalManifestData.get().setShipmentCountry(manifestData.getShipmentCountry());
            optionalManifestData.get().setConsigneeName(manifestData.getConsigneeName());
            optionalManifestData.get().setConsigneeCity(manifestData.getConsigneeCity());

            return manifestDataRepository.save(optionalManifestData.get());
        }
        throw new RecordNotFoundException(String.format("Manifest Data Not Found On this Id => %d",id));
    }

    public Optional<List<ManifestData>> getManifestDataByAwbs(String awbs){
        List<String> awbList = Arrays.stream(awbs.split(","))
                .map(String::trim)
                .collect(Collectors.toList());

//        List<String> convertedAwbList = awbList.stream()
//                .map(ManifestDataService::convertPlainNumberToScientific)
//                .collect(Collectors.toList());
        try {
            Optional<List<ManifestData>> manifestData = Optional.of(this.manifestDataRepository.findByAwbIn(awbList));
            if(manifestData.isPresent()){
                return manifestData;
            }
        } catch (Exception e) {
            log.error("Error fetching manifest data", e);
        }
        throw new RecordNotFoundException("Couldn't find data");
    }

    private static String convertPlainNumberToScientific(String value) {
        NumberFormat numFormat = new DecimalFormat();
        numFormat = new DecimalFormat("0.#####E0");
        String format = numFormat.format(Double.parseDouble(value));
        if (format.contains("E")) {
            // Split the string at "E" into two parts
            String[] parts = format.split("E");
            // Add "+" after "E"
            String modifiedFormat = parts[0] + "E+" + parts[1];
            return modifiedFormat;
        } else {
            // If the string does not contain "E", handle it accordingly
            System.out.println("String does not contain scientific notation.");
        }
        return null;
    }
    public Optional<List<ManifestData>> getManifestDataByManifestNoOnly(String manifestNumber){
        return Optional.of(manifestDataRepository.getManifestDataByManifestNoOnly(manifestNumber));
    }
    public Optional<List<ManifestData>> getManifestDataByManifestNoAndPrefix(String prefix, String manifestNumber){
        return Optional.of(manifestDataRepository.getManifestDataByManifestNoAndPrefix(prefix, manifestNumber));
    }
}
