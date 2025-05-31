package com.onlinecourse.backend.service;

import com.onlinecourse.backend.model.Enrollment;
import com.onlinecourse.backend.model.LessonProgress;
import com.onlinecourse.backend.repository.EnrollmentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LessonProgressService {

    private final EnrollmentRepository enrollmentRepository;

    public LessonProgressService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public ResponseEntity<?> completeLesson(int userId, int courseId, int lessonId) {
        Optional<Enrollment> enrollmentOpt = enrollmentRepository.findByUserIdAndCourseId(userId, courseId);
        if (enrollmentOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Không tìm thấy đăng ký.");
        }

        Enrollment enrollment = enrollmentOpt.get();

        LessonProgress progress = enrollment.getLessonProgresses().stream()
                .filter(p -> p.getLesson().getId() == lessonId)
                .findFirst()
                .orElse(null);

        progress.setCompleted(true);
        progress.setCompletedAt(LocalDateTime.now());

        enrollmentRepository.save(enrollment);
        return ResponseEntity.ok("Đã hoàn thành bài học.");
    }

    public ResponseEntity<Boolean> checkLessonCompletion(int userId, int courseId, int lessonId) {
        Optional<Enrollment> enrollmentOpt = enrollmentRepository.findByUserIdAndCourseId(userId, courseId);
        if (enrollmentOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(false);
        }

        LessonProgress progress = enrollmentOpt.get().getLessonProgresses().stream()
                .filter(p -> p.getLesson().getId() == lessonId)
                .findFirst()
                .orElse(null);

        if (progress == null) return ResponseEntity.badRequest().body(false);
        return ResponseEntity.ok(progress.isCompleted());
    }
}

