package com.smsa.backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SalesReportHelperDto {
    byte[] excelFile;
    Double totalChargesAsPerCustomDeclarationForm;
    Double TotalAmount;
    Double vatOnSmsaFees;
    Double smsaFeesCharges;
}
