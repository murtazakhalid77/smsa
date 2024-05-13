package com.smsa.backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ExcelImportDto {
    private Long id;
    private String customPort;
    private String custom;
    private Double smsaFeeVat;
    private boolean isPresent;
    private String date1;
    private String date2;
    private String date3;
    private String formattedStartDate;
    private String formattedEndDate;
    private String invoiceDate;
    private byte[] excel;


    private boolean customPlusExcel;
    private String   vatAmountPercentage;
    private String customFormValue;
}
