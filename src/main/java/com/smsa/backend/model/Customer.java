package com.smsa.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private String nameArabic;
    private String nameEnglish;
    private String VatNumber;
    private String address;
    private String poBox;
    private String country;
    private Boolean status;
    private boolean isPresent;

    @OneToMany(mappedBy = "customer")
    private Set<InvoiceDetails> invoiceDetailsSet = new HashSet<>();
}

