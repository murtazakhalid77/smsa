package com.smsa.backend.dto;


import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

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
