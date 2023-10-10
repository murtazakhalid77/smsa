package com.smsa.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
    String excelDownload;
    String pdfDownload;
    LocalDate createdAt;
    @OneToMany(mappedBy = "salesReport")
    @JsonIgnore
    private List<SalesReportAwb> salesReportAwbs = new ArrayList<>();

    @Override
    public String toString() {
        return "SalesReport{" +
                "id=" + id +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", customerAccountNumber='" + customerAccountNumber + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerRegion='" + customerRegion + '\'' +
                ", period='" + period + '\'' +
                ", totalChargesAsPerCustomerDeclarationForm=" + totalChargesAsPerCustomerDeclarationForm +
                ", smsaFeeCharges=" + smsaFeeCharges +
                ", vatOnSmsaFees=" + vatOnSmsaFees +
                ", totalAmount=" + totalAmount +
                ", invoiceCurrency='" + invoiceCurrency + '\'' +
                ", excelDownload='" + excelDownload + '\'' +
                ", pdfDownload='" + pdfDownload + '\'' +
                ", createdAt=" + createdAt +
                ", salesReportAwbs=" + salesReportAwbs +
                '}';
    }

}
