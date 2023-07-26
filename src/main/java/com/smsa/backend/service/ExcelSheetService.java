package com.smsa.backend.service;

import com.smsa.backend.model.Customer;
import com.smsa.backend.model.InvoiceDetails;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

@Service
public class ExcelSheetService {
    public void updateExcelFile(List<InvoiceDetails> invoiceDetailsList , Customer customer) {

        System.out.println("inside update excel file method");

        String excelFilePath = "C:\\Users\\Bionic Computer\\Desktop\\backend\\src\\main\\java\\com\\smsa\\backend\\assets\\cdv invoice detail email.xlsx";  // provide your excel file path

        try {
            FileInputStream fileInputStream = new FileInputStream(excelFilePath);
            Workbook workbook = WorkbookFactory.create(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getLastRowNum();

            for (InvoiceDetails invoiceDetails : invoiceDetailsList) {
                Row row = sheet.createRow(++rowCount);
                int columnCount = 0;

                // Row Count
                Cell cell = row.createCell(columnCount);
                cell.setCellValue(rowCount);

                // MAWB
                cell = row.createCell(++columnCount);
                cell.setCellValue(invoiceDetails.getInvoiceDetailsId().getMawb());

                // Manifest Date
                cell = row.createCell(++columnCount);
                cell.setCellValue(invoiceDetails.getInvoiceDetailsId().getManifestDate().toString());

                // Account Number
                cell = row.createCell(++columnCount);
                cell.setCellValue(invoiceDetails.getInvoiceDetailsId().getAccountNumber());

                // AWB
                cell = row.createCell(++columnCount);
                cell.setCellValue(invoiceDetails.getInvoiceDetailsId().getAwb());

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

            }

            fileInputStream.close();

            FileOutputStream fileOutputStream = new FileOutputStream(excelFilePath);
            workbook.write(fileOutputStream);
            workbook.close();
            fileOutputStream.close();
            System.out.println("Excel sheet updated successfully........");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
