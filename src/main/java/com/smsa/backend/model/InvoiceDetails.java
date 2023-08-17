//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.smsa.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(
        name = "invoiceDetails"
)
public class InvoiceDetails {
    @EmbeddedId
    private InvoiceDetailsId invoiceDetailsId = new InvoiceDetailsId();
    private String orderNumber;
    private String origin;
    private String destination;
    private String shippersName;
    private String consigneeName;
    private Double weight;
    private Double declaredValue;
    private Double valueCustom;
    private Double vatAmount;
    private Double customFormCharges;
    private Double other;
    private Double totalCharges;
    private String customDeclarationNumber;
    private String customDeclarationDate;
    private String ref;
    private String sheetUniqueId;
    private LocalDate sheetTimesStamp;
    private String customerUniqueId;
    private LocalDate CustomerTimestamp;
    private Boolean isSentInMail;
    @JsonIgnore
    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "accountNumber",
            referencedColumnName = "accountNumber"
    )
    Customer customer;

    public static InvoiceDetailsBuilder builder() {
        return new InvoiceDetailsBuilder();
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof InvoiceDetails)) {
            return false;
        } else {
            InvoiceDetails other = (InvoiceDetails)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$weight = this.getWeight();
                Object other$weight = other.getWeight();
                if (this$weight == null) {
                    if (other$weight != null) {
                        return false;
                    }
                } else if (!this$weight.equals(other$weight)) {
                    return false;
                }

                Object this$declaredValue = this.getDeclaredValue();
                Object other$declaredValue = other.getDeclaredValue();
                if (this$declaredValue == null) {
                    if (other$declaredValue != null) {
                        return false;
                    }
                } else if (!this$declaredValue.equals(other$declaredValue)) {
                    return false;
                }

                Object this$valueCustom = this.getValueCustom();
                Object other$valueCustom = other.getValueCustom();
                if (this$valueCustom == null) {
                    if (other$valueCustom != null) {
                        return false;
                    }
                } else if (!this$valueCustom.equals(other$valueCustom)) {
                    return false;
                }

                label254: {
                    Object this$vatAmount = this.getVatAmount();
                    Object other$vatAmount = other.getVatAmount();
                    if (this$vatAmount == null) {
                        if (other$vatAmount == null) {
                            break label254;
                        }
                    } else if (this$vatAmount.equals(other$vatAmount)) {
                        break label254;
                    }

                    return false;
                }

                label247: {
                    Object this$customFormCharges = this.getCustomFormCharges();
                    Object other$customFormCharges = other.getCustomFormCharges();
                    if (this$customFormCharges == null) {
                        if (other$customFormCharges == null) {
                            break label247;
                        }
                    } else if (this$customFormCharges.equals(other$customFormCharges)) {
                        break label247;
                    }

                    return false;
                }

                Object this$other = this.getOther();
                Object other$other = other.getOther();
                if (this$other == null) {
                    if (other$other != null) {
                        return false;
                    }
                } else if (!this$other.equals(other$other)) {
                    return false;
                }

                label233: {
                    Object this$totalCharges = this.getTotalCharges();
                    Object other$totalCharges = other.getTotalCharges();
                    if (this$totalCharges == null) {
                        if (other$totalCharges == null) {
                            break label233;
                        }
                    } else if (this$totalCharges.equals(other$totalCharges)) {
                        break label233;
                    }

                    return false;
                }

                label226: {
                    Object this$customDeclarationNumber = this.getCustomDeclarationNumber();
                    Object other$customDeclarationNumber = other.getCustomDeclarationNumber();
                    if (this$customDeclarationNumber == null) {
                        if (other$customDeclarationNumber == null) {
                            break label226;
                        }
                    } else if (this$customDeclarationNumber.equals(other$customDeclarationNumber)) {
                        break label226;
                    }

                    return false;
                }

                Object this$isSentInMail = this.getIsSentInMail();
                Object other$isSentInMail = other.getIsSentInMail();
                if (this$isSentInMail == null) {
                    if (other$isSentInMail != null) {
                        return false;
                    }
                } else if (!this$isSentInMail.equals(other$isSentInMail)) {
                    return false;
                }

                Object this$invoiceDetailsId = this.getInvoiceDetailsId();
                Object other$invoiceDetailsId = other.getInvoiceDetailsId();
                if (this$invoiceDetailsId == null) {
                    if (other$invoiceDetailsId != null) {
                        return false;
                    }
                } else if (!this$invoiceDetailsId.equals(other$invoiceDetailsId)) {
                    return false;
                }

                label205: {
                    Object this$orderNumber = this.getOrderNumber();
                    Object other$orderNumber = other.getOrderNumber();
                    if (this$orderNumber == null) {
                        if (other$orderNumber == null) {
                            break label205;
                        }
                    } else if (this$orderNumber.equals(other$orderNumber)) {
                        break label205;
                    }

                    return false;
                }

                label198: {
                    Object this$origin = this.getOrigin();
                    Object other$origin = other.getOrigin();
                    if (this$origin == null) {
                        if (other$origin == null) {
                            break label198;
                        }
                    } else if (this$origin.equals(other$origin)) {
                        break label198;
                    }

                    return false;
                }

                Object this$destination = this.getDestination();
                Object other$destination = other.getDestination();
                if (this$destination == null) {
                    if (other$destination != null) {
                        return false;
                    }
                } else if (!this$destination.equals(other$destination)) {
                    return false;
                }

                label184: {
                    Object this$shippersName = this.getShippersName();
                    Object other$shippersName = other.getShippersName();
                    if (this$shippersName == null) {
                        if (other$shippersName == null) {
                            break label184;
                        }
                    } else if (this$shippersName.equals(other$shippersName)) {
                        break label184;
                    }

                    return false;
                }

                Object this$consigneeName = this.getConsigneeName();
                Object other$consigneeName = other.getConsigneeName();
                if (this$consigneeName == null) {
                    if (other$consigneeName != null) {
                        return false;
                    }
                } else if (!this$consigneeName.equals(other$consigneeName)) {
                    return false;
                }

                label170: {
                    Object this$customDeclarationDate = this.getCustomDeclarationDate();
                    Object other$customDeclarationDate = other.getCustomDeclarationDate();
                    if (this$customDeclarationDate == null) {
                        if (other$customDeclarationDate == null) {
                            break label170;
                        }
                    } else if (this$customDeclarationDate.equals(other$customDeclarationDate)) {
                        break label170;
                    }

                    return false;
                }

                Object this$ref = this.getRef();
                Object other$ref = other.getRef();
                if (this$ref == null) {
                    if (other$ref != null) {
                        return false;
                    }
                } else if (!this$ref.equals(other$ref)) {
                    return false;
                }

                Object this$sheetUniqueId = this.getSheetUniqueId();
                Object other$sheetUniqueId = other.getSheetUniqueId();
                if (this$sheetUniqueId == null) {
                    if (other$sheetUniqueId != null) {
                        return false;
                    }
                } else if (!this$sheetUniqueId.equals(other$sheetUniqueId)) {
                    return false;
                }

                Object this$sheetTimesStamp = this.getSheetTimesStamp();
                Object other$sheetTimesStamp = other.getSheetTimesStamp();
                if (this$sheetTimesStamp == null) {
                    if (other$sheetTimesStamp != null) {
                        return false;
                    }
                } else if (!this$sheetTimesStamp.equals(other$sheetTimesStamp)) {
                    return false;
                }

                label142: {
                    Object this$customerUniqueId = this.getCustomerUniqueId();
                    Object other$customerUniqueId = other.getCustomerUniqueId();
                    if (this$customerUniqueId == null) {
                        if (other$customerUniqueId == null) {
                            break label142;
                        }
                    } else if (this$customerUniqueId.equals(other$customerUniqueId)) {
                        break label142;
                    }

                    return false;
                }

                label135: {
                    Object this$CustomerTimestamp = this.getCustomerTimestamp();
                    Object other$CustomerTimestamp = other.getCustomerTimestamp();
                    if (this$CustomerTimestamp == null) {
                        if (other$CustomerTimestamp == null) {
                            break label135;
                        }
                    } else if (this$CustomerTimestamp.equals(other$CustomerTimestamp)) {
                        break label135;
                    }

                    return false;
                }

                Object this$customer = this.getCustomer();
                Object other$customer = other.getCustomer();
                if (this$customer == null) {
                    if (other$customer != null) {
                        return false;
                    }
                } else if (!this$customer.equals(other$customer)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof InvoiceDetails;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $weight = this.getWeight();
        result = result * 59 + ($weight == null ? 43 : $weight.hashCode());
        Object $declaredValue = this.getDeclaredValue();
        result = result * 59 + ($declaredValue == null ? 43 : $declaredValue.hashCode());
        Object $valueCustom = this.getValueCustom();
        result = result * 59 + ($valueCustom == null ? 43 : $valueCustom.hashCode());
        Object $vatAmount = this.getVatAmount();
        result = result * 59 + ($vatAmount == null ? 43 : $vatAmount.hashCode());
        Object $customFormCharges = this.getCustomFormCharges();
        result = result * 59 + ($customFormCharges == null ? 43 : $customFormCharges.hashCode());
        Object $other = this.getOther();
        result = result * 59 + ($other == null ? 43 : $other.hashCode());
        Object $totalCharges = this.getTotalCharges();
        result = result * 59 + ($totalCharges == null ? 43 : $totalCharges.hashCode());
        Object $customDeclarationNumber = this.getCustomDeclarationNumber();
        result = result * 59 + ($customDeclarationNumber == null ? 43 : $customDeclarationNumber.hashCode());
        Object $isSentInMail = this.getIsSentInMail();
        result = result * 59 + ($isSentInMail == null ? 43 : $isSentInMail.hashCode());
        Object $invoiceDetailsId = this.getInvoiceDetailsId();
        result = result * 59 + ($invoiceDetailsId == null ? 43 : $invoiceDetailsId.hashCode());
        Object $orderNumber = this.getOrderNumber();
        result = result * 59 + ($orderNumber == null ? 43 : $orderNumber.hashCode());
        Object $origin = this.getOrigin();
        result = result * 59 + ($origin == null ? 43 : $origin.hashCode());
        Object $destination = this.getDestination();
        result = result * 59 + ($destination == null ? 43 : $destination.hashCode());
        Object $shippersName = this.getShippersName();
        result = result * 59 + ($shippersName == null ? 43 : $shippersName.hashCode());
        Object $consigneeName = this.getConsigneeName();
        result = result * 59 + ($consigneeName == null ? 43 : $consigneeName.hashCode());
        Object $customDeclarationDate = this.getCustomDeclarationDate();
        result = result * 59 + ($customDeclarationDate == null ? 43 : $customDeclarationDate.hashCode());
        Object $ref = this.getRef();
        result = result * 59 + ($ref == null ? 43 : $ref.hashCode());
        Object $sheetUniqueId = this.getSheetUniqueId();
        result = result * 59 + ($sheetUniqueId == null ? 43 : $sheetUniqueId.hashCode());
        Object $sheetTimesStamp = this.getSheetTimesStamp();
        result = result * 59 + ($sheetTimesStamp == null ? 43 : $sheetTimesStamp.hashCode());
        Object $customerUniqueId = this.getCustomerUniqueId();
        result = result * 59 + ($customerUniqueId == null ? 43 : $customerUniqueId.hashCode());
        Object $CustomerTimestamp = this.getCustomerTimestamp();
        result = result * 59 + ($CustomerTimestamp == null ? 43 : $CustomerTimestamp.hashCode());
        Object $customer = this.getCustomer();
        result = result * 59 + ($customer == null ? 43 : $customer.hashCode());
        return result;
    }

    public InvoiceDetails(final InvoiceDetailsId invoiceDetailsId, final String orderNumber, final String origin, final String destination, final String shippersName, final String consigneeName, final Double weight, final Double declaredValue, final Double valueCustom, final Double vatAmount, final Double customFormCharges, final Double other, final Double totalCharges, final String customDeclarationNumber, final String customDeclarationDate, final String ref, final String sheetUniqueId, final LocalDate sheetTimesStamp, final String customerUniqueId, final LocalDate CustomerTimestamp, final Boolean isSentInMail, final Customer customer) {
        this.invoiceDetailsId = invoiceDetailsId;
        this.orderNumber = orderNumber;
        this.origin = origin;
        this.destination = destination;
        this.shippersName = shippersName;
        this.consigneeName = consigneeName;
        this.weight = weight;
        this.declaredValue = declaredValue;
        this.valueCustom = valueCustom;
        this.vatAmount = vatAmount;
        this.customFormCharges = customFormCharges;
        this.other = other;
        this.totalCharges = totalCharges;
        this.customDeclarationNumber = customDeclarationNumber;
        this.customDeclarationDate = customDeclarationDate;
        this.ref = ref;
        this.sheetUniqueId = sheetUniqueId;
        this.sheetTimesStamp = sheetTimesStamp;
        this.customerUniqueId = customerUniqueId;
        this.CustomerTimestamp = CustomerTimestamp;
        this.isSentInMail = isSentInMail;
        this.customer = customer;
    }

    public InvoiceDetails() {
    }

    public InvoiceDetailsId getInvoiceDetailsId() {
        return this.invoiceDetailsId;
    }

    public String getOrderNumber() {
        return this.orderNumber;
    }

    public String getOrigin() {
        return this.origin;
    }

    public String getDestination() {
        return this.destination;
    }

    public String getShippersName() {
        return this.shippersName;
    }

    public String getConsigneeName() {
        return this.consigneeName;
    }

    public Double getWeight() {
        return this.weight;
    }

    public Double getDeclaredValue() {
        return this.declaredValue;
    }

    public Double getValueCustom() {
        return this.valueCustom;
    }

    public Double getVatAmount() {
        return this.vatAmount;
    }

    public Double getCustomFormCharges() {
        return this.customFormCharges;
    }

    public Double getOther() {
        return this.other;
    }

    public Double getTotalCharges() {
        return this.totalCharges;
    }

    public String getCustomDeclarationNumber() {
        return this.customDeclarationNumber;
    }

    public String getCustomDeclarationDate() {
        return this.customDeclarationDate;
    }

    public String getRef() {
        return this.ref;
    }

    public String getSheetUniqueId() {
        return this.sheetUniqueId;
    }

    public LocalDate getSheetTimesStamp() {
        return this.sheetTimesStamp;
    }

    public String getCustomerUniqueId() {
        return this.customerUniqueId;
    }

    public LocalDate getCustomerTimestamp() {
        return this.CustomerTimestamp;
    }

    public Boolean getIsSentInMail() {
        return this.isSentInMail;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setInvoiceDetailsId(final InvoiceDetailsId invoiceDetailsId) {
        this.invoiceDetailsId = invoiceDetailsId;
    }

    public void setOrderNumber(final String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setOrigin(final String origin) {
        this.origin = origin;
    }

    public void setDestination(final String destination) {
        this.destination = destination;
    }

    public void setShippersName(final String shippersName) {
        this.shippersName = shippersName;
    }

    public void setConsigneeName(final String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public void setWeight(final Double weight) {
        this.weight = weight;
    }

    public void setDeclaredValue(final Double declaredValue) {
        this.declaredValue = declaredValue;
    }

    public void setValueCustom(final Double valueCustom) {
        this.valueCustom = valueCustom;
    }

    public void setVatAmount(final Double vatAmount) {
        this.vatAmount = vatAmount;
    }

    public void setCustomFormCharges(final Double customFormCharges) {
        this.customFormCharges = customFormCharges;
    }

    public void setOther(final Double other) {
        this.other = other;
    }

    public void setTotalCharges(final Double totalCharges) {
        this.totalCharges = totalCharges;
    }

    public void setCustomDeclarationNumber(final String customDeclarationNumber) {
        this.customDeclarationNumber = customDeclarationNumber;
    }

    public void setCustomDeclarationDate(final String customDeclarationDate) {
        this.customDeclarationDate = customDeclarationDate;
    }

    public void setRef(final String ref) {
        this.ref = ref;
    }

    public void setSheetUniqueId(final String sheetUniqueId) {
        this.sheetUniqueId = sheetUniqueId;
    }

    public void setSheetTimesStamp(final LocalDate sheetTimesStamp) {
        this.sheetTimesStamp = sheetTimesStamp;
    }

    public void setCustomerUniqueId(final String customerUniqueId) {
        this.customerUniqueId = customerUniqueId;
    }

    public void setCustomerTimestamp(final LocalDate CustomerTimestamp) {
        this.CustomerTimestamp = CustomerTimestamp;
    }

    public void setIsSentInMail(final Boolean isSentInMail) {
        this.isSentInMail = isSentInMail;
    }

    @JsonIgnore
    public void setCustomer(final Customer customer) {
        this.customer = customer;
    }

    public static class InvoiceDetailsBuilder {
        private InvoiceDetailsId invoiceDetailsId;
        private String orderNumber;
        private String origin;
        private String destination;
        private String shippersName;
        private String consigneeName;
        private Double weight;
        private Double declaredValue;
        private Double valueCustom;
        private Double vatAmount;
        private Double customFormCharges;
        private Double other;
        private Double totalCharges;
        private String customDeclarationNumber;
        private String customDeclarationDate;
        private String ref;
        private String sheetUniqueId;
        private LocalDate sheetTimesStamp;
        private String customerUniqueId;
        private LocalDate CustomerTimestamp;
        private Boolean isSentInMail;
        private Customer customer;

        InvoiceDetailsBuilder() {
        }

        public InvoiceDetailsBuilder invoiceDetailsId(final InvoiceDetailsId invoiceDetailsId) {
            this.invoiceDetailsId = invoiceDetailsId;
            return this;
        }

        public InvoiceDetailsBuilder orderNumber(final String orderNumber) {
            this.orderNumber = orderNumber;
            return this;
        }

        public InvoiceDetailsBuilder origin(final String origin) {
            this.origin = origin;
            return this;
        }

        public InvoiceDetailsBuilder destination(final String destination) {
            this.destination = destination;
            return this;
        }

        public InvoiceDetailsBuilder shippersName(final String shippersName) {
            this.shippersName = shippersName;
            return this;
        }

        public InvoiceDetailsBuilder consigneeName(final String consigneeName) {
            this.consigneeName = consigneeName;
            return this;
        }

        public InvoiceDetailsBuilder weight(final Double weight) {
            this.weight = weight;
            return this;
        }

        public InvoiceDetailsBuilder declaredValue(final Double declaredValue) {
            this.declaredValue = declaredValue;
            return this;
        }

        public InvoiceDetailsBuilder valueCustom(final Double valueCustom) {
            this.valueCustom = valueCustom;
            return this;
        }

        public InvoiceDetailsBuilder vatAmount(final Double vatAmount) {
            this.vatAmount = vatAmount;
            return this;
        }

        public InvoiceDetailsBuilder customFormCharges(final Double customFormCharges) {
            this.customFormCharges = customFormCharges;
            return this;
        }

        public InvoiceDetailsBuilder other(final Double other) {
            this.other = other;
            return this;
        }

        public InvoiceDetailsBuilder totalCharges(final Double totalCharges) {
            this.totalCharges = totalCharges;
            return this;
        }

        public InvoiceDetailsBuilder customDeclarationNumber(final String customDeclarationNumber) {
            this.customDeclarationNumber = customDeclarationNumber;
            return this;
        }

        public InvoiceDetailsBuilder customDeclarationDate(final String customDeclarationDate) {
            this.customDeclarationDate = customDeclarationDate;
            return this;
        }

        public InvoiceDetailsBuilder ref(final String ref) {
            this.ref = ref;
            return this;
        }

        public InvoiceDetailsBuilder sheetUniqueId(final String sheetUniqueId) {
            this.sheetUniqueId = sheetUniqueId;
            return this;
        }

        public InvoiceDetailsBuilder sheetTimesStamp(final LocalDate sheetTimesStamp) {
            this.sheetTimesStamp = sheetTimesStamp;
            return this;
        }

        public InvoiceDetailsBuilder customerUniqueId(final String customerUniqueId) {
            this.customerUniqueId = customerUniqueId;
            return this;
        }

        public InvoiceDetailsBuilder CustomerTimestamp(final LocalDate CustomerTimestamp) {
            this.CustomerTimestamp = CustomerTimestamp;
            return this;
        }

        public InvoiceDetailsBuilder isSentInMail(final Boolean isSentInMail) {
            this.isSentInMail = isSentInMail;
            return this;
        }

        @JsonIgnore
        public InvoiceDetailsBuilder customer(final Customer customer) {
            this.customer = customer;
            return this;
        }

        public InvoiceDetails build() {
            return new InvoiceDetails(this.invoiceDetailsId, this.orderNumber, this.origin, this.destination, this.shippersName, this.consigneeName, this.weight, this.declaredValue, this.valueCustom, this.vatAmount, this.customFormCharges, this.other, this.totalCharges, this.customDeclarationNumber, this.customDeclarationDate, this.ref, this.sheetUniqueId, this.sheetTimesStamp, this.customerUniqueId, this.CustomerTimestamp, this.isSentInMail, this.customer);
        }

    }
}
