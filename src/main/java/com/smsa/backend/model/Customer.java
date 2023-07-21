package com.smsa.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Builder
@Table(name="customer")
public class Customer {

    @Id
    private String accountNumber;
    private String invoiceCurrency;
    private Long currencyRateFromSAR;
    private Long smsaServiceFromSAR;
    private String email;
    private String customerNameArabic;
    private String NameEnglish;
    private String VatNumber;
    private String address;
    private String poBox;
    private String country;
    private Boolean isEmailSent;
    private Boolean status;
    private boolean isPresent;

}

