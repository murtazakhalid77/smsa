package com.smsa.backend.scheduler;

import com.smsa.backend.dto.SalesReportHelperDto;
import com.smsa.backend.model.*;
import com.smsa.backend.repository.*;
import com.smsa.backend.service.EmailService;
import com.smsa.backend.service.ExcelService;
import com.smsa.backend.service.HelperService;
import com.smsa.backend.service.PdfService;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class EmailSchedular {
    @Autowired
    SheetHistoryRepository sheetHistoryRepository;
    @Autowired
    InvoiceDetailsRepository invoiceDetailsRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ExcelService excelService;
    @Autowired
    PdfService pdfService;
    @Autowired
    EmailService emailService;
    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    SalesReportRespository salesReportRespository;
    @Autowired
    HelperService helperService;
    private static final Logger logger = LoggerFactory.getLogger(EmailSchedular.class);

    @Scheduled(initialDelay = 5000, fixedDelay = 120000)
    public void markSentAndProcessInvoices() throws Exception {
        List<SheetHistory> sheetsToBeSent = sheetHistoryRepository.findAllByIsEmail(false);
        if(!sheetsToBeSent.isEmpty()){
            for (SheetHistory sheetHistory : sheetsToBeSent) {
                String sheetUniqueId = sheetHistory.getUniqueUUid();

                // Call a method to process invoices for this sheetUniqueId
                processInvoicesForSheet(sheetUniqueId);
                logger.info(String.format("work done sheet %S",sheetHistory.getName()));
            }
        }
        logger.info("No work to do");
    }
    public void processInvoicesForSheet(String sheetUniqueId) throws Exception {
        Map<String, List<InvoiceDetails>> invoiceDetailsMap = new HashMap<>();

        Invoice invoice =invoiceRepository.findById(1L).get();

        Long invoiceNumber = invoice.getNumber();
        boolean anyUnsentInvoice = false;

        List<InvoiceDetails> invoicesForSheet = invoiceDetailsRepository.findAllBySheetUniqueId(sheetUniqueId);

        invoiceDetailsMap = groupInvoicesByAccountNumber(invoicesForSheet,invoiceDetailsMap);

        for (String accountNumber : invoiceDetailsMap.keySet()) {

            List<InvoiceDetails> invoiceDetailsList = invoiceDetailsMap.get(accountNumber);

            if (invoiceDetailsList == null) {
                logger.warn("No invoice details found for account number: " + accountNumber);
                anyUnsentInvoice = true;
                continue; // Skip processing for this account number
            }

            if (!checkIsSentInMail(accountNumber,invoiceDetailsMap)) {
                Optional<Customer> customer = customerRepository.findByAccountNumber(accountNumber);

                if (customer.isPresent() && customer.get().getEmail() != null && customer.get().getStatus().equals(true)) {
                    logger.info("Making excel for Account Number: " + accountNumber);
                    try {
                        Long invoiceNo = invoiceNumber; // Use the current invoiceNumber
                        invoiceNumber++; //
                        SalesReportHelperDto salesReportHelperDto = excelService.updateExcelFile(invoiceDetailsList, customer.get(), sheetUniqueId,invoiceNo);
                        byte[] pdfFileData = pdfService.makePdf(invoiceDetailsList, customer.get(), sheetUniqueId,invoiceNo);

                        if (emailService.sendMailWithAttachments(customer.get(), salesReportHelperDto.getExcelFile(), pdfFileData,sheetUniqueId)){
                            logger.info(String.format("All the work done for account number %S with name %S",customer.get().getAccountNumber(),customer.get().getNameEnglish()));

                            SalesReport salesReport =SalesReport.builder()
                                    .invoiceNumber(String.valueOf(invoiceNumber))
                                    .customerAccountNumber(customer.get().getAccountNumber())
                                    .customerName(customer.get().getNameEnglish())
                                    .customerRegion(customer.get().getRegion().getCustomerRegion())
                                    .period(helperService.generateInvoiceDatePeriod(sheetUniqueId))
                                    .totalChargesAsPerCustomerDeclarationForm(salesReportHelperDto.getTotalChargesAsPerCustomDeclarationForm())
                                    .vatOnSmsaFees(salesReportHelperDto.getVatOnSmsaFees())
                                    .totalAmount(salesReportHelperDto.getTotalAmount())
                                    .smsaFeeCharges(salesReportHelperDto.getSmsaFeesCharges())
                                    .invoiceCurrency(customer.get().getInvoiceCurrency())
                                    .createdAt(LocalDate.now())
                                    .build();
                            salesReportRespository.save(salesReport);
                        }


                    } catch (Exception e) {
                        logger.error(String.format("Error while creating Excel for Account Number %S: " , accountNumber));
                        anyUnsentInvoice = true;
                        throw new Exception(e);
                        //continue
                    }
                } else {
                    logger.warn("Kindly update customer's email and status: " + accountNumber);
                    anyUnsentInvoice = true;
                }
            }
        }

        invoice.setNumber(invoiceNumber);
        invoiceRepository.save(invoice);
//         Update sheet history status only if there were no exceptions during the process
        if (!anyUnsentInvoice) {
            updateSheetHistoryStatus(sheetUniqueId, anyUnsentInvoice);
        }
    }

    private void updateSheetHistoryStatus(String sheetUniqueId, boolean anyUnsentInvoice) {
        SheetHistory sheetHistory = sheetHistoryRepository.findByUniqueUUid(sheetUniqueId);
        if (sheetHistory != null) {
            sheetHistory.setIsEmailSent(!anyUnsentInvoice);
            sheetHistoryRepository.save(sheetHistory);
        }
    }

    private Map<String, List<InvoiceDetails>> groupInvoicesByAccountNumber(List<InvoiceDetails> invoicesForSheet, Map<String, List<InvoiceDetails>> invoiceDetailsMap){
        for (InvoiceDetails invoiceDetails : invoicesForSheet) {
            String accountNumber = invoiceDetails.getInvoiceDetailsId().getAccountNumber();
            // If the account number is not already in the map, create a new list for it
            invoiceDetailsMap.putIfAbsent(accountNumber, new ArrayList<>());
            // Add the InvoiceDetails object to the list associated with the account number in the map
            invoiceDetailsMap.get(accountNumber).add(invoiceDetails);
        }
        return invoiceDetailsMap;
    }

    private boolean checkIsSentInMail(String accountNumber, Map<String, List<InvoiceDetails>> invoiceDetailsMap) {
        List<InvoiceDetails> invoiceDetailsList = invoiceDetailsMap.get(accountNumber);
        return invoiceDetailsList != null && !invoiceDetailsList.isEmpty()
                && invoiceDetailsList.get(0).getIsSentInMail();
    }
}



