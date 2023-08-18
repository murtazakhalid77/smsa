package com.smsa.backend.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(
        name = "sales_report"
)
public class SalesReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
