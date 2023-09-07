package com.smsa.backend.dto;


import lombok.*;

import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

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
