package com.smsa.backend.dto;

import com.smsa.backend.service.CustomerService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;
import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CustomerDTO {
    private Long Id;
    private String name;
    private String email;
    private Long phoneNumber;
    private String address;
    private Long smsaCharges;
    private Boolean isPresent;
}
