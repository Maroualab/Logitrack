package com.logitrack.logitrack.dto.client;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class ClientDTO {

    private Long id;

    @NotBlank(message = "Client name cannot be blank")
    private String name;

    @NotBlank(message = "Contact email cannot be blank")
    @Email(message = "Must be a valid email address")
    private String contactEmail;

    private String contactPhone;
}