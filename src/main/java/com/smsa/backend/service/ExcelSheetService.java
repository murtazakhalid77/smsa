package com.smsa.backend.service;

import com.smsa.backend.model.Customer;
import com.smsa.backend.model.InvoiceDetails;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelSheetService {
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

}

