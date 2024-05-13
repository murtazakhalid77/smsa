package com.smsa.backend.security.util;

import com.smsa.backend.model.Currency;
import com.smsa.backend.model.Custom;
import com.smsa.backend.model.Customer;
import com.smsa.backend.model.InvoiceDetails;
import com.smsa.backend.service.CurrencyService;
import com.smsa.backend.service.HelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class HashMapHelper {

    @Autowired
    CurrencyService currencyService;
    @Autowired
    HelperService   helperService;

    public List<Map<String, Object>> calculateValues(Map<String, List<InvoiceDetails>> filteredRowsMap, Customer customer, Custom custom, Long invoiceNumber, String sheetUniqueId) {
        List<Map<String, Object>> resultList = new ArrayList<>();

        Currency currency = currencyService.findByCurrencyFromAndCurrencyTo(custom, customer);
        Double conversionRate = currency.getConversionRate();


        Long totalAwbCount = 0L;
        Double customerShipmentValue = 0.0;
        Double vatAmountCustomDeclartionForm = 0.0;
        Double customFormChares = 0.0;
        Double others = 0.0;
        Double totalDeclaredValue = 0.0;
        //
        Double vatAmountCustomerCurrency = 0.0;
        Double customFormChargesCustomerCurrency = 0.0;
        Double otherCustomerCurrency = 0.0;


        for (Map.Entry<String, List<InvoiceDetails>> entry : filteredRowsMap.entrySet()) {
            String mawbNumber = entry.getKey();
            List<InvoiceDetails> filterInvoiceDetails = entry.getValue();

            Map<String, Object> calculatedValuesMap = new HashMap<>(); // Create a new Map for each iteration

            Set<String> customDecarationNumberSet = new HashSet<>();
            Set<String> customDecarationDateSet = new HashSet<>();


            totalAwbCount = 0L;
            customerShipmentValue = 0.0;
            vatAmountCustomDeclartionForm = 0.0;
            customFormChares = 0.0;
            others = 0.0;
            totalDeclaredValue = 0.0;

            vatAmountCustomerCurrency = 0.0;
            customFormChargesCustomerCurrency = 0.0;
            otherCustomerCurrency = 0.0;
            try {
                for (InvoiceDetails invoiceDetails : filterInvoiceDetails) {
                    totalAwbCount += 1;
                    customerShipmentValue = customerShipmentValue + invoiceDetails.getValueCustom();
                    vatAmountCustomDeclartionForm += invoiceDetails.getVatAmount();
                    customFormChares += invoiceDetails.getCustomFormCharges();
                    others += invoiceDetails.getOther();

                    vatAmountCustomerCurrency += invoiceDetails.getVatAmount() * conversionRate;
                    customFormChargesCustomerCurrency += invoiceDetails.getCustomFormCharges() * conversionRate;
                    otherCustomerCurrency += invoiceDetails.getOther() * conversionRate;
                    totalDeclaredValue += invoiceDetails.getDeclaredValue();
//                    totalDeclaredValue += invoiceDetails.getTotalCharges() + invoiceDetails.getValueCustom() + invoiceDetails.getVatAmount() + invoiceDetails.getCustomFormCharges() + invoiceDetails.getOther();
                    customDecarationNumberSet.add(invoiceDetails.getCustomDeclarationNumber());
                    customDecarationDateSet.add(invoiceDetails.getCustomDeclarationDate());

                }
                String dateSetString = customDecarationDateSet.toString();
                String customDeclarationNumbers = customDecarationNumberSet.stream()
                        .limit(3)  // Limit to the first three elements
                        .map(String::valueOf)
                        .collect(Collectors.joining("/"));
                // Put the MAWB number into the calculatedValuesMap for each iteration
                calculatedValuesMap.put("MawbNumber", mawbNumber);
                Double totalCharges = customFormChares + others + vatAmountCustomDeclartionForm;
                Double totalChargesCustomerCurrency = customFormChargesCustomerCurrency + otherCustomerCurrency + vatAmountCustomerCurrency;

                // Put the calculated values into the result map
                calculatedValuesMap.put("TotalAwbCount", totalAwbCount);
                calculatedValuesMap.put("CustomerShipmentValue", customerShipmentValue);
                calculatedValuesMap.put("VatAmountCustomDeclarationForm", vatAmountCustomDeclartionForm);
                calculatedValuesMap.put("CustomFormCharges", customFormChares);
                calculatedValuesMap.put("Others", others);
                calculatedValuesMap.put("TotalCharges", totalCharges); //for excel
                calculatedValuesMap.put("CustomDeclarationCurrency", custom.getCurrency());
                calculatedValuesMap.put("TotalDeclaredValue", totalDeclaredValue);   //for excel
                calculatedValuesMap.put("VatAmountCustomerCurrency", vatAmountCustomerCurrency);
                calculatedValuesMap.put("CustomFormChargesCustomerCurrency", customFormChargesCustomerCurrency);
                calculatedValuesMap.put("OtherCustomerCurrency", otherCustomerCurrency);
                calculatedValuesMap.put("TotalChargesCustomerCurrency", totalChargesCustomerCurrency);
                calculatedValuesMap.put("CustomDeclarationNumber", customDeclarationNumbers);
                calculatedValuesMap.put("CustomerAccountNumber", customer.getAccountNumber());
                calculatedValuesMap.put("InvoiceNumber", "ECDV-" + invoiceNumber);
                calculatedValuesMap.put("InvoiceType", "Bill-Shipper");
                calculatedValuesMap.put("CustomDeclarationDate",
                        dateSetString.substring(1, dateSetString.length() - 1));
                calculatedValuesMap.put("SMSAFeeCharges", customer.getSmsaServiceFromSAR());

                calculatedValuesMap.put("CustomPort", custom.getCustomPort());
                calculatedValuesMap.put("VatOnSmsaFees",
                        calculateVatOnSmsaFees(Double.valueOf(calculatedValuesMap.get("SMSAFeeCharges").toString()),
                                customer.getRegion().getVat())); //for pdf

                calculatedValuesMap.put("TotalAmount",
                        calculateTotalAmount(calculatedValuesMap.get("TotalChargesCustomerCurrency").toString(),
                                calculatedValuesMap.get("SMSAFeeCharges").toString(),
                                calculatedValuesMap.get("VatOnSmsaFees").toString()));

                resultList.add(calculatedValuesMap);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }

        return resultList;
    }

    public Map<String, Double> sumNumericColumns(List<Map<String, Object>> resultList) {
        Map<String, Double> sumMap = new HashMap<>();
        try {
            Double totalValue = 0.0;
            Double customerShipmentValueSum = 0.0;
            Double vatAmountCustomDeclarationFormSum = 0.0;
            Double customFormChargesSum = 0.0;
            Double othersSum = 0.0;
            Double totalChargesSum = 0.0;

            Double vatAmountCustomerCurrencySum = 0.0;
            Double customFormChargesCustomerCurrencySum = 0.0;
            Double otherCustomerCurrencySum = 0.0;
            Double totalChargesCustomerCurrencySum = 0.0;

            for (Map<String, Object> calculatedValuesMap : resultList) {
                customerShipmentValueSum += (Double) calculatedValuesMap.get("CustomerShipmentValue");
                vatAmountCustomDeclarationFormSum += (Double) calculatedValuesMap.get("VatAmountCustomDeclarationForm");
                customFormChargesSum += (Double) calculatedValuesMap.get("CustomFormCharges");
                othersSum += (Double) calculatedValuesMap.get("Others");
                totalChargesSum += (Double) calculatedValuesMap.get("TotalCharges");
                totalValue += (Double) calculatedValuesMap.get("TotalDeclaredValue");

                vatAmountCustomerCurrencySum += (Double) calculatedValuesMap.get("VatAmountCustomerCurrency");
                customFormChargesCustomerCurrencySum += (Double) calculatedValuesMap.get(
                        "CustomFormChargesCustomerCurrency");
                otherCustomerCurrencySum += (Double) calculatedValuesMap.get("OtherCustomerCurrency");
                totalChargesCustomerCurrencySum += (Double) calculatedValuesMap.get("TotalChargesCustomerCurrency");
            }

            sumMap.put("TotalValueSum", totalValue);
            sumMap.put("CustomerShipmentValueSum", customerShipmentValueSum);
            sumMap.put("VatAmountCustomDeclarationFormSum", vatAmountCustomDeclarationFormSum);
            sumMap.put("CustomFormChargesSum", customFormChargesSum);
            sumMap.put("OthersSum", othersSum);
            sumMap.put("TotalChargesSum", totalChargesSum);

            sumMap.put("VatAmountCustomerCurrencySum", vatAmountCustomerCurrencySum);
            sumMap.put("CustomFormChargesCustomerCurrencySum", customFormChargesCustomerCurrencySum);
            sumMap.put("OtherCustomerCurrencySum", otherCustomerCurrencySum);
            sumMap.put("TotalChargesCustomerCurrencySum", totalChargesCustomerCurrencySum);

            return sumMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException
                    (e.getMessage());
        }

    }

    public Double calculateVatOnSmsaFees(Double smsaFeesCharges, Double smsaFeeVat) {
        try{
            return (smsaFeesCharges * smsaFeeVat) / 100;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    private Double calculateTotalAmount(String totalChargesCustomerCurrency, String smsaFeesCharges, String vatOnSmsaFees) {
        Double totalCharges = Double.valueOf(totalChargesCustomerCurrency);
        Double vatPercentage = Double.valueOf(vatOnSmsaFees);
        Double smsaFeesChargesValue = Double.valueOf(smsaFeesCharges);


        // Calculate the total amount by summing up all the charges
        Double totalAmount = totalCharges + vatPercentage + smsaFeesChargesValue;

        return totalAmount;
    }


    private Map<String, String> getColumnMapping() {
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("Invoice Type", "InvoiceType");
        columnMapping.put("Custom Port", "CustomPort");
        columnMapping.put("Custom Declartion Date", "CustomDeclarationDate");
        columnMapping.put("Invoice#", "InvoiceNumber");
        columnMapping.put("MAWB Number", "MAWBNumber");
        columnMapping.put("Custom Declartion#", "CustomDeclarationNumber");
        columnMapping.put("Total AWB Count", "TotalAwbCount");
        columnMapping.put("Total Value", "TotalValue");
        columnMapping.put("Value (Custom)", "CustomerShipmentValue");
        columnMapping.put("VAT Amount", "VatAmountCustomDeclarationForm");
        columnMapping.put("Custom Form Charges", "CustomFormCharges");
        columnMapping.put("Other", "Others");
        columnMapping.put("Total Charges", "TotalCharges");
        columnMapping.put("Custom Declartion Currency", "CustomDeclarationCurrency");
        columnMapping.put("VAT Amount", "VatAmountCustomerCurrency");
        columnMapping.put("Custom Form Charges", "CustomFormChargesCustomerCurrency");
        columnMapping.put("Other", "OtherCustomerCurrency");
        columnMapping.put("Total Charges", "TotalChargesCustomerCurrency");
        return columnMapping;
    }


    public Map<String, List<InvoiceDetails>> filterRowsByMawbNumber(List<InvoiceDetails> invoiceDetailsList) throws RuntimeException {
        Map<String, List<InvoiceDetails>> filteredRowsMap = new HashMap<>();

        try {
            for (InvoiceDetails invoiceDetails : invoiceDetailsList) {
                String mawbNumber = invoiceDetails.getInvoiceDetailsId().getMawb();

                if (!mawbNumber.isEmpty()) {
                    filteredRowsMap.putIfAbsent(mawbNumber, new ArrayList<>());
                    filteredRowsMap.get(mawbNumber).add(invoiceDetails);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return filteredRowsMap;
    }
}
