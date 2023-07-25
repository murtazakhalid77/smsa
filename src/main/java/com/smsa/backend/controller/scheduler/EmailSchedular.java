package com.smsa.backend.controller.scheduler;

import com.smsa.backend.model.Customer;
import com.smsa.backend.model.InvoiceDetails;
import com.smsa.backend.model.SheetHistory;
import com.smsa.backend.repository.CustomerRepository;
import com.smsa.backend.repository.InvoiceDetailsRepository;
import com.smsa.backend.repository.SheetHistoryRepository;
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

    private static final Logger logger = LoggerFactory.getLogger(EmailSchedular.class);

    @Scheduled(cron = "*/10 * * * * *") // After every 5 min
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
        List<InvoiceDetails> invoicesForSheet = invoiceDetailsRepository.findAllBySheetUniqueId(sheetUniqueId);

        // Create the invoiceDetailsMap to efficiently access InvoiceDetails by accountNumber
        Map<String, List<InvoiceDetails>> invoiceDetailsMap = new HashMap<>();

        for (InvoiceDetails invoiceDetails : invoicesForSheet) {
            String accountNumber = invoiceDetails.getInvoiceDetailsId().getAccountNumber();
            // If the account number is not already in the map, create a new list for it
            invoiceDetailsMap.putIfAbsent(accountNumber, new ArrayList<>());
            // Add the InvoiceDetails object to the list associated with the account number in the map
            invoiceDetailsMap.get(accountNumber).add(invoiceDetails);
        }

            // Iterate over the invoiceDetailsMap and process each InvoiceDetails
            for (List<InvoiceDetails> invoiceDetailsList : invoiceDetailsMap.values()) {
                String accountNumber = invoiceDetailsList.get(0).getInvoiceDetailsId().getAccountNumber();

                // Check if the invoice has an account number
            if (checkAccountNumberInCustomerTable(accountNumber) != null) {
                Customer customer = customerRepository.findByAccountNumber(accountNumber);

                // Perform checks to see if all required fields in the Customer entity have values
                if (customer.getEmail() != null) {
                    // If all required fields have values, then send the email for each InvoiceDetails in the list
                    for (InvoiceDetails invoiceDetails : invoiceDetailsList) {
                        sendEmail(invoiceDetails, customer); // Implement this method to send the email
                    }
                }
            }

            // Perform calculations for other fields in the InvoiceDetails entity for each InvoiceDetails in the list
            // ...

            // Save the updated InvoiceDetails objects in the database for each InvoiceDetails in the list
            for (InvoiceDetails invoiceDetails : invoiceDetailsList) {
                invoiceDetailsRepository.save(invoiceDetails);
            }
        }

        // After processing all rows, update the isEmailSent flag in the SheetHistory and save the changes
        SheetHistory sheetHistory = sheetHistoryRepository.findByUniqueUUid(sheetUniqueId);
        if (sheetHistory != null) {
            sheetHistory.setIsEmailSent(true);
            sheetHistoryRepository.save(sheetHistory);
        }
    }


    private void sendEmail(InvoiceDetails invoiceDetails, Customer customer) {

    }

    private Customer checkAccountNumberInCustomerTable(String accountNumber) {
        Customer customer = customerRepository.findByAccountNumber(accountNumber);
        return customer;
    }


}



