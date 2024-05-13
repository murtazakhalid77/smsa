//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.smsa.backend.model;

import javax.persistence.Column;
import java.io.Serializable;

public class InvoiceDetailsId implements Serializable {
    private String mawb;
    private String manifestDate;


    @Column(name = "account_number_id")
    private String accountNumber;
    private String awb;




    public static InvoiceDetailsIdBuilder builder() {
        return new InvoiceDetailsIdBuilder();
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof InvoiceDetailsId)) {
            return false;
        } else {
            InvoiceDetailsId other = (InvoiceDetailsId)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label59: {
                    Object this$awb = this.getAwb();
                    Object other$awb = other.getAwb();
                    if (this$awb == null) {
                        if (other$awb == null) {
                            break label59;
                        }
                    } else if (this$awb.equals(other$awb)) {
                        break label59;
                    }

                    return false;
                }

                Object this$mawb = this.getMawb();
                Object other$mawb = other.getMawb();
                if (this$mawb == null) {
                    if (other$mawb != null) {
                        return false;
                    }
                } else if (!this$mawb.equals(other$mawb)) {
                    return false;
                }

                Object this$manifestDate = this.getManifestDate();
                Object other$manifestDate = other.getManifestDate();
                if (this$manifestDate == null) {
                    if (other$manifestDate != null) {
                        return false;
                    }
                } else if (!this$manifestDate.equals(other$manifestDate)) {
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

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof InvoiceDetailsId;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $awb = this.getAwb();
        result = result * 59 + ($awb == null ? 43 : $awb.hashCode());
        Object $mawb = this.getMawb();
        result = result * 59 + ($mawb == null ? 43 : $mawb.hashCode());
        Object $manifestDate = this.getManifestDate();
        result = result * 59 + ($manifestDate == null ? 43 : $manifestDate.hashCode());
        Object $accountNumber = this.getAccountNumber();
        result = result * 59 + ($accountNumber == null ? 43 : $accountNumber.hashCode());
        return result;
    }

    public InvoiceDetailsId(final String mawb, final String manifestDate, final String accountNumber, final String awb) {
        this.mawb = mawb;
        this.manifestDate = manifestDate;
        this.accountNumber = accountNumber;
        this.awb = awb;
    }

    public InvoiceDetailsId() {
    }

    public String getMawb() {
        return this.mawb;
    }

    public String getManifestDate() {
        return this.manifestDate;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public String getAwb() {
        return this.awb;
    }

    public void setMawb(final String mawb) {
        this.mawb = mawb;
    }

    public void setManifestDate(final String manifestDate) {
        this.manifestDate = manifestDate;
    }

    public void setAccountNumber(final String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAwb(final String awb) {
        this.awb = awb;
    }

    public static class InvoiceDetailsIdBuilder {
        private String mawb;
        private String manifestDate;
        private String accountNumber;
        private String awb;

        InvoiceDetailsIdBuilder() {
        }

        public InvoiceDetailsIdBuilder mawb(final String mawb) {
            this.mawb = mawb;
            return this;
        }

        public InvoiceDetailsIdBuilder manifestDate(final String manifestDate) {
            this.manifestDate = manifestDate;
            return this;
        }

        public InvoiceDetailsIdBuilder accountNumber(final String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public InvoiceDetailsIdBuilder awb(final String awb) {
            this.awb = awb;
            return this;
        }

        public InvoiceDetailsId build() {
            return new InvoiceDetailsId(this.mawb, this.manifestDate, this.accountNumber, this.awb);
        }

    }
}
