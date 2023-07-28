package com.smsa.backend.service;

import com.smsa.backend.model.Customer;
import com.smsa.backend.model.InvoiceDetails;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExcelSheetService {

    Map<String, List<InvoiceDetails>> filteredRowsMap;
    private static final Logger logger = LoggerFactory.getLogger(ExcelSheetService.class);


    public void updateExcelFile (List<InvoiceDetails> invoiceDetailsList, Customer customer) throws IOException {

        logger.info("Inside update excel method");

        String excelFilePath = "C:\\Users\\Bionic Computer\\Desktop\\backend\\src\\main\\java\\com\\smsa\\backend\\assets\\testFile.xlsx";

        logger.info("Inside update excel method");

        FileInputStream fileInputStream = new FileInputStream(excelFilePath);
        Workbook existingWorkbook = WorkbookFactory.create(fileInputStream);

        Sheet invoiceDetailSheet = existingWorkbook.getSheetAt(1);
        CellStyle style = makeStyleForTheSheet(existingWorkbook);

        int rowCount = 1;

        // Iterate through the invoiceDetailsList for the current group
        for (InvoiceDetails invoiceDetails : invoiceDetailsList) {

            Row row = invoiceDetailSheet.createRow(rowCount);
            row.setRowStyle(style);
            int columnCount = 0;


            Cell cell;
            // MAWB
            cell = row.createCell(columnCount);
            cell.setCellValue(invoiceDetails.getInvoiceDetailsId().getMawb().toString());

            // Manifest Date
            cell = row.createCell(++columnCount);
            cell.setCellValue(invoiceDetails.getInvoiceDetailsId().getManifestDate().toString());

            cell = row.createCell(++columnCount);
            cell.setCellValue(invoiceDetails.getInvoiceDetailsId().getAccountNumber());

            // AWB
            cell = row.createCell(++columnCount);
            cell.setCellValue(invoiceDetails.getInvoiceDetailsId().getAwb().toString());

            // Order Number
            cell = row.createCell(++columnCount);
            cell.setCellValue(invoiceDetails.getOrderNumber());

            // Origin
            cell = row.createCell(++columnCount);
            cell.setCellValue(invoiceDetails.getOrigin());

            // Destination
            cell = row.createCell(++columnCount);
            cell.setCellValue(invoiceDetails.getDestination());

            // Shipper Name
            cell = row.createCell(++columnCount);
            cell.setCellValue(invoiceDetails.getShippersName());

            // Consignee Name
            cell = row.createCell(++columnCount);
            cell.setCellValue(invoiceDetails.getConsigneeName());

            // Weight
            cell = row.createCell(++columnCount);
            cell.setCellValue(invoiceDetails.getWeight());

            // Declared Value
            cell = row.createCell(++columnCount);
            cell.setCellValue(invoiceDetails.getDeclaredValue());

            // Value (Custom)
            cell = row.createCell(++columnCount);
            cell.setCellValue(invoiceDetails.getValueCustom());

            // VAT Amount
            cell = row.createCell(++columnCount);
            cell.setCellValue(invoiceDetails.getVatAmount());

            // Custom Form Charges
            cell = row.createCell(++columnCount);
            cell.setCellValue(invoiceDetails.getCustomFormCharges());

            // Other
            cell = row.createCell(++columnCount);
            cell.setCellValue(invoiceDetails.getOther());

            // Total Charges
            cell = row.createCell(++columnCount);
            cell.setCellValue(invoiceDetails.getTotalCharges());

            // Custom Declaration #
            cell = row.createCell(++columnCount);
            cell.setCellValue(invoiceDetails.getCustomDeclarationNumber());

            // Custom Declaration Date
            cell = row.createCell(++columnCount);
            cell.setCellValue(invoiceDetails.getCustomDeclarationDate().toString());

            rowCount++;
        }

        Sheet summarySheet = existingWorkbook.getSheetAt(0);
        Cell nameCell = summarySheet.getRow(5).getCell(1);
        nameCell.setCellValue(customer.getNameEnglish().toString());

        Cell accountNumberCell = summarySheet.getRow(6).getCell(1);
        accountNumberCell.setCellValue(customer.getAccountNumber().toString());

        Cell invoiceDateCell = summarySheet.getRow(6).getCell(9);
        invoiceDateCell.setCellValue(LocalDate.now());

        filteredRowsMap = new HashMap<>();
        filteredRowsMap = filterRowsByMawbNumber(invoiceDetailsList);

        List<Map<String, Object>> calculatedValuesList = calculateValues(filteredRowsMap, customer.getAccountNumber());

        int startingRow = 9;
        for (Map<String, Object> calculatedValuesMap : calculatedValuesList) {
            // Set the Invoice#
            String invoiceNumber = calculatedValuesMap.get("InvoiceNumber").toString();
            Row row = summarySheet.createRow(startingRow);
            Cell invoiceNumberCell = row.createCell(0);
            invoiceNumberCell.setCellValue(invoiceNumber);

            // Set the Custom Declaration#
            Set<Long> customDeclarationNumbers = (Set<Long>) calculatedValuesMap.get("CustomDeclarationNumber");
            String customDeclarationNumbersString = customDeclarationNumbers.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining("/"));

            Cell customDeclarationNumberCell = row.createCell(1);
            customDeclarationNumberCell.setCellValue(customDeclarationNumbersString);

            // Set the Customer Account
            String customerAccountNumber = calculatedValuesMap.get("CustomerAccountNumber").toString();
            Cell customerAccountNumberCell = row.createCell(2);
            customerAccountNumberCell.setCellValue(customerAccountNumber);

            // Set the MAWB Number
            String mawbNumber = calculatedValuesMap.get("MawbNumber").toString();
            Cell mawbNumberCell = row.createCell(3);
            mawbNumberCell.setCellValue(mawbNumber);

            // Set other calculated values
            Cell totalAwbCountCell = row.createCell(4);
            totalAwbCountCell.setCellValue(Long.parseLong(calculatedValuesMap.get("TotalAwbCount").toString()));

            Cell totalValueCell = row.createCell(5);
            totalValueCell.setCellValue(Long.parseLong(calculatedValuesMap.get("TotalValue").toString()));

            Cell customerShipmentValueCell = row.createCell(6);
            customerShipmentValueCell.setCellValue(Long.parseLong(calculatedValuesMap.get("CustomerShipmentValue").toString()));

            Cell vatAmountCell = row.createCell(7);
            vatAmountCell.setCellValue(Double.parseDouble(calculatedValuesMap.get("VatAmountCustomDeclarationForm").toString()));

            Cell customFormChargesCell = row.createCell(8);
            customFormChargesCell.setCellValue(Long.parseLong(calculatedValuesMap.get("CustomFormCharges").toString()));

            Cell othersCell = row.createCell(9);
            othersCell.setCellValue(Long.parseLong(calculatedValuesMap.get("Others").toString()));

            Cell totalChargesCell = row.createCell(10);
            totalChargesCell.setCellValue(Long.parseLong(calculatedValuesMap.get("TotalCharges").toString()));

            // Move to the next row
            startingRow++;
        }




        fileInputStream.close();

            FileOutputStream fileOutputStream = new FileOutputStream(excelFilePath);
            existingWorkbook.write(fileOutputStream);
            existingWorkbook.close();
            fileOutputStream.close();

            logger.info("Excel sheet updated successfully......");

//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        }

    private CellStyle makeStyleForTheSheet(Workbook existingWorkbook){

        CellStyle style = existingWorkbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());

        return style;
    }

    public List<Map<String, Object>> calculateValues(Map<String, List<InvoiceDetails>> filteredRowsMap, String accountNumber) {
        List<Map<String, Object>>  testList = new ArrayList<>();

        Set<Long> customDecarationNumberSet = new HashSet<>();

        Map<String, Object> calculatedValuesMap = new HashMap<>();

        Long totalAwbCount = 0L;
        Long customerShipmentValue = 0L;
        Double vatAmountCustomDeclartionForm = 0.0;
        Long customFormChares = 0L;
        Long others = 0L;

        for (Map.Entry<String, List<InvoiceDetails>> entry : filteredRowsMap.entrySet()) {
            String mawbNumber = entry.getKey();
            List<InvoiceDetails> filterInvoiceDetails = entry.getValue();
            customDecarationNumberSet.clear();

            totalAwbCount = 0L;
            customerShipmentValue = 0L;
            vatAmountCustomDeclartionForm = 0.0;
            customFormChares = 0L;
            others = 0L;

            for (InvoiceDetails invoiceDetails : filterInvoiceDetails) {
                totalAwbCount += 1;
                customerShipmentValue += invoiceDetails.getValueCustom();
                vatAmountCustomDeclartionForm += invoiceDetails.getVatAmount();
                customFormChares += invoiceDetails.getCustomFormCharges();
                others += invoiceDetails.getOther();
                customDecarationNumberSet.add(invoiceDetails.getCustomDeclarationNumber());
            }
            // Put the MAWB number into the calculatedValuesMap for each iteration
            calculatedValuesMap.put("MawbNumber", mawbNumber);
            Long totalCharges = customFormChares + others;

            // Put the calculated values into the result map
            calculatedValuesMap.put("TotalAwbCount", totalAwbCount);
            calculatedValuesMap.put("CustomerShipmentValue", customerShipmentValue);
            calculatedValuesMap.put("VatAmountCustomDeclarationForm", vatAmountCustomDeclartionForm);
            calculatedValuesMap.put("CustomFormCharges", customFormChares);
            calculatedValuesMap.put("Others", others);
            calculatedValuesMap.put("TotalCharges", totalCharges);
            calculatedValuesMap.put("CustomDeclarationNumber", customDecarationNumberSet);
            calculatedValuesMap.put("CustomerAccountNumber", accountNumber);
            calculatedValuesMap.put("InvoiceNumber","dummy");

            testList.add(calculatedValuesMap);
        }

        // Calculate total charges


        return testList;
    }

    public Map<String, List<InvoiceDetails>> filterRowsByMawbNumber(List<InvoiceDetails> invoiceDetailsList) {

        for (InvoiceDetails invoiceDetails : invoiceDetailsList) {
            String mawbNumber = invoiceDetails.getInvoiceDetailsId().getMawb().toString();

            if (!mawbNumber.isEmpty()) {
                filteredRowsMap.putIfAbsent(mawbNumber, new ArrayList<>());
                filteredRowsMap.get(mawbNumber).add(invoiceDetails);
            }
        }
        return filteredRowsMap;
    }





}

