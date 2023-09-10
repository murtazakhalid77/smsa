//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.smsa.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
        name = "custom"
)
public class Custom {
        @Id
        @GeneratedValue(
                strategy = GenerationType.IDENTITY
        )
        private Long id;
        private String customPort;
        private String custom;
        private Double smsaFeeVat;
        private String currency;
        private boolean isPresent;
        @JsonIgnore
        @OneToMany(
                mappedBy = "custom",
                cascade = {CascadeType.ALL},
                orphanRemoval = true
        )
        private List<SheetHistory> sheetHistories;

        public static CustomBuilder builder() {
                return new CustomBuilder();
        }

        public boolean equals(final Object o) {
                if (o == this) {
                        return true;
                } else if (!(o instanceof Custom)) {
                        return false;
                } else {
                        Custom other = (Custom)o;
                        if (!other.canEqual(this)) {
                                return false;
                        } else if (this.isPresent() != other.isPresent()) {
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

                                Object this$smsaFeeVat = this.getSmsaFeeVat();
                                Object other$smsaFeeVat = other.getSmsaFeeVat();
                                if (this$smsaFeeVat == null) {
                                        if (other$smsaFeeVat != null) {
                                                return false;
                                        }
                                } else if (!this$smsaFeeVat.equals(other$smsaFeeVat)) {
                                        return false;
                                }

                                label71: {
                                        Object this$customPort = this.getCustomPort();
                                        Object other$customPort = other.getCustomPort();
                                        if (this$customPort == null) {
                                                if (other$customPort == null) {
                                                        break label71;
                                                }
                                        } else if (this$customPort.equals(other$customPort)) {
                                                break label71;
                                        }

                                        return false;
                                }

                                label64: {
                                        Object this$custom = this.getCustom();
                                        Object other$custom = other.getCustom();
                                        if (this$custom == null) {
                                                if (other$custom == null) {
                                                        break label64;
                                                }
                                        } else if (this$custom.equals(other$custom)) {
                                                break label64;
                                        }

                                        return false;
                                }

                                Object this$currency = this.getCurrency();
                                Object other$currency = other.getCurrency();
                                if (this$currency == null) {
                                        if (other$currency != null) {
                                                return false;
                                        }
                                } else if (!this$currency.equals(other$currency)) {
                                        return false;
                                }

                                Object this$sheetHistories = this.getSheetHistories();
                                Object other$sheetHistories = other.getSheetHistories();
                                if (this$sheetHistories == null) {
                                        if (other$sheetHistories != null) {
                                                return false;
                                        }
                                } else if (!this$sheetHistories.equals(other$sheetHistories)) {
                                        return false;
                                }

                                return true;
                        }
                }
        }

        protected boolean canEqual(final Object other) {
                return other instanceof Custom;
        }

        public int hashCode() {
                boolean PRIME = true;
                int result = 1;
                result = result * 59 + (this.isPresent() ? 79 : 97);
                Object $id = this.getId();
                result = result * 59 + ($id == null ? 43 : $id.hashCode());
                Object $smsaFeeVat = this.getSmsaFeeVat();
                result = result * 59 + ($smsaFeeVat == null ? 43 : $smsaFeeVat.hashCode());
                Object $customPort = this.getCustomPort();
                result = result * 59 + ($customPort == null ? 43 : $customPort.hashCode());
                Object $custom = this.getCustom();
                result = result * 59 + ($custom == null ? 43 : $custom.hashCode());
                Object $currency = this.getCurrency();
                result = result * 59 + ($currency == null ? 43 : $currency.hashCode());
                Object $sheetHistories = this.getSheetHistories();
                result = result * 59 + ($sheetHistories == null ? 43 : $sheetHistories.hashCode());
                return result;
        }

        public Custom(final Long id, final String customPort, final String custom, final Double smsaFeeVat, final String currency, final boolean isPresent, final List<SheetHistory> sheetHistories) {
                this.id = id;
                this.customPort = customPort;
                this.custom = custom;
                this.smsaFeeVat = smsaFeeVat;
                this.currency = currency;
                this.isPresent = isPresent;
                this.sheetHistories = sheetHistories;
        }

        public Custom() {
        }

        public Long getId() {
                return this.id;
        }

        public String getCustomPort() {
                return this.customPort;
        }

        public String getCustom() {
                return this.custom;
        }

        public Double getSmsaFeeVat() {
                return this.smsaFeeVat;
        }

        public String getCurrency() {
                return this.currency;
        }

        public boolean isPresent() {
                return this.isPresent;
        }

        public List<SheetHistory> getSheetHistories() {
                return this.sheetHistories;
        }

        public void setId(final Long id) {
                this.id = id;
        }

        public void setCustomPort(final String customPort) {
                this.customPort = customPort;
        }

        public void setCustom(final String custom) {
                this.custom = custom;
        }

        public void setSmsaFeeVat(final Double smsaFeeVat) {
                this.smsaFeeVat = smsaFeeVat;
        }

        public void setCurrency(final String currency) {
                this.currency = currency;
        }

        public void setPresent(final boolean isPresent) {
                this.isPresent = isPresent;
        }

        public void setSheetHistories(final List<SheetHistory> sheetHistories) {
                this.sheetHistories = sheetHistories;
        }

        public static class CustomBuilder {
                private Long id;
                private String customPort;
                private String custom;
                private Double smsaFeeVat;
                private String currency;
                private boolean isPresent;
                private List<SheetHistory> sheetHistories;

                CustomBuilder() {
                }

                public CustomBuilder id(final Long id) {
                        this.id = id;
                        return this;
                }

                public CustomBuilder customPort(final String customPort) {
                        this.customPort = customPort;
                        return this;
                }

                public CustomBuilder custom(final String custom) {
                        this.custom = custom;
                        return this;
                }

                public CustomBuilder smsaFeeVat(final Double smsaFeeVat) {
                        this.smsaFeeVat = smsaFeeVat;
                        return this;
                }

                public CustomBuilder currency(final String currency) {
                        this.currency = currency;
                        return this;
                }

                public CustomBuilder isPresent(final boolean isPresent) {
                        this.isPresent = isPresent;
                        return this;
                }

                public CustomBuilder sheetHistories(final List<SheetHistory> sheetHistories) {
                        this.sheetHistories = sheetHistories;
                        return this;
                }

                public Custom build() {
                        return new Custom(this.id, this.customPort, this.custom, this.smsaFeeVat, this.currency, this.isPresent, this.sheetHistories);
                }

        }
}
