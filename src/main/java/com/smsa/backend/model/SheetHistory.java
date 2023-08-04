package com.smsa.backend.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "SheetHistory")
public class SheetHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String uniqueUUid;
    private Boolean isEmailSent;
    private LocalDate startDate;
    private LocalDate endDate;
    // Use @OneToMany with mappedBy to specify the relationship field in the Custom entity
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "custom_id")
    private Custom custom;
}
