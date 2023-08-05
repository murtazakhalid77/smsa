package com.smsa.backend.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.smsa.backend.model.*;
import com.smsa.backend.repository.InvoiceRepository;
import com.smsa.backend.repository.SheetHistoryRepository;
import com.smsa.backend.security.util.HashMapHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

@Service
public class PdfService {
    @Autowired
    HashMapHelper hashMapHelper;
    @Autowired
    SheetHistoryRepository sheetHistoryRepository;
    @Autowired
    HelperService helperService;
    @Autowired
    ResourceLoader resourceLoader;
    Font englishFont = new Font(Font.FontFamily.TIMES_ROMAN, 10);
    Font pdfFont = new Font(Font.FontFamily.TIMES_ROMAN, 8);
    Font englishBoldFont = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD);
    Font columnFontBold = new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.BOLD);
    Font arabicFont;
    public SheetHistory getSheetHistory(String sheetUniqueUUid){
        return  sheetHistoryRepository.findByUniqueUUid(sheetUniqueUUid);
    }
    private static final Logger logger = LoggerFactory.getLogger(PdfService.class);
    public byte[] makePdf(List<InvoiceDetails> invoiceDetailsList, Customer customer, String sheetUniqueId,Long invoiceNumber){
        Resource resource =resourceLoader.getResource("classpath:afont.ttf");

        arabicFont  = FontFactory.getFont(resource.getFilename(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        Custom custom= getSheetHistory(sheetUniqueId).getCustom();

        Document document = new Document(PageSize.A4, 10, 10, 30, 50);
        arabicFont.setSize(10);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();

        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
        headerTable.setLockedWidth(true);

        // Left column: Picture
        PdfPCell pictureCell = new PdfPCell();
        pictureCell.setBorder(Rectangle.NO_BORDER);

        Image img = Image.getInstance("classpath:img.png");
        img.scaleToFit(100, 100);
        pictureCell.addElement(new Chunk(img, 0, 0));
        headerTable.addCell(pictureCell);

        // Right column: Arabic sentences
        PdfPCell arabicCell = new PdfPCell();
        arabicCell.setBorder(Rectangle.NO_BORDER);
        arabicCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

        String test = " ﺍﻟﺮﻗﻢ ﺍﻟﻀﺮﻳﺒﻲ:"+custom.getSmsaFeeVat();
        arabicCell.addElement(new Paragraph("ﺷﺮﻛﺔ ﺳﻤﺴﺎ ﻟﻠﻨﻘﻞ ﺍﻟﺴﺮﻳﻊ ﺍﻟﻤﺤﺪﻭﺩﺓ", arabicFont));
        arabicCell.addElement(new Paragraph(test, arabicFont));


        headerTable.addCell(arabicCell);

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
        englishCell.addElement(new Paragraph("Customer Name:\t" + (customer.getNameEnglish() != null ? customer.getNameEnglish() : ""), englishFont));
        englishCell.addElement(new Paragraph("Customer VAT#\t\t" + (customer.getVatNumber() != null ? customer.getVatNumber() : ""), englishFont));
        englishCell.addElement(new Paragraph("\t\t\t" + (customer.getAddress() != null ? customer.getAddress() : ""), englishFont));

        // Right column: Arabic content
        PdfPCell arabicContentCell = new PdfPCell();
        arabicContentCell.setBorder(Rectangle.NO_BORDER);
        arabicContentCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

        arabicContentCell.addElement(new Paragraph("الفاتورة إلى.", arabicFont));
        arabicContentCell.addElement(new Paragraph("اسم الشركة: " + (customer.getNameArabic() != null ? customer.getNameArabic() : ""), arabicFont));
        arabicContentCell.addElement(new Paragraph("\tالرقم الضريبي #: " + (customer.getVatNumber() != null ? customer.getVatNumber() : ""), arabicFont));
        arabicContentCell.addElement(new Paragraph("\t" + (customer.getAddress() != null ? customer.getAddress() : ""), arabicFont));

        invoiceTable.addCell(englishCell);
        invoiceTable.addCell(arabicContentCell);
        invoiceTable.setSpacingAfter(20);
        document.add(invoiceTable);

        PdfPTable additionalContentTable = new PdfPTable(2);
        additionalContentTable.setWidthPercentage(100);

        // Left column: English content
        PdfPCell englishAdditionalCell = new PdfPCell();
        englishAdditionalCell.setBorder(Rectangle.NO_BORDER);

        String invoice=helperService.generateInvoiceDate(sheetUniqueId);

        englishAdditionalCell.addElement(new Paragraph("Customer Account Number: " + (customer.getAccountNumber() != null ? customer.getAccountNumber() : ""), englishFont));
        englishAdditionalCell.addElement(new Paragraph("Invoice#:"+"Inv-"+invoiceNumber, englishFont));
        englishAdditionalCell.addElement(new Paragraph("Invoice Date:\t" + invoice,englishFont));
        englishAdditionalCell.addElement(new Paragraph("Invoice Currency: " + (customer.getInvoiceCurrency() != null ? customer.getInvoiceCurrency() : ""), englishFont));

        // Right column: Arabic content
        PdfPCell arabicAdditionalCell = new PdfPCell();
        arabicAdditionalCell.setBorder(Rectangle.NO_BORDER);
        arabicAdditionalCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

        arabicAdditionalCell.addElement(new Paragraph("رقم حساب العميل: " + (customer.getAccountNumber() != null ? customer.getAccountNumber() : ""), arabicFont));
        arabicAdditionalCell.addElement(new Paragraph("رقم الفاتورة:" +"Inv"+"-"+invoiceNumber,arabicFont));
        arabicAdditionalCell.addElement(new Paragraph("تاريخ الفاتورة:"+invoice,arabicFont));
        arabicAdditionalCell.addElement(new Paragraph("عملة الفاتورة: " + (customer.getInvoiceCurrency() != null ? customer.getInvoiceCurrency() : ""), arabicFont));

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
        PdfPTable dataTable = new PdfPTable(11);
        dataTable.setWidthPercentage(100);
        dataTable.setSpacingBefore(20); // Add space before the table

        // Set column height to 50 points
        float columnHeight = 50;

        List<String> columnNames =getColumnNamesList();

        Map<String, String> columnMapping = getColumnMapping();


        // Add the column names to the table
        for (String columnName : columnNames) {
            PdfPCell cell = new PdfPCell(new Paragraph(columnName, columnFontBold));
            cell.setMinimumHeight(columnHeight);
            cell.setBackgroundColor(new BaseColor(23, 54, 93)); // Light blue color
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // Align content in the middle
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            columnFontBold.setColor(BaseColor.WHITE);
            dataTable.addCell(cell);
        }

        Map<String, List<InvoiceDetails>> filteredRowsMap ;
        filteredRowsMap = hashMapHelper.filterRowsByMawbNumber(invoiceDetailsList);

        List<Map<String, Object>> calculatedValuesList = hashMapHelper.calculateValues(filteredRowsMap, customer,custom,invoiceNumber);

            // Loop through the data and columns to populate the table
            for (Map<String, Object> rowDataMap : calculatedValuesList) {
                for (String columnName : columnNames) {
                    String keyName = columnMapping.get(columnName);
                    Object value = rowDataMap.get(keyName);

                    PdfPCell cell = new PdfPCell();
                    cell.setVerticalAlignment(Element.ALIGN_RIGHT); // Align content in the middle

                    if (shouldApplyCurrencyFormat(columnName)) {
                        String formattedValue = formatCurrency(Double.valueOf(value.toString()));
                        cell.addElement(new Paragraph(formattedValue, pdfFont));
                    } else {
                        cell.addElement(new Paragraph(value != null ? value.toString() : "", pdfFont));
                    }

                    dataTable.addCell(cell);
                }
            }

            double grandTotal = 0.0;
            for (Map<String, Object> rowDataMap : calculatedValuesList) {
                String totalAmountKey = columnMapping.get("Total Amount");
                Object totalAmountValue = rowDataMap.get(totalAmountKey);

                if (totalAmountValue instanceof Number) {
                    grandTotal += ((Number) totalAmountValue).doubleValue();
                }
            }
            // Add grand total cell
            PdfPCell grandTotalCell = new PdfPCell(new Paragraph("Grand Total:"+formatCurrency(grandTotal), pdfFont));
            grandTotalCell.setBorder(Rectangle.TOP);
            grandTotalCell.setColspan(columnNames.size()); // Set the colspan to span across all columns
            grandTotalCell.setHorizontalAlignment(Element.ALIGN_RIGHT); // Align content to the right
            dataTable.addCell(grandTotalCell);



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
        writer.close();
        logger.info("pdf created successfully");
            return outputStream.toByteArray();
    } catch (DocumentException | IOException e) {
        e.printStackTrace();
        throw new RuntimeException("There was an issue in making pdf");
    }
}

    private Map<String, String> getColumnMapping() {
       HashMap<String,String>  columnMapping = new HashMap<>();

        columnMapping.put("MAWB No.", "MawbNumber");
        columnMapping.put("Total AWB Count", "TotalAwbCount");
        columnMapping.put("Customer Shipment Value", "CustomerShipmentValue");
        columnMapping.put("VAT Charges as per Custom Declaration Form", "VatAmountCustomDeclarationForm");
        columnMapping.put("Custom Form Charges", "CustomFormCharges");
        columnMapping.put("Other Charges", "Others");
        columnMapping.put("Total Charges", "TotalCharges");
        columnMapping.put("Total Value", "TotalValue");
        columnMapping.put("Custom Declaration No", "CustomDeclarationNumber");
        columnMapping.put("Customer Account Number", "CustomerAccountNumber");
        columnMapping.put("Invoice No", "InvoiceNumber");
        columnMapping.put("Invoice Type", "InvoiceType");
        columnMapping.put("SMSA Fee Charges", "SMSAFeeCharges");
        columnMapping.put("Total Amount", "TotalAmount");
        columnMapping.put("Custom Port", "CustomPort");
        columnMapping.put("VAT on SMSA Fee","VatOnSmsaFees");

        return columnMapping;
    }

    private boolean shouldApplyCurrencyFormat(String columnName) {
        List<String> currencyFormattedColumns = Arrays.asList(
                "VAT Charges as per Custom Declaration Form",
                "Custom Form Charges",
                "Other Charges",
                "SMSA Fee Charges",
                "VAT on SMSA Fee",
                "Total Amount"
        );

        return currencyFormattedColumns.contains(columnName);
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
    private String formatCurrency(Double value) {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(value);
    }
}

