package com.smsa.backend.service;


import com.smsa.backend.Exception.SheetAlreadyExistException;
import com.smsa.backend.dto.InvoiceDetailsDto;
import com.smsa.backend.model.Customer;
import com.smsa.backend.model.InvoiceDetails;
import com.smsa.backend.model.InvoiceDetailsId;
import com.smsa.backend.model.SheetHistory;
import com.smsa.backend.repository.CustomerRepository;
import com.smsa.backend.repository.InvoiceDetailsRepository;
import com.smsa.backend.repository.SheetHistoryRepository;
import com.smsa.backend.security.util.ExcelHelper;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;



@Service
public class ExcelService {
    @Autowired
    ExcelHelper excelHelper;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    SheetHistoryRepository sheetHistoryRepository;
    @Autowired
    private InvoiceDetailsRepository invoiceDetailsRepository;

    List<InvoiceDetails> invoicesWithAccount = new ArrayList<>();
    List<InvoiceDetails>invoicesWithoutAccount = new ArrayList<>();
    Map<String, List<InvoiceDetails>> mappedRowsMap = new HashMap<>();
    Map<String, String> accountNumberUuidMap = new HashMap<>();

    LocalDate currentDate = LocalDate.now();
    String sheetId = UUID.randomUUID().toString();
    private static final Logger logger = LoggerFactory.getLogger(ExcelService.class);

    public void saveInvoicesToDatabase(MultipartFile file) {
        Map<String, List<InvoiceDetails>> filterd =filterRowsByAccountNumber(file);
        for (List<InvoiceDetails> invoiceDetailsList : filterd.values()) {
            invoiceDetailsRepository.saveAll(invoiceDetailsList);
        }
    }
    private void validateSheetName(String sheetName) {
        if (sheetHistoryRepository.existsByName(sheetName)) {
            logger.info(String.format("Sheet with the name %s already exists!", sheetName));
            throw new SheetAlreadyExistException(String.format("Sheet with the name %s already exists!", sheetName));
        }
    }

    public Map<String, List<InvoiceDetails>> filterRowsByAccountNumber(MultipartFile multipartFile) {
        List<List<String>> rowsToBeFiltered;
        String originalFilename = multipartFile.getOriginalFilename();

        try {
            rowsToBeFiltered = excelHelper.parseExcelFile(multipartFile);
        } catch (Exception e) {
            // Handle the exception, log the error, and return an empty map or null as appropriate
            logger.error("Error parsing Excel file: " + e.getMessage(), e);
            return Collections.emptyMap();
        }

        //TODO: UPDATE WORK OF SHEET
        try {
            validateSheetName(originalFilename);
        } catch (SheetAlreadyExistException e) {
            // Handle the exception, log the error, and throw a new exception or return as appropriate
            logger.error("Sheet with the name " + originalFilename + " already exists!", e);
            throw e;
        }

        if (!rowsToBeFiltered.isEmpty()) {
            rowsToBeFiltered.remove(0);
        }

        for (List<String> row : rowsToBeFiltered) {
            if (row.size() > 2) {
                String commonKey = row.get(2);
                if (!commonKey.isEmpty()) {
                    // Check if the key is not already in the map
                    if (!mappedRowsMap.containsKey(commonKey)) {
                        mappedRowsMap.put(commonKey, new ArrayList<>());
                    }

                    try {
                        // Map the fields from the row list to the InvoiceDetails object
                        InvoiceDetails invoiceDetails = mapToDomain(row);
                        mapHelperFields(invoiceDetails);

                        // Check if the account number exists in the accountNumberUuidMap
                        String accountNumber = invoiceDetails.getInvoiceDetailsId().getAccountNumber();
                        String customerUniqueId = generateCustomerUniqueId(accountNumber);

                        invoiceDetails.setCustomerUniqueId(customerUniqueId);

                        // Add the InvoiceDetails object to the list associated with the common key in the map
                        mappedRowsMap.get(commonKey).add(invoiceDetails);

                        filterAccountAndNonAccountInvoice(invoiceDetails, accountNumber);

                    } catch (Exception e) {
                        // Handle the exception, log the error, and continue processing the next row
                        logger.error("Error mapping row to InvoiceDetails: " + e.getMessage(), e);
                    }
                }
            }
        }

        SheetHistory sheetHistory = createSheetHistory(originalFilename);
        sheetHistoryRepository.save(sheetHistory);

        return mappedRowsMap;

    }

    private void filterAccountAndNonAccountInvoice(InvoiceDetails invoiceDetails, String accountNumber) {

        if (checkAccountNumberInCustomerTable(accountNumber)) {
            invoicesWithAccount.add(invoiceDetails);
        } else {
            invoicesWithoutAccount.add(invoiceDetails);
            Customer customer = createPsedoCustomer(accountNumber);
            customerRepository.save(customer);
        }
    }

    private String generateCustomerUniqueId(String accountNumber) {
        if (accountNumberUuidMap.containsKey(accountNumber)) {
            // If the UUID already exists for the account number, return it
            return accountNumberUuidMap.get(accountNumber);
        } else {
            // If the UUID does not exist for the account number, generate a new one and store it in the map
            String customerUniqueId = UUID.randomUUID().toString();
            accountNumberUuidMap.put(accountNumber, customerUniqueId);
            return customerUniqueId;
        }
    }
    private InvoiceDetails mapHelperFields(InvoiceDetails invoiceDetails) {
        invoiceDetails.setSheetTimesStamp(currentDate);
        invoiceDetails.setSheetUniqueId(sheetId);
        invoiceDetails.setIsSentInMail(Boolean.FALSE);
        invoiceDetails.setCustomerTimestamp(currentDate);
        return invoiceDetails;
    }

