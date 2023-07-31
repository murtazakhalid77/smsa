//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.smsa.backend.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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
        private boolean isPresent;
        @OneToOne(
                mappedBy = "custom"
        )
        private SheetHistory sheetHistory;

        public static CustomBuilder builder() {
                return new CustomBuilder();
        }

        public Custom(final Long id, final String customPort, final String custom, final Double smsaFeeVat, final boolean isPresent, final SheetHistory sheetHistory) {
                this.id = id;
                this.customPort = customPort;
                this.custom = custom;
                this.smsaFeeVat = smsaFeeVat;
                this.isPresent = isPresent;
                this.sheetHistory = sheetHistory;
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

        public boolean isPresent() {
                return this.isPresent;
        }

        public SheetHistory getSheetHistory() {
                return this.sheetHistory;
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

        public void setPresent(final boolean isPresent) {
                this.isPresent = isPresent;
        }

        public void setSheetHistory(final SheetHistory sheetHistory) {
                this.sheetHistory = sheetHistory;
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
                                label73: {
                                        Object this$id = this.getId();
                                        Object other$id = other.getId();
                                        if (this$id == null) {
                                                if (other$id == null) {
                                                        break label73;
                                                }
                                        } else if (this$id.equals(other$id)) {
                                                break label73;
                                        }

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

                                label59: {
                                        Object this$customPort = this.getCustomPort();
                                        Object other$customPort = other.getCustomPort();
                                        if (this$customPort == null) {
                                                if (other$customPort == null) {
                                                        break label59;
                                                }
                                        } else if (this$customPort.equals(other$customPort)) {
                                                break label59;
                                        }

                                        return false;
                                }

                                Object this$custom = this.getCustom();
                                Object other$custom = other.getCustom();
                                if (this$custom == null) {
                                        if (other$custom != null) {
                                                return false;
                                        }
                                } else if (!this$custom.equals(other$custom)) {
                                        return false;
                                }

                                Object this$sheetHistory = this.getSheetHistory();
                                Object other$sheetHistory = other.getSheetHistory();
                                if (this$sheetHistory == null) {
                                        if (other$sheetHistory != null) {
                                                return false;
                                        }
                                } else if (!this$sheetHistory.equals(other$sheetHistory)) {
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
                Object $sheetHistory = this.getSheetHistory();
                result = result * 59 + ($sheetHistory == null ? 43 : $sheetHistory.hashCode());
                return result;
        }

        public Custom() {
        }


        public static class CustomBuilder {
                private Long id;
                private String customPort;
                private String custom;
                private Double smsaFeeVat;
                private boolean isPresent;
                private SheetHistory sheetHistory;

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

                public CustomBuilder isPresent(final boolean isPresent) {
                        this.isPresent = isPresent;
                        return this;
                }

                public CustomBuilder sheetHistory(final SheetHistory sheetHistory) {
                        this.sheetHistory = sheetHistory;
                        return this;
                }

                public Custom build() {
                        return new Custom(this.id, this.customPort, this.custom, this.smsaFeeVat, this.isPresent, this.sheetHistory);
                }

        }
}
