package com.smsa.backend.model;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  String accountNumber;

    private String sheetId;

    private String excelDownload;

    private String pdfDownload;

    private String invoiceNumber;

    private Boolean MailSent;

    private String currentStatus;
}
