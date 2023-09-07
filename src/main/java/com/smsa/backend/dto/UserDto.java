package com.smsa.backend.dto;
import lombok.*;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UserDto {

    private Long id;

    private String name;
    private String password;
    private boolean status;
    private String role;
}

