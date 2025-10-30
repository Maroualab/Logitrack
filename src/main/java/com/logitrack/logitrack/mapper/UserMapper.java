package com.logitrack.logitrack.mapper;

import com.logitrack.logitrack.dto.UserDTO;
import com.logitrack.logitrack.dto.UserResponseDTO;
import com.logitrack.logitrack.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // Convert User Entity to UserResponseDTO
    public UserResponseDTO toResponseDTO(User user) {
        if (user == null) {
            return null;
        }

        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(user.getId());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setRole(user.getRole());
        responseDTO.setFullName(user.getFullName());
        responseDTO.setActive(user.getActive());

        return responseDTO;
    }

    // Convert UserDTO to User Entity
    public User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPasswordHash(userDTO.getPassword()); // In real app, we'd hash this
        user.setRole(userDTO.getRole());
        user.setFullName(userDTO.getFullName());
        user.setActive(userDTO.getActive() != null ? userDTO.getActive() : true);

        return user;
    }

    // Update existing User entity with DTO data
    public void updateEntityFromDTO(UserDTO userDTO, User user) {
        if (userDTO == null || user == null) {
            return;
        }

        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getPassword() != null) {
            user.setPasswordHash(userDTO.getPassword()); // Hash in real app
        }
        if (userDTO.getRole() != null) {
            user.setRole(userDTO.getRole());
        }
        if (userDTO.getFullName() != null) {
            user.setFullName(userDTO.getFullName());
        }
        if (userDTO.getActive() != null) {
            user.setActive(userDTO.getActive());
        }
    }
}