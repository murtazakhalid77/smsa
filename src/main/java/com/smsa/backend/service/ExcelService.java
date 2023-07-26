package com.smsa.backend.service;


import com.smsa.backend.Exception.SheetAlreadyExistException;
import com.smsa.backend.controller.scheduler.EmailSchedular;
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
import java.util.*;



@Service
public class ExcelService {
    @Autowired
    ExcelHelper excelHelper;
    ModelMapper modelMapper;
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
    public Map<String, List<InvoiceDetails>> filterRowsByAccountNumber(MultipartFile multipartFile) {
        List<List<String>> rowsToBeFiltered = excelHelper.parseExcelFile(multipartFile);

        String originalFilename = multipartFile.getOriginalFilename();

        //TODO: UPDATE WORK OF SHEET
        if (sheetHistoryRepository.existsByName(originalFilename)) {
            logger.info(String.format("Sheet with the name %s already exists!",originalFilename));
            throw new SheetAlreadyExistException(String.format("Sheet with the name %s already exists!",originalFilename));
        }

        if (rowsToBeFiltered.size() > 0) {
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
                    // Map the fields from the row list to the InvoiceDetails object

                    InvoiceDetails invoiceDetails = mapToDomain(row);

                    invoiceDetails.setSheetTimesStamp(currentDate);
                    invoiceDetails.setSheetUniqueId(sheetId);
                    invoiceDetails.setIsSentInMail(Boolean.FALSE);
                    invoiceDetails.setCustomerTimestamp(currentDate);

                    // Check if the account number exists in the accountNumberUuidMap
                    String accountNumber = invoiceDetails.getInvoiceDetailsId().getAccountNumber();
                    if (accountNumberUuidMap.containsKey(accountNumber)) {
                        // If the UUID already exists for the account number, set it to the InvoiceDetails
                        String customerUniqueId = accountNumberUuidMap.get(accountNumber);
                        invoiceDetails.setCustomerUniqueId(customerUniqueId);
                    } else {
                        // If the UUID does not exist for the account number, generate a new one and set it
                        String customerUniqueId = UUID.randomUUID().toString();
                        accountNumberUuidMap.put(accountNumber, customerUniqueId);
                        invoiceDetails.setCustomerUniqueId(customerUniqueId);
                    }

                    // Add the InvoiceDetails object to the list associated with the common key in the map
                    mappedRowsMap.get(commonKey).add(invoiceDetails);

                    // Check if the invoice has an account number
                    if (checkAccountNumberInCustomerTable(accountNumber)) {
                        invoicesWithAccount.add(invoiceDetails);
                    } else {
                        invoicesWithoutAccount.add(invoiceDetails);
                        Customer customer = Customer
                                .builder()
                                .nameEnglish("System")
                                .accountNumber(accountNumber)
                                .status(false)
                                .build();
                        customerRepository.save(customer);
                    }
                }
            }
        }

        SheetHistory sheetHistory =SheetHistory.builder()
                .uniqueUUid(sheetId)
                .name(originalFilename)
                .isEmailSent(false)
                .build();
        sheetHistoryRepository.save(sheetHistory);

        return mappedRowsMap;
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
                .mawb(row.get(0).equals("-")||row.get(0).equals("") ? Long.parseLong("") :Long.parseLong(row.get(0)))
                .manifestDate(row.get(1).equals("-") || row.get(1).equals("") ? LocalDate.parse("") :LocalDate.parse(row.get(1),formatter))
                .accountNumber(row.get(2).equals("-") || row.get(2).equals("") ? "" :row.get(2))
                .awb(row.get(3).equals("-") || row.get(3).equals("") ? Long.parseLong("") :Long.parseLong(row.get(3)))
                .build();

        InvoiceDetails invoiceDetails = InvoiceDetails.builder()
                .invoiceDetailsId(invoiceDetailsId)
                .orderNumber(row.get(4).equals("-") || row.get(4).equals("") ? "" : row.get(4))
                .origin(row.get(5).equals("-") || row.get(5).equals("") ? "" : row.get(5))
                .destination(row.get(6).equals("-") || row.get(6).equals("") ? "" : row.get(6))
                .shippersName(row.get(7).equals("-") || row.get(7).equals("") ? "" : row.get(7))
                .consigneeName(row.get(8).equals("-") || row.get(8).equals("") ? "" : row.get(8))
                .weight(row.get(9).equals("-") || row.get(9).equals("") ? "" : row.get(9))
                .declaredValue(row.get(10).equals("-") || row.get(10).equals("") ? Long.parseLong("") : Long.parseLong(row.get(10)))
                .valueCustom(row.get(11).equals("-") || row.get(11).equals("") ? Long.parseLong("") : Long.parseLong(row.get(11)))
                .vatAmount(row.get(12).equals("-") || row.get(12).equals("") ? Double.parseDouble("") : Double.parseDouble(row.get(12)))
                .customFormCharges(row.get(13).equals("-") || row.get(13).equals("") ? Long.parseLong("") : Long.parseLong(row.get(13)))
                .other(row.get(14).equals("-") || row.get(14).equals("") ? Long.parseLong("") : Long.parseLong(row.get(14)))
                .totalCharges(row.get(15).equals("-") || row.get(15).equals("") ? Double.parseDouble("") : Double.parseDouble(row.get(15)))
                .customDeclarationNumber(row.get(16).equals("") || row.get(16).equals("") ? Long.parseLong("") : Long.parseLong(row.get(16)))
                .customDeclarationDate(row.get(17).equals("") || row.get(17).equals("") ? LocalDate.parse("") : LocalDate.parse(row.get(17), formatter))
                .build();
        // Custom Declaration Date

        return invoiceDetails;
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


    //    public  void  fiterValuesbyMawb(List<List<String>> rowsToBeFilteredByMawb, int secondColumnIndex, String mawb) {
//
//        List<String> pdfRow= new ArrayList<>();
//        for (List<String> row : rowsToBeFilteredByMawb) {
//            if (row.size() > secondColumnIndex) {
//                String secondColumnValue = row.get(secondColumnIndex);
//                if (mawb.equals(secondColumnValue)) {
//                    weight+=Double.parseDouble(row.get(9));
//                    awbsCount+=1;
//                    devalaredValueSAR+=Double.parseDouble(row.get(10));
//                    vatAmount+=Double.parseDouble(row.get(11));
//                    documentation+=Double.parseDouble(row.get(12));
//
//
//                }
//            }
//        }
//        pdfRow.add(mawb);
//        pdfRow.add(String.valueOf(weight));
//        pdfRow.add(String.valueOf(awbsCount));
//        pdfRow.add(String.valueOf(devalaredValueSAR));
//        pdfRow.add(String.valueOf(vatAmount));
//        pdfRow.add(String.valueOf(documentation));
//
//        pdfRows.add(pdfRow);
//        pdfRow.clear();
//
//        awbsCount=0.0;
//        weight=0.0;
//        devalaredValueSAR=0.0;
//        vatAmount=0.0;
//        documentation=0.0;
//
//    }
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
    public List<InvoiceDetails> toDomain(List<InvoiceDetailsDto> invoiceDetailsDtoList) {
        java.lang.reflect.Type targetListType = new TypeToken<List<InvoiceDetails>>() {}.getType();
        return modelMapper.map(invoiceDetailsDtoList, targetListType);
    }
    public  InvoiceDetailsDto toDto(InvoiceDetails invoiceDetails){
        return modelMapper.map(invoiceDetails,InvoiceDetailsDto.class);
    }

}



