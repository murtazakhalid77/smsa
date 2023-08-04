package com.smsa.backend.service;

import com.smsa.backend.model.Custom;
import com.smsa.backend.model.Customer;
import com.smsa.backend.repository.InvoiceDetailsRepository;
import com.smsa.backend.scheduler.EmailSchedular;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailSchedular.class);
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    InvoiceDetailsRepository invoiceDetailsRepository;
    @Value("${spring.mail.username}")
    private String sender;
    @Value("${smsa.file.location}")
    private String smsaFolderLocation;
    public boolean sendMailWithAttachments(Customer customer, byte[] excelFileData, byte[] pdfFileData,String sheetUniqueId) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(sender);
            helper.setTo(customer.getEmail());
            helper.setSubject("Invoice Generated");

            // Attach the Excel file
            helper.addAttachment("invoice.xlsx", new ByteArrayDataSource(excelFileData, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

            // Attach the PDF file
            helper.addAttachment("invoice.pdf", new ByteArrayDataSource(pdfFileData, "application/pdf"));

            helper.setText("hello");
            logger.info("Email Sent to: " + customer.getNameEnglish() + " " + customer.getAccountNumber());
            javaMailSender.send(message);
            invoiceDetailsRepository.updateIsSentInMailByAccountNumberAndSheetUniqueId(customer.getAccountNumber(), sheetUniqueId);
            return true;
        } catch (Exception e) {
            //reverting it back
            invoiceDetailsRepository.revertIsSentInMailByAccountNumberAndSheetUniqueId(customer.getAccountNumber(),sheetUniqueId);
            throw new RuntimeException("Error while sending mail with attachments.");

        }

    }
}
