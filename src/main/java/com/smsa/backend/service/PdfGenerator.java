package com.smsa.backend.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.smsa.backend.model.Custom;
import com.smsa.backend.model.Customer;
import com.smsa.backend.model.InvoiceDetails;
import com.smsa.backend.model.SheetHistory;
import com.smsa.backend.repository.SheetHistoryRepository;
import com.smsa.backend.security.util.HashMapHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PdfGenerator {
    Map<String, List<InvoiceDetails>> filteredRowsMap;
    @Autowired
    HashMapHelper hashMapHelper;

    @Autowired
    SheetHistoryRepository sheetHistoryRepository;

    Font arabicFont = FontFactory.getFont("C:\\Users\\Bionic Computer\\IdeaProjects\\untitled5\\src\\main\\java\\org\\example\\NotoNaskhArabic-VariableFont_wght.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);


    Font englishFont = new Font(Font.FontFamily.TIMES_ROMAN, 10);
    Font pdfFont = new Font(Font.FontFamily.TIMES_ROMAN, 8);
    Font englishBoldFont = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD);
    Font columnFontBold = new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.BOLD);
    Custom custom;

    public void makePdf(List<InvoiceDetails> invoiceDetailsList, Customer customer, String sheetUniqueId){
        this.custom =getCustom(sheetUniqueId).getCustom();

    Document document = new Document(PageSize.A4, 10, 10, 30, 50);
        arabicFont.setSize(10);
    try {
        PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\Bionic Computer\\Desktop\\backend\\src\\main\\java\\com\\smsa\\backend\\assets\\testFile.pdf"));
        document.open();

        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
        headerTable.setLockedWidth(true);

        // Left column: Picture
        PdfPCell pictureCell = new PdfPCell();
        pictureCell.setBorder(Rectangle.NO_BORDER);

        // Replace "path/to/your/image.png" with the actual path to your image file
        Image img = Image.getInstance("C:\\Users\\Bionic Computer\\IdeaProjects\\untitled5\\src\\img.png");
        img.scaleToFit(100, 100); // Adjust the size of the image as needed
        pictureCell.addElement(new Chunk(img, 0, 0));
        headerTable.addCell(pictureCell);

        // Right column: Arabic sentences
        PdfPCell arabicCell = new PdfPCell();
        arabicCell.setBorder(Rectangle.NO_BORDER);
        arabicCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);


        String test = ": ﻟﺮﻗﻢ ﺍﻟﻀﺮﻳﺒﻲ:"+customer.getVatNumber();
        arabicCell.addElement(new Paragraph("ﺷﺮﻛﺔ ﺳﻤﺴﺎ ﻟﻠﻨﻘﻞ ﺍﻟﺴﺮﻳﻊ ﺍﻟﻤﺤﺪﻭﺩﺓ", arabicFont));
        arabicCell.addElement(new Paragraph(test, arabicFont));


        headerTable.addCell(arabicCell);


        // Set the height of the table to one inch (72 points)

        document.add(headerTable);

        // Add the paragraph below the header table
        Paragraph paragraph = new Paragraph(" فاتورة الرسوم والضرائب للشحنات الواردة Duty & Taxes Invoice for Inbound Shipments ", arabicFont);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingBefore(10); // Add some space before the paragraph

        // Create a new table with one column and set its width to 100%
        PdfPTable contentTable = new PdfPTable(1);
        contentTable.setWidthPercentage(100);
        contentTable.setSpacingAfter(20);
        PdfPCell contentCell = new PdfPCell();
        contentCell.setBorder(Rectangle.NO_BORDER);
        contentCell.addElement(paragraph);
        contentCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        contentTable.addCell(contentCell);

        document.add(contentTable);

        PdfPTable invoiceTable = new PdfPTable(2);
        invoiceTable.setWidthPercentage(100);

        // Left column: English content
        PdfPCell englishCell = new PdfPCell();
        englishCell.setBorder(Rectangle.NO_BORDER);

        englishCell.addElement(new Paragraph("Invoice To.", englishBoldFont));
        englishCell.addElement(new Paragraph("Customer Name:\t"+customer.getNameEnglish(), englishFont));
        englishCell.addElement(new Paragraph("Customer VAT#\t\t+"+customer.getVatNumber(), englishFont));
        englishCell.addElement(new Paragraph("\t\t\t"+customer.getAddress(), englishFont));

        // Right column: Arabic content
        PdfPCell arabicContentCell = new PdfPCell();
        arabicContentCell.setBorder(Rectangle.NO_BORDER);
        arabicContentCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

        arabicContentCell.addElement(new Paragraph("الفاتورة  إلى.", arabicFont));
        arabicContentCell.addElement(new Paragraph(	"اسم الشركة: "+customer.getNameArabic(), arabicFont));
        arabicContentCell.addElement(new Paragraph("\tالرقم الضريبي #:"+customer.getVatNumber(), arabicFont));
        arabicContentCell.addElement(new Paragraph("\t"+customer.getAddress(), arabicFont));

        invoiceTable.addCell(englishCell);
        invoiceTable.addCell(arabicContentCell);
        invoiceTable.setSpacingAfter(20);
        document.add(invoiceTable);

        PdfPTable additionalContentTable = new PdfPTable(2);
        additionalContentTable.setWidthPercentage(100);

        // Left column: English content
        PdfPCell englishAdditionalCell = new PdfPCell();
        englishAdditionalCell.setBorder(Rectangle.NO_BORDER);

        englishAdditionalCell.addElement(new Paragraph("Customer Account Number:"+customer.getAccountNumber(), englishFont));
        englishAdditionalCell.addElement(new Paragraph("Invoice#", englishFont)); //TODO: invoice work
        englishAdditionalCell.addElement(new Paragraph("Invocie Date:\t"+ LocalDate.now(), englishFont));
        englishAdditionalCell.addElement(new Paragraph("Invoice Currency:"+customer.getInvoiceCurrency(), englishFont));

        // Right column: Arabic content
        PdfPCell arabicAdditionalCell = new PdfPCell();
        arabicAdditionalCell.setBorder(Rectangle.NO_BORDER);
        arabicAdditionalCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

        arabicAdditionalCell.addElement(new Paragraph("رقم حساب العميل: "+customer.getAccountNumber(), arabicFont));
        arabicAdditionalCell.addElement(new Paragraph("رقم  الفاتورة:" ,arabicFont)); //TODO:invoice work
        arabicAdditionalCell.addElement(new Paragraph("تاريخ الفاتورة:\t"+LocalDate.now(), arabicFont));
        arabicAdditionalCell.addElement(new Paragraph("عملة الفاتورة:"+customer.getInvoiceCurrency(), arabicFont));

        additionalContentTable.addCell(englishAdditionalCell);
        additionalContentTable.addCell(arabicAdditionalCell);

        document.add(additionalContentTable);

        PdfPTable subjectTable = new PdfPTable(2);
        subjectTable.setWidthPercentage(100);

        // Left column: English subject


// Left column: English subject
        PdfPCell englishSubjectCell = new PdfPCell();
        englishSubjectCell.setBorder(Rectangle.NO_BORDER);
        englishSubjectCell.setHorizontalAlignment(Element.ALIGN_LEFT); // Align content to the left
        englishSubjectCell.addElement(new Paragraph("Subject: Duty & Taxes Invoice for Inbound Shipments", englishFont));
        subjectTable.addCell(englishSubjectCell);

// Right column: Arabic subject
        PdfPCell arabicSubjectCell = new PdfPCell();
        arabicSubjectCell.setBorder(Rectangle.NO_BORDER);
        arabicSubjectCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        arabicSubjectCell.setHorizontalAlignment(Element.ALIGN_RIGHT); // Align content to the right
        arabicSubjectCell.addElement(new Paragraph("الموضوع:فاتورة الرسوم والضرائب للشحنات الواردة", arabicFont));
        subjectTable.addCell(arabicSubjectCell);

        subjectTable.setSpacingBefore(20);

        document.add(subjectTable);

        // Add the content paragraph
        Paragraph contentParagraph = new Paragraph("" +
                "Dear Customer,\n\nThis is Duty & Taxes Invoice in connection with " +
                "Inbound Shipments mentioned therein, The Charges as per Saudi Custom " +
                "Declaration form are paid to Saudi Custom on behalf of Consignee.", englishFont);
        contentParagraph.setAlignment(Element.ALIGN_LEFT);
        contentParagraph.setSpacingBefore(5); // Add some space before the content paragraph
        document.add(contentParagraph);


        // Create the table with specified columns and cell properties
        PdfPTable dataTable = new PdfPTable(11 );
        dataTable.setWidthPercentage(100);
        dataTable.setSpacingBefore(20); // Add space before the table

        // Set column height to 50 points
        float columnHeight = 50;

        List<String> columnNames =getColumnNamesList();
        // List to store column names

        Map<String, String> columnMapping = new HashMap<>();

        columnMapping.put("MAWB No.", "MawbNumber");
        columnMapping.put("Total AWB Count", "TotalAwbCount");
        columnMapping.put("Customer Shipment Value", "CustomerShipmentValue");
        columnMapping.put("VAT Charges as per Custom Declaration Form", "VatAmountCustomDeclarationForm");
        columnMapping.put("Custom Form Charges", "CustomFormCharges");
        columnMapping.put("Other Charges", "Others");
        columnMapping.put("Total Charges", "TotalCharges");
        columnMapping.put("Total Value", "TotalValue");
        columnMapping.put("Custom Declaration Number", "CustomDeclarationNumber");
        columnMapping.put("Customer Account Number", "CustomerAccountNumber");
        columnMapping.put("Invoice Number", "InvoiceNumber");
        columnMapping.put("Invoice Type", "InvoiceType");
        columnMapping.put("SMSA Fee Charges", "SMSAFeeCharges");
        columnMapping.put("Total Amount", "TotalAmount");
        columnMapping.put("Custom Port", "CustomPort");
        columnMapping.put("VAT on SMSA Fee","VatOnSmsaFees");


        // Add the column names to the table
        for (String columnName : columnNames) {
            PdfPCell cell = new PdfPCell(new Paragraph(columnName, columnFontBold));
            cell.setMinimumHeight(columnHeight);
            cell.setBackgroundColor(new BaseColor(42, 52, 189)); // Light blue color
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // Align content in the middle
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            columnFontBold.setColor(BaseColor.WHITE);
            dataTable.addCell(cell);
        }

        filteredRowsMap = hashMapHelper.filterRowsByMawbNumber(invoiceDetailsList);

        List<Map<String, Object>> calculatedValuesList = hashMapHelper.calculateValues(filteredRowsMap, customer,this.custom);

        for (Map<String, Object> rowDataMap : calculatedValuesList) {
            for (String columnName : columnNames) {
                String keyName = columnMapping.get(columnName);
                Object value = rowDataMap.get(keyName);

                // Get the data as strings and add them to the table cells
                PdfPCell cell = new PdfPCell(new Paragraph(value != null ? value.toString() : "", pdfFont));
                cell.setVerticalAlignment(Element.ALIGN_CENTER); // Align content in the middle
                dataTable.addCell(cell);
            }
        }
        // Add the table to the document
        document.add(dataTable);


        // Create the single column table
        PdfPTable table1 = new PdfPTable(1);
        table1.setWidthPercentage(100);
        table1.setSpacingBefore(20); // Add space before the table

// Add the first paragraph
        Paragraph paragraph1 = new Paragraph("If you have any questions regarding this invoice, please contact us by email at cdvbill@smsaexpress.com", englishFont);
        paragraph1.setAlignment(Element.ALIGN_LEFT);
        PdfPCell cell1 = new PdfPCell(paragraph1);
        cell1.setBorder(Rectangle.NO_BORDER);
        table1.addCell(cell1);

// Add the second paragraph
        Paragraph paragraph2 = new Paragraph("ﺇﺫﺍ ﻟﺪﻳﻚ ﺃﻱ ﺍﺳﺘﻔﺴﺎﺭ ﻳﺘﻌﻠﻖ ﺑﻬﺬﻩ ﺍﻟﻔﺎﺗﻮﺭﺓ، ﺍﻟﺮﺟﺎء ﺍﻻﺗﺼﺎﻝ ﺑﻨﺎ ﻋﺒﺮ ﺍﻟﺒﺮﻳﺪ ﺍﻹﻟﻜﺘﺮﻭﻧﻲ : cdvbill@smsaexpress.com ", arabicFont);
        paragraph2.setAlignment(Element.ALIGN_LEFT);
        PdfPCell cell2 = new PdfPCell(paragraph2);
        cell2.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        cell2.setBorder(Rectangle.NO_BORDER);
        table1.addCell(cell2);

        document.add(table1); // Add the table to the document

        PdfPTable table2 = new PdfPTable(2);
        table2.setWidthPercentage(100);
        table2.setSpacingBefore(50); // Add space of 50 points before the table

// Left column: English content
        PdfPCell englishCellend = new PdfPCell();
        englishCellend.setBorder(Rectangle.NO_BORDER);
        Paragraph englishContent = new Paragraph("This is a computer-generated TAX invoice and no signature required.", englishFont);
        englishContent.setAlignment(Element.ALIGN_LEFT);
        englishCellend.addElement(englishContent);
        table2.addCell(englishCellend);

// Right column: Arabic content
        PdfPCell arabicCellend = new PdfPCell();
        arabicCellend.setBorder(Rectangle.NO_BORDER);
        arabicCellend.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        Paragraph arabicContent = new Paragraph("ﻫﺬﻩ ﺍﻟﻔﺎﺗﻮﺭﺓ ﺍﻟﻀﺮﻳﺒﻴﺔ ﻣﺴﺘﺨﺮﺟﺔ ﻣﻦ ﺍﻟﻜﻤﺒﻴﻮﺗﺮ ﻭﻻ ﺗﺤﺘﺎﺝ ﺇﻟﻰ ﺗﻮﻗﻴﻊ", arabicFont);
        arabicContent.setAlignment(Element.ALIGN_LEFT);
        arabicCellend.addElement(arabicContent);
        table2.addCell(arabicCellend);

        document.add(table2);


        document.close();
        System.out.println("PDF Created successfully.");

    } catch (DocumentException | IOException e) {
        e.printStackTrace();
    }
}
    private List<String> getColumnNamesList() {
        List<String> columnNames = new ArrayList<>();
        columnNames.add("Custom Port");
        columnNames.add("Invoice Type");
        columnNames.add("Invoice No");
        columnNames.add("MAWB No.");
        columnNames.add("Custom Declaration No");
        columnNames.add("VAT Charges as per Custom Declaration Form");
        columnNames.add("Custom Form Charges");
        columnNames.add("Other Charges");
        columnNames.add("SMSA Fee Charges");
        columnNames.add("VAT on SMSA Fee");
        columnNames.add("Total Amount");
        return columnNames;
    }
    public SheetHistory getCustom(String sheetUniqueUUid){
        return  sheetHistoryRepository.findByUniqueUUid(sheetUniqueUUid);
    }
}

