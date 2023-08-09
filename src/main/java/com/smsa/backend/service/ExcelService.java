package com.smsa.backend.service;


import com.smsa.backend.Exception.AwbDublicateException;
import com.smsa.backend.Exception.ExcelMakingException;
import com.smsa.backend.Exception.ParsingExcelException;
import com.smsa.backend.Exception.SheetAlreadyExistException;
import com.smsa.backend.dto.ExcelImportDto;
import com.smsa.backend.model.*;
import com.smsa.backend.repository.*;
import com.smsa.backend.security.util.ExcelImportHelper;
import com.smsa.backend.security.util.HashMapHelper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;



@Service
public class ExcelService {
    @Autowired
    ExcelImportHelper excelImportHelper;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    SheetHistoryRepository sheetHistoryRepository;
    @Autowired
    private InvoiceDetailsRepository invoiceDetailsRepository;
    @Autowired
    private CustomRepository customRepository;
    @Autowired
    HelperService helperService;
    @Autowired
    HashMapHelper hashMapHelper;
    @Value("${smsa.file.local.location}")
    String sampleFileLocalLocation;
    List<InvoiceDetails> invoicesWithAccount = new ArrayList<>();
    List<InvoiceDetails> invoicesWithoutAccount = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(ExcelService.class);

    public void saveInvoicesToDatabase(MultipartFile file, ExcelImportDto excelImportDto) throws Exception {
        invoicesWithAccount.clear();
        invoicesWithoutAccount.clear();
        Map<String, List<InvoiceDetails>> filterd;
        try {
            filterd = filterRowsByAccountNumber(file, excelImportDto);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        for (List<InvoiceDetails> invoiceDetailsList : filterd.values()) {
            invoiceDetailsRepository.saveAll(invoiceDetailsList);
        }
    }

    public Map<String, List<InvoiceDetails>> filterRowsByAccountNumber(MultipartFile multipartFile, ExcelImportDto excelImportDto) {

        LocalDate currentDate = LocalDate.now();
        String sheetId = UUID.randomUUID().toString();
        Map<String, List<InvoiceDetails>> mappedRowsMap = new HashMap<>();
        List<List<String>> rowsToBeFiltered;
        String originalFilename = null;
        try {
            originalFilename = multipartFile.getOriginalFilename();
            validateSheetName(originalFilename);
        } catch (SheetAlreadyExistException e) {
            // Handle the exception, log the error, and throw a new exception or return as appropriate
            logger.error("Sheet with the name " + originalFilename + " already exists!");
            throw new SheetAlreadyExistException(String.format("Sheet with the name %S already exists!", originalFilename));
        }

        try {
            rowsToBeFiltered = excelImportHelper.parseExcelFile(multipartFile);
        } catch (Exception e) {
            logger.error("Error parsing Excel file: " + e.getMessage(), e);
            throw new ParsingExcelException(String.format("There was an issue in parsing the file %S", originalFilename));
        }

        try {
            findDuplicatesOfAwb(rowsToBeFiltered);
        }catch (AwbDublicateException e){
            logger.error("There is an awb duplication couldn't upload the file");
            throw new AwbDublicateException("There was a duplication of AWB, couldn't upload the file");
        }


        if (!rowsToBeFiltered.isEmpty()) {
            rowsToBeFiltered.remove(0);
        }

        for (List<String> row : rowsToBeFiltered) {
            if (row.size() > 2) {
                String commonKey = row.get(2);
                if (!commonKey.isEmpty()) {
                    // Check if the key is not already in the map
                    if (!mappedRowsMap.containsKey(commonKey)) {
                        mappedRowsMap.put(commonKey, new ArrayList<>());
                    }
                    try {
                        // Map the fields from the row list to the InvoiceDetails object
                        InvoiceDetails invoiceDetails = mapToDomain(row);

                        mapHelperFields(invoiceDetails, sheetId, currentDate);

                        // Check if the account number exists in the accountNumberUuidMap
                        String accountNumber = invoiceDetails.getInvoiceDetailsId().getAccountNumber();
                        String customerUniqueId = generateCustomerUniqueId(accountNumber);

                        invoiceDetails.setCustomerUniqueId(customerUniqueId);

                        // Add the InvoiceDetails object to the list associated with the common key in the map
                        mappedRowsMap.get(commonKey).add(invoiceDetails);

                        filterAccountAndNonAccountInvoice(invoiceDetails, accountNumber);

                    } catch (Exception e) {
                        logger.error("Error mapping row to InvoiceDetails: " + e.getMessage());
                        throw new RuntimeException("There was a problem in filtering Invoices against account Numbers");
                    }
                }
            }
        }

        SheetHistory sheetHistory = createSheetHistory(originalFilename, excelImportDto, sheetId);
        sheetHistoryRepository.save(sheetHistory);

        return mappedRowsMap;

    }
    public static List<String> findDuplicatesOfAwb(List<List<String>> data) {
        Set<String> seenValues = new HashSet<>();
        List<String> duplicates = new ArrayList<>();

        for (List<String> row : data) {
            if (row.size() > 3) { // Make sure the row has at least 4 columns
                String value = row.get(3);
                if (!seenValues.add(value)) {
                    duplicates.add(value);
                }
            }
        }

        if (!duplicates.isEmpty()){
            throw new AwbDublicateException("There was a duplication of AWB, couldn't upload the file");
        }
        return duplicates;
    }

    private void validateSheetName(String sheetName) {
        if (sheetHistoryRepository.existsByName(sheetName)) {
            logger.info(String.format("Sheet with the name %s already exists!", sheetName));
            throw new SheetAlreadyExistException(String.format("Sheet with the name %s already exists!", sheetName));
        }
    }

    private void filterAccountAndNonAccountInvoice(InvoiceDetails invoiceDetails, String accountNumber) {
        if (checkAccountNumberInCustomerTable(accountNumber)) {
            invoicesWithAccount.add(invoiceDetails);
        } else {
            invoicesWithoutAccount.add(invoiceDetails);
            Customer customer = createPsedoCustomer(accountNumber);
            customerRepository.save(customer);
        }
    }

    public List<InvoiceDetails> getInvoicesWithoutAccount() {
        return invoicesWithoutAccount;
    }

    private String generateCustomerUniqueId(String accountNumber) {
        Map<String, String> accountNumberUuidMap = new HashMap<>();
        if (accountNumberUuidMap.containsKey(accountNumber)) {
            // If the UUID already exists for the account number, return it
            return accountNumberUuidMap.get(accountNumber);
        } else {
            // If the UUID does not exist for the account number, generate a new one and store it in the map
            String customerUniqueId = UUID.randomUUID().toString();
            accountNumberUuidMap.put(accountNumber, customerUniqueId);
            return customerUniqueId;
        }
    }

    private InvoiceDetails mapHelperFields(InvoiceDetails invoiceDetails, String sheetId, LocalDate currentDate) {


        invoiceDetails.setSheetTimesStamp(currentDate);
        invoiceDetails.setSheetUniqueId(sheetId);
        invoiceDetails.setIsSentInMail(Boolean.FALSE);
        invoiceDetails.setCustomerTimestamp(currentDate);
        return invoiceDetails;
    }

    private SheetHistory createSheetHistory(String originalFilename, ExcelImportDto excelImportDto, String sheetId) {
        Optional<Custom> custom = customRepository.findByCustom(excelImportDto.getCustom());

        return SheetHistory.builder()
                .uniqueUUid(sheetId)
                .name(originalFilename)
                .isEmailSent(false)
                .custom(custom.get())
                .startDate(excelImportDto.getFormattedStartDate() != null ? excelImportDto.getFormattedStartDate() : null)
                .endDate(excelImportDto.getFormattedEndDate() != null ? excelImportDto.getFormattedEndDate() : null)
                .build();

    }

    private Customer createPsedoCustomer(String accountNumber) {
        return Customer
                .builder()
                .nameEnglish("System")
                .accountNumber(accountNumber)
                .isPresent(true) //soft delete
                .status(false)
                .build();
    }

    private boolean checkAccountNumberInCustomerTable(String accountNumber) {
        Optional<Customer> customer = customerRepository.findByAccountNumber(accountNumber);
        return customer.isPresent();
    }


    private InvoiceDetails mapToDomain(List<String> row) {

        InvoiceDetailsId invoiceDetailsId = InvoiceDetailsId.builder()
                .mawb(row.get(0))
                .manifestDate(row.get(1))
                .accountNumber(row.get(2))
                .awb(parseLongOrDefault(row.get(3), 0L))
                .build();

        InvoiceDetails invoiceDetails = InvoiceDetails.builder()
                .invoiceDetailsId(invoiceDetailsId)
                .orderNumber(row.get(4))
                .origin(row.get(5))
                .destination(row.get(6))
                .shippersName(row.get(7))
                .consigneeName(row.get(8))
                .weight(parseDoubleOrDefault(row.get(9), 0.0))
                .declaredValue(parseDoubleOrDefault(row.get(10), 0.0))
                .valueCustom(parseDoubleOrDefault(row.get(11), 0.0))
                .vatAmount(parseDoubleOrDefault(row.get(12), 0.0))
                .customFormCharges(parseDoubleOrDefault(row.get(13), 0.0))
                .other(parseDoubleOrDefault(row.get(14), 0.0))
                .totalCharges(parseDoubleOrDefault(row.get(15), 0.0))
                .customDeclarationNumber(parseLongOrDefault(row.get(16), 0L))
                .ref(row.get(17))
                .customDeclarationDate(row.get(18))
                .build();

        return invoiceDetails;
    }

    // Helper method to parse long values with default value on exception
    private Long parseLongOrDefault(String value, Long defaultValue) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private Double parseDoubleOrDefault(String value, Double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    public SheetHistory getSheetHistory(String sheetUniqueUUid) {
        return sheetHistoryRepository.findByUniqueUUid(sheetUniqueUUid);
    }
    public byte[] updateExcelFile(List<InvoiceDetails> invoiceDetailsList, Customer customer, String sheetUniqueId, Long invoiceNumber) throws Exception {
        logger.info(String.format("Inside update excel method for account ", customer.getAccountNumber()));

        Map<String, List<InvoiceDetails>> filteredRowsMap;
        Custom custom = getSheetHistory(sheetUniqueId).getCustom();


        FileInputStream fileInputStream = new FileInputStream(sampleFileLocalLocation);

        Workbook newWorkBook = WorkbookFactory.create(fileInputStream);

        Sheet invoiceDetailSheet = newWorkBook.getSheetAt(1);

        invoiceDetailSheet.setDisplayGridlines(false);

        setInvoiceDetailsCellValues(invoiceDetailSheet, invoiceDetailsList);

        Sheet summarySheet = newWorkBook.getSheetAt(0);

        summarySheet.setDisplayGridlines(false);

        setSummarySheetCellValues(summarySheet, customer, sheetUniqueId, invoiceNumber);

        filteredRowsMap = hashMapHelper.filterRowsByMawbNumber(invoiceDetailsList);

        List<Map<String, Object>> calculatedValuesList = hashMapHelper.calculateValues(filteredRowsMap, customer, custom, invoiceNumber);

        Map<String, Double> sumMap = hashMapHelper.sumNumericColumns(calculatedValuesList);

        populateCalculatedValues(summarySheet, calculatedValuesList);

        populateSumValues(summarySheet, sumMap);

        fileInputStream.close();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            newWorkBook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Failed to create Excel file.");
        }


    }
    public void populateSumValues(Sheet summarySheet, Map<String, Double> sumMap) {
        try {
            int lastRowIndex = summarySheet.getLastRowNum();
            int startingRow = findStartingRow(summarySheet);

            if (startingRow > lastRowIndex) {
                startingRow = lastRowIndex + 1;
            }

            Row row = summarySheet.createRow(startingRow);
            setCellValue(row.createCell(6), formatCurrency(sumMap.get("CustomerShipmentValueSum")));
            setCellValue(row.createCell(7), formatCurrency(sumMap.get("VatAmountCustomDeclarationFormSum")));
            setCellValue(row.createCell(8), formatCurrency(sumMap.get("CustomFormChargesSum")));
            setCellValue(row.createCell(9), formatCurrency(sumMap.get("OthersSum")));
            setCellValue(row.createCell(10), formatCurrency(sumMap.get("TotalChargesSum")))         ;
        } catch (ExcelMakingException e) {
            throw new RuntimeException("There was an issue in populating sum values in summary file");
        }
    }

    private int findStartingRow(Sheet sheet) {
        DataFormatter dataFormatter = new DataFormatter();
        int lastRowIndex = sheet.getLastRowNum();

        for (int rowIndex = lastRowIndex; rowIndex >= 0; rowIndex--) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                boolean isRowEmpty = true;
                for (Cell cell : row) {
                    String cellValue = dataFormatter.formatCellValue(cell);
                    if (cellValue != null && !cellValue.trim().isEmpty()) {
                        isRowEmpty = false;
                        break;
                    }
                }

                if (!isRowEmpty) {
                    return rowIndex + 1;
                }
            }
        }

