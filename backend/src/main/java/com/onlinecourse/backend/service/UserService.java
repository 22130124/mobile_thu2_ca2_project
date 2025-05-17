package com.onlinecourse.backend.service;

import com.onlinecourse.backend.dto.UserProgress;
import com.onlinecourse.backend.model.User;
import com.onlinecourse.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProgress createUser(UserProgress userProgress) {
        User user = new User();

        user.setName(userProgress.getName());
        user.setEmail(userProgress.getEmail());
        user.setRole(userProgress.getRole());
        user.setActive(userProgress.isActive());

        // Mã hoá mật khẩu
        String encodedPassword = passwordEncoder.encode(userProgress.getPassword());
        user.setPassword(encodedPassword);

        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return convertToDTO(savedUser);
    }

    private UserProgress convertToDTO(User user) {
        return new UserProgress(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.isActive()
        );
    }


}
