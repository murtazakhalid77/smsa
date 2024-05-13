package com.smsa.backend.service;


import com.smsa.backend.Exception.*;
import com.smsa.backend.dto.ExcelImportDto;
import com.smsa.backend.dto.SalesReportHelperDto;
import com.smsa.backend.model.*;
import com.smsa.backend.model.Currency;
import com.smsa.backend.repository.*;
import com.smsa.backend.security.util.ExcelImportHelper;
import com.smsa.backend.security.util.HashMapHelper;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
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
    CustomerService customerService;

    @Autowired
    CustomService customService;

    @Autowired
    RegionService regionService;

    @Autowired
    UserService userService;

    @Autowired
    CurrencyAuditLogService currencyAuditLogService;

    @Autowired
    SalesReportRepository salesReportRepository;

    @Autowired
    ManifestDataRepository manifestDataRepository;

    @Autowired
    StorageService storageService;
    @Value("${smsa.file.location}")
    String sampleFileLocalLocation;

    private static final String DUPLICATE_AWB_STRING="There was a duplication of these AWBs ";

    private static final Logger logger = LoggerFactory.getLogger(ExcelService.class);

    public HashMap<String,List<InvoiceDetails>> saveInvoicesToDatabase(MultipartFile file, ExcelImportDto excelImportDto, Map<String, String> userInputMap) throws Exception {

        if(!this.helperService.checkExcelSize(file)){
            throw new ExcelImportException("File size should not be greater then 2MB");
        }

        HashMap<String,List<InvoiceDetails>> invoicesHashmap = new HashMap<>();

        List<InvoiceDetails> invoicesWithAccount = new ArrayList<>();
        List<InvoiceDetails> invoicesWithoutAccount = new ArrayList<>();

        Map<String, List<InvoiceDetails>> filterd;

        filterd = filterRowsByAccountNumber(file, excelImportDto,invoicesWithAccount,invoicesWithoutAccount,userInputMap);


        for (List<InvoiceDetails> invoiceDetailsList : filterd.values()) {
            invoiceDetailsRepository.saveAll(invoiceDetailsList);
        }
        invoicesHashmap.put("invoicesWithAccount",invoicesWithAccount);
        invoicesHashmap.put("invoicesWithoutAccount",invoicesWithoutAccount);
        return invoicesHashmap;
    }
    public Map<String, List<InvoiceDetails>> filterRowsByAccountNumber(MultipartFile multipartFile, ExcelImportDto excelImportDto, List<InvoiceDetails> invoiceWithAccount, List<InvoiceDetails> invoicesWithoutAccount, Map<String, String> userInputMap) {

        LocalDate currentDate = getTodaysDate();
        String sheetId = getUuId();

        Map<String, List<InvoiceDetails>> mappedRowsMap = new HashMap<>();

        List<List<String>> rowsToBeFiltered;

        String originalFilename =  multipartFile.getOriginalFilename();

        validateSheetName(originalFilename);

                rowsToBeFiltered = ExcelImportHelper.parseExcelFile(multipartFile);

        findDuplicatesOfAwb(rowsToBeFiltered);

        HashMap<String,Long> mawbCounts=getMawbCounts(rowsToBeFiltered);

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
                        InvoiceDetails invoiceDetails;

                        if(!excelImportDto.isCustomPlusExcel()){
                             invoiceDetails = mapToDomain(row);
                        }else {
                             invoiceDetails = mapToDomainForCustomPlus(row,excelImportDto,rowsToBeFiltered.size(),userInputMap,mawbCounts);
                        }
                        // Map the fields from the row list to the InvoiceDetails object


                        mapHelperFields(invoiceDetails, sheetId, currentDate);

                        // Check if the account number exists in the accountNumberUuidMap
                        String accountNumber = getAccountNumber(invoiceDetails);
                        String customerUniqueId = generateCustomerUniqueId(accountNumber);

                        invoiceDetails.setCustomerUniqueId(customerUniqueId);

                        // Add the InvoiceDetails object to the list associated with the common key in the map
                        mappedRowsMap.get(commonKey).add(invoiceDetails);

                        filterAccountAndNonAccountInvoice(invoiceDetails, accountNumber,invoiceWithAccount,invoicesWithoutAccount);

                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("Error mapping row to InvoiceDetails: " + e.getMessage());
                        throw new RuntimeException("There was a problem in filtering Invoices against account Numbers");
                    }
                }
            }
        }

        SheetHistory sheetHistory = createSheetHistory(originalFilename, excelImportDto, sheetId);
        String excelFileName = multipartFile.getOriginalFilename();
        sheetHistory.setExcelDownload(excelFileName);

        SheetHistory sheetHistorySaved = sheetHistoryRepository.save(sheetHistory);
        if(sheetHistorySaved!=null){
            try {
                storageService.uploadFile(multipartFile.getBytes(), excelFileName);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return mappedRowsMap;

    }

    public Set<String> extractUniqueFromTheExcel(MultipartFile file) {

        if(!this.helperService.checkExcelSize(file)){
            throw new ExcelImportException("File size should not be greater then 2MB");
        }
        List<List<String>> rowsToBeFiltered=ExcelImportHelper.parseExcelFile(file);

        if (!rowsToBeFiltered.isEmpty()) {
            rowsToBeFiltered.remove(0);
        }
        Set<String> uniqueMawb= new HashSet<>();
        for (List<String> row : rowsToBeFiltered) {
            uniqueMawb.add(row.get(0));
        }
        logger.info(String.valueOf(uniqueMawb));
        return uniqueMawb;
    }//continue from here of aligning the sum values in the sumamary excel


    private String getAccountNumber(InvoiceDetails invoiceDetails) {
        if (invoiceDetails!=null && invoiceDetails.getInvoiceDetailsId()!=null){
            return invoiceDetails.getInvoiceDetailsId().getAccountNumber();
        }
        throw new RecordNotFoundException("Invoice detail is null");
    }

    public void findDuplicatesOfAwb(List<List<String>> data) {
        Map<String, Integer> valueCountMap = new HashMap<>();
        List<String> duplicates = new ArrayList<>();

        for (List<String> row : data) {
            if (row.size() > 3) { // Make sure the row has at least 3 columns
                String value = row.get(3); // Third column (index 2)

                // Increment the count for the value in the hashmap
                valueCountMap.put(value, valueCountMap.getOrDefault(value, 0) + 1);
            }
        }

        for (Map.Entry<String, Integer> entry : valueCountMap.entrySet()) {
            if (entry.getValue() > 1) {
                duplicates.add(entry.getKey());
            }
        }

        if (!duplicates.isEmpty()) {
            String duplicateAwbList = String.join(", ", duplicates);
            String errorMessage = DUPLICATE_AWB_STRING + " (" + duplicateAwbList + ") in the excel, couldn't upload the file";
            throw new AwbDublicateException(errorMessage);
        }

        Set<String> keys = valueCountMap.keySet();

        // Check for duplicates in the database using a single query with the IN keyword
        List<String> dbDuplicates = invoiceDetailsRepository.findDuplicateAwbs(keys);

        if (!dbDuplicates.isEmpty()) {
            String dbDuplicatesAwbList=String.join(",",dbDuplicates);
            String errorMessage = DUPLICATE_AWB_STRING + "(" + dbDuplicatesAwbList + ") in the database, couldn't upload the file";
            throw new AwbDublicateException(errorMessage);
        }

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

    private void mapHelperFields(InvoiceDetails invoiceDetails, String sheetId, LocalDate currentDate) {

        invoiceDetails.setSheetTimesStamp(currentDate);
        invoiceDetails.setSheetUniqueId(sheetId);
        invoiceDetails.setIsSentInMail(Boolean.FALSE);
        invoiceDetails.setCustomerTimestamp(currentDate);
    }

    private SheetHistory createSheetHistory(String originalFilename, ExcelImportDto excelImportDto, String sheetId) {
        Optional<Custom> custom = customRepository.findById(excelImportDto.getId());

        return SheetHistory.builder()
                .uniqueUUid(sheetId)
                .name(originalFilename)
                .isEmailSent(false)
                .custom(custom.get())
                .startDate(excelImportDto.getFormattedStartDate() != null ? excelImportDto.getFormattedStartDate() : "")
                .endDate(excelImportDto.getFormattedEndDate() != null ? excelImportDto.getFormattedEndDate() : "")
                .invoiceDate(excelImportDto.getInvoiceDate()!=null ?excelImportDto.getInvoiceDate(): "")
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
        if (customer.isPresent()){
            return true;
        }
       return false;
    }


    private InvoiceDetails mapToDomain(List<String> row) {

        InvoiceDetailsId invoiceDetailsId = InvoiceDetailsId.builder()
                .mawb(row.get(0))
                .manifestDate(row.get(1))
                .accountNumber(row.get(2))
                .awb(row.get(3))
                .build();

        return InvoiceDetails.builder()
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
    }

    private HashMap<String, Long> getMawbCounts(List<List<String>> rowsToBeFiltered) {
        HashMap<String, Long> mawbCount = new HashMap<>();


        for (List<String> row : rowsToBeFiltered) {
            String mawb = row.get(0);

            mawbCount.put(mawb, mawbCount.getOrDefault(mawb, 0L) + 1);
        }

        return mawbCount;
    }

    private InvoiceDetails mapToDomainForCustomPlus(List<String> row, ExcelImportDto excelImportDto, Integer totalRows, Map<String, String> userInputMap, HashMap<String, Long> mawbCounts) {

        InvoiceDetailsId invoiceDetailsId = InvoiceDetailsId.builder()
                .mawb(row.get(0))
                .manifestDate(row.get(1))
                .accountNumber(row.get(2))
                .awb(row.get(3))
                .build();

        double valueCustom = parseDoubleOrDefault(row.get(11), 0.0); //541

        Optional<Custom> custom = customRepository.findByCustomPort(excelImportDto.getCustomPort());//5

        double others=parseDoubleOrDefault(row.get(13), 0.0); //0.0

        double customFormValueCalculated=
                Double.parseDouble(
                        String.format("%3f",
                                ((double)
                                        Double.valueOf(userInputMap.get(row.get(0))) /
                                        mawbCounts.get(row.get(0)))));

        double vatAmount=(valueCustom+customFormValueCalculated)*custom.get().getSmsaFeeVat()/100;
        double total=vatAmount+customFormValueCalculated+others;


        return InvoiceDetails.builder()
                .invoiceDetailsId(invoiceDetailsId)
                .orderNumber(row.get(4))
                .origin(row.get(5))
                .destination(row.get(6))
                .shippersName(row.get(7))
                .consigneeName(row.get(8))
                .weight(parseDoubleOrDefault(row.get(9), 0.0))
                .declaredValue(parseDoubleOrDefault(row.get(10), 0.0))
                .valueCustom(parseDoubleOrDefault(row.get(11), 0.0))
                .vatAmount(vatAmount)
                .customFormCharges(customFormValueCalculated)
                .totalCharges(total)
                .customDeclarationNumber(row.get(12))
                .other(parseDoubleOrDefault(row.get(13), 0.0))
                .ref(row.get(14))
                .customDeclarationDate(row.get(15))
                .build();
    }
    public SalesReportHelperDto updateExcelFile(List<InvoiceDetails> invoiceDetailsList, Customer customer, String sheetUniqueId, Long invoiceNumber) throws Exception {
        logger.info(String.format("Inside update excel method for account %s ", customer.getAccountNumber()));

        try{
            Map<String, List<InvoiceDetails>> filteredRowsMap;
            Custom custom = getSheetHistory(sheetUniqueId).getCustom();

            FileInputStream fileInputStream = new FileInputStream(sampleFileLocalLocation + "/sample.xlsx");
            Workbook newWorkBook = WorkbookFactory.create(fileInputStream);

            setSheetDetails(newWorkBook, customer, sheetUniqueId);

            Sheet invoiceDetailSheet = newWorkBook.getSheetAt(1);
            setInvoiceDetailsCellValues(invoiceDetailSheet, invoiceDetailsList, custom, customer);


            filteredRowsMap = hashMapHelper.filterRowsByMawbNumber(invoiceDetailsList);
            List<Map<String, Object>> calculatedValuesList = hashMapHelper.calculateValues(filteredRowsMap,
                    customer,
                    custom,
                    invoiceNumber,
                    sheetUniqueId);
            Map<String, Double> sumMap = hashMapHelper.sumNumericColumns(calculatedValuesList);


            Sheet summarySheet = newWorkBook.getSheetAt(0);
            populateCalculatedValues(summarySheet, calculatedValuesList, sheetUniqueId);
            populateSumValues(summarySheet, sumMap, customer);

            fileInputStream.close();

            SalesReportHelperDto salesReportHelperDto = calculateSalesReportHelperDto(calculatedValuesList);

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                newWorkBook.write(outputStream);
                salesReportHelperDto.setExcelFile(outputStream.toByteArray());
                return salesReportHelperDto;
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException(e.getMessage());
            }
        }
           catch (Exception e){
            throw new ExcelMakingException(e.getMessage());
           }
    }
    public SalesReportHelperDto calculateSalesReportHelperDto(List<Map<String, Object>> calculatedValuesList) {
        Double totalChargesAsPerCustomDeclarationForm = 0.0;
        Double smsaFeesCharges = 0.0;
        Double totalAmount = 0.0;
        Double vatOnsmsaFees = 0.0;

        for (Map<String, Object> singleRecord : calculatedValuesList) {
                totalChargesAsPerCustomDeclarationForm += Double.parseDouble(singleRecord.get("TotalCharges").toString());
            smsaFeesCharges += Double.parseDouble(singleRecord.get("SMSAFeeCharges").toString());
            totalAmount += Double.parseDouble(singleRecord.get("TotalAmount").toString());
            vatOnsmsaFees += Double.parseDouble(singleRecord.get("VatOnSmsaFees").toString());
        }

        return SalesReportHelperDto.builder()
                .totalChargesAsPerCustomDeclarationForm(totalChargesAsPerCustomDeclarationForm)
                .smsaFeesCharges(smsaFeesCharges)
                .vatOnSmsaFees(vatOnsmsaFees)
                .TotalAmount(totalAmount)
                .build();
    }


    private void setSheetDetails(Workbook workbook, Customer customer, String sheetUniqueId) {
        try{
            Sheet sheet = workbook.getSheetAt(0);
            setCommonSheetDetails(sheet, customer, sheetUniqueId);

            Sheet sheet1 = workbook.getSheetAt(1);
            setCommonSheetDetails(sheet1, customer, sheetUniqueId);
        }catch (Exception e){
            e.printStackTrace();
            throw new ExcelMakingException(e.getMessage());
        }

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
    public void populateSumValues(Sheet summarySheet, Map<String, Double> sumMap, Customer customer) throws Exception {
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
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExcelMakingException(e.getMessage());
        }
    }

    private static List<String> getSumColumnsList() {
        return Arrays.asList(
                "TotalValueSum",
                "CustomerShipmentValueSum",
                "CustomFormChargesSum",
                "VatAmountCustomDeclarationFormSum",
                "OthersSum",
                "TotalChargesSum",
                "CustomDeclartionCurrency", //ignore
                "CustomFormChargesCustomerCurrencySum",
                "VatAmountCustomerCurrencySum",
                "OtherCustomerCurrencySum",
                "TotalChargesCustomerCurrencySum"
        );
    }


    private void setInvoiceDetailsCellValues(Sheet invoiceDetailSheet, List<InvoiceDetails> invoiceDetailsList,Custom custom,Customer customer) throws Exception {
        int rowCount = 11;


            Currency currency = currencyService.findByCurrencyFromAndCurrencyTo(custom,customer);
            Double conversionRate = currency.getConversionRate();

            try {
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
                    setCellValue(row, ++columnCount, formatCurrency(invoiceDetails.getCustomFormCharges()),rightAlignedStyle);
                    setCellValue(row, ++columnCount, formatCurrency(invoiceDetails.getVatAmount()),rightAlignedStyle);

                    setCellValue(row, ++columnCount, formatCurrency(invoiceDetails.getOther()),rightAlignedStyle);
                    setCellValue(row, ++columnCount, formatCurrency(invoiceDetails.getTotalCharges()),rightAlignedStyle);
                    setCellValue(row, ++columnCount,custom.getCurrency(),centeredStyle);
                    setCellValue(row, ++columnCount, formatCurrency(customFormCharges),rightAlignedStyle);
                    setCellValue(row, ++columnCount, formatCurrency(vatAmount),rightAlignedStyle);
                    setCellValue(row, ++columnCount, formatCurrency(other),rightAlignedStyle);
                    setCellValue(row, ++columnCount, formatCurrency(totalCharges),rightAlignedStyle);
                    setCellValue(row, ++columnCount, invoiceDetails.getCustomDeclarationNumber(),centeredStyle);
                    setCellValue(row,++columnCount,invoiceDetails.getRef(),centeredStyle);
                    setCellValue(row, ++columnCount, invoiceDetails.getCustomDeclarationDate(),centeredStyle);

                    rowCount++;
                }

            }catch (Exception e){
                e.printStackTrace();
                throw new ExcelMakingException(e.getMessage());
            }

    }

    private String formatCurrency(Double value) {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(value);
    }
    private void populateCalculatedValues(Sheet summarySheet, List<Map<String, Object>> calculatedValuesList, String sheetUniqueId) throws Exception {
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

        } catch (Exception e) {
            e.printStackTrace();
            throw new ExcelMakingException(e.getMessage());
        }
    }

    private static Map<String, String> getColumnMapping() {
        Map<String, String> columnMapping = new LinkedHashMap<>();

        columnMapping.put("Invoice Type", "InvoiceType");
        columnMapping.put("Custom Port", "CustomPort");
        columnMapping.put("Custom Declartion Date", "CustomDeclarationDate");
        columnMapping.put("Invoice#", "InvoiceNumber");
        columnMapping.put("MAWB Number", "MawbNumber");
        columnMapping.put("Custom Declartion Currency", "CustomDeclarationCurrency");
        columnMapping.put("Custom Declartion#", "CustomDeclarationNumber");
        columnMapping.put("Total AWB Count", "TotalAwbCount");
        columnMapping.put("Total Declared Value", "TotalDeclaredValue");
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
        columnNames.add("Total Declared Value");
        columnNames.add("Value (Custom)");
        columnNames.add("Custom Form Charges");
        columnNames.add("VAT Amount");
        columnNames.add("Other");
        columnNames.add("Total Charges");
        columnNames.add("Custom Declartion Currency");
        columnNames.add("Custom Form Charges-");
        columnNames.add("VAT Amount-");
        columnNames.add("Other-");
        columnNames.add("Total Charges-");
        columnNames.add("Total Amount");
        return columnNames;
    }



    public Resource salesReportExcel(List<Long> salesReportIds) throws IOException {

        try{
            List<SalesReport> salesReports = this.salesReportRepository.findAllByIdIn(salesReportIds);
            FileInputStream fileInputStream = new FileInputStream(sampleFileLocalLocation + "/excel.xlsx");
            Workbook  newWorkBook = WorkbookFactory.create(fileInputStream);
            Sheet summarySheet= newWorkBook.getSheetAt(0);
            int rowCount = 1;

            CellStyle rightAlignedStyle = newWorkBook.createCellStyle();
            rightAlignedStyle.setAlignment(HorizontalAlignment.RIGHT);
            CellStyle style = newWorkBook.createCellStyle();

            for (SalesReport salesReport : salesReports) {
                Row row = summarySheet.createRow(rowCount++);
                int columnCount = 0;

                createCell(row, columnCount++, salesReport.getId().toString(), style);
                createCell(row, columnCount++, salesReport.getInvoiceNumber(), style);
                createCell(row, columnCount++, salesReport.getCustomerAccountNumber(), style);
                createCell(row, columnCount++, salesReport.getCustomerName(), style);
                createCell(row, columnCount++, salesReport.getCustomerRegion(), style);
                createCell(row, columnCount++, salesReport.getPeriod(), style);
                createCell(row, columnCount++, formatCurrency(salesReport.getTotalChargesAsPerCustomerDeclarationForm()).toString(), rightAlignedStyle);
                createCell(row, columnCount++, salesReport.getSmsaFeeCharges().toString(), rightAlignedStyle);
                createCell(row, columnCount++, salesReport.getVatOnSmsaFees().toString(), rightAlignedStyle);
                createCell(row, columnCount++, salesReport.getTotalAmount().toString(), rightAlignedStyle);
                createCell(row, columnCount++, salesReport.getInvoiceCurrency(), style);
                createCell(row, columnCount++, salesReport.getCreatedAt().toString(), style);
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            newWorkBook.write(byteArrayOutputStream);

            byte[] workbookBytes = byteArrayOutputStream.toByteArray();

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(workbookBytes);

            Resource resource = new InputStreamResource(byteArrayInputStream);
            return resource;

        }
       catch (Exception e){
            e.printStackTrace();
            throw new SalesReportException(e.getMessage());
       }

    }

    public Resource customerExcelDownload() throws IOException {

        try{
            List<Customer> customers = this.customerService.getAllCustomer();
            FileInputStream fileInputStream = new FileInputStream(sampleFileLocalLocation + "/customer.xlsx");
            Workbook  newWorkBook = WorkbookFactory.create(fileInputStream);
            Sheet summarySheet= newWorkBook.getSheetAt(0);
            int rowCount = 1;

            CellStyle rightAlignedStyle = newWorkBook.createCellStyle();
            rightAlignedStyle.setAlignment(HorizontalAlignment.RIGHT);
            CellStyle style = newWorkBook.createCellStyle();

            for (Customer customer : customers) {
                Row row = summarySheet.createRow(rowCount++);
                int columnCount = 0;

                createCell(row, columnCount++, customer.getAccountNumber() != null ? customer.getAccountNumber().toString() : " ", style);
                createCell(row, columnCount++, customer.getInvoiceCurrency() != null ? customer.getInvoiceCurrency().toString(): " ", style);
                createCell(row, columnCount++, customer.getRegion() != null ? customer.getRegion().getCustomerRegion().toString(): " ", style);
                createCell(row, columnCount++, customer.getSmsaServiceFromSAR() != null ? customer.getSmsaServiceFromSAR().toString(): " ", rightAlignedStyle);
                createCell(row, columnCount++, customer.getStatus() != null ? customer.getStatus().toString(): " ", style);
                createCell(row, columnCount++, customer.getNameArabic() != null ? customer.getNameArabic().toString(): " ", style);
                createCell(row, columnCount++, customer.getNameEnglish() != null ? customer.getNameEnglish().toString(): " ", style);
                createCell(row, columnCount++, customer.getEmail() != null ? customer.getEmail().toString(): " ", style);
                createCell(row, columnCount++, customer.getCcMail() != null ? customer.getCcMail().toString(): " ", style);
                createCell(row, columnCount++, customer.getVatNumber() != null ? customer.getVatNumber().toString(): " ", rightAlignedStyle);
                createCell(row, columnCount++, customer.getAddress() != null ? customer.getAddress().toString(): " ", style);
                createCell(row, columnCount++, customer.getPoBox() != null ? customer.getPoBox().toString(): " ", style);
                createCell(row, columnCount++, customer.getCountry() != null ? customer.getCountry().toString(): " ", style);
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            newWorkBook.write(byteArrayOutputStream);

            byte[] workbookBytes = byteArrayOutputStream.toByteArray();

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(workbookBytes);

            Resource resource = new InputStreamResource(byteArrayInputStream);
            return resource;

        }
        catch (Exception e){
            e.printStackTrace();
            throw new SalesReportException(e.getMessage());
        }

    }

    public Resource currencyExcelDownload() throws IOException {

        try{
            List<Currency> currencies = this.currencyService.getAllCurrency();
            FileInputStream fileInputStream = new FileInputStream(sampleFileLocalLocation + "/currency.xlsx");
            Workbook  newWorkBook = WorkbookFactory.create(fileInputStream);
            Sheet summarySheet= newWorkBook.getSheetAt(0);
            int rowCount = 1;

            CellStyle rightAlignedStyle = newWorkBook.createCellStyle();
            rightAlignedStyle.setAlignment(HorizontalAlignment.RIGHT);
            CellStyle style = newWorkBook.createCellStyle();

            for (Currency currency : currencies) {
                Row row = summarySheet.createRow(rowCount++);
                int columnCount = 0;

                createCell(row, columnCount++, currency.getId() != null ? currency.getId().toString() : " ", style);
                createCell(row, columnCount++, currency.getCurrencyFrom() != null ? currency.getCurrencyFrom().toString(): " ", style);
                createCell(row, columnCount++, currency.getCurrencyTo() != null ? currency.getCurrencyTo().toString(): " ", style);
                createCell(row, columnCount++, currency.getConversionRate() != null ? currency.getConversionRate().toString(): " ", rightAlignedStyle);
                createCell(row, columnCount++, currency.getCreatedBy() != null ? currency.getCreatedBy().toString(): " ", style);
                createCell(row, columnCount++, currency.getCreatedAt() != null ? currency.getCreatedAt().toString(): " ", style);
                createCell(row, columnCount++, currency.getUpdatedBy() != null ? currency.getUpdatedBy().toString(): " ", style);
                createCell(row, columnCount++, currency.getUpdatedAt() != null ? currency.getUpdatedAt().toString(): " ", style);
                createCell(row, columnCount++, currency.getIsPresent() != null ? currency.getIsPresent().toString(): " ", style);
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            newWorkBook.write(byteArrayOutputStream);

            byte[] workbookBytes = byteArrayOutputStream.toByteArray();

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(workbookBytes);

            Resource resource = new InputStreamResource(byteArrayInputStream);
            return resource;

        }
        catch (Exception e){
            e.printStackTrace();
            throw new SalesReportException(e.getMessage());
        }

    }

    public Resource userExcelDownload() throws IOException {

        try{
            List<User> users = this.userService.getAllUsers();
            FileInputStream fileInputStream = new FileInputStream(sampleFileLocalLocation + "/user.xlsx");
            Workbook  newWorkBook = WorkbookFactory.create(fileInputStream);
            Sheet summarySheet= newWorkBook.getSheetAt(0);
            int rowCount = 1;

            CellStyle rightAlignedStyle = newWorkBook.createCellStyle();
            rightAlignedStyle.setAlignment(HorizontalAlignment.RIGHT);
            CellStyle style = newWorkBook.createCellStyle();

            for (User user : users) {
                Row row = summarySheet.createRow(rowCount++);
                int columnCount = 0;

                createCell(row, columnCount++, user.getId() != null ? user.getId().toString() : " ", style);
                createCell(row, columnCount++, user.getName() != null ? user.getName().toString(): " ", style);
                createCell(row, columnCount++, user.getStatus(), style);
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            newWorkBook.write(byteArrayOutputStream);

            byte[] workbookBytes = byteArrayOutputStream.toByteArray();

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(workbookBytes);

            Resource resource = new InputStreamResource(byteArrayInputStream);
            return resource;

        }
        catch (Exception e){
            e.printStackTrace();
            throw new SalesReportException(e.getMessage());
        }

    }

    public Resource customExcelDownload() throws IOException {

        try{
            List<Custom> customs = this.customService.getAllCustoms();
            FileInputStream fileInputStream = new FileInputStream(sampleFileLocalLocation + "/custom.xlsx");
            Workbook  newWorkBook = WorkbookFactory.create(fileInputStream);
            Sheet summarySheet= newWorkBook.getSheetAt(0);
            int rowCount = 1;

            CellStyle rightAlignedStyle = newWorkBook.createCellStyle();
            rightAlignedStyle.setAlignment(HorizontalAlignment.RIGHT);
            CellStyle style = newWorkBook.createCellStyle();

            for (Custom custom : customs) {
                Row row = summarySheet.createRow(rowCount++);
                int columnCount = 0;

                createCell(row, columnCount++, custom.getId() != null ? custom.getId().toString() : " ", style);
                createCell(row, columnCount++, custom.getCustom() != null ? custom.getCustom().toString(): " ", style);
                createCell(row, columnCount++, custom.getCustomPort() != null ? custom.getCustomPort().toString(): " ", style);
                createCell(row, columnCount++, custom.getSmsaFeeVat() != null ? custom.getSmsaFeeVat().toString(): " ", rightAlignedStyle);
                createCell(row, columnCount++, custom.getCurrency() != null ? custom.getCurrency().toString(): " ", style);
                createCell(row, columnCount++, custom.isPresent(), style);
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            newWorkBook.write(byteArrayOutputStream);

            byte[] workbookBytes = byteArrayOutputStream.toByteArray();

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(workbookBytes);

            Resource resource = new InputStreamResource(byteArrayInputStream);
            return resource;

        }
        catch (Exception e){
            e.printStackTrace();
            throw new SalesReportException(e.getMessage());
        }

    }

    public Resource regionExcelDownload() throws IOException {

        try{
            List<Region> regions = this.regionService.getAllRegions();
            FileInputStream fileInputStream = new FileInputStream(sampleFileLocalLocation + "/region.xlsx");
            Workbook  newWorkBook = WorkbookFactory.create(fileInputStream);
            Sheet summarySheet= newWorkBook.getSheetAt(0);
            int rowCount = 1;

            CellStyle rightAlignedStyle = newWorkBook.createCellStyle();
            rightAlignedStyle.setAlignment(HorizontalAlignment.RIGHT);
            CellStyle style = newWorkBook.createCellStyle();

            for (Region region : regions) {
                Row row = summarySheet.createRow(rowCount++);
                int columnCount = 0;
                createCell(row, columnCount++, region.getId() != null ? region.getId().toString() : " ", style);
                createCell(row, columnCount++, region.getCustomerRegion() != null ? region.getCustomerRegion().toString() : " ", style);
                createCell(row, columnCount++, region.getDescription() != null ? region.getDescription().toString() : " ", style);
                createCell(row, columnCount++, region.getHeaderName() != null ? region.getHeaderName().toString() : " ", style);
                createCell(row, columnCount++, region.getStatus(), style);
                createCell(row, columnCount++, region.getVat() != null ? region.getVat().toString() : " ", rightAlignedStyle);
                createCell(row, columnCount++, region.getVatNumber() != null ? region.getVatNumber().toString() : " ", rightAlignedStyle);
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            newWorkBook.write(byteArrayOutputStream);

            byte[] workbookBytes = byteArrayOutputStream.toByteArray();

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(workbookBytes);

            Resource resource = new InputStreamResource(byteArrayInputStream);
            return resource;

        }
        catch (Exception e){
            e.printStackTrace();
            throw new SalesReportException(e.getMessage());
        }

    }


    public Resource currencyAuditDownload(Long id) throws IOException {

        try{
            List<CurrencyAuditLog> currencyAuditLogs = this.currencyAuditLogService.getCurrencyAuditLogById(id);
            FileInputStream fileInputStream = new FileInputStream(sampleFileLocalLocation + "/currency_audit_logs.xlsx");
            Workbook  newWorkBook = WorkbookFactory.create(fileInputStream);
            Sheet summarySheet= newWorkBook.getSheetAt(0);
            int rowCount = 1;

            CellStyle rightAlignedStyle = newWorkBook.createCellStyle();
            rightAlignedStyle.setAlignment(HorizontalAlignment.RIGHT);
            CellStyle style = newWorkBook.createCellStyle();

            for (CurrencyAuditLog currencyAuditLog : currencyAuditLogs) {
                Row row = summarySheet.createRow(rowCount++);
                int columnCount = 0;
                createCell(row, columnCount++, currencyAuditLog.getId() != null ? currencyAuditLog.getId().toString() : " ", style);
                createCell(row, columnCount++, currencyAuditLog.getCurrencyFrom() != null ? currencyAuditLog.getCurrencyFrom().toString() : " ", style);
                createCell(row, columnCount++, currencyAuditLog.getCurrencyTo() != null ? currencyAuditLog.getCurrencyTo().toString() : " ", style);
                createCell(row, columnCount++, currencyAuditLog.getConversionRate() != null ? currencyAuditLog.getConversionRate().toString() : " ", rightAlignedStyle);
                createCell(row, columnCount++, currencyAuditLog.getCreatedBy() != null ? currencyAuditLog.getCreatedBy().toString() : " ", style);
                createCell(row, columnCount++, currencyAuditLog.getCreatedAt() != null ? currencyAuditLog.getCreatedAt().toString() : " ", style);
                createCell(row, columnCount++, currencyAuditLog.getUpdatedBy() != null ? currencyAuditLog.getUpdatedBy().toString() : " ", style);
                createCell(row, columnCount++, currencyAuditLog.getCreatedAt() != null ? currencyAuditLog.getCreatedAt().toString() : " ", style);
                createCell(row, columnCount++, currencyAuditLog.getIsPresent() != null ? currencyAuditLog.getIsPresent().toString() : " ", style);
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            newWorkBook.write(byteArrayOutputStream);

            byte[] workbookBytes = byteArrayOutputStream.toByteArray();

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(workbookBytes);

            Resource resource = new InputStreamResource(byteArrayInputStream);
            return resource;

        }
        catch (Exception e){
            e.printStackTrace();
            throw new SalesReportException(e.getMessage());
        }

    }

    public Resource manifestDataExcel(List<Long> manifestDataIds) throws IOException {

        try{
            List<ManifestData> manifestDataList = this.manifestDataRepository.findAllByIdIn(manifestDataIds);
            FileInputStream fileInputStream = new FileInputStream(sampleFileLocalLocation + "/excel-manifest-data.xlsx");
            Workbook  newWorkBook = WorkbookFactory.create(fileInputStream);
            Sheet summarySheet= newWorkBook.getSheetAt(0);
            int rowCount = 1;

            CellStyle rightAlignedStyle = newWorkBook.createCellStyle();
            rightAlignedStyle.setAlignment(HorizontalAlignment.RIGHT);
            CellStyle style = newWorkBook.createCellStyle();

            for (ManifestData manifestData : manifestDataList) {
                Row row = summarySheet.createRow(rowCount++);
                int columnCount = 0;

                createCell(row, columnCount++, manifestData.getId().toString(), style);
                createCell(row, columnCount++, manifestData.getAwb(), style);
                createCell(row, columnCount++, manifestData.getPrefix(), style);
                createCell(row, columnCount++, manifestData.getManifestNumber(), style);
                createCell(row, columnCount++, manifestData.getWeight(), style);
                createCell(row, columnCount++, manifestData.getDimWeight(), style);
                createCell(row, columnCount++, manifestData.getActualWeight(), style);
                createCell(row, columnCount++, manifestData.getCompanyName(), style);
                createCell(row, columnCount++, manifestData.getMode(), style);
                createCell(row, columnCount++, manifestData.getShipmentMode(), style);
                createCell(row, columnCount++, manifestData.getEncodeDesc(), style);
                createCell(row, columnCount++, manifestData.getLoadingPortCode(), style);
                createCell(row, columnCount++, manifestData.getEncodeDescSec(), style);
                createCell(row, columnCount++, manifestData.getDestinationPort(), style);
                createCell(row, columnCount++, manifestData.getCarrierCode(), style);
                createCell(row, columnCount++, manifestData.getFlightNumber(), style);
                createCell(row, columnCount++, manifestData.getDepartureDate(), style);
                createCell(row, columnCount++, manifestData.getArrivalDate(), style);
                createCell(row, columnCount++, manifestData.getBlDate(), style);
                createCell(row, columnCount++, manifestData.getOrderNumber(), style);
                createCell(row, columnCount++, manifestData.getCustomShipDate(), style);
                createCell(row, columnCount++, manifestData.getAccountNumber(), style);
                createCell(row, columnCount++, manifestData.getAmount(), style);
                createCell(row, columnCount++, manifestData.getShipmentCountry(), style);
                createCell(row, columnCount++, manifestData.getConsigneeName(), style);
                createCell(row, columnCount++, manifestData.getConsigneeCity(), style);

            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            newWorkBook.write(byteArrayOutputStream);

            byte[] workbookBytes = byteArrayOutputStream.toByteArray();

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(workbookBytes);

            Resource resource = new InputStreamResource(byteArrayInputStream);
            return resource;

        }
        catch (Exception e){
            e.printStackTrace();
            throw new SalesReportException(e.getMessage());
        }

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
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

    private void setCellValue(Row row, int columnCount, Object value, CellStyle cellStyle) {
        Cell cell = row.createCell(columnCount);
        cell.setCellValue(value != null ? value.toString() : "");
        cell.setCellStyle(cellStyle);
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
    private String getUuId(){
        return UUID.randomUUID().toString();
    }
    private LocalDate getTodaysDate(){
        return LocalDate.now();
    }


    private Double parseDoubleOrDefault(String value, Double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();

            return defaultValue;
        }
    }


    public SheetHistory getSheetHistory(String sheetUniqueUUid) {

        return sheetHistoryRepository.findByUniqueUUid(sheetUniqueUUid);
    }
}



