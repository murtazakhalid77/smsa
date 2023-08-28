//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.smsa.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDate;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
public class InvoiceDetails {
    @EmbeddedId
    private InvoiceDetailsId invoiceDetailsId = new InvoiceDetailsId();
    private String orderNumber;
    private String origin;
    private String destination;
    private String shippersName;
    private String consigneeName;
    private Double weight;
    private Double declaredValue;
    private Double valueCustom;
    private Double vatAmount;
    private Double customFormCharges;
    private Double other;
    private Double totalCharges;
    private String customDeclarationNumber;
    private String customDeclarationDate;
    private String ref;
    private String sheetUniqueId;
    private LocalDate sheetTimesStamp;
    private String customerUniqueId;
    private LocalDate CustomerTimestamp;
    private Boolean isSentInMail;
}
