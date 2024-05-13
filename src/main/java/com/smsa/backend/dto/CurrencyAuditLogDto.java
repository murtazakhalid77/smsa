package com.smsa.backend.dto;

import com.smsa.backend.model.Currency;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CurrencyAuditLogDto {
    private Long id;
    private String currencyFrom;
    private String currencyTo;
    private Double conversionRate;
    private String createdAt;
    private String createdBy;
    private String updatedAt;
    private String updatedBy;
    private Boolean isPresent;
    private Currency currency;
}
