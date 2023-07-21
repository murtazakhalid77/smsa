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
    private String origin = "";
    private Double awbsCount = 0.0;
    private Double weight = 0.0;
    private Double devalaredValueSAR = 0.0;
    private Double vatAmount = 0.0;
    private Double documentation = 0.0;
    private Double sar = 0.0;
    private Double smsaFeesCharges = 0.0;
    private Double usDollar = 0.0;
    List<List<String>> pdfRows = new ArrayList<>();

    public void importDataFromExcel(MultipartFile file) {
        List<List<String>> rows = ExcelHelper.parseExcelFile(file);


        // Skipping the header row
        if (rows.size() > 0) {
            rows.remove(0);
        }

        List<List<InvoiceDetails>> allInvoices = maptoDomain(rows);

        List<List<InvoiceDetails>> NonAccountInvoice = filterNonAccountInvoices(allInvoices);


        List<InvoiceDetails> flatInvoiceList = new ArrayList<>();

        for (List<InvoiceDetails> invoiceList : allInvoices) {
            flatInvoiceList.addAll(invoiceList);
        }

        invoiceDetailsRepository.saveAll(flatInvoiceList);
    }
    public List<List<InvoiceDetails>> filterNonAccountInvoices(List<List<InvoiceDetails>> allInvoices) {
        List<List<InvoiceDetails>> invoicesWithAccount = new ArrayList<>();
        List<List<InvoiceDetails>> invoicesWithoutAccount = new ArrayList<>();

        // Generate a single UUID for the sheetUniqueId column for all invoices
        String sheetUniqueId = UUID.randomUUID().toString();

        // HashMap to store unique customers and their UUIDs
        HashMap<String, String> customerUuidMap = new HashMap<>();

        for (List<InvoiceDetails> invoiceList : allInvoices) {
            boolean hasAccountNumber = false;

            for (InvoiceDetails invoiceDetails : invoiceList) {
                String accountNumber = invoiceDetails.getInvoiceDetailsId().getAccountNumber();
                if (checkAccountNumberInCustomerTable(accountNumber)) {
                    hasAccountNumber = true;
                    break;
                }
            }

            if (hasAccountNumber) {
                invoicesWithAccount.add(invoiceList);
            } else {
                // Assign the same UUID for the sheetUniqueId column for all invoices
                for (InvoiceDetails invoiceDetails : invoiceList) {
                    invoiceDetails.setSheetUniqueId(sheetUniqueId);
                    invoiceDetails.setSheetTimesStamp(LocalDate.now());
                }
                invoicesWithoutAccount.add(invoiceList);
            }

            // Ensure each customer has a unique UUID and update the customerUniqueId column
            for (InvoiceDetails invoiceDetails : invoiceList) {
                String accountNumber = invoiceDetails.getInvoiceDetailsId().getAccountNumber();
                if (!customerUuidMap.containsKey(accountNumber)) {
                    String customerUniqueId = UUID.randomUUID().toString();
                    customerUuidMap.put(accountNumber, customerUniqueId);
                }
                invoiceDetails.setCustomerUniqueId(customerUuidMap.get(accountNumber));
                invoiceDetails.setCustomerTimestamp(LocalDate.now() );
            }
        }

        return invoicesWithoutAccount;
    }


    private List<List<InvoiceDetails>> maptoDomain(List<List<String>> rows) {
        List<List<InvoiceDetails>> invoices = new ArrayList<>();

        for (List<String> rowData : rows) {
            InvoiceDetails invoiceDetails = mapRowToEntity(rowData);
            invoices.add(Collections.singletonList(invoiceDetails));
        }
        return invoices;
    }
    private boolean checkAccountNumberInCustomerTable(String accountNumber) {
        Customer customer = customerRepository.findByAccountNumber(accountNumber);
        return customer != null;
    }

    private InvoiceDetails mapRowToEntity(List<String> rowData) {
        InvoiceDetails invoiceDetails = new InvoiceDetails();
        InvoiceDetailsId invoiceDetailsId = new InvoiceDetailsId();

        invoiceDetailsId.setMawb(Long.parseLong(rowData.get(0)));
        invoiceDetailsId.setManifestDate(rowData.get(1));
        invoiceDetailsId.setAccountNumber(rowData.get(2));
        invoiceDetailsId.setAwb(Long.parseLong(rowData.get(3)));

        invoiceDetails.setInvoiceDetailsId(invoiceDetailsId);
        invoiceDetails.setOrderNumber(rowData.get(4));
        invoiceDetails.setOrigin(rowData.get(5));
        invoiceDetails.setDestination(rowData.get(6));
        invoiceDetails.setShippersName(rowData.get(7));
        invoiceDetails.setConsigneeName(rowData.get(8));
        invoiceDetails.setWeight(rowData.get(9));
        invoiceDetails.setDeclaredValue(Long.parseLong(rowData.get(10)));
        invoiceDetails.setValueCustom(Long.parseLong(rowData.get(11)));
        invoiceDetails.setVatAmount(Double.parseDouble(rowData.get(12)));
        invoiceDetails.setCustomFormCharges(Long.parseLong(rowData.get(13)));
        invoiceDetails.setOther(Long.parseLong(rowData.get(14)));
        invoiceDetails.setCustomDeclaration(rowData.get(15));
        if(rowData.get(16).equals("")|| rowData.get(16)==null){
            invoiceDetails.setCustomDeclarationDate(null);
        }


        return invoiceDetails;
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



