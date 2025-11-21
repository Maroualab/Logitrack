package com.logitrack.logitrack.dto;

import com.logitrack.logitrack.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String email;
    private UserRole role;
    private String fullName;
    private Boolean active;



}
