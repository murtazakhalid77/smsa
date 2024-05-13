package com.smsa.backend.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Builder
public class RoleDto {
    private Long  id;
    private String name;
}
