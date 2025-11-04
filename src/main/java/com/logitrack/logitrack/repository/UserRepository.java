package com.logitrack.logitrack.repository;

import com.logitrack.logitrack.model.User;
import com.logitrack.logitrack.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email
    Optional<User> findByEmail(String email);

    // Find all users by role
    List<User> findByRole(UserRole role);

    // Find all active users
    List<User> findByActiveTrue();

    // Check if email already exists
    boolean existsByEmail(String email);

    // Find users by name containing (for search)
    List<User> findByFullNameContainingIgnoreCase(String name);
}