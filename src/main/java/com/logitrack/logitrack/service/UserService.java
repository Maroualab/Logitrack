package com.logitrack.logitrack.service;

import com.logitrack.logitrack.dto.UpdateUserDTO;
import com.logitrack.logitrack.exception.BusinessException;
import com.logitrack.logitrack.exception.ResourceNotFoundException;
import com.logitrack.logitrack.mapper.UserMapper;
import com.logitrack.logitrack.model.User;
import com.logitrack.logitrack.model.enums.UserRole;
import com.logitrack.logitrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Get user by email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Create new user
    public User createUser(User user) {
        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BusinessException("Email already exists: " + user.getEmail());
        }
        
        // Encode password
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        
        // Set default active status if not provided
        if (user.getActive() == null) {
            user.setActive(true);
        }

        return userRepository.save(user);
    }

    // Update user
    public User updateUser(Long id, UpdateUserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        userMapper.updateEntityFromDTO(userDTO, existingUser);

        return userRepository.save(existingUser);
    }


    // Delete user
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        userRepository.delete(user);
    }

    // Get users by role
    public List<User> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    //login user
    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new BusinessException("Invalid password");
        }
        
        if (!user.getActive()) {
            throw new BusinessException("User account is inactive");
        }
        
        return user;
    }

}