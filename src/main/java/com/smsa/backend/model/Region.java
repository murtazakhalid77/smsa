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
@Table(name = "region")
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerRegion;
    private Double vat;
    private String headerName;
    private String vatNumber;
    private String description;
    private boolean status;
}
