//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.smsa.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(
        name = "customer"
)
public class Customer {
    @Id
    private String accountNumber;
    private String invoiceCurrency;
    private Double currencyRateFromSAR;
    private Double smsaServiceFromSAR;
    private String email;
    private String nameArabic;
    private String nameEnglish;
    private String VatNumber;
    private String address;
    private String poBox;
    private String country;
    private Boolean status;
    private boolean isPresent;
    @OneToMany(
            mappedBy = "customer"
    )
    @JsonIgnore
    private Set<InvoiceDetails> invoiceDetailsSet = new HashSet();

    public static CustomerBuilder builder() {
        return new CustomerBuilder();
    }

    public Customer(final String accountNumber, final String invoiceCurrency, final Double currencyRateFromSAR, final Double smsaServiceFromSAR, final String email, final String nameArabic, final String nameEnglish, final String VatNumber, final String address, final String poBox, final String country, final Boolean status, final boolean isPresent, final Set<InvoiceDetails> invoiceDetailsSet) {
        this.accountNumber = accountNumber;
        this.invoiceCurrency = invoiceCurrency;
        this.currencyRateFromSAR = currencyRateFromSAR;
        this.smsaServiceFromSAR = smsaServiceFromSAR;
        this.email = email;
        this.nameArabic = nameArabic;
        this.nameEnglish = nameEnglish;
        this.VatNumber = VatNumber;
        this.address = address;
        this.poBox = poBox;
        this.country = country;
        this.status = status;
        this.isPresent = isPresent;
        this.invoiceDetailsSet = invoiceDetailsSet;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public String getInvoiceCurrency() {
        return this.invoiceCurrency;
    }

    public Double getCurrencyRateFromSAR() {
        return this.currencyRateFromSAR;
    }

    public Double getSmsaServiceFromSAR() {
        return this.smsaServiceFromSAR;
    }

    public String getEmail() {
        return this.email;
    }

    public String getNameArabic() {
        return this.nameArabic;
    }

    public String getNameEnglish() {
        return this.nameEnglish;
    }

    public String getVatNumber() {
        return this.VatNumber;
    }

    public String getAddress() {
        return this.address;
    }

    public String getPoBox() {
        return this.poBox;
    }

    public String getCountry() {
        return this.country;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public boolean isPresent() {
        return this.isPresent;
    }

    public Set<InvoiceDetails> getInvoiceDetailsSet() {
        return this.invoiceDetailsSet;
    }

    public void setAccountNumber(final String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setInvoiceCurrency(final String invoiceCurrency) {
        this.invoiceCurrency = invoiceCurrency;
    }

    public void setCurrencyRateFromSAR(final Double currencyRateFromSAR) {
        this.currencyRateFromSAR = currencyRateFromSAR;
    }

    public void setSmsaServiceFromSAR(final Double smsaServiceFromSAR) {
        this.smsaServiceFromSAR = smsaServiceFromSAR;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setNameArabic(final String nameArabic) {
        this.nameArabic = nameArabic;
    }

    public void setNameEnglish(final String nameEnglish) {
        this.nameEnglish = nameEnglish;
    }

    public void setVatNumber(final String VatNumber) {
        this.VatNumber = VatNumber;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public void setPoBox(final String poBox) {
        this.poBox = poBox;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public void setStatus(final Boolean status) {
        this.status = status;
    }

    public void setPresent(final boolean isPresent) {
        this.isPresent = isPresent;
    }

    @JsonIgnore
    public void setInvoiceDetailsSet(final Set<InvoiceDetails> invoiceDetailsSet) {
        this.invoiceDetailsSet = invoiceDetailsSet;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Customer)) {
            return false;
        } else {
            Customer other = (Customer)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.isPresent() != other.isPresent()) {
                return false;
            } else {
                label169: {
                    Object this$currencyRateFromSAR = this.getCurrencyRateFromSAR();
                    Object other$currencyRateFromSAR = other.getCurrencyRateFromSAR();
                    if (this$currencyRateFromSAR == null) {
                        if (other$currencyRateFromSAR == null) {
                            break label169;
                        }
                    } else if (this$currencyRateFromSAR.equals(other$currencyRateFromSAR)) {
                        break label169;
                    }

                    return false;
                }

                Object this$smsaServiceFromSAR = this.getSmsaServiceFromSAR();
                Object other$smsaServiceFromSAR = other.getSmsaServiceFromSAR();
                if (this$smsaServiceFromSAR == null) {
                    if (other$smsaServiceFromSAR != null) {
                        return false;
                    }
                } else if (!this$smsaServiceFromSAR.equals(other$smsaServiceFromSAR)) {
                    return false;
                }

                label155: {
                    Object this$status = this.getStatus();
                    Object other$status = other.getStatus();
                    if (this$status == null) {
                        if (other$status == null) {
                            break label155;
                        }
                    } else if (this$status.equals(other$status)) {
                        break label155;
                    }

                    return false;
                }

                Object this$accountNumber = this.getAccountNumber();
                Object other$accountNumber = other.getAccountNumber();
                if (this$accountNumber == null) {
                    if (other$accountNumber != null) {
                        return false;
                    }
                } else if (!this$accountNumber.equals(other$accountNumber)) {
                    return false;
                }

                Object this$invoiceCurrency = this.getInvoiceCurrency();
                Object other$invoiceCurrency = other.getInvoiceCurrency();
                if (this$invoiceCurrency == null) {
                    if (other$invoiceCurrency != null) {
                        return false;
                    }
                } else if (!this$invoiceCurrency.equals(other$invoiceCurrency)) {
                    return false;
                }

                label134: {
                    Object this$email = this.getEmail();
                    Object other$email = other.getEmail();
                    if (this$email == null) {
                        if (other$email == null) {
                            break label134;
                        }
                    } else if (this$email.equals(other$email)) {
                        break label134;
                    }

                    return false;
                }

                label127: {
                    Object this$nameArabic = this.getNameArabic();
                    Object other$nameArabic = other.getNameArabic();
                    if (this$nameArabic == null) {
                        if (other$nameArabic == null) {
                            break label127;
                        }
                    } else if (this$nameArabic.equals(other$nameArabic)) {
                        break label127;
                    }

                    return false;
                }

                Object this$nameEnglish = this.getNameEnglish();
                Object other$nameEnglish = other.getNameEnglish();
                if (this$nameEnglish == null) {
                    if (other$nameEnglish != null) {
                        return false;
                    }
                } else if (!this$nameEnglish.equals(other$nameEnglish)) {
                    return false;
                }

                Object this$VatNumber = this.getVatNumber();
                Object other$VatNumber = other.getVatNumber();
                if (this$VatNumber == null) {
                    if (other$VatNumber != null) {
                        return false;
                    }
                } else if (!this$VatNumber.equals(other$VatNumber)) {
                    return false;
                }

                label106: {
                    Object this$address = this.getAddress();
                    Object other$address = other.getAddress();
                    if (this$address == null) {
                        if (other$address == null) {
                            break label106;
                        }
                    } else if (this$address.equals(other$address)) {
                        break label106;
                    }

                    return false;
                }

                label99: {
                    Object this$poBox = this.getPoBox();
                    Object other$poBox = other.getPoBox();
                    if (this$poBox == null) {
                        if (other$poBox == null) {
                            break label99;
                        }
                    } else if (this$poBox.equals(other$poBox)) {
                        break label99;
                    }

                    return false;
                }

                Object this$country = this.getCountry();
                Object other$country = other.getCountry();
                if (this$country == null) {
                    if (other$country != null) {
                        return false;
                    }
                } else if (!this$country.equals(other$country)) {
                    return false;
                }

                Object this$invoiceDetailsSet = this.getInvoiceDetailsSet();
                Object other$invoiceDetailsSet = other.getInvoiceDetailsSet();
                if (this$invoiceDetailsSet == null) {
                    if (other$invoiceDetailsSet != null) {
                        return false;
                    }
                } else if (!this$invoiceDetailsSet.equals(other$invoiceDetailsSet)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Customer;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        result = result * 59 + (this.isPresent() ? 79 : 97);
        Object $currencyRateFromSAR = this.getCurrencyRateFromSAR();
        result = result * 59 + ($currencyRateFromSAR == null ? 43 : $currencyRateFromSAR.hashCode());
        Object $smsaServiceFromSAR = this.getSmsaServiceFromSAR();
        result = result * 59 + ($smsaServiceFromSAR == null ? 43 : $smsaServiceFromSAR.hashCode());
        Object $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        Object $accountNumber = this.getAccountNumber();
        result = result * 59 + ($accountNumber == null ? 43 : $accountNumber.hashCode());
        Object $invoiceCurrency = this.getInvoiceCurrency();
        result = result * 59 + ($invoiceCurrency == null ? 43 : $invoiceCurrency.hashCode());
        Object $email = this.getEmail();
        result = result * 59 + ($email == null ? 43 : $email.hashCode());
        Object $nameArabic = this.getNameArabic();
        result = result * 59 + ($nameArabic == null ? 43 : $nameArabic.hashCode());
        Object $nameEnglish = this.getNameEnglish();
        result = result * 59 + ($nameEnglish == null ? 43 : $nameEnglish.hashCode());
        Object $VatNumber = this.getVatNumber();
        result = result * 59 + ($VatNumber == null ? 43 : $VatNumber.hashCode());
        Object $address = this.getAddress();
        result = result * 59 + ($address == null ? 43 : $address.hashCode());
        Object $poBox = this.getPoBox();
        result = result * 59 + ($poBox == null ? 43 : $poBox.hashCode());
        Object $country = this.getCountry();
        result = result * 59 + ($country == null ? 43 : $country.hashCode());
        Object $invoiceDetailsSet = this.getInvoiceDetailsSet();
        result = result * 59 + ($invoiceDetailsSet == null ? 43 : $invoiceDetailsSet.hashCode());
        return result;
    }

    public Customer() {
    }

    public static class CustomerBuilder {
        private String accountNumber;
        private String invoiceCurrency;
        private Double currencyRateFromSAR;
        private Double smsaServiceFromSAR;
        private String email;
        private String nameArabic;
        private String nameEnglish;
        private String VatNumber;
        private String address;
        private String poBox;
        private String country;
        private Boolean status;
        private boolean isPresent;
        private Set<InvoiceDetails> invoiceDetailsSet;

        CustomerBuilder() {
        }

        public CustomerBuilder accountNumber(final String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public CustomerBuilder invoiceCurrency(final String invoiceCurrency) {
            this.invoiceCurrency = invoiceCurrency;
            return this;
        }

        public CustomerBuilder currencyRateFromSAR(final Double currencyRateFromSAR) {
            this.currencyRateFromSAR = currencyRateFromSAR;
            return this;
        }

        public CustomerBuilder smsaServiceFromSAR(final Double smsaServiceFromSAR) {
            this.smsaServiceFromSAR = smsaServiceFromSAR;
            return this;
        }

        public CustomerBuilder email(final String email) {
            this.email = email;
            return this;
        }

        public CustomerBuilder nameArabic(final String nameArabic) {
            this.nameArabic = nameArabic;
            return this;
        }

        public CustomerBuilder nameEnglish(final String nameEnglish) {
            this.nameEnglish = nameEnglish;
            return this;
        }

        public CustomerBuilder VatNumber(final String VatNumber) {
            this.VatNumber = VatNumber;
            return this;
        }

        public CustomerBuilder address(final String address) {
            this.address = address;
            return this;
        }

        public CustomerBuilder poBox(final String poBox) {
            this.poBox = poBox;
            return this;
        }

        public CustomerBuilder country(final String country) {
            this.country = country;
            return this;
        }

        public CustomerBuilder status(final Boolean status) {
            this.status = status;
            return this;
        }

        public CustomerBuilder isPresent(final boolean isPresent) {
            this.isPresent = isPresent;
            return this;
        }

        @JsonIgnore
        public CustomerBuilder invoiceDetailsSet(final Set<InvoiceDetails> invoiceDetailsSet) {
            this.invoiceDetailsSet = invoiceDetailsSet;
            return this;
        }

        public Customer build() {
            return new Customer(this.accountNumber, this.invoiceCurrency, this.currencyRateFromSAR, this.smsaServiceFromSAR, this.email, this.nameArabic, this.nameEnglish, this.VatNumber, this.address, this.poBox, this.country, this.status, this.isPresent, this.invoiceDetailsSet);
        }

    }
}
