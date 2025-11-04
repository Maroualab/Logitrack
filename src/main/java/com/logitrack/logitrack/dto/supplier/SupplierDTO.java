package com.logitrack.logitrack.dto.supplier;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

@Data
@NoArgsConstructor
public class SupplierDTO {

    private Long id;

    @NotBlank(message = "Supplier name cannot be blank")
    private String name;

    private String contactName;

    @Email(message = "Must be a valid email address")
    private String contactEmail;

    private String contactPhone;
}
