package com.onlinecourse.backend.controller;

import com.onlinecourse.backend.dto.LessonManagement;
import com.onlinecourse.backend.model.Lesson;
import com.onlinecourse.backend.service.LessonManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/management/lesson")
@CrossOrigin
public class LessonManagementContr {
    private final LessonManagementService lessonService ;

    public LessonManagementContr(LessonManagementService lessonService) {
        this.lessonService = lessonService;
    }

    //Lấy bài học theo khóa học - Hương
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<LessonManagement>> getLessonsByCourseId(@PathVariable int courseId) {
        List<LessonManagement> lessons = lessonService.getLessonsByCourseId(courseId);
        if (lessons.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        return ResponseEntity.ok(lessons);
    }

    // Lấy bài học theo ID - Hương
    @GetMapping("/{id}")
    public ResponseEntity<LessonManagement> getLessonById(@PathVariable int id) {
        Optional<LessonManagement> lesson = lessonService.getLessonById(id);
        return lesson.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // Tìm kiếm bài học theo từ khóa - Hương
    @GetMapping("/search")
    public ResponseEntity<List<LessonManagement>> searchLessons(@RequestParam String query) {
        List<LessonManagement> lessons = lessonService.searchLessons(query);
        if (lessons.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lessons);
    }

    // Cập nhật bài học - Hương
    @PutMapping("/{id}")
    public ResponseEntity<LessonManagement> updateLesson(@PathVariable int id, @RequestBody Lesson lesson) {
        Optional<LessonManagement> updatedLesson = lessonService.updateLesson(id, lesson);
        return updatedLesson.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // Xóa bài học - Hương
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable int id) {
        System.out.println("Request to delete lesson with ID: " + id);
        if (!lessonService.existsById(id)) {
            System.out.println("Lesson with ID " + id + " does not exist.");
            return ResponseEntity.notFound().build();
        }
        try {
            lessonService.deleteById(id);
            System.out.println("Successfully deleted lesson with ID: " + id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // Log lỗi nếu quá trình xóa gặp vấn đề
            System.err.println("Error deleting lesson with ID " + id + ": " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }


    // Thêm bài học mới - Hương
    @PostMapping
    public ResponseEntity<LessonManagement> addLesson(@RequestBody Lesson lesson) {
        LessonManagement savedLesson = lessonService.addLesson(lesson);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLesson);
    }
}