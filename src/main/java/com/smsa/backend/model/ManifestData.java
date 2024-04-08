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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getShipmentMode() {
        return shipmentMode;
    }

    public void setShipmentMode(String shipmentMode) {
        this.shipmentMode = shipmentMode;
    }

    public String getEncodeDesc() {
        return encodeDesc;
    }

    public void setEncodeDesc(String encodeDesc) {
        this.encodeDesc = encodeDesc;
    }

    public String getLoadingPortCode() {
        return loadingPortCode;
    }

    public void setLoadingPortCode(String loadingPortCode) {
        this.loadingPortCode = loadingPortCode;
    }

    public String getEncodeDescSec() {
        return encodeDescSec;
    }

    public void setEncodeDescSec(String encodeDescSec) {
        this.encodeDescSec = encodeDescSec;
    }

    public String getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(String destinationPort) {
        this.destinationPort = destinationPort;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(String actualWeight) {
        this.actualWeight = actualWeight;
    }

    public String getDimWeight() {
        return dimWeight;
    }

    public void setDimWeight(String dimWeight) {
        this.dimWeight = dimWeight;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getManifestNumber() {
        return manifestNumber;
    }

    public void setManifestNumber(String manifestNumber) {
        this.manifestNumber = manifestNumber;
    }

    public String getBlDate() {
        return blDate;
    }

    public void setBlDate(String blDate) {
        this.blDate = blDate;
    }

    public String getAwb() {
        return awb;
    }

    public void setAwb(String awb) {
        this.awb = awb;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomShipDate() {
        return customShipDate;
    }

    public void setCustomShipDate(String customShipDate) {
        this.customShipDate = customShipDate;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getShipmentCountry() {
        return shipmentCountry;
    }

    public void setShipmentCountry(String shipmentCountry) {
        this.shipmentCountry = shipmentCountry;
    }
}
