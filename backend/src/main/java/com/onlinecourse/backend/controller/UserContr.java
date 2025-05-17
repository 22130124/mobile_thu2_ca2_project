package com.onlinecourse.backend.controller;

import com.onlinecourse.backend.dto.UserProgress;
import com.onlinecourse.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
