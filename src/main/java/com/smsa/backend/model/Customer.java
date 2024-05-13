//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.smsa.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(
        name = "customer"
)
public class Customer {
    @Id
    private String accountNumber;
    private String invoiceCurrency;
    @OneToOne
    @JoinColumn(
            name = "id"
    )
    private Region region;
    private Double smsaServiceFromSAR;
    private String email;
    private String ccMail;
    private String nameArabic;
    private String nameEnglish;
    private String VatNumber;
    private String address;
    private String poBox;
    private String country;
    private Boolean status;
    private boolean isPresent;

    public static CustomerBuilder builder() {
        return new CustomerBuilder();
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
                Object this$smsaServiceFromSAR = this.getSmsaServiceFromSAR();
                Object other$smsaServiceFromSAR = other.getSmsaServiceFromSAR();
                if (this$smsaServiceFromSAR == null) {
                    if (other$smsaServiceFromSAR != null) {
                        return false;
                    }
                } else if (!this$smsaServiceFromSAR.equals(other$smsaServiceFromSAR)) {
                    return false;
                }

                Object this$status = this.getStatus();
                Object other$status = other.getStatus();
                if (this$status == null) {
                    if (other$status != null) {
                        return false;
                    }
                } else if (!this$status.equals(other$status)) {
                    return false;
                }

                label167: {
                    Object this$accountNumber = this.getAccountNumber();
                    Object other$accountNumber = other.getAccountNumber();
                    if (this$accountNumber == null) {
                        if (other$accountNumber == null) {
                            break label167;
                        }
                    } else if (this$accountNumber.equals(other$accountNumber)) {
                        break label167;
                    }

                    return false;
                }

                label160: {
                    Object this$invoiceCurrency = this.getInvoiceCurrency();
                    Object other$invoiceCurrency = other.getInvoiceCurrency();
                    if (this$invoiceCurrency == null) {
                        if (other$invoiceCurrency == null) {
                            break label160;
                        }
                    } else if (this$invoiceCurrency.equals(other$invoiceCurrency)) {
                        break label160;
                    }

                    return false;
                }

                Object this$region = this.getRegion();
                Object other$region = other.getRegion();
                if (this$region == null) {
                    if (other$region != null) {
                        return false;
                    }
                } else if (!this$region.equals(other$region)) {
                    return false;
                }

                Object this$email = this.getEmail();
                Object other$email = other.getEmail();
                if (this$email == null) {
                    if (other$email != null) {
                        return false;
                    }
                } else if (!this$email.equals(other$email)) {
                    return false;
                }

                label139: {
                    Object this$ccMail = this.getCcMail();
                    Object other$ccMail = other.getCcMail();
                    if (this$ccMail == null) {
                        if (other$ccMail == null) {
                            break label139;
                        }
                    } else if (this$ccMail.equals(other$ccMail)) {
                        break label139;
                    }

                    return false;
                }

                Object this$nameArabic = this.getNameArabic();
                Object other$nameArabic = other.getNameArabic();
                if (this$nameArabic == null) {
                    if (other$nameArabic != null) {
                        return false;
                    }
                } else if (!this$nameArabic.equals(other$nameArabic)) {
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

                label118: {
                    Object this$VatNumber = this.getVatNumber();
                    Object other$VatNumber = other.getVatNumber();
                    if (this$VatNumber == null) {
                        if (other$VatNumber == null) {
                            break label118;
                        }
                    } else if (this$VatNumber.equals(other$VatNumber)) {
                        break label118;
                    }

                    return false;
                }

                label111: {
                    Object this$address = this.getAddress();
                    Object other$address = other.getAddress();
                    if (this$address == null) {
                        if (other$address == null) {
                            break label111;
                        }
                    } else if (this$address.equals(other$address)) {
                        break label111;
                    }

                    return false;
                }

                label104: {
                    Object this$poBox = this.getPoBox();
                    Object other$poBox = other.getPoBox();
                    if (this$poBox == null) {
                        if (other$poBox == null) {
                            break label104;
                        }
                    } else if (this$poBox.equals(other$poBox)) {
                        break label104;
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
        Object $smsaServiceFromSAR = this.getSmsaServiceFromSAR();
        result = result * 59 + ($smsaServiceFromSAR == null ? 43 : $smsaServiceFromSAR.hashCode());
        Object $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        Object $accountNumber = this.getAccountNumber();
        result = result * 59 + ($accountNumber == null ? 43 : $accountNumber.hashCode());
        Object $invoiceCurrency = this.getInvoiceCurrency();
        result = result * 59 + ($invoiceCurrency == null ? 43 : $invoiceCurrency.hashCode());
        Object $region = this.getRegion();
        result = result * 59 + ($region == null ? 43 : $region.hashCode());
        Object $email = this.getEmail();
        result = result * 59 + ($email == null ? 43 : $email.hashCode());
        Object $ccMail = this.getCcMail();
        result = result * 59 + ($ccMail == null ? 43 : $ccMail.hashCode());
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
        return result;
    }


    public Customer(final String accountNumber, final String invoiceCurrency, final Region region, final Double smsaServiceFromSAR, final String email, final String ccMail, final String nameArabic, final String nameEnglish, final String VatNumber, final String address, final String poBox, final String country, final Boolean status, final boolean isPresent, final Set<InvoiceDetails> invoiceDetailsSet) {
        this.accountNumber = accountNumber;
        this.invoiceCurrency = invoiceCurrency;
        this.region = region;
        this.smsaServiceFromSAR = smsaServiceFromSAR;
        this.email = email;
        this.ccMail = ccMail;
        this.nameArabic = nameArabic;
        this.nameEnglish = nameEnglish;
        this.VatNumber = VatNumber;
        this.address = address;
        this.poBox = poBox;
        this.country = country;
        this.status = status;
        this.isPresent = isPresent;
    }

    public Customer() {
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public String getInvoiceCurrency() {
        return this.invoiceCurrency;
    }

    public Region getRegion() {
        return this.region;
    }

    public Double getSmsaServiceFromSAR() {
        return this.smsaServiceFromSAR;
    }

    public String getEmail() {
        return this.email;
    }

    public String getCcMail() {
        return this.ccMail;
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

    public void setAccountNumber(final String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setInvoiceCurrency(final String invoiceCurrency) {
        this.invoiceCurrency = invoiceCurrency;
    }

    public void setRegion(final Region region) {
        this.region = region;
    }

    public void setSmsaServiceFromSAR(final Double smsaServiceFromSAR) {
        this.smsaServiceFromSAR = smsaServiceFromSAR;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setCcMail(final String ccMail) {
        this.ccMail = ccMail;
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

    public static class CustomerBuilder {
        private String accountNumber;
        private String invoiceCurrency;
        private Region region;
        private Double smsaServiceFromSAR;
        private String email;
        private String ccMail;
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

        public CustomerBuilder region(final Region region) {
            this.region = region;
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

        public CustomerBuilder ccMail(final String ccMail) {
            this.ccMail = ccMail;
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
            return new Customer(this.accountNumber, this.invoiceCurrency, this.region, this.smsaServiceFromSAR, this.email, this.ccMail, this.nameArabic, this.nameEnglish, this.VatNumber, this.address, this.poBox, this.country, this.status, this.isPresent, this.invoiceDetailsSet);
        }

    }
}
