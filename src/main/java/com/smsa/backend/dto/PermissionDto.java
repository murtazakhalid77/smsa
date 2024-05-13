package com.smsa.backend.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Builder
public class PermissionDto {

    private Long id;

    private String name;

    private String description;

    private boolean present;

}
