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
@Table(
        name = "currency_audit_log"
)
public class CurrencyAuditLog {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    private String currencyFrom;
    private String currencyTo;
    private Double conversionRate;
    private String createdAt;
    private String createdBy;
    private String updatedAt;
    private String updatedBy;
    private Boolean isPresent;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_id")
    private Currency currency;
}
