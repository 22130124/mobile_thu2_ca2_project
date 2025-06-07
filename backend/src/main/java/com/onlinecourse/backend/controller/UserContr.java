package com.onlinecourse.backend.controller;

import com.onlinecourse.backend.dto.*;
import com.onlinecourse.backend.model.User;
import com.onlinecourse.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
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
            return ResponseEntity.ok(Collections.singletonMap("message", "Xác minh thành công"));
        } else {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Mã xác minh không đúng"));
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestBody Map<String, Object> payload) {
        String email = (String) payload.get("email");
        boolean isPasswordReset = Boolean.parseBoolean(payload.getOrDefault("isPasswordReset", "false").toString());

        User user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Email không tồn tại"));
        }

        // Nếu là đặt lại mật khẩu thì không cần kiểm tra isActive
        if (!isPasswordReset && user.isActive()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Tài khoản đã được xác minh"));
        }

        userService.generateAndSendVerificationCode(email);
        return ResponseEntity.ok(Collections.singletonMap("message", "Mã xác minh đã được gửi"));
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable int id, @RequestBody ChangePasswordRequest request) {
        try {
            userService.changePassword(id, request.getOldPassword(), request.getNewPassword());
            return ResponseEntity.ok(Collections.singletonMap("message", "Đổi mật khẩu thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            System.out.println("Đang thay đổi mật khẩu");
            userService.resetPassword(request.getEmail(), request.getNewPassword());
            return ResponseEntity.ok(Collections.singletonMap("message", "Đặt lại mật khẩu thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    // Hàm upload ảnh user
    @PostMapping("/{id}/upload-image")
    public ResponseEntity<?> uploadUserImage(@PathVariable int id, @RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = userService.uploadUserImage(id, file);
            return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not upload file: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable int id) {
        try {
            User user = userService.getUserProfile(id);
            user.setPassword(null);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserProfile(@PathVariable int id, @RequestBody User updatedUser) {
        try {
            User user = userService.updateUserProfile(id, updatedUser);
            user.setPassword(null);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //----Quan ly user trong daskboard---------
    // Get all users
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserProgress> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Failed to load users: " + e.getMessage()));
        }
    }

    // Get user by ID
    @GetMapping("/byId{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        try {
            UserProgress user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    // Add user
    @PostMapping("/addUser")
    public ResponseEntity<?> addUser(@RequestBody UserProgress userProgress) {
        try {
            UserProgress createdUser = userService.addUser(userProgress);
            return ResponseEntity.ok(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "Failed to create user: " + e.getMessage()));
        }
    }

    // Update user
    @PutMapping("/updateId{id}")
    public ResponseEntity<?> updateUser(@PathVariable int id, @RequestBody UserProgress user) {
        try {
            UserProgress updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    // Update user status
    @PutMapping("/updateId{id}/status")
    public ResponseEntity<?> updateUserStatus(@PathVariable int id, @RequestParam("is_active") boolean active) {
        try {
            UserProgress updatedUser = userService.updateUserStatus(id, active);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    // Search users
    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(@RequestParam String query) {
        try {
            List<UserProgress> users = userService.searchUsers(query);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Failed to search users: " + e.getMessage()));
        }
    }

    // Filter users by status
    @GetMapping("/filter")
    public ResponseEntity<?> filterUsersByStatus(@RequestParam("is_active") boolean active) {
        try {
            List<UserProgress> users = userService.filterUsersByStatus(active);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Failed to filter users: " + e.getMessage()));
        }
    }
}