    private SheetHistory createSheetHistory(String originalFilename){
        return  SheetHistory.builder()
                .uniqueUUid(sheetId)
                .name(originalFilename)
                .isEmailSent(false)
                .build();
    }
    private Customer createPsedoCustomer(String accountNumber){
        return  Customer
                .builder()
                .nameEnglish("System")
                .accountNumber(accountNumber)
                .status(false)
                .build();
    }
    private boolean checkAccountNumberInCustomerTable(String accountNumber) {
        Optional<Customer> customer = customerRepository.findByAccountNumber(accountNumber);
        return customer.isPresent();
    }

    public List<InvoiceDetails> getInvoicesWithAccount() {
        return invoicesWithAccount;
    }
    public List<InvoiceDetails> getInvoicesWithoutAccount() {
        return invoicesWithoutAccount;
    }

    private InvoiceDetails mapToDomain(List<String> row) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy", Locale.ENGLISH);
        InvoiceDetailsId invoiceDetailsId = InvoiceDetailsId.builder()
                .mawb(parseLongOrDefault(row.get(0), 0L))
                .manifestDate(parseLocalDateOrDefault(row.get(1), null, formatter))
                .accountNumber(row.get(2))
                .awb(parseLongOrDefault(row.get(3), 0L))
                .build();

        InvoiceDetails invoiceDetails = InvoiceDetails.builder()
                .invoiceDetailsId(invoiceDetailsId)
                .orderNumber(row.get(4))
                .origin(row.get(5))
                .destination(row.get(6))
                .shippersName(row.get(7))
                .consigneeName(row.get(8))
                .weight(row.get(9))
                .declaredValue(parseLongOrDefault(row.get(10), 0L))
                .valueCustom(parseLongOrDefault(row.get(11), 0L))
                .vatAmount(parseDoubleOrDefault(row.get(12), 0.0))
                .customFormCharges(parseLongOrDefault(row.get(13), 0L))
                .other(parseLongOrDefault(row.get(14), 0L))
                .totalCharges(parseDoubleOrDefault(row.get(15), 0.0))
                .customDeclarationNumber(parseLongOrDefault(row.get(16), 0L))
                .customDeclarationDate(parseLocalDateOrDefault(row.get(17), null, formatter))
                .build();

        return invoiceDetails;
    }

    // Helper method to parse long values with default value on exception
    private Long parseLongOrDefault(String value, Long defaultValue) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // Helper method to parse double values with default value on exception
    private Double parseDoubleOrDefault(String value, Double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // Helper method to parse LocalDate values with default value on exception
    private LocalDate parseLocalDateOrDefault(String value, LocalDate defaultValue, DateTimeFormatter formatter) {
        try {
            return LocalDate.parse(value, formatter);
        } catch (DateTimeParseException e) {
            return defaultValue;
        }
    }


    public void print(Map<String, List<List<String>>> filteredRowsMap){
        for (Map.Entry<String, List<List<String>>> entry : filteredRowsMap.entrySet()) {
            String commonKey = entry.getKey();
            List<List<String>> rowsForKey = entry.getValue();

            System.out.println("Common Key: " + commonKey);
            for (List<String> row : rowsForKey) {
                // Print each row
                for (String cellValue : row) {
                    System.out.print(cellValue + "\t");
                }
                System.out.println(); // Newline after printing each row
            }
            System.out.println(); // Empty line between each group of rows
        }
    }


    public  Map<String, Map<String, List<List<String>>>> filterRowsByCommonKeyAndMawbNumber(MultipartFile multipartFile) {

        List<List<String>> rowsToBeFiltered = excelHelper.parseExcelFile(multipartFile);
        Map<String, Map<String, List<List<String>>>> filteredRowsMap = new HashMap<>();

        for (List<String> row : rowsToBeFiltered) {
            if (row.size() > 23 && row.size() > 0) {
                String commonKey = row.get(23);
                String mawbNumber = row.get(0);

                if (!commonKey.isEmpty()) {
                    filteredRowsMap.putIfAbsent(commonKey, new HashMap<>());

                    Map<String, List<List<String>>> mawbFilteredRowsMap = filteredRowsMap.get(commonKey);
                    mawbFilteredRowsMap.putIfAbsent(mawbNumber, new ArrayList<>());
                    mawbFilteredRowsMap.get(mawbNumber).add(row);
                }
            }
        }
        printHashMap(filteredRowsMap);
        return filteredRowsMap;
    }


    public void printHashMap(Map<String, Map<String, List<List<String>>>> filteredRowsMap){
        for (Map.Entry<String, Map<String, List<List<String>>>> accountEntry : filteredRowsMap.entrySet()) {
            String accountNumber = accountEntry.getKey();
            Map<String, List<List<String>>> mawbFilteredRowsMap = accountEntry.getValue();

            System.out.println("Account Number: " + accountNumber);

            for (Map.Entry<String, List<List<String>>> mawbEntry : mawbFilteredRowsMap.entrySet()) {
                String mawbNumber = mawbEntry.getKey();
                List<List<String>> rowsForMawb = mawbEntry.getValue();

                System.out.println("MAWB Number: " + mawbNumber);

                for (List<String> row : rowsForMawb) {
                    // Print each row
                    for (String cellValue : row) {
                        System.out.print(cellValue + "\t");
                    }
                    System.out.println(); // Newline after printing each row
                }
                System.out.println(); // Empty line between each group of rows with the same MAWB number
            }
            System.out.println(); // Empty line between each group of rows with the same account number
        }
    }
}



