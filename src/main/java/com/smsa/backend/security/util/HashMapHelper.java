package com.smsa.backend.security.util;

import com.smsa.backend.model.Custom;
import com.smsa.backend.model.Customer;
import com.smsa.backend.model.InvoiceDetails;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class HashMapHelper {
    Long totalAwbCount = 0L;
    Double customerShipmentValue = 0.0;
    Double vatAmountCustomDeclartionForm = 0.0;
    Double customFormChares = 0.0;
    Double others = 0.0;
    Double totalValue=0.0;
    Set<Long> customDecarationNumberSet = new HashSet<>();
    Map<String, List<InvoiceDetails>> filteredRowsMap  = new HashMap<>();

    public List<Map<String, Object>> calculateValues(Map<String, List<InvoiceDetails>> filteredRowsMap, Customer customer, Custom custom) {
        List<Map<String, Object>> resultList = new ArrayList<>();

        for (Map.Entry<String, List<InvoiceDetails>> entry : filteredRowsMap.entrySet()) {
            String mawbNumber = entry.getKey();
            List<InvoiceDetails> filterInvoiceDetails = entry.getValue();

            Map<String, Object> calculatedValuesMap = new HashMap<>(); // Create a new Map for each iteration

            customDecarationNumberSet.clear();

            for (InvoiceDetails invoiceDetails : filterInvoiceDetails) {
                totalAwbCount += 1;
                customerShipmentValue = customerShipmentValue + invoiceDetails.getValueCustom();
                vatAmountCustomDeclartionForm += invoiceDetails.getVatAmount();
                customFormChares += invoiceDetails.getCustomFormCharges();
                others += invoiceDetails.getOther();
                totalValue += invoiceDetails.getDeclaredValue() + invoiceDetails.getValueCustom() + invoiceDetails.getVatAmount() + invoiceDetails.getCustomFormCharges() + invoiceDetails.getOther();
                customDecarationNumberSet.add(invoiceDetails.getCustomDeclarationNumber());
            }

            // Put the MAWB number into the calculatedValuesMap for each iteration
            calculatedValuesMap.put("MawbNumber", mawbNumber);
            Double totalCharges = customFormChares + others;

            // Put the calculated values into the result map
            calculatedValuesMap.put("TotalAwbCount", totalAwbCount);
            calculatedValuesMap.put("CustomerShipmentValue", customerShipmentValue);
            calculatedValuesMap.put("VatAmountCustomDeclarationForm", vatAmountCustomDeclartionForm);
            calculatedValuesMap.put("CustomFormCharges", customFormChares);
            calculatedValuesMap.put("Others", others);
            calculatedValuesMap.put("TotalCharges", totalCharges);
            calculatedValuesMap.put("TotalValue", totalValue);
            calculatedValuesMap.put("CustomDeclarationNumber", customDecarationNumberSet);
            calculatedValuesMap.put("CustomerAccountNumber", customer.getAccountNumber());
            calculatedValuesMap.put("InvoiceNumber", "dummy");
            calculatedValuesMap.put("InvoiceType", "dummy");
            calculatedValuesMap.put("SMSAFeeCharges", customer.getSmsaServiceFromSAR());
            calculatedValuesMap.put("TotalAmount", calculateTotalAmount(calculatedValuesMap.get("VatAmountCustomDeclarationForm").toString(), calculatedValuesMap.get("CustomFormCharges").toString(), calculatedValuesMap.get("Others").toString(), calculatedValuesMap.get("SMSAFeeCharges").toString(), custom.getSmsaFeeVat()));
            calculatedValuesMap.put("CustomPort",custom.getCustomPort());
            resultList.add(calculatedValuesMap);
        }

        return resultList;
    }


    private Double calculateTotalAmount(String vatChargesAsPerCustomDeclarationForm, String customFormCharges, String otherCharges, String smsaFeesCharges, Double vatOnSmsaFees) {
        Double vatCharges = Double.valueOf(vatChargesAsPerCustomDeclarationForm);
        Double customFormChargesValue = Double.valueOf(customFormCharges);
        Double otherChargesValue = Double.valueOf(otherCharges);
        Double smsaFeesChargesValue = Double.valueOf(smsaFeesCharges);

        // Calculate the VAT amount on SMSA fees based on the percentage
        Double vatAmountOnSmsaFees = (smsaFeesChargesValue * vatOnSmsaFees) / 100;

        // Calculate the total amount by summing up all the charges
        Double totalAmount = vatCharges + customFormChargesValue + otherChargesValue + smsaFeesChargesValue + vatAmountOnSmsaFees;

        return totalAmount;
    }


    public Map<String, List<InvoiceDetails>> filterRowsByMawbNumber(List<InvoiceDetails> invoiceDetailsList) {

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
