package com.smsa.backend.dto;

import lombok.*;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class InvoiceDetailsDto {
    private InvoiceDetailsIdDto invoiceDetailsIdDTO;
    private String orderNumber;
    private String origin;
    private String destination;
    private String shippersName;
    private String consigneeName;
    private String weight;
    private Long declaredValue;
    private Long valueCustom;
    private Long vatAmount;
    private Long customFormCharges;
    private Long other;
    private String customDeclaration;
    private LocalDate customDeclarationDate;
}
