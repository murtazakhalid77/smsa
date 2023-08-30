//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.smsa.backend.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(
        name = "currency"
)
public class Currency {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    Long id;
    String currencyFrom;
    String currencyTo;
    Double conversionRate;
    Boolean isPresent;
    String createdBy;
    String createdAt;
    String updatedBy;
    String updatedAt;
    @OneToMany(
            mappedBy = "currency",
            cascade = {CascadeType.ALL},
            orphanRemoval = true
    )
    private List<CurrencyAuditLog> currencyAuditLogs;

    public static CurrencyBuilder builder() {
        return new CurrencyBuilder();
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Currency)) {
            return false;
        } else {
            Currency other = (Currency)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$id = this.getId();
                Object other$id = other.getId();
                if (this$id == null) {
                    if (other$id != null) {
                        return false;
                    }
                } else if (!this$id.equals(other$id)) {
                    return false;
                }

                Object this$conversionRate = this.getConversionRate();
                Object other$conversionRate = other.getConversionRate();
                if (this$conversionRate == null) {
                    if (other$conversionRate != null) {
                        return false;
                    }
                } else if (!this$conversionRate.equals(other$conversionRate)) {
                    return false;
                }

                Object this$isPresent = this.getIsPresent();
                Object other$isPresent = other.getIsPresent();
                if (this$isPresent == null) {
                    if (other$isPresent != null) {
                        return false;
                    }
                } else if (!this$isPresent.equals(other$isPresent)) {
                    return false;
                }

                label110: {
                    Object this$currencyFrom = this.getCurrencyFrom();
                    Object other$currencyFrom = other.getCurrencyFrom();
                    if (this$currencyFrom == null) {
                        if (other$currencyFrom == null) {
                            break label110;
                        }
                    } else if (this$currencyFrom.equals(other$currencyFrom)) {
                        break label110;
                    }

                    return false;
                }

                label103: {
                    Object this$currencyTo = this.getCurrencyTo();
                    Object other$currencyTo = other.getCurrencyTo();
                    if (this$currencyTo == null) {
                        if (other$currencyTo == null) {
                            break label103;
                        }
                    } else if (this$currencyTo.equals(other$currencyTo)) {
                        break label103;
                    }

                    return false;
                }

                Object this$createdBy = this.getCreatedBy();
                Object other$createdBy = other.getCreatedBy();
                if (this$createdBy == null) {
                    if (other$createdBy != null) {
                        return false;
                    }
                } else if (!this$createdBy.equals(other$createdBy)) {
                    return false;
                }

                label89: {
                    Object this$createdAt = this.getCreatedAt();
                    Object other$createdAt = other.getCreatedAt();
                    if (this$createdAt == null) {
                        if (other$createdAt == null) {
                            break label89;
                        }
                    } else if (this$createdAt.equals(other$createdAt)) {
                        break label89;
                    }

                    return false;
                }

                label82: {
                    Object this$updatedBy = this.getUpdatedBy();
                    Object other$updatedBy = other.getUpdatedBy();
                    if (this$updatedBy == null) {
                        if (other$updatedBy == null) {
                            break label82;
                        }
                    } else if (this$updatedBy.equals(other$updatedBy)) {
                        break label82;
                    }

                    return false;
                }

                Object this$updatedAt = this.getUpdatedAt();
                Object other$updatedAt = other.getUpdatedAt();
                if (this$updatedAt == null) {
                    if (other$updatedAt != null) {
                        return false;
                    }
                } else if (!this$updatedAt.equals(other$updatedAt)) {
                    return false;
                }

                Object this$currencyAuditLogs = this.getCurrencyAuditLogs();
                Object other$currencyAuditLogs = other.getCurrencyAuditLogs();
                if (this$currencyAuditLogs == null) {
                    if (other$currencyAuditLogs != null) {
                        return false;
                    }
                } else if (!this$currencyAuditLogs.equals(other$currencyAuditLogs)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Currency;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        Object $conversionRate = this.getConversionRate();
        result = result * 59 + ($conversionRate == null ? 43 : $conversionRate.hashCode());
        Object $isPresent = this.getIsPresent();
        result = result * 59 + ($isPresent == null ? 43 : $isPresent.hashCode());
        Object $currencyFrom = this.getCurrencyFrom();
        result = result * 59 + ($currencyFrom == null ? 43 : $currencyFrom.hashCode());
        Object $currencyTo = this.getCurrencyTo();
        result = result * 59 + ($currencyTo == null ? 43 : $currencyTo.hashCode());
        Object $createdBy = this.getCreatedBy();
        result = result * 59 + ($createdBy == null ? 43 : $createdBy.hashCode());
        Object $createdAt = this.getCreatedAt();
        result = result * 59 + ($createdAt == null ? 43 : $createdAt.hashCode());
        Object $updatedBy = this.getUpdatedBy();
        result = result * 59 + ($updatedBy == null ? 43 : $updatedBy.hashCode());
        Object $updatedAt = this.getUpdatedAt();
        result = result * 59 + ($updatedAt == null ? 43 : $updatedAt.hashCode());
        Object $currencyAuditLogs = this.getCurrencyAuditLogs();
        result = result * 59 + ($currencyAuditLogs == null ? 43 : $currencyAuditLogs.hashCode());
        return result;
    }

    public Currency(final Long id, final String currencyFrom, final String currencyTo, final Double conversionRate, final Boolean isPresent, final String createdBy, final String createdAt, final String updatedBy, final String updatedAt, final List<CurrencyAuditLog> currencyAuditLogs) {
        this.id = id;
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
        this.conversionRate = conversionRate;
        this.isPresent = isPresent;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
        this.currencyAuditLogs = currencyAuditLogs;
    }

    public Currency() {
    }

    public Long getId() {
        return this.id;
    }

    public String getCurrencyFrom() {
        return this.currencyFrom;
    }

    public String getCurrencyTo() {
        return this.currencyTo;
    }

    public Double getConversionRate() {
        return this.conversionRate;
    }

    public Boolean getIsPresent() {
        return this.isPresent;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public List<CurrencyAuditLog> getCurrencyAuditLogs() {
        return this.currencyAuditLogs;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setCurrencyFrom(final String currencyFrom) {
        this.currencyFrom = currencyFrom;
    }

    public void setCurrencyTo(final String currencyTo) {
        this.currencyTo = currencyTo;
    }

    public void setConversionRate(final Double conversionRate) {
        this.conversionRate = conversionRate;
    }

    public void setIsPresent(final Boolean isPresent) {
        this.isPresent = isPresent;
    }

    public void setCreatedBy(final String createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedAt(final String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedBy(final String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setUpdatedAt(final String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCurrencyAuditLogs(final List<CurrencyAuditLog> currencyAuditLogs) {
        this.currencyAuditLogs = currencyAuditLogs;
    }


    public static class CurrencyBuilder {
        private Long id;
        private String currencyFrom;
        private String currencyTo;
        private Double conversionRate;
        private Boolean isPresent;
        private String createdBy;
        private String createdAt;
        private String updatedBy;
        private String updatedAt;
        private List<CurrencyAuditLog> currencyAuditLogs;

        CurrencyBuilder() {
        }

        public CurrencyBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        public CurrencyBuilder currencyFrom(final String currencyFrom) {
            this.currencyFrom = currencyFrom;
            return this;
        }

        public CurrencyBuilder currencyTo(final String currencyTo) {
            this.currencyTo = currencyTo;
            return this;
        }

        public CurrencyBuilder conversionRate(final Double conversionRate) {
            this.conversionRate = conversionRate;
            return this;
        }

        public CurrencyBuilder isPresent(final Boolean isPresent) {
            this.isPresent = isPresent;
            return this;
        }

        public CurrencyBuilder createdBy(final String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public CurrencyBuilder createdAt(final String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public CurrencyBuilder updatedBy(final String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public CurrencyBuilder updatedAt(final String updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public CurrencyBuilder currencyAuditLogs(final List<CurrencyAuditLog> currencyAuditLogs) {
            this.currencyAuditLogs = currencyAuditLogs;
            return this;
        }

        public Currency build() {
            return new Currency(this.id, this.currencyFrom, this.currencyTo, this.conversionRate, this.isPresent, this.createdBy, this.createdAt, this.updatedBy, this.updatedAt, this.currencyAuditLogs);
        }

    }
}
