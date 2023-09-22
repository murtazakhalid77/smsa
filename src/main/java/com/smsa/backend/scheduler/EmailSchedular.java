package com.smsa.backend.scheduler;

import com.smsa.backend.dto.SalesReportHelperDto;
import com.smsa.backend.model.*;
import com.smsa.backend.repository.*;
import com.smsa.backend.service.*;
import com.sun.jmx.snmp.Timestamp;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
    SalesReportRepository salesReportRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    HelperService helperService;
    @Autowired
    private SchedularAssembler schedularAssembler;
    @Autowired
    StorageService storageService;

    private static final Logger logger = LoggerFactory.getLogger(EmailSchedular.class);

    @Scheduled(initialDelay = 5000, fixedDelay = 300000)
    public void markSentAndProcessInvoices()  {
        try {
            List<SheetHistory> sheetsToBeSent = sheetHistoryRepository.findAllByIsEmail(false);
            if (!sheetsToBeSent.isEmpty()) {
                for (SheetHistory sheetHistory : sheetsToBeSent) {
                    String sheetUniqueId = sheetHistory.getUniqueUUid();
                    processInvoicesForSheet(sheetUniqueId);
                    logger.info(String.format("work done sheet %S", sheetHistory.getName()));
                }
            }
            logger.info("No work to do");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processInvoicesForSheet(String sheetUniqueId) throws Exception {
        Map<String, List<InvoiceDetails>> invoiceDetailsMap = new HashMap<>();

        Invoice invoice =invoiceRepository.findById(1L).get();

        Long invoiceNumber = invoice.getNumber();
        boolean anyUnsentInvoice = false;
        boolean documentsMade = false;
        String excelFileName = null;
        String pdfFileName = null;

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
                            Long invoiceNo = invoiceNumber;
                        invoiceNumber++;
                        SalesReportHelperDto salesReportHelperDto = excelService.updateExcelFile(invoiceDetailsList, customer.get(), sheetUniqueId,invoiceNo);
                        byte[] pdfFileData = pdfService.makePdf(invoiceDetailsList, customer.get(), sheetUniqueId,invoiceNo);
                        logger.info(String.format("Excel and pdf made for the account number %s",accountNumber));
                        documentsMade=true;

                        if(documentsMade){
                            String dateTime= String.valueOf(DateTime.now());
                            excelFileName = dateTime+ accountNumber + "excel.xlsx";
                            storageService.uploadFile(salesReportHelperDto.getExcelFile(), excelFileName);

                            pdfFileName = dateTime + accountNumber + "invoice.pdf";
                            storageService.uploadFile(pdfFileData, pdfFileName);
                            logger.info("uploaded to amazon s3 bucket");
                        }
                        final String finalExcelFileName = excelFileName;
                        final String finalPdfFileName = pdfFileName;

                        if (emailService.sendMailWithAttachments(customer.get(), salesReportHelperDto.getExcelFile(), pdfFileData,sheetUniqueId)) {
                            logger.info(String.format("All the work done for account number %s with name %s",
                                    customer.get().getAccountNumber(),
                                    customer.get().getNameEnglish()));

                            SalesReport salesReport = schedularAssembler.createSalesReport(invoiceNumber, customer.get(), sheetUniqueId, salesReportHelperDto);
                            salesReportRepository.save(salesReport);

                            Transaction newTransaction = transactionRepository
                                    .findByAccountNumberAndSheetId(accountNumber, sheetUniqueId)
                                    .orElseGet(() -> schedularAssembler.transaction(accountNumber, sheetUniqueId,
                                            "Success", Boolean.TRUE, finalExcelFileName, finalPdfFileName));

                            newTransaction.setCurrentStatus("Success");
                            transactionRepository.save(newTransaction);
                        }
                    } catch (Exception e) {

                        logger.error(String.format("Error while scheduling for  for Account Number %s: " , accountNumber));
                        anyUnsentInvoice = true;
                        Transaction newTransaction = transactionRepository
                                .findByAccountNumberAndSheetId(accountNumber, sheetUniqueId)
                                .orElseGet(() -> schedularAssembler.transaction(accountNumber, sheetUniqueId,
                                        e.getMessage(), Boolean.FALSE,null,null));

                        newTransaction.setCurrentStatus(e.getMessage());
                        transactionRepository.save(newTransaction);

                        e.printStackTrace();
                    }
                } else {
                    logger.warn("Kindly update customer's email and status: " + accountNumber);
                    anyUnsentInvoice = true;
                }
            }
        }

        invoice.setNumber(invoiceNumber);
        invoiceRepository.save(invoice);


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
            invoiceDetailsMap.putIfAbsent(accountNumber, new ArrayList<>());
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



