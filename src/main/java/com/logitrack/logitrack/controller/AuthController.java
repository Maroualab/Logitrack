package com.logitrack.logitrack.controller;

import com.logitrack.logitrack.dto.LoginDTO;
import com.logitrack.logitrack.dto.UserDTO;
import com.logitrack.logitrack.dto.UserResponseDTO;
import com.logitrack.logitrack.mapper.UserMapper;
import com.logitrack.logitrack.model.User;
import com.logitrack.logitrack.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;


    @PostMapping("/register")
    public  ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);


        User savedUser = userService.createUser(user);

        UserResponseDTO responseDTO = userMapper.toResponseDTO(savedUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDTO loginDTO) {
        User loggedInUser = userService.loginUser(loginDTO.getEmail(), loginDTO.getPassword());
        UserResponseDTO responseDTO = userMapper.toResponseDTO(loggedInUser);
        return ResponseEntity.ok("Login successful for user: " + responseDTO.getEmail());
    }
}