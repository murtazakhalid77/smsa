package com.smsa.backend.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class InvoiceDetailsId implements Serializable {
    private Long mawb;
    private String manifestDate;
    private String accountNumber;
    private Long awb;
}
