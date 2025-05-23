package com.onlinecourse.backend.service;

import com.onlinecourse.backend.dto.UserProgress;
import com.onlinecourse.backend.model.User;
import com.onlinecourse.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final Map<String, String> verificationCodes = new HashMap<>();
    private final EmailService emailService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public UserProgress createUser(UserProgress userProgress) {
        // Kiểm tra email đã tồn tại chưa
        if (userRepository.findByEmail(userProgress.getEmail()) != null) {
            throw new RuntimeException("Email đã được sử dụng");
        }

        User user = new User();
        user.setName(userProgress.getName());
        user.setEmail(userProgress.getEmail());
        user.setRole(userProgress.getRole());
        user.setActive(false);

        String encodedPassword = passwordEncoder.encode(userProgress.getPassword());
        user.setPassword(encodedPassword);

        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        // Tạo mã xác minh và gửi email
        String code = String.format("%06d", new Random().nextInt(999999));
        verificationCodes.put(user.getEmail(), code);

        emailService.sendVerificationCode(user.getEmail(), code);

        return convertToDTO(savedUser);
    }

    public UserProgress login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Email không tồn tại");
        }

        boolean match = passwordEncoder.matches(rawPassword, user.getPassword());
        if (!match) {
            throw new RuntimeException("Mật khẩu không chính xác");
        }

        if (!user.isActive()) {
            throw new RuntimeException("Tài khoản đã bị khóa");
        }

        return convertToDTO(user);
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

    public void activateUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setActive(true);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        }
    }

    public String getVerificationCode(String email) {
        return verificationCodes.get(email);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void generateAndSendVerificationCode(String email) {
        String code = String.format("%06d", new Random().nextInt(999999));
        verificationCodes.put(email, code);
        emailService.sendVerificationCode(email, code);
    }
}
