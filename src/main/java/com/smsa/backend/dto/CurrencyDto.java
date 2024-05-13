package com.smsa.backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CurrencyDto {

    Long id;
    String currencyFrom;
    String currencyTo;
    Double conversionRate;
    Boolean isPresent;
    String createdBy;
    String createdAt;
    String updatedBy;
    String updatedAt;
}
