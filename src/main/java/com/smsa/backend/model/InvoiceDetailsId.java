package com.smsa.backend.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class InvoiceDetailsId implements Serializable {
    private Long mawb;
    private LocalDate manifestDate;
    @Column(name = "account_number_id")
    private String accountNumber;
    private Long awb;
}
