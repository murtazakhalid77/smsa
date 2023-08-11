//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.smsa.backend.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
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


        @OneToMany(mappedBy = "custom", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<SheetHistory> sheetHistories;
}
