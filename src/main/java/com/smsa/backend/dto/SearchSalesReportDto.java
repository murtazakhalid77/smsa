package com.smsa.backend.dto;

import lombok.*;

import java.util.List;

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
    Integer page;
    Integer size;
    String awbs;
    String search;
    String mapper;
}
