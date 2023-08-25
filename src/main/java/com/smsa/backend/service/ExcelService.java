package com.smsa.backend.service;


import com.smsa.backend.Exception.AwbDublicateException;
import com.smsa.backend.Exception.ExcelMakingException;
import com.smsa.backend.Exception.ParsingExcelException;
import com.smsa.backend.Exception.SheetAlreadyExistException;
import com.smsa.backend.dto.ExcelImportDto;
import com.smsa.backend.dto.SalesReportHelperDto;
import com.smsa.backend.model.*;
import com.smsa.backend.model.Currency;
import com.smsa.backend.repository.*;
import com.smsa.backend.security.util.ExcelImportHelper;
import com.smsa.backend.security.util.HashMapHelper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.Element;
import java.io.*;
import java.text.NumberFormat;
import java.time.LocalDate;
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
    @Autowired
    CurrencyService currencyService;
    @Autowired
    SalesReportRepository salesReportRepository;
    @Value("${smsa.file.location}")
    String sampleFileLocalLocation;

    private static final Logger logger = LoggerFactory.getLogger(ExcelService.class);

    public HashMap<String,List<InvoiceDetails>> saveInvoicesToDatabase(MultipartFile file, ExcelImportDto excelImportDto) throws Exception {
        HashMap<String,List<InvoiceDetails>> invoicesHashmap = new HashMap<>();

        List<InvoiceDetails> invoicesWithAccount = new ArrayList<>();
        List<InvoiceDetails> invoicesWithoutAccount = new ArrayList<>();

        Map<String, List<InvoiceDetails>> filterd;
        try {
            filterd = filterRowsByAccountNumber(file, excelImportDto,invoicesWithAccount,invoicesWithoutAccount);
        } catch (Exception e) {

            throw new Exception(e.getMessage());
        }

        for (List<InvoiceDetails> invoiceDetailsList : filterd.values()) {
            invoiceDetailsRepository.saveAll(invoiceDetailsList);
        }
        invoicesHashmap.put("invoicesWithAccount",invoicesWithAccount);
        invoicesHashmap.put("invoicesWithoutAccount",invoicesWithoutAccount);
        return invoicesHashmap;
    }

    public Map<String, List<InvoiceDetails>> filterRowsByAccountNumber(MultipartFile multipartFile, ExcelImportDto excelImportDto,List<InvoiceDetails> invoiceWithAccount,List<InvoiceDetails> invoicesWithoutAccount) {

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

                        filterAccountAndNonAccountInvoice(invoiceDetails, accountNumber,invoiceWithAccount,invoicesWithoutAccount);

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

    private void filterAccountAndNonAccountInvoice(InvoiceDetails invoiceDetails, String accountNumber,List<InvoiceDetails> invoicesWithAccount,List<InvoiceDetails> invoicesWithoutAccount) {
        if (checkAccountNumberInCustomerTable(accountNumber)) {
            invoicesWithAccount.add(invoiceDetails);
        } else {
            invoicesWithoutAccount.add(invoiceDetails);
            Customer customer = createPsedoCustomer(accountNumber);
            customerRepository.save(customer);
        }
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
        Optional<Custom> custom = customRepository.findById(excelImportDto.getId());

        return SheetHistory.builder()
                .uniqueUUid(sheetId)
                .name(originalFilename)
                .isEmailSent(false)
                .custom(custom.get())
                .startDate(excelImportDto.getFormattedStartDate() != null ? excelImportDto.getFormattedStartDate() : null)
                .endDate(excelImportDto.getFormattedEndDate() != null ? excelImportDto.getFormattedEndDate() : null)
                .invoiceDate(excelImportDto.getInvoiceDate()!=null ?excelImportDto.getInvoiceDate():null)
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
                .customDeclarationNumber(row.get(16))
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

    public SalesReportHelperDto updateExcelFile(List<InvoiceDetails> invoiceDetailsList, Customer customer, String sheetUniqueId, Long invoiceNumber) throws Exception {
        Double totalChargesAsPerCustomDeclarationForm=0.0;
        Double smsaFeesCharges=0.0;
        Double totalAmount=0.0;
        Double vatOnsmsaFees=0.0;
        logger.info(String.format("Inside update excel method for account ", customer.getAccountNumber()));

        Map<String, List<InvoiceDetails>> filteredRowsMap;
        Custom custom = getSheetHistory(sheetUniqueId).getCustom();

        FileInputStream fileInputStream = new FileInputStream(sampleFileLocalLocation + "/sample.xlsx");
        Workbook newWorkBook = WorkbookFactory.create(fileInputStream);

        setSheetDetails(newWorkBook, customer, sheetUniqueId);

        Sheet invoiceDetailSheet = newWorkBook.getSheetAt(1);
        setInvoiceDetailsCellValues(invoiceDetailSheet, invoiceDetailsList, custom, customer);


        filteredRowsMap = hashMapHelper.filterRowsByMawbNumber(invoiceDetailsList);
        List<Map<String, Object>> calculatedValuesList = hashMapHelper.calculateValues(filteredRowsMap, customer, custom, invoiceNumber, sheetUniqueId);
        Map<String, Double> sumMap = hashMapHelper.sumNumericColumns(calculatedValuesList);


        Sheet summarySheet = newWorkBook.getSheetAt(0);
        populateCalculatedValues(summarySheet, calculatedValuesList, sheetUniqueId);
        populateSumValues(summarySheet, sumMap, customer);

        fileInputStream.close();

        for (Map<String,Object> singleRecord: calculatedValuesList) {
            totalChargesAsPerCustomDeclarationForm+=Double.parseDouble(singleRecord.get("VatAmountCustomDeclarationForm").toString());
            smsaFeesCharges+=Double.parseDouble(singleRecord.get("SMSAFeeCharges").toString());
            totalAmount+=Double.parseDouble(singleRecord.get("TotalAmount").toString());
            vatOnsmsaFees+=Double.parseDouble(singleRecord.get("VatOnSmsaFees").toString());


        }

        SalesReportHelperDto salesReportHelperDto = new SalesReportHelperDto();
        salesReportHelperDto.setTotalChargesAsPerCustomDeclarationForm(totalChargesAsPerCustomDeclarationForm);
        salesReportHelperDto.setSmsaFeesCharges(smsaFeesCharges);
        salesReportHelperDto.setVatOnSmsaFees(vatOnsmsaFees);
        salesReportHelperDto.setTotalAmount(totalAmount);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            newWorkBook.write(outputStream);
            salesReportHelperDto.setExcelFile(outputStream.toByteArray());
            return salesReportHelperDto;
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Failed to create Excel file.");
        }
    }

    private void setSheetDetails(Workbook workbook, Customer customer, String sheetUniqueId) {
        Sheet sheet = workbook.getSheetAt(0);
        setCommonSheetDetails(sheet, customer, sheetUniqueId);

        Sheet sheet1 = workbook.getSheetAt(1);
        setCommonSheetDetails(sheet1, customer, sheetUniqueId);
    }

    private void setCommonSheetDetails(Sheet sheet, Customer customer, String sheetUniqueId) {
        sheet.setDisplayGridlines(Boolean.FALSE);

        Cell customerNameCell = sheet.getRow(4).getCell(1);
        setCellValue(customerNameCell, customer.getNameEnglish());

        Cell accountNumberCell = sheet.getRow(5).getCell(1);
        setCellValue(accountNumberCell, customer.getAccountNumber());

        Cell invoicePeriodCell = sheet.getRow(6).getCell(1);
        setCellValue(invoicePeriodCell, helperService.generateInvoiceDatePeriod(sheetUniqueId));

        Cell invoiceDateCell = sheet.getRow(7).getCell(1);
        setCellValue(invoiceDateCell, helperService.generateInvoiceDate(sheetUniqueId));

        Cell currencyCell = sheet.getRow(8).getCell(1);
        setCellValue(currencyCell, customer.getInvoiceCurrency());
    }
    public void populateSumValues(Sheet summarySheet, Map<String, Double> sumMap, Customer customer) {
        CellStyle boldStyle = summarySheet.getWorkbook().createCellStyle();
        Font boldFont = summarySheet.getWorkbook().createFont();
        boldFont.setBold(true);
        boldStyle.setFont(boldFont);

        CellStyle rightAlignedStyle = summarySheet.getWorkbook().createCellStyle();
        rightAlignedStyle.setAlignment(HorizontalAlignment.RIGHT);

        try {
            int lastRowIndex = summarySheet.getLastRowNum();
            int startingRow = helperService.findStartingRow(summarySheet);

            if (startingRow > lastRowIndex) {
                startingRow = lastRowIndex + 1;
            }

            Row row = summarySheet.createRow(startingRow);

            List<String> sumColumns = getSumColumnsList();

            int columnIndex = 7; // Starting column index

            for (String sumColumn : sumColumns) {

                if (columnIndex == 13) {
                    columnIndex++; // Move to the next column without setting the value
                    continue; // Skip this iteration and move to the next one
                }

                Double sumValue = sumMap.get(sumColumn);

                if (sumValue != null) {
                    Cell cell = row.createCell(columnIndex);
                    setCellValue(cell, formatCurrency(sumValue));

                    // Apply the same cell style here
                    CellStyle style = summarySheet.getWorkbook().createCellStyle();
                    style.setAlignment(HorizontalAlignment.RIGHT);
                    style.setFont(boldFont);// Example of setting alignment
                    cell.setCellStyle(style);

                    columnIndex++; // Move to the next column
                }
            }
        } catch (ExcelMakingException e) {
            throw new RuntimeException("There was an issue in populating sum values in the summary file");
        }
    }

    private static List<String> getSumColumnsList() {
        return Arrays.asList(
                "TotalValueSum",
                "CustomerShipmentValueSum",
                "VatAmountCustomDeclarationFormSum",
                "CustomFormChargesSum",
                "OthersSum",
                "TotalChargesSum",
                "CustomDeclartionCurrency", //ignore
                "VatAmountCustomerCurrencySum",
                "CustomFormChargesCustomerCurrencySum",
                "OtherCustomerCurrencySum",
                "TotalChargesCustomerCurrencySum"
        );
    }

    private void setCellValue(Cell cell, Double value) {
        cell.setCellValue(value);
    }
    private void setSummarySheetCellValues(Sheet summarySheet, Customer customer, String sheetUniqueId,Long invoiceNumber) throws RuntimeException {
        try {
            Cell invoiceNumberCell = summarySheet.getRow(2).getCell(9);
            setCellValue(invoiceNumberCell,"ECDV-"+invoiceNumber);

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

    private void setInvoiceDetailsCellValues(Sheet invoiceDetailSheet, List<InvoiceDetails> invoiceDetailsList,Custom custom,Customer customer) {
        int rowCount = 11;
        try {
            Currency currency = currencyService.findByCurrencyFromAndCurrencyTo(custom,customer);
            Double conversionRate = currency.getConversionRate();

            CellStyle rightAlignedStyle = invoiceDetailSheet.getWorkbook().createCellStyle();
            rightAlignedStyle.setAlignment(HorizontalAlignment.RIGHT);

            CellStyle centeredStyle = invoiceDetailSheet.getWorkbook().createCellStyle();
            centeredStyle.setAlignment(HorizontalAlignment.CENTER);


            for (InvoiceDetails invoiceDetails : invoiceDetailsList) {
                Row row = invoiceDetailSheet.createRow(rowCount);
                int columnCount = 0;

                Double vatAmount = invoiceDetails.getVatAmount() * conversionRate;
                Double customFormCharges = invoiceDetails.getCustomFormCharges() * conversionRate;
                Double other = invoiceDetails.getOther() * conversionRate;
                Double totalCharges = invoiceDetails.getTotalCharges() * conversionRate;


                setCellValue(row, columnCount, invoiceDetails.getInvoiceDetailsId().getMawb(),centeredStyle);
                setCellValue(row, ++columnCount, invoiceDetails.getInvoiceDetailsId().getManifestDate(),centeredStyle);
                setCellValue(row, ++columnCount, invoiceDetails.getInvoiceDetailsId().getAccountNumber(),centeredStyle);
                setCellValue(row, ++columnCount, invoiceDetails.getInvoiceDetailsId().getAwb(),centeredStyle);
                setCellValue(row, ++columnCount, invoiceDetails.getOrderNumber(),centeredStyle);
                setCellValue(row, ++columnCount, invoiceDetails.getOrigin(),centeredStyle);
                setCellValue(row, ++columnCount, invoiceDetails.getDestination(),centeredStyle);
                setCellValue(row, ++columnCount, invoiceDetails.getShippersName(),centeredStyle);
                setCellValue(row, ++columnCount, invoiceDetails.getConsigneeName(),centeredStyle);
                setCellValue(row, ++columnCount, invoiceDetails.getWeight(),centeredStyle);
                setCellValue(row, ++columnCount, formatCurrency(invoiceDetails.getDeclaredValue()),rightAlignedStyle);
                setCellValue(row, ++columnCount, formatCurrency(invoiceDetails.getValueCustom()),rightAlignedStyle);
                setCellValue(row, ++columnCount, formatCurrency(invoiceDetails.getVatAmount()),rightAlignedStyle);
                setCellValue(row, ++columnCount, formatCurrency(invoiceDetails.getCustomFormCharges()),rightAlignedStyle);
                setCellValue(row, ++columnCount, formatCurrency(invoiceDetails.getOther()),rightAlignedStyle);
                setCellValue(row, ++columnCount, formatCurrency(invoiceDetails.getTotalCharges()),rightAlignedStyle);
                setCellValue(row, ++columnCount,custom.getCurrency(),centeredStyle);
                setCellValue(row, ++columnCount, formatCurrency(vatAmount),rightAlignedStyle);
                setCellValue(row, ++columnCount, formatCurrency(customFormCharges),rightAlignedStyle);
                setCellValue(row, ++columnCount, formatCurrency(other),rightAlignedStyle);
                setCellValue(row, ++columnCount, formatCurrency(totalCharges),rightAlignedStyle);
                setCellValue(row, ++columnCount, invoiceDetails.getCustomDeclarationNumber(),centeredStyle);
                setCellValue(row,++columnCount,invoiceDetails.getRef(),centeredStyle);
                setCellValue(row, ++columnCount, invoiceDetails.getCustomDeclarationDate(),centeredStyle);

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
    private void populateCalculatedValues(Sheet summarySheet, List<Map<String, Object>> calculatedValuesList, String sheetUniqueId) {
        try {


            int startingRow = 11;
            List<String> columnNames = getColumnNamesList();
            Map<String, String> columnMapping = getColumnMapping();

            for (Map<String, Object> calculatedValuesMap : calculatedValuesList) {
                Row row = summarySheet.createRow(startingRow);

                for (int columnIndex = 0; columnIndex < columnNames.size(); columnIndex++) {
                    String columnName = columnNames.get(columnIndex);
                    String mapKey = columnMapping.get(columnName);

                    if (mapKey != null && calculatedValuesMap.containsKey(mapKey)) {
                        Object value = calculatedValuesMap.get(mapKey);
                        Cell cell = row.createCell(columnIndex);

                        if (value instanceof Double) {
                            setCellValue(cell, formatCurrency((Double) value));

                            // Apply cell style for currency values
                            CellStyle currencyStyle = summarySheet.getWorkbook().createCellStyle();
                            currencyStyle.setAlignment(HorizontalAlignment.RIGHT);
                            cell.setCellStyle(currencyStyle);
                        } else {
                            setCellValue(cell, value != null ? value.toString() : "");
                            CellStyle centerStyle = summarySheet.getWorkbook().createCellStyle();
                            centerStyle.setAlignment(HorizontalAlignment.CENTER);
                            cell.setCellStyle(centerStyle);
                        }
                    }
                }

                startingRow++; // Move to the next row
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

    private void setCellValue(Row row, int columnCount, Object value, CellStyle cellStyle) {
        Cell cell = row.createCell(columnCount);
        cell.setCellValue(value != null ? value.toString() : "");
        cell.setCellStyle(cellStyle);
    }

    private static Map<String, String> getColumnMapping() {
        Map<String, String> columnMapping = new LinkedHashMap<>();
        // Column names list
        List<String> columnNames = getColumnNamesList();
        // Add mappings for columns with potentially duplicate keys
        columnMapping.put("Invoice Type", "InvoiceType");
        columnMapping.put("Custom Port", "CustomPort");
        columnMapping.put("Custom Declartion Date", "CustomDeclarationDate");
        columnMapping.put("Invoice#", "InvoiceNumber");
        columnMapping.put("MAWB Number", "MawbNumber");
        columnMapping.put("Custom Declartion Currency", "CustomDeclarationCurrency");
        columnMapping.put("Custom Declartion#", "CustomDeclarationNumber");
        columnMapping.put("Total AWB Count", "TotalAwbCount");
        columnMapping.put("Total Value", "TotalValue");
        columnMapping.put("Value (Custom)", "CustomerShipmentValue");
        columnMapping.put("VAT Amount", "VatAmountCustomDeclarationForm");
        columnMapping.put("Custom Form Charges","CustomFormCharges");
        columnMapping.put("Other", "Others"); // Duplicate key
        columnMapping.put("Total Charges", "TotalCharges");

        columnMapping.put("VAT Amount-","VatAmountCustomerCurrency" );
        columnMapping.put("Custom Form Charges-","CustomFormChargesCustomerCurrency");
        columnMapping.put("Other-", "OtherCustomerCurrency");
        columnMapping.put("Total Charges-", "TotalChargesCustomerCurrency");
        columnMapping.put("Total Amount", "TotalAmountCustomerCurrency");
        return columnMapping;
    }
    private static List<String> getColumnNamesList() {
        List<String> columnNames = new ArrayList<>();
        columnNames.add("Invoice Type");
        columnNames.add("Custom Port");
        columnNames.add("Custom Declartion Date");
        columnNames.add("Invoice#");
        columnNames.add("MAWB Number");
        columnNames.add("Custom Declartion#");
        columnNames.add("Total AWB Count");
        columnNames.add("Total Value");
        columnNames.add("Value (Custom)");
        columnNames.add("VAT Amount");
        columnNames.add("Custom Form Charges");
        columnNames.add("Other");
        columnNames.add("Total Charges");
        columnNames.add("Custom Declartion Currency");
        columnNames.add("VAT Amount-");
        columnNames.add("Custom Form Charges-");
        columnNames.add("Other-");
        columnNames.add("Total Charges-");
        columnNames.add("Total Amount");
        return columnNames;
    }

    Workbook newWorkBook;
    Sheet summarySheet;
    private List<SalesReport> salesReports;
    public Resource excelData(List<Long> salesReportIds) throws IOException {

        this.salesReports = this.salesReportRepository.findAllByIdIn(salesReportIds);
        FileInputStream fileInputStream = new FileInputStream(sampleFileLocalLocation + "/excel.xlsx");
        newWorkBook = WorkbookFactory.create(fileInputStream);
        summarySheet = newWorkBook.getSheetAt(0);
        int rowCount = 1;

        CellStyle rightAlignedStyle = newWorkBook.createCellStyle();
        rightAlignedStyle.setAlignment(HorizontalAlignment.RIGHT);
        CellStyle style = newWorkBook.createCellStyle();
        for (SalesReport salesReport : salesReports) {
            Row row = summarySheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, salesReport.getId().toString(), style);
            createCell(row, columnCount++, salesReport.getInvoiceNumber().toString(), style);
            createCell(row, columnCount++, salesReport.getCustomerAccountNumber().toString(), style);
            createCell(row, columnCount++, salesReport.getCustomerName().toString(), style);
            createCell(row, columnCount++, salesReport.getCustomerRegion().toString(), style);
            createCell(row, columnCount++, salesReport.getPeriod().toString(), style);
            createCell(row, columnCount++, formatCurrency(salesReport.getTotalChargesAsPerCustomerDeclarationForm()).toString(), rightAlignedStyle);
            createCell(row, columnCount++, salesReport.getSmsaFeeCharges().toString(), rightAlignedStyle);
            createCell(row, columnCount++, salesReport.getVatOnSmsaFees().toString(), rightAlignedStyle);
            createCell(row, columnCount++, salesReport.getTotalAmount().toString(), rightAlignedStyle);
            createCell(row, columnCount++, salesReport.getInvoiceCurrency().toString(), style);
            createCell(row, columnCount++, salesReport.getCreatedAt().toString(), style);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        newWorkBook.write(byteArrayOutputStream);
        byte[] workbookBytes = byteArrayOutputStream.toByteArray();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(workbookBytes);

        Resource resource = new InputStreamResource(byteArrayInputStream);
        return resource;

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        summarySheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

//    public Resource export() throws IOException {
//
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        newWorkBook.write(byteArrayOutputStream);
//        byte[] workbookBytes = byteArrayOutputStream.toByteArray();
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(workbookBytes);
//        Resource resource = new InputStreamResource(byteArrayInputStream);
//        return resource;
//    }



}



