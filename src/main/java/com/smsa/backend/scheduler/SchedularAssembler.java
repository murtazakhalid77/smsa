package com.smsa.backend.scheduler;

import com.smsa.backend.dto.SalesReportHelperDto;
import com.smsa.backend.model.Customer;
import com.smsa.backend.model.SalesReport;
import com.smsa.backend.model.Transaction;
import com.smsa.backend.service.HelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SchedularAssembler {

    @Autowired
    HelperService helperService;

    public SalesReport createSalesReport(Long invoiceNumber, Customer customer, String sheetUniqueId, SalesReportHelperDto salesReportHelperDto) {
        return SalesReport.builder()
                .invoiceNumber(String.valueOf(invoiceNumber))
                .customerAccountNumber(customer.getAccountNumber())
                .customerName(customer.getNameEnglish())
                .customerRegion(customer.getRegion().getCustomerRegion())
                .period(helperService.generateInvoiceDatePeriod(sheetUniqueId))
                .totalChargesAsPerCustomerDeclarationForm(salesReportHelperDto.getTotalChargesAsPerCustomDeclarationForm())
                .vatOnSmsaFees(salesReportHelperDto.getVatOnSmsaFees())
                .totalAmount(salesReportHelperDto.getTotalAmount())
                .smsaFeeCharges(salesReportHelperDto.getSmsaFeesCharges())
                .invoiceCurrency(customer.getInvoiceCurrency())
                .createdAt(LocalDate.now())
                .build();
    }

    public Transaction transaction(String accountNumber,String sheetUniqueId,String message,Boolean mailSent){
        return  Transaction.builder()
                .accountNumber(accountNumber)
                .sheetId(sheetUniqueId)
                .downloadUrl("https/:s3buket")
                .currentStatus(message)
                .MailSent(mailSent)
                .build();
    }

}
