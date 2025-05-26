package com.onlinecourse.backend.controller;

import com.onlinecourse.backend.model.Course;
import com.onlinecourse.backend.repository.CourseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@CrossOrigin
public class CourseContr {
    private final CourseRepository courseRepository;

    public CourseContr(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // lay tat ca khoa hoc
    @GetMapping
    public List<Course> getAllCourses() {
        return this.courseRepository.findAll();
    }


    //Lấy khoá học theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable int id) {
        return courseRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Tìm kiếm khóa học theo từ khóa
    @GetMapping("/search")
    public List<Course> searchCourses(@RequestParam String query) {
        return courseRepository.findByTitleContainingIgnoreCase(query);
    }

    //Chỉnh sửa khóa học - Huong
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable int id, @RequestBody Course course) {
        return courseRepository.findById(id)
                .map(existingCourse -> {
                   existingCourse.setCourse(course);
                    Course updated = courseRepository.save(existingCourse);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    //Xóa khóa học - Huong
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable int id) {
        if (!courseRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        courseRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // HTTP 204
    }

    //Thêm khóa học - Hương
    @PostMapping
    public ResponseEntity<Course> addCourse(@RequestBody Course course) {
        Course savedCourse = courseRepository.save(course);
        return ResponseEntity.status(201).body(savedCourse);
    }


}
