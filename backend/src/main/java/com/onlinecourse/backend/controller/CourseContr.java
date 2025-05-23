package com.onlinecourse.backend.controller;

import com.onlinecourse.backend.dto.CourseOverview;
import com.onlinecourse.backend.dto.LessonOverview;
import com.onlinecourse.backend.model.Course;
import com.onlinecourse.backend.repository.CourseRepository;
import com.onlinecourse.backend.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/courses")
@CrossOrigin
public class CourseContr {
    private final CourseRepository courseRepository;
    private final CourseService courseService;

    public CourseContr(CourseRepository courseRepository, CourseService courseService) {
        this.courseRepository = courseRepository;
        this.courseService = courseService;
    }

    // lay tat ca khoa hoc
    @GetMapping
    public List<Course> getAllCourses() {
        return this.courseRepository.findAll();
    }

    //Lấy khoá học theo ID
    @GetMapping("/{id}")
    public ResponseEntity<CourseOverview> getCourseOverviewById(@PathVariable int id) {
        CourseOverview overview = courseService.getCourseOverviewById(id);
        return (overview != null) ? ResponseEntity.ok(overview) : ResponseEntity.notFound().build();
    }


    // Tìm kiếm khóa học theo từ khóa
    @GetMapping("/search")
    public List<Course> searchCourses(@RequestParam String query) {
        return courseRepository.findByTitleContainingIgnoreCase(query);
    }


}
