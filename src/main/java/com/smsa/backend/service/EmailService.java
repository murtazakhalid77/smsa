package com.smsa.backend.service;

import com.smsa.backend.model.Custom;
import com.smsa.backend.model.Customer;
import com.smsa.backend.scheduler.EmailSchedular;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
@Component
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailSchedular.class);
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;
    public String sendMailWithAttachment(Customer customer) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(sender);
            helper.setTo(customer.getEmail());
            helper.setSubject("Invoice Generated");

            // Add attachment
            FileSystemResource excel = new FileSystemResource("src/main/resources/static/invoice.xlsx");
            helper.addAttachment(excel.getFilename(), excel);
            FileSystemResource pdf = new FileSystemResource("src/main/resources/static/invoice.pdf");
            helper.addAttachment(pdf.getFilename(), pdf);;
            logger.info("Email Sent to: " +customer.getNameEnglish()+ " "+ customer.getAccountNumber());
            javaMailSender.send(message);
            return "Mail sent successfully with attachment.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while sending mail with attachment.";
        }
    }
}
