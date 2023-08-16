package com.smsa.backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class RegionDto {
    private Long id;
    private String customerRegion;
    private Double vat;
    private String headerName;
    private String vatNumber;
    private String description;
    private boolean status;
}
