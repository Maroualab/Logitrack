package com.logitrack.logitrack.controller;

import com.logitrack.logitrack.dto.UpdateUserDTO;
import com.logitrack.logitrack.dto.UserDTO;
import com.logitrack.logitrack.dto.UserResponseDTO;
import com.logitrack.logitrack.exception.ResourceNotFoundException;
import com.logitrack.logitrack.mapper.UserMapper;
import com.logitrack.logitrack.model.User;
import com.logitrack.logitrack.model.enums.UserRole;
import com.logitrack.logitrack.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserDTO userDTO) {

        // 1. Call the service, which now correctly returns a User entity
        User updatedUser = userService.updateUser(id, userDTO);

        // 2. Map the entity to the response DTO
        UserResponseDTO responseDTO = userMapper.toResponseDTO(updatedUser);

        // 3. Return the DTO in the ResponseEntity
        return ResponseEntity.ok(responseDTO);
    }


    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponseDTO> userDTOs = users.stream()
                .map(userMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return ResponseEntity.ok(userMapper.toResponseDTO(user));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserDTO userDTO) {

        // Convert DTO to Entity
        User user = userMapper.toEntity(userDTO);

        // Save user
        User savedUser = userService.createUser(user);

        // Convert back to Response DTO
        UserResponseDTO responseDTO = userMapper.toResponseDTO(savedUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

   
}