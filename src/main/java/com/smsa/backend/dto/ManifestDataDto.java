package com.smsa.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@JsonFormat
public class ManifestDataDto {
    String manifestNo;
    String prefix;
    String awbs;
    String mapper;
}
