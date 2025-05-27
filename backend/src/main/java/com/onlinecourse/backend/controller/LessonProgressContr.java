package com.onlinecourse.backend.controller;

import com.onlinecourse.backend.service.LessonProgressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/progress")
@CrossOrigin
public class LessonProgressContr {

    private final LessonProgressService lessonProgressService;

    public LessonProgressContr(LessonProgressService lessonProgressService) {
        this.lessonProgressService = lessonProgressService;
    }

    // Thuy - Cập nhật tiến trình isCompleted = 1 (khi bấm nút Hoàn thành)
    @PutMapping("/complete")
    public ResponseEntity<?> completeLesson(
            @RequestParam int userId,
            @RequestParam int courseId,
            @RequestParam int lessonId) {

        return lessonProgressService.completeLesson(userId, courseId, lessonId);
    }

}
