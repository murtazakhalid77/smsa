package com.smsa.backend.model;

import lombok.*;

import javax.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
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
    private boolean isPresent;
}
