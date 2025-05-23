package com.onlinecourse.backend.controller;

import com.onlinecourse.backend.dto.CourseProgress;
import com.onlinecourse.backend.dto.StatisticsResponse;
import com.onlinecourse.backend.model.User;
import com.onlinecourse.backend.repository.UserRepository;
import com.onlinecourse.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserContr {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserContr(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    //Lấy tất cả User
    @GetMapping
    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    //Lấy User theo ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

//    // Lấy thống kê học tập theo user
//    @GetMapping("/{userId}/statistics")
//    public StatisticsResponse getUserStatistics(@PathVariable int userId) {
//        return userService.getUserStatistics(userId);
//    }
//
//    // Lấy danh sách tiến độ khoá học theo từng user
//    @GetMapping("/{userId}/progress")
//    public List<CourseProgress> getUserCourseProgress(@PathVariable int userId) {
//        return userService.getUserCourseProgress(userId);
//    }
//
//    // Lấy tiến độ của 1 khoá học theo từng user
//    @GetMapping("/{userId}/progress/{courseId}")
//    public CourseProgress getCourseProgressByUserAndCourse(
//            @PathVariable int userId,
//            @PathVariable int courseId) {
//        return userService.getCourseProgressByUserAndCourse(userId, courseId);
//    }



}
