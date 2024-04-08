package com.smsa.backend.model;

import lombok.*;
import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "manifest_data")
public class ManifestData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String companyName;
    private String mode;
    private String shipmentMode;
    private String encodeDesc;
    private String loadingPortCode;
    private String encodeDescSec;
    private String destinationPort;
    private String carrierCode;
    private String flightNumber;
    private String departureDate;
    private String arrivalDate;
    private String actualWeight;
    private String dimWeight;
    private String prefix;
    private String manifestNumber;
    private String blDate;
    private String awb;
    private String orderNumber;
    private String customShipDate;
    private String accountNumber;
    private String weight;
    private String amount;
    private String shipmentCountry;
}
