package com.smsa.backend.service;


import com.smsa.backend.dto.InvoiceDetailsDto;
import com.smsa.backend.model.Customer;
import com.smsa.backend.model.InvoiceDetails;
import com.smsa.backend.model.InvoiceDetailsId;
import com.smsa.backend.repository.CustomerRepository;
import com.smsa.backend.repository.InvoiceDetailsRepository;
import com.smsa.backend.security.util.ExcelHelper;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ExcelService {
    @Autowired
    ExcelHelper excelHelper;
    ModelMapper modelMapper;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    private InvoiceDetailsRepository invoiceDetailsRepository;
    List<InvoiceDetails> invoicesWithAccount = new ArrayList<>();
    List<InvoiceDetails>invoicesWithoutAccount = new ArrayList<>();


    public void saveInvoicesToDatabase(MultipartFile file) {
        Map<String, List<InvoiceDetails>> filterd =filterRowsByAccountNumber(file);
        for (List<InvoiceDetails> invoiceDetailsList : filterd.values()) {

            invoiceDetailsRepository.saveAll(invoiceDetailsList);
        }
    }
    public Map<String, List<InvoiceDetails>> filterRowsByAccountNumber(MultipartFile multipartFile) {
        List<List<String>> rowsToBeFiltered = excelHelper.parseExcelFile(multipartFile);

        Map<String, List<InvoiceDetails>> mappedRowsMap = new HashMap<>();
        Map<String, String> accountNumberUuidMap = new HashMap<>();

        LocalDate currentDate = LocalDate.now();
        String sheetId = UUID.randomUUID().toString();

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
                    }
                }
            }
        }
        return mappedRowsMap;
    }
    private boolean checkAccountNumberInCustomerTable(String accountNumber) {
        Customer customer = customerRepository.findByAccountNumber(accountNumber);
        return customer != null;
    }



    private InvoiceDetails mapToDomain(List<String> row) {
        InvoiceDetails invoiceDetails = new InvoiceDetails();

        // Assuming the index format is as follows:
        invoiceDetails.getInvoiceDetailsId().setMawb(Long.parseLong(row.get(0))); // MAWB
        invoiceDetails.getInvoiceDetailsId().setManifestDate(row.get(1)); // Manifest Date
        invoiceDetails.getInvoiceDetailsId().setAccountNumber(row.get(2)); // Account Number
        invoiceDetails.getInvoiceDetailsId().setAwb(Long.parseLong(row.get(3))); // AWB

        invoiceDetails.setInvoiceDetailsId(invoiceDetails.getInvoiceDetailsId());
        invoiceDetails.setOrderNumber(row.get(4)); // OrderNumber
        invoiceDetails.setOrigin(row.get(5)); // Origin
        invoiceDetails.setDestination(row.get(6)); // Destination
        invoiceDetails.setShippersName(row.get(7)); // Shipper Name
        invoiceDetails.setConsigneeName(row.get(8)); // Consignee Name
        invoiceDetails.setWeight(row.get(9)); // Weight
        invoiceDetails.setDeclaredValue(Long.parseLong(row.get(10))); // Declared Value
        invoiceDetails.setValueCustom(Long.parseLong(row.get(11))); // Value (Custom)
        invoiceDetails.setVatAmount(Double.parseDouble(row.get(12))); // VAT Amount
        invoiceDetails.setCustomFormCharges(Long.parseLong(row.get(13))); // Custom Form Charges
        invoiceDetails.setOther(Long.parseLong(row.get(14))); // Other
        invoiceDetails.setTotalCharges(Double.parseDouble(row.get(15))); // Total Charges
        invoiceDetails.setCustomDeclaration(row.get(16)); // Custom Declaration #
        if (row.get(17).equals("")||row.get(17)==null){
        invoiceDetails.setCustomDeclarationDate(null);} // Custom Declaration Date

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



