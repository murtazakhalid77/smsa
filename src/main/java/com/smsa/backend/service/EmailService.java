package com.smsa.backend.service;

import com.smsa.backend.Exception.InvalidEmailException;
import com.smsa.backend.model.Customer;
import com.smsa.backend.repository.InvoiceDetailsRepository;
import com.smsa.backend.scheduler.EmailSchedular;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailSchedular.class);
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    InvoiceDetailsRepository invoiceDetailsRepository;

    @Value("${spring.mail.username}")
    private String sender;
    @Async
    public boolean sendMailWithAttachments(Customer customer, byte[] excelFileData, byte[] pdfFileData,String sheetUniqueId, Long invoiceNumber) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

                helper.setFrom(sender);

            String[] emailAddresses = customer.getEmail().split(",");
            if(HelperService.checkValidEmail(emailAddresses)){
                helper.setTo(emailAddresses);
            }else {
                logger.warn("One or more email addresses do not follow the valid email pattern or are null.");
                throw new InvalidEmailException("One or more email addresses do not follow the valid email pattern or are null.");
            }


            String ccEmails = customer.getCcMail();
            if (ccEmails != null && !ccEmails.isEmpty()) {
                String[] ccEmailAddresses = ccEmails.split(",");
                if (HelperService.checkValidEmail(ccEmailAddresses)) {
                    helper.setCc(ccEmailAddresses);
                } else {
                    logger.warn("One or more CC email addresses do not follow the valid email pattern or are null.");
                    throw new InvalidEmailException("One or more CC email addresses do not follow the valid email pattern or are null.");
                }
            }


            helper.setSubject("SMSA Express Invoice for Custom Duty & Taxes for Inbound Shipments (ECDV-"+invoiceNumber.toString() + ")");

            // Attach the Excel file
            helper.addAttachment("invoice.xlsx", new ByteArrayDataSource(excelFileData, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

            // Attach the PDF file
            helper.addAttachment("invoice.pdf", new ByteArrayDataSource(pdfFileData, "application/pdf"));
            helper.setText("<html><body>"
                    + "<p>Dear Customer,</p>"
                    + "<p>"
                    + "Please find attached the Invoice for Custom Duty & Taxes for Inbound Shipments, Details are available under the attached file."
                    + "</p>"
                    + "<p>If you have any questions regarding this invoice, please contact us by email at <a href='mailto:cdvbill@smsaexpress.com'>cdvbill@smsaexpress.com</a>.</p>"
                    + "<p dir='rtl'>عزيزي العميل,</p>"
                    + "<p dir='rtl'>"
                    + "يرجى الاطلاع على الفاتورة المرفقة للرسوم والضرائب المخصصة للشحنات الواردة ، التفاصيل متاحة تحت الملف المرفق."
                    + "</p>"
                    + "<p dir='rtl'>"
                    + "إذا كان لديك أي أسئلة بخصوص هذه الفاتورة ، يرجى الاتصال بنا عبر البريد الإلكتروني على <a href='mailto:cdvbill@smsaexpress.com'>cdvbill@smsaexpress.com</a>."
                    + "</p>"
                    + "<br>Regards,"
                    + "<br>Finance Department"
                    + "<br>SMSA Express"
                    + "</body>" +
                    "</html>", true);


            logger.info("Email Sent to: " + customer.getNameEnglish() + " " + customer.getAccountNumber());
            javaMailSender.send(message);
            invoiceDetailsRepository.updateIsSentInMailByAccountNumberAndSheetUniqueId(customer.getAccountNumber(), sheetUniqueId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            invoiceDetailsRepository.revertIsSentInMailByAccountNumberAndSheetUniqueId(customer.getAccountNumber(),sheetUniqueId);
            throw new RuntimeException(e.getMessage());

        }

    }
}
