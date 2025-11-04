package com.logitrack.logitrack.mapper;

import com.logitrack.logitrack.dto.UpdateUserDTO;
import com.logitrack.logitrack.dto.UserDTO;
import com.logitrack.logitrack.dto.UserResponseDTO;
import com.logitrack.logitrack.model.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class UserMapper {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    // Convert User Entity to UserResponseDTO
    public abstract UserResponseDTO toResponseDTO(User user);

    // Convert UserDTO to User Entity (map plain password -> passwordHash)
    @Mapping(target = "passwordHash", source = "password")
    public abstract User toEntity(UserDTO userDTO);

    // Update existing User entity with DTO data (ignore nulls)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateEntityFromDTO(UpdateUserDTO userDTO, @MappingTarget User user);

    @AfterMapping
    protected void encodePassword(UpdateUserDTO dto, @MappingTarget User user) {
        if (dto == null || user == null) {
            return;
        }
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            if (passwordEncoder != null) {
                user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
            } else {
                user.setPasswordHash(dto.getPassword());
            }
        }
    }
}