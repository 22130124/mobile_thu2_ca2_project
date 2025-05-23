package com.onlinecourse.backend.controller;

import com.onlinecourse.backend.dto.LoginRequest;
import com.onlinecourse.backend.dto.UserProgress;
import com.onlinecourse.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserContr {
    @Autowired
    private UserService userService;

    @PostMapping
    public UserProgress createUser(@RequestBody UserProgress userProgress) {
        return userService.createUser(userProgress);
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

}
