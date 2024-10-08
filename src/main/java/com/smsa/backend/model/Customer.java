//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.smsa.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(
        name = "customer"
)
@Builder
@Getter
@Setter
@Data
@ToString
public class Customer {
    @Id
    private String accountNumber;
    private String invoiceCurrency;
    @OneToOne
    @JoinColumn(
            name = "id"
    )
    private Region region;
    private Double smsaServiceFromSAR;
    private String email;
    private String ccMail;
    private String nameArabic;
    private String nameEnglish;
    private String VatNumber;
    private String address;
    private String poBox;
    private String country;
    private Boolean status;
    private boolean isPresent;
    private Double smsaAdminChargesFromSAR;
}
