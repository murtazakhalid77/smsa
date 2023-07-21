package com.smsa.backend.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class InvoiceDetailsIdDto implements Serializable {
    private Long mawb;
    private LocalDate manifestDate;
    private String accountNumber;
    private Long awb;
}
