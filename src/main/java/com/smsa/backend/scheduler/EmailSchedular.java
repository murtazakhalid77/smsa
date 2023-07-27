package com.smsa.backend.scheduler;

import com.smsa.backend.model.Customer;
import com.smsa.backend.model.InvoiceDetails;
import com.smsa.backend.model.SheetHistory;
import com.smsa.backend.repository.CustomerRepository;
import com.smsa.backend.repository.InvoiceDetailsRepository;
import com.smsa.backend.repository.SheetHistoryRepository;
import com.smsa.backend.service.ExcelSheetService;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class EmailSchedular {
    @Autowired
    SheetHistoryRepository sheetHistoryRepository;

    @Autowired
    InvoiceDetailsRepository invoiceDetailsRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ExcelSheetService excelSheetService;

    private static final Logger logger = LoggerFactory.getLogger(EmailSchedular.class);
    private Map<String, List<InvoiceDetails>> invoiceDetailsMap;

    @Scheduled(initialDelay = 5000, fixedDelay = 600000)
    public void markSentAndProcessInvoices() {
        List<SheetHistory> sheetsToBeSent = sheetHistoryRepository.findAllByIsEmailSentFalse();

        for (SheetHistory sheetHistory : sheetsToBeSent) {
            String sheetUniqueId = sheetHistory.getUniqueUUid();

            // Call a method to process invoices for this sheetUniqueId
            processInvoicesForSheet(sheetUniqueId);
        }
    }
    public void processInvoicesForSheet(String sheetUniqueId) {
        boolean anyUnsentInvoice = false;

        List<InvoiceDetails> invoicesForSheet = invoiceDetailsRepository.findAllBySheetUniqueId(sheetUniqueId);
        invoiceDetailsMap = new HashMap<>();
        invoiceDetailsMap = groupInvoicesByAccountNumber(invoicesForSheet);

        for (String accountNumber : invoiceDetailsMap.keySet()) {
            List<InvoiceDetails> invoiceDetailsList = invoiceDetailsMap.get(accountNumber);

            if (invoiceDetailsList == null) {
                logger.warn("No invoice details found for account number: " + accountNumber);
                anyUnsentInvoice = true;
                continue; // Skip processing for this account number
            }

            if (!checkIsSentInMail(accountNumber)) {
                Optional<Customer> customer = customerRepository.findByAccountNumber(accountNumber);

                if (customer.isPresent() && customer.get().getEmail() != null) {
                    logger.info("Making excel");
                    try {
                        excelSheetService.updateExcelFile(invoiceDetailsList, customer.get());

                        invoiceDetailsRepository.updateIsSentInMailByAccountNumberAndSheetUniqueId(accountNumber, sheetUniqueId);
                    }
                     catch (IOException e) {
                        throw new RuntimeException("Error while creating Excel");
                    }
                } else {
                    logger.warn("Customer not found or no email provided for account number: " + accountNumber);
                    anyUnsentInvoice = true;
                }
            }
        }

        updateSheetHistoryStatus(sheetUniqueId, anyUnsentInvoice);
    }

    private void updateSheetHistoryStatus(String sheetUniqueId, boolean anyUnsentInvoice) {
        SheetHistory sheetHistory = sheetHistoryRepository.findByUniqueUUid(sheetUniqueId);
        if (sheetHistory != null) {
            sheetHistory.setIsEmailSent(!anyUnsentInvoice);
            sheetHistoryRepository.save(sheetHistory);
        }
    }

    private Map<String, List<InvoiceDetails>> groupInvoicesByAccountNumber(List<InvoiceDetails> invoicesForSheet){
        for (InvoiceDetails invoiceDetails : invoicesForSheet) {
            String accountNumber = invoiceDetails.getInvoiceDetailsId().getAccountNumber();
            // If the account number is not already in the map, create a new list for it
            invoiceDetailsMap.putIfAbsent(accountNumber, new ArrayList<>());
            // Add the InvoiceDetails object to the list associated with the account number in the map
            invoiceDetailsMap.get(accountNumber).add(invoiceDetails);
        }
        return invoiceDetailsMap;
    }

    private boolean checkIsSentInMail(String accountNumber) {
        List<InvoiceDetails> invoiceDetailsList = invoiceDetailsMap.get(accountNumber);
        return invoiceDetailsList != null && !invoiceDetailsList.isEmpty()
                && invoiceDetailsList.get(0).getIsSentInMail();
    }
}



