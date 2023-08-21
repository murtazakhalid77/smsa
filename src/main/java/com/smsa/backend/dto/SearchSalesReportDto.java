package com.smsa.backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SearchSalesReportDto {

    String invoiceTo;
    String invoiceFrom;
    String startDate;
    String endDate;

}