        return 0; // If no non-empty row is found, start from the first row
    }


    private void setCellValue(Cell cell, Double value) {
        cell.setCellValue(value);
    }
    private void setSummarySheetCellValues(Sheet summarySheet, Customer customer, String sheetUniqueId,Long invoiceNumber) throws RuntimeException {
        try {
            Cell invoiceNumberCell = summarySheet.getRow(2).getCell(9);
            setCellValue(invoiceNumberCell,"CDV-"+invoiceNumber);

            Cell nameCell = summarySheet.getRow(3).getCell(1);
            setCellValue(nameCell, customer.getNameEnglish());

            Cell currencyCell = summarySheet.getRow(3).getCell(9);
            setCellValue(currencyCell, customer.getInvoiceCurrency());

            Cell accountNumberCell = summarySheet.getRow(4).getCell(1);
            setCellValue(accountNumberCell, customer.getAccountNumber());

            Cell invoiceDateCell = summarySheet.getRow(4).getCell(9);
            setCellValue(invoiceDateCell, helperService.generateInvoiceDate(sheetUniqueId));
        }catch (Exception e){
            logger.warn("The name,account,invoiceDate cell in the summary sheet are null replace them with XXXX");
            throw new RuntimeException(String.format("Summary Sheet Exception"));
        }

    }

    private void setInvoiceDetailsCellValues(Sheet invoiceDetailSheet, List<InvoiceDetails> invoiceDetailsList) {
        int rowCount = 1;
        try {
            for (InvoiceDetails invoiceDetails : invoiceDetailsList) {
                Row row = invoiceDetailSheet.createRow(rowCount);
                int columnCount = 0;

                setCellValue(row, columnCount, invoiceDetails.getInvoiceDetailsId().getMawb());
                setCellValue(row, ++columnCount, invoiceDetails.getInvoiceDetailsId().getManifestDate());
                setCellValue(row, ++columnCount, invoiceDetails.getInvoiceDetailsId().getAccountNumber());
                setCellValue(row, ++columnCount, invoiceDetails.getInvoiceDetailsId().getAwb());
                setCellValue(row, ++columnCount, invoiceDetails.getOrderNumber());
                setCellValue(row, ++columnCount, invoiceDetails.getOrigin());
                setCellValue(row, ++columnCount, invoiceDetails.getDestination());
                setCellValue(row, ++columnCount, invoiceDetails.getShippersName());
                setCellValue(row, ++columnCount, invoiceDetails.getConsigneeName());
                setCellValue(row, ++columnCount, invoiceDetails.getWeight());
                setCellValue(row, ++columnCount, formatCurrency(invoiceDetails.getDeclaredValue()));
                setCellValue(row, ++columnCount, formatCurrency(invoiceDetails.getValueCustom()));
                setCellValue(row, ++columnCount, formatCurrency(invoiceDetails.getVatAmount()));
                setCellValue(row, ++columnCount, formatCurrency(invoiceDetails.getCustomFormCharges()));
                setCellValue(row, ++columnCount, formatCurrency(invoiceDetails.getOther()));
                setCellValue(row, ++columnCount, formatCurrency(invoiceDetails.getTotalCharges()));
                setCellValue(row, ++columnCount, invoiceDetails.getCustomDeclarationNumber());
                setCellValue(row,++columnCount,invoiceDetails.getRef());
                setCellValue(row, ++columnCount, invoiceDetails.getCustomDeclarationDate());

                rowCount++;
            }
        }catch (ExcelMakingException e){
            throw new ExcelMakingException("There was an issue in invoice sheet");
        }

    }

    private String formatCurrency(Double value) {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(value);
    }
    private void populateCalculatedValues(Sheet summarySheet, List<Map<String, Object>> calculatedValuesList) {
        try {
            int startingRow = 6;
            for (Map<String, Object> calculatedValuesMap : calculatedValuesList) {
                Row row = summarySheet.createRow(startingRow);

                setCellValue(row.createCell(0), calculatedValuesMap.get("InvoiceNumber"));
                setCellValue(row.createCell(1), calculatedValuesMap.get("CustomDeclarationNumber"));
                setCellValue(row.createCell(2), calculatedValuesMap.get("CustomerAccountNumber"));
                setCellValue(row.createCell(3), calculatedValuesMap.get("MawbNumber"));
                setCellValue(row.createCell(4), calculatedValuesMap.get("TotalAwbCount"));

                // Convert String to Double and pass to formatCurrency
                setCellValue(row.createCell(5), formatCurrency(Double.valueOf(calculatedValuesMap.get("TotalValue").toString())));
                setCellValue(row.createCell(6), formatCurrency(Double.valueOf(calculatedValuesMap.get("CustomerShipmentValue").toString())));
                setCellValue(row.createCell(7), formatCurrency(Double.valueOf(calculatedValuesMap.get("VatAmountCustomDeclarationForm").toString())));
                setCellValue(row.createCell(8), formatCurrency(Double.valueOf(calculatedValuesMap.get("CustomFormCharges").toString())));
                setCellValue(row.createCell(9), formatCurrency(Double.valueOf(calculatedValuesMap.get("Others").toString())));
                setCellValue(row.createCell(10), formatCurrency(Double.valueOf(calculatedValuesMap.get("TotalCharges").toString())));

                CellStyle style = summarySheet.getWorkbook().createCellStyle();
                style.setAlignment(HorizontalAlignment.RIGHT);
                row.getCell(5).setCellStyle(style);
                row.getCell(6).setCellStyle(style);
                row.getCell(7).setCellStyle(style);
                row.getCell(8).setCellStyle(style);
                row.getCell(9).setCellStyle(style);
                row.getCell(10).setCellStyle(style);

                // Move to the next row
                startingRow++;
            }
        } catch (ExcelMakingException e) {
            throw new RuntimeException("There was an issue in populating calculated values in the summary file");
        }
    }


    private void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        }
    }

    private void setCellValue(Row row, int columnCount, Object value) {
        Cell cell = row.createCell(columnCount);
        cell.setCellValue(value != null ? value.toString() : "");
    }
}



