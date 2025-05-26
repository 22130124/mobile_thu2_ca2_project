package com.onlinecourse.backend.repository;

import com.onlinecourse.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    // Find users by role
    List<User> findByRole(String role);

    // Find users created after a specific date
    List<User> findByCreatedAtAfter(LocalDateTime date);
}
