package com.smsa.backend.controller.scheduler;

import com.smsa.backend.Exception.RecordNotFoundException;
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
        // Get all InvoiceDetails objects for the given sheetUniqueId from the database
        boolean anyUnsentInvoice = false;

        List<InvoiceDetails> invoicesForSheet = invoiceDetailsRepository.findAllBySheetUniqueId(sheetUniqueId);
        invoiceDetailsMap = new HashMap<>();
        invoiceDetailsMap = groupInvoicesByAccountNumber(invoicesForSheet);

        // Iterate over the invoiceDetailsMap and process each InvoiceDetails for each account number
        for (String accountNumber : invoiceDetailsMap.keySet()) {
            List<InvoiceDetails> invoiceDetailsList = invoiceDetailsMap.get(accountNumber);

            // Check if the isSentInMail flag is false for this account number
            if (!checkIsSentInMail(accountNumber)) {
                Optional<Customer> customer = customerRepository.findByAccountNumber(accountNumber);

                // Check if the customer exists and has a valid email
                if (customer != null && customer.get().getEmail() != null) {
                   logger.info("Making excel");
                   excelSheetService.updateExcelFile(invoiceDetailsList,customer.get());

                    invoiceDetailsRepository.updateIsSentInMailByAccountNumber(accountNumber);

                } else {
                    anyUnsentInvoice = true; // Mark anyUnsentInvoice as true if any invoice doesn't have the required fields
                }
            }

        }
        // If any unsent invoice is found, set the isEmailSent flag in SheetHistory to false; otherwise, set it to true
        updateSheetHistoryStatus(sheetUniqueId,anyUnsentInvoice);
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



    private void sendEmail(InvoiceDetails invoiceDetails, Customer customer) {

    }

    private Customer checkAccountNumberInCustomerTable(String accountNumber) {
        Customer customer = customerRepository.findByAccountNumber(accountNumber).get();
        return customer;
    }


}



