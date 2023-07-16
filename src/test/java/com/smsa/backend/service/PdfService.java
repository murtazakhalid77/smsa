package com.smsa.backend.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.wp.usermodel.Paragraph;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class PdfService {

    Image logo;
    Document document;

    PdfService() throws BadElementException, IOException {
        logo = Image.getInstance("com/smsa/backend/images/img.png");
    }
    public void pdfGenerator() throws DocumentException {

        document.open();


        logo.scaleToFit(100,50);
        logo.setSpacingBefore(0f);

        PdfPTable header = new PdfPTable(3);

        header.setWidthPercentage(100f);
        header.setWidths(new float[] {33f, 34f,33f});

        PdfPCell headerCell = new PdfPCell();
        headerCell.setBorder(Rectangle.NO_BORDER);
        headerCell.setBackgroundColor(BaseColor.WHITE);
        headerCell.setImage(logo);
        header.addCell(headerCell);

        headerCell.setPhrase(new Phrase("Portfolio Health Report", FontFactory.getFont("Arial", 15)));
        headerCell.setPaddingTop(15f);
        headerCell.setHorizontalAlignment( Element.ALIGN_CENTER);
        header.addCell(headerCell);

        headerCell.setPhrase(new Phrase(LocalDate.now().toString()));
        header.addCell(headerCell);
        document.add(header);
        document.close();
    }
}
