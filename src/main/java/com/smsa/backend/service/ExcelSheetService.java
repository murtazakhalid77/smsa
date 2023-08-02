//package com.smsa.backend.service;
//
//import com.smsa.backend.Exception.ExcelMakingException;
//import com.smsa.backend.model.Custom;
//import com.smsa.backend.model.Customer;
//import com.smsa.backend.model.InvoiceDetails;
//import com.smsa.backend.model.SheetHistory;
//import com.smsa.backend.repository.SheetHistoryRepository;
//import com.smsa.backend.security.util.HashMapHelper;
//import org.apache.poi.ss.usermodel.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.ResourceLoader;
//import org.springframework.stereotype.Service;
//
//import java.io.*;
//import java.util.*;
//
//@Service
//public class ExcelSheetService {
//
//    Map<String, List<InvoiceDetails>> filteredRowsMap;
//
//    @Autowired
//    HashMapHelper hashMapHelper;
//    @Autowired
//    SheetHistoryRepository sheetHistoryRepository;
//
//    @Autowired
//    ResourceLoader resourceLoader;
//    private static final Logger logger = LoggerFactory.getLogger(ExcelSheetService.class);
//
//    Custom custom;
//    public SheetHistory getSheetHistory(String sheetUniqueUUid){
//        return  sheetHistoryRepository.findByUniqueUUid(sheetUniqueUUid);
//    }
//
//
//    public void updateExcelFile (List<InvoiceDetails> invoiceDetailsList, Customer customer,String sheetUniqueId) throws Exception {
//        logger.info(String.format("Inside update excel method for account ",customer.getAccountNumber()));
//        try {
//            this.custom = getSheetHistory(sheetUniqueId).getCustom();
//            Resource resource =resourceLoader.getResource("classpath:sample.xlsx");
//        FileInputStream fileInputStream = new FileInputStream(resource.getFile());
//
//        Workbook newWorkBook = WorkbookFactory.create(fileInputStream);
//
//        Sheet invoiceDetailSheet = newWorkBook.getSheetAt(1);
//
//        CellStyle style = makeStyleForTheSheet(newWorkBook);
//
//        setInvoiceDetailsCellValues(invoiceDetailSheet, invoiceDetailsList, style);
//
//        Sheet summarySheet = newWorkBook.getSheetAt(0);
//
//        setSummarySheetCellValues(summarySheet, customer, sheetUniqueId);
//
//        filteredRowsMap = hashMapHelper.filterRowsByMawbNumber(invoiceDetailsList);
//
//        List<Map<String, Object>> calculatedValuesList = hashMapHelper.calculateValues(filteredRowsMap, customer, this.custom);
//
//        populateCalculatedValues(summarySheet, calculatedValuesList);
//
//        fileInputStream.close();
//
//
//        FileOutputStream fileOutputStream = new FileOutputStream("classpath:invoice.xlsx");
//        newWorkBook.write(fileOutputStream);
//        newWorkBook.close();
//        fileOutputStream.close();
//        logger.info("Excel sheet updated successfully......");
//
//    }catch (Exception e) {
//        logger.warn(e.getMessage());
//        throw new Exception(e.getMessage());
//    }
//
//
//}
//
//    private void setSummarySheetCellValues(Sheet summarySheet, Customer customer, String sheetUniqueId) throws RuntimeException {
//        try {
//            Cell nameCell = summarySheet.getRow(3).getCell(1);
//            setCellValue(nameCell, customer.getNameEnglish());
//
//            Cell accountNumberCell = summarySheet.getRow(4).getCell(1);
//            setCellValue(accountNumberCell, customer.getAccountNumber());
//
//            Cell invoiceDateCell = summarySheet.getRow(3).getCell(9);
//            setCellValue(invoiceDateCell, getSheetHistory(sheetUniqueId).getInvoiceDate());
//        }catch (Exception e){
//            logger.warn("The name,account,invoiceDate cell in the summary sheet are null replace them with XXXX");
//          throw new RuntimeException(String.format("Summary Sheet Exception"));
//        }
//
//    }
//
//    private void setInvoiceDetailsCellValues(Sheet invoiceDetailSheet, List<InvoiceDetails> invoiceDetailsList,CellStyle style) {
//        int rowCount = 1;
//        try {
//            for (InvoiceDetails invoiceDetails : invoiceDetailsList) {
//                Row row = invoiceDetailSheet.createRow(rowCount);
//                int columnCount = 0;
//                Cell cell;
//
//                setCellValue(row, columnCount, invoiceDetails.getInvoiceDetailsId().getMawb());
//                setCellValue(row, ++columnCount, invoiceDetails.getInvoiceDetailsId().getManifestDate());
//                setCellValue(row, ++columnCount, invoiceDetails.getInvoiceDetailsId().getAccountNumber());
//                setCellValue(row, ++columnCount, invoiceDetails.getInvoiceDetailsId().getAwb());
//                setCellValue(row, ++columnCount, invoiceDetails.getOrderNumber());
//                setCellValue(row, ++columnCount, invoiceDetails.getOrigin());
//                setCellValue(row, ++columnCount, invoiceDetails.getDestination());
//                setCellValue(row, ++columnCount, invoiceDetails.getShippersName());
//                setCellValue(row, ++columnCount, invoiceDetails.getConsigneeName());
//                setCellValue(row, ++columnCount, invoiceDetails.getWeight());
//                setCellValue(row, ++columnCount, invoiceDetails.getDeclaredValue());
//                setCellValue(row, ++columnCount, invoiceDetails.getValueCustom());
//                setCellValue(row, ++columnCount, invoiceDetails.getVatAmount());
//                setCellValue(row, ++columnCount, invoiceDetails.getCustomFormCharges());
//                setCellValue(row, ++columnCount, invoiceDetails.getOther());
//                setCellValue(row, ++columnCount, invoiceDetails.getTotalCharges());
//                setCellValue(row, ++columnCount, invoiceDetails.getCustomDeclarationNumber());
//                setCellValue(row, ++columnCount, invoiceDetails.getCustomDeclarationDate());
//
//                rowCount++;
//            }
//        }catch (ExcelMakingException e){
//            throw new ExcelMakingException("There was an issue in invoice sheet");
//        }
//
//    }
//
//
//    private CellStyle makeStyleForTheSheet(Workbook existingWorkbook){
//
//        CellStyle style = existingWorkbook.createCellStyle();
//        style.setAlignment(HorizontalAlignment.CENTER);
//        style.setBorderBottom(BorderStyle.THICK);
//        style.setBorderLeft(BorderStyle.THICK);
//        style.setBorderRight(BorderStyle.THICK);
//        style.setBorderTop(BorderStyle.THICK);
//        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
//        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
//
//        return style;
//    }
//
//    private void populateCalculatedValues(Sheet summarySheet,List<Map<String, Object>> calculatedValuesList) {
//        try {
//            int startingRow=6;
//            for (Map<String, Object> calculatedValuesMap : calculatedValuesList) {
//                Row row = summarySheet.createRow(startingRow);
//
//                setCellValue(row.createCell(0), calculatedValuesMap.get("InvoiceNumber"));
//                setCellValue(row.createCell(1), calculatedValuesMap.get("CustomDeclarationNumber"));
//                setCellValue(row.createCell(2), calculatedValuesMap.get("CustomerAccountNumber"));
//                setCellValue(row.createCell(3), calculatedValuesMap.get("MawbNumber"));
//                setCellValue(row.createCell(4), calculatedValuesMap.get("TotalAwbCount"));
//                setCellValue(row.createCell(5), calculatedValuesMap.get("TotalValue"));
//                setCellValue(row.createCell(6), calculatedValuesMap.get("CustomerShipmentValue"));
//                setCellValue(row.createCell(7), calculatedValuesMap.get("VatAmountCustomDeclarationForm"));
//                setCellValue(row.createCell(8), calculatedValuesMap.get("CustomFormCharges"));
//                setCellValue(row.createCell(9), calculatedValuesMap.get("Others"));
//                setCellValue(row.createCell(10), calculatedValuesMap.get("TotalCharges"));
//
//                // Move to the next row
//                startingRow++;
//            }
//        }
//        catch (ExcelMakingException e) {
//            throw new RuntimeException("There was an issue in populating calculated values in summary file");
//        }
//
//
//
//    }
//
//    private void setCellValue(Cell cell, Object value) {
//        if (value == null) {
//            cell.setCellValue("");
//        } else if (value instanceof String) {
//            cell.setCellValue((String) value);
//        } else if (value instanceof Long) {
//            cell.setCellValue((Long) value);
//        } else if (value instanceof Double) {
//            cell.setCellValue((Double) value);
//        }
//    }
//
//    private void setCellValue(Row row, int columnCount, Object value) {
//        Cell cell = row.createCell(columnCount);
//        cell.setCellValue(value != null ? value.toString() : "");
//    }
//}
//
