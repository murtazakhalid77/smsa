package com.smsa.backend.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "currency")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String currencyFrom;
    String currencyTo;
    Double conversionRate;
    Boolean isPresent;
}
