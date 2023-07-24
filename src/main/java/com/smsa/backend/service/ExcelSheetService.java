package com.smsa.backend.service;

import com.smsa.backend.model.InvoiceDetails;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

@Service
public class ExcelSheetService {
    public void updateExcelFile() {

        System.out.println("inside update excel file method");

        String excelFilePath = "C:\\Users\\Bionic Computer\\Desktop\\backend\\src\\main\\java\\com\\smsa\\backend\\assets\\cdv invoice detail email.xlsx";  // provide your excel file path

        try {
            FileInputStream fileInputStream = new FileInputStream(excelFilePath);

            Workbook workbook = WorkbookFactory.create(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);

            int lastRowCount = sheet.getLastRowNum();
            System.out.println("lastRowCount.. "  +  lastRowCount);

            InvoiceDetails userList = InvoiceDetails.builder().consigneeName("Murtaza").shippersName("khalid").build();

                Row dataRow = sheet.createRow(++lastRowCount);
                dataRow.createCell(8).setCellValue(userList.getConsigneeName());
                dataRow.createCell(7).setCellValue(userList.getShippersName());



            System.out.println("lastRowCount after excel sheet modified.. "  +  lastRowCount);
            fileInputStream.close();

            FileOutputStream fileOutputStream = new FileOutputStream(excelFilePath);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            System.out.println("excel sheet updated successfully........");


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
