package com.smsa.backend.dto;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
}
