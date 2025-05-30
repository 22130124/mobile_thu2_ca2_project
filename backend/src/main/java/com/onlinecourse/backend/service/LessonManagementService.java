package com.onlinecourse.backend.service;

import com.onlinecourse.backend.dto.LessonManagement;
import com.onlinecourse.backend.model.Lesson;
import com.onlinecourse.backend.repository.LessonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LessonManagementService {

    private final LessonRepository lessonRepository;

    public LessonManagementService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public List<LessonManagement> getLessonsByCourseId(int courseId) {
        List<Lesson> lessonList = lessonRepository.findByCourseId(courseId);
        return lessonList.stream()
                .map(LessonManagement::new)
                .collect(Collectors.toList());
    }

    public Optional<LessonManagement> getLessonById(int id) {
        return lessonRepository.findById(id)
                .map(LessonManagement::new);
    }

    public List<LessonManagement> searchLessons(String query) {
        List<Lesson> lessons = lessonRepository.findByTitleContainingIgnoreCase(query);
        return lessons.stream()
                .map(LessonManagement::new)
                .collect(Collectors.toList());
    }

    public Optional<LessonManagement> updateLesson(int id, Lesson lesson) {
        return lessonRepository.findById(id)
                .map(existingLesson -> {
                    lesson.setId(existingLesson.getId());
                    lesson.setCourse(existingLesson.getCourse());
                    lesson.setCreatedAt(existingLesson.getCreatedAt());
                    existingLesson.setLesson(lesson);
                    Lesson updatedLesson = lessonRepository.save(existingLesson);
                    return new LessonManagement(updatedLesson);
                });
    }

    public LessonManagement addLesson(Lesson lesson) {
        Lesson savedLesson = lessonRepository.save(lesson);
        return new LessonManagement(savedLesson);
    }

    public boolean existsById(int id) {
        return lessonRepository.existsById(id);
    }
//
//    public void deleteById(int id) {
//        lessonRepository.deleteById(id);
//    }

    @Transactional
    public boolean deleteLesson(int id) {
        try {
            Optional<Lesson> lessonOpt = lessonRepository.findById(id);
            if (lessonOpt.isPresent()) {
                Lesson lesson = lessonOpt.get();
                System.out.println("Found lesson to delete: " + lesson.getId() + " - " + lesson.getTitle());

                // Sử dụng JPA repository để xóa lesson
                lessonRepository.delete(lesson);

                // Verify deletion
                boolean stillExists = lessonRepository.existsById(id);
                if (stillExists) {
                    System.err.println("Lesson still exists after deletion attempt!");
                    return false;
                }

                System.out.println("Lesson successfully deleted from database");
                return true;
            }
            System.out.println("Lesson not found with ID: " + id);
            return false;
        } catch (Exception e) {
            System.err.println("Error deleting lesson: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
