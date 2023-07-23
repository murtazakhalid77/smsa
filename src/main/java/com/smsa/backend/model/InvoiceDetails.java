package com.smsa.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Builder
@Table(name="invoiceDetails")
    public class InvoiceDetails {
        @EmbeddedId
        private InvoiceDetailsId invoiceDetailsId = new InvoiceDetailsId();;
        private String orderNumber;
        private String origin;
        private String destination;
        private String shippersName;
        private String consigneeName;
        private String weight;
        private Long declaredValue;
        private Long valueCustom;
        private Double vatAmount;
        private Long customFormCharges;
        private Long other;
        private Double totalCharges;
        private String customDeclaration;
        private LocalDate customDeclarationDate;
    private String  sheetUniqueId;
    private LocalDate sheetTimesStamp;
    private String customerUniqueId;
    private LocalDate CustomerTimestamp;
}
