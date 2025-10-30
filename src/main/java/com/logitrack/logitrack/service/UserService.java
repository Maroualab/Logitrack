package com.logitrack.logitrack.service;

import com.logitrack.logitrack.dto.UserDTO;
import com.logitrack.logitrack.model.User;
import com.logitrack.logitrack.model.UserRole;
import com.logitrack.logitrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }

        return userRepository.save(user);
    }

    // Update user
    public User updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        UserMapper.updateUserFromDTO(userDTO, existingUser);
//
//        userRepository.save(existingUser);
//        return "User updated successfully.";
    }


//    public String updateUser(Long id , UserDTO userDTO) {
//        User existingUser = userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        UserMapper.updateUserFromDTO(userDTO, existingUser);
//
//        userRepository.save(existingUser);
//        return "User updated successfully.";
//    }


    // Delete user
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        userRepository.delete(user);
    }

    // Get users by role
    public List<User> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    //login user
    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        if (!user.getPasswordHash().equals(password)) {
            throw new RuntimeException("Invalid password for email: " + email);
        }

        return user;
    }

}