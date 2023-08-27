package com.smsa.backend.dto;


import com.smsa.backend.model.Region;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CustomerDTO {


    private Long id;

    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @NotBlank(message = "Invoice currency is required")
    private String invoiceCurrency;


    private Region region;

    @NotNull(message = "SMSA service from SAR is required")
    @Min(value = 0, message = "SMSA service from SAR must be greater than or equal to 0")
    private Double smsaServiceFromSAR;

    @NotBlank(message = "Emails are required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "CC Emails are required")
    @Email(message = "Invalid email format")
    private String Ccmail;

    @NotBlank(message = "Customer name (Arabic) is required")
    private String nameArabic;

    @NotBlank(message = "Name (English) is required")
    private String nameEnglish;

    @NotBlank(message = "VAT number is required")
    private String vatNumber;

    @NotBlank(message = "Address is required")
    private String address;

    private String poBox;

    @NotBlank(message = "Country is required")
    private String country;
    private Boolean status;

    private boolean isPresent=Boolean.TRUE;


}

