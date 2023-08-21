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

    Long saledIdStart;
    Long saleIdEnd;
    String startDate;
    String endDate;

}
