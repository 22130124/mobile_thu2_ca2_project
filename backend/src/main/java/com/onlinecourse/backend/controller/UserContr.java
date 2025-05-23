package com.onlinecourse.backend.controller;

import com.onlinecourse.backend.dto.LoginRequest;
import com.onlinecourse.backend.dto.UserProgress;
import com.onlinecourse.backend.dto.VerificationRequest;
import com.onlinecourse.backend.model.User;
import com.onlinecourse.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserContr {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody UserProgress userProgress) {
        try {
            UserProgress createdUser = userService.createUser(userProgress);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Đăng ký thành công");
            response.put("user", createdUser);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "Đăng ký thất bại: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            UserProgress user = userService.login(request.getEmail(), request.getPassword());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Đăng nhập thành công");
            response.put("user", user);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "Đăng nhập thất bại: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody VerificationRequest request) {
        String expectedCode = userService.getVerificationCode(request.getEmail());

        if (expectedCode != null && expectedCode.equals(request.getCode())) {
            userService.activateUser(request.getEmail());
            System.out.println("Corrected code!");
            return ResponseEntity.ok(Collections.singletonMap("message", "Xác minh thành công"));
        } else {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Mã xác minh không đúng"));
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");

        User user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Email không tồn tại"));
        }

        if (user.isActive()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Tài khoản đã được xác minh"));
        }

        // Gửi lại mã
        userService.generateAndSendVerificationCode(email);
        return ResponseEntity.ok(Collections.singletonMap("message", "Mã xác minh đã được gửi lại"));
    }

}
