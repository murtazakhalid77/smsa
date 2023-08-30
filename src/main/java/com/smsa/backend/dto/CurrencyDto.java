package com.smsa.backend.dto;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

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
