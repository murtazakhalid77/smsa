package com.smsa.backend.dto;

import lombok.*;

import java.time.LocalDate;

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
    private LocalDate startDate;
    private LocalDate endDate;
}
