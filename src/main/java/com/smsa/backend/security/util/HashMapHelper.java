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
    HelperService helperService;

    public List<Map<String, Object>>    calculateValues(Map<String, List<InvoiceDetails>> filteredRowsMap, Customer customer, Custom custom,Long invoiceNumber,String sheetUniqueId) {
        List<Map<String, Object>> resultList = new ArrayList<>();

        Currency currency = currencyService.findByCurrencyFromAndCurrencyTo(custom,customer);
        Double conversionRate = Double.parseDouble(currency.getConversionRate());


        Long totalAwbCount = 0L;
        Double customerShipmentValue = 0.0;
        Double vatAmountCustomDeclartionForm = 0.0;
        Double customFormChares = 0.0;
        Double others = 0.0;
        Double totalValue=0.0;
        //
        Double vatAmountCustomerCurrency=0.0;
        Double customFormChargesCustomerCurrency=0.0;
        Double otherCustomerCurrency=0.0;




        for (Map.Entry<String, List<InvoiceDetails>> entry : filteredRowsMap.entrySet()) {
            String mawbNumber = entry.getKey();
            List<InvoiceDetails> filterInvoiceDetails = entry.getValue();

            Map<String, Object> calculatedValuesMap = new HashMap<>(); // Create a new Map for each iteration

            Set<Long> customDecarationNumberSet = new HashSet<>();
            Set<String> customDecarationDateSet = new HashSet<>();


            totalAwbCount = 0L;
            customerShipmentValue = 0.0;
            vatAmountCustomDeclartionForm = 0.0;
            customFormChares = 0.0;
            others = 0.0;
            totalValue=0.0;

            vatAmountCustomerCurrency=0.0;
            customFormChargesCustomerCurrency=0.0;
            otherCustomerCurrency=0.0;





            for (InvoiceDetails invoiceDetails : filterInvoiceDetails) {
                totalAwbCount += 1;
                customerShipmentValue = customerShipmentValue + invoiceDetails.getValueCustom();
                vatAmountCustomDeclartionForm += invoiceDetails.getVatAmount();
                customFormChares += invoiceDetails.getCustomFormCharges();
                others += invoiceDetails.getOther();

                vatAmountCustomerCurrency+=invoiceDetails.getVatAmount()*conversionRate;
                customFormChargesCustomerCurrency+=invoiceDetails.getCustomFormCharges()*conversionRate;
                otherCustomerCurrency+=invoiceDetails.getOther()*conversionRate;

                totalValue += invoiceDetails.getTotalCharges()+ invoiceDetails.getValueCustom() + invoiceDetails.getVatAmount() + invoiceDetails.getCustomFormCharges() + invoiceDetails.getOther();
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
            Double totalCharges = customFormChares + others;
            Double totalChargesCustomerCurrency=customFormChargesCustomerCurrency+otherCustomerCurrency;

            // Put the calculated values into the result map
            calculatedValuesMap.put("TotalAwbCount", totalAwbCount);
            calculatedValuesMap.put("CustomerShipmentValue", customerShipmentValue);
            calculatedValuesMap.put("VatAmountCustomDeclarationForm", vatAmountCustomDeclartionForm);
            calculatedValuesMap.put("CustomFormCharges", customFormChares);
            calculatedValuesMap.put("Others", others);
            calculatedValuesMap.put("TotalCharges", totalCharges); //for excel
            calculatedValuesMap.put("CustomDeclarationCurrency", custom.getCurrency());
            calculatedValuesMap.put("TotalValue", totalValue);   //for excel
            calculatedValuesMap.put("VatAmountCustomerCurrency", vatAmountCustomerCurrency);
            calculatedValuesMap.put("CustomFormChargesCustomerCurrency",customFormChargesCustomerCurrency);
            calculatedValuesMap.put("OtherCustomerCurrency",otherCustomerCurrency);
            calculatedValuesMap.put("TotalChargesCustomerCurrency",totalChargesCustomerCurrency);
            calculatedValuesMap.put("CustomDeclarationNumber", customDeclarationNumbers);
            calculatedValuesMap.put("CustomerAccountNumber", customer.getAccountNumber());
            calculatedValuesMap.put("InvoiceNumber", "ECDV-"+invoiceNumber);
            calculatedValuesMap.put("InvoiceType", "Bill-Shipper");
            calculatedValuesMap.put("CustomDeclarationDate",dateSetString.substring(1, dateSetString.length() - 1));
            calculatedValuesMap.put("SMSAFeeCharges", customer.getSmsaServiceFromSAR());
            calculatedValuesMap.put("TotalAmount", calculateTotalAmount(calculatedValuesMap
                    .get("VatAmountCustomDeclarationForm").toString(),
                    calculatedValuesMap.get("CustomFormCharges").toString(),
                    calculatedValuesMap.get("Others").toString(),
                    calculatedValuesMap.get("SMSAFeeCharges").toString(),
                    custom.getSmsaFeeVat()));

            calculatedValuesMap.put("TotalAmountCustomerCurrency", calculateTotalAmountForExcel(calculatedValuesMap
                            .get("VatAmountCustomerCurrency").toString(),
                    calculatedValuesMap.get("CustomFormChargesCustomerCurrency").toString(),
                    calculatedValuesMap.get("OtherCustomerCurrency").toString(),
                    calculatedValuesMap.get("TotalChargesCustomerCurrency").toString())); //for excel


            calculatedValuesMap.put("CustomPort",custom.getCustom());
            calculatedValuesMap.put("VatOnSmsaFees",
                    calculateVatOnSmsaFees(Double.valueOf(calculatedValuesMap.
                            get("SMSAFeeCharges").toString()),custom.getSmsaFeeVat())); //for pdf

            resultList.add(calculatedValuesMap);
        }
        return resultList;
    }

    private Double calculateTotalAmountForExcel(String vatAmountCustomerCurrency, String customFormChargesCustomerCurrency, String otherCustomerCurrency, String totalChargesCustomerCurrency) {
        Double vatCharges = Double.valueOf(vatAmountCustomerCurrency);
        Double customFormChargesValue = Double.valueOf(customFormChargesCustomerCurrency);
        Double otherChargesValue = Double.valueOf(otherCustomerCurrency);
        Double totalCharges = Double.valueOf(totalChargesCustomerCurrency);

        return  vatCharges+customFormChargesValue+otherChargesValue+totalCharges;
    }


    public Map<String, Double> sumNumericColumns(List<Map<String, Object>> resultList) {
        Map<String, Double> sumMap = new HashMap<>();


        Double customerShipmentValueSum = 0.0;
        Double vatAmountCustomDeclarationFormSum = 0.0;
        Double customFormChargesSum = 0.0;
        Double othersSum = 0.0;
        Double totalChargesSum = 0.0;

        Double vatAmountCustomerCurrencySum=0.0;
        Double customFormChargesCustomerCurrencySum=0.0;
        Double otherCustomerCurrencySum=0.0;
        Double totalChargesCustomerCurrencySum=0.0;
        Double totalAmountCustomerCurrency=0.0;

        for (Map<String, Object> calculatedValuesMap : resultList) {
            customerShipmentValueSum += (Double) calculatedValuesMap.get("CustomerShipmentValue");
            vatAmountCustomDeclarationFormSum += (Double) calculatedValuesMap.get("VatAmountCustomDeclarationForm");
            customFormChargesSum += (Double) calculatedValuesMap.get("CustomFormCharges");
            othersSum += (Double) calculatedValuesMap.get("Others");
            totalChargesSum += (Double) calculatedValuesMap.get("TotalCharges");


            vatAmountCustomerCurrencySum+=(Double) calculatedValuesMap.get("VatAmountCustomerCurrency");
             customFormChargesCustomerCurrencySum+=(Double) calculatedValuesMap.get("CustomFormChargesCustomerCurrency");
             otherCustomerCurrencySum+=(Double) calculatedValuesMap.get("OtherCustomerCurrency");
             totalChargesCustomerCurrencySum+=(Double)calculatedValuesMap.get("TotalChargesCustomerCurrency");
            totalAmountCustomerCurrency+=(Double)calculatedValuesMap.get("TotalAmountCustomerCurrency");
        }

        sumMap.put("CustomerShipmentValueSum", customerShipmentValueSum);
        sumMap.put("VatAmountCustomDeclarationFormSum", vatAmountCustomDeclarationFormSum);
        sumMap.put("CustomFormChargesSum", customFormChargesSum);
        sumMap.put("OthersSum", othersSum);
        sumMap.put("TotalChargesSum", totalChargesSum);

        sumMap.put("VatAmountCustomerCurrencySum",vatAmountCustomerCurrencySum);
        sumMap.put("CustomFormChargesCustomerCurrencySum",customFormChargesCustomerCurrencySum);
        sumMap.put("OtherCustomerCurrencySum",otherCustomerCurrencySum);
        sumMap.put("TotalChargesCustomerCurrencySum",totalChargesCustomerCurrencySum);
        sumMap.put("TotalAmountCustomerCurrency",totalAmountCustomerCurrency);
        return sumMap;
    }

    public Double calculateVatOnSmsaFees(Double smsaFeesCharges,Double smsaFeeVat){
        return (smsaFeesCharges * smsaFeeVat) / 100;
    }

    private Double calculateTotalAmount(String vatChargesAsPerCustomDeclarationForm, String customFormCharges, String otherCharges, String smsaFeesCharges, Double vatOnSmsaFees) {
        Double vatCharges = Double.valueOf(vatChargesAsPerCustomDeclarationForm);
        Double customFormChargesValue = Double.valueOf(customFormCharges);
        Double otherChargesValue = Double.valueOf(otherCharges);
        Double smsaFeesChargesValue = Double.valueOf(smsaFeesCharges);

        // Calculate the VAT amount on SMSA fees based on the percentage
        Double vatAmountOnSmsaFees =  calculateVatOnSmsaFees(smsaFeesChargesValue ,vatOnSmsaFees);

        // Calculate the total amount by summing up all the charges
        Double totalAmount = vatCharges + customFormChargesValue + otherChargesValue + smsaFeesChargesValue + vatAmountOnSmsaFees;

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



    public Map<String, List<InvoiceDetails>> filterRowsByMawbNumber(List<InvoiceDetails> invoiceDetailsList) {
        Map<String, List<InvoiceDetails>> filteredRowsMap  = new HashMap<>();

        for (InvoiceDetails invoiceDetails : invoiceDetailsList) {
            String mawbNumber = invoiceDetails.getInvoiceDetailsId().getMawb().toString();

            if (!mawbNumber.isEmpty()) {
                filteredRowsMap.putIfAbsent(mawbNumber, new ArrayList<>());
                filteredRowsMap.get(mawbNumber).add(invoiceDetails);
            }
        }
        return filteredRowsMap;
    }
}
