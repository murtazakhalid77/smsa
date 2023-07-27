package com.smsa.backend.dto;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CustomDto {
    private Long id;
    private String customPort;
    private String custom;
    private Long smsaFeeVat;
    private boolean isPresent;
}
