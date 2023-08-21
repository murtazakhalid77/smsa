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

    Long invoiceTo;
    Long invoiceFrom;
    String startDate;
    String endDate;

}
