package com.smsa.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@JsonFormat
public class SalesReportDto {
    Long id;
    String invoiceNumber;
    String customerAccountNumber;
    String customerName;
    String customerRegion;
    String period;
    Double totalChargesAsPerCustomerDeclarationForm;
    Double smsaFeeCharges;
    Double vatOnSmsaFees;
    Double totalAmount;
    String invoiceCurrency;
    LocalDate createdAt;

}
