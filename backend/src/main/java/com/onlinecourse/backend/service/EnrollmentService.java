package com.onlinecourse.backend.service;

import com.onlinecourse.backend.dto.course_progress.CourseProgress;
import com.onlinecourse.backend.dto.course_progress.StatisticsResponse;
import com.onlinecourse.backend.model.*;
import com.onlinecourse.backend.repository.CourseRepository;
import com.onlinecourse.backend.repository.EnrollmentRepository;
import com.onlinecourse.backend.repository.LessonProgressRepository;
import com.onlinecourse.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

//Kieu- service cho trang tien do khoa hoc
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final LessonProgressRepository lessonProgressRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, UserRepository userRepository, CourseRepository courseRepository, LessonProgressRepository lessonProgressRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.lessonProgressRepository = lessonProgressRepository;
    }

    public StatisticsResponse getUserStatistics(int userId) {
        List<CourseProgress> progresses = getUserCourseProgress(userId);

        int totalCourses = progresses.size();
        int completedCourses = (int) progresses.stream()
                .filter(p -> p.getCompletionPercentage() >= 100)
                .count();

        int inProgressCourses = totalCourses - completedCourses;
        double totalLearningTime = progresses.stream()
                .mapToDouble(CourseProgress::getCompletedDurationMinutes)
                .sum();

        float averageCompletionRate = totalCourses > 0
                ? (float) progresses.stream().mapToDouble(CourseProgress::getCompletionPercentage).sum() / totalCourses
                : 0;

        StatisticsResponse stats = new StatisticsResponse();
        stats.setTotalCoursesEnrolled(totalCourses);
        stats.setCompletedCourses(completedCourses);
        stats.setInProgressCourses(inProgressCourses);
        stats.setTotalLearningTimeMinutes(totalLearningTime);
        stats.setAverageCompletionRate(averageCompletionRate);
        stats.setTotalAchievements(completedCourses); // giả định mỗi khoá hoàn thành là 1 achievement

        return stats;
    }

    public List<CourseProgress> getUserCourseProgress(int userId) {
        List<Enrollment> enrollments = enrollmentRepository.findByUserId(userId);

        return enrollments.stream().map(enrollment -> {
            Course course = enrollment.getCourse();
            List<Lesson> lessons = course.getLessons();
            List<Integer> completedLessonIds = enrollment.getCompletedLessonIds();

            int totalLessons = lessons.size();
            int completedLessons = (int) lessons.stream()
                    .filter(lesson -> completedLessonIds.contains(lesson.getId()))
                    .count();

            double totalDuration = lessons.stream()
                    .mapToDouble(Lesson::getDurationMinutes)
                    .sum();

            double completedDuration = lessons.stream()
                    .filter(lesson -> completedLessonIds.contains(lesson.getId()))
                    .mapToDouble(Lesson::getDurationMinutes)
                    .sum();

            float completionPercentage = totalLessons > 0 ? (completedLessons * 100f / totalLessons) : 0;

            return new CourseProgress(
                    course.getId(),
                    course.getTitle(),
                    course.getImagePath(),
                    enrollment.getId(),
                    totalLessons,
                    completedLessons,
                    totalDuration,
                    completedDuration,
                    completionPercentage
            );
        }).collect(Collectors.toList());
    }

    public CourseProgress getCourseProgressByUserAndCourse(int userId, int courseId) {
        List<CourseProgress> allProgress = getUserCourseProgress(userId);
        for (CourseProgress p : allProgress) {
            if (p.getCourseId() == courseId) {
                return p;
            }
        }
        throw new RuntimeException("Không tìm thấy tiến độ cho khóa học " + courseId);
    }

    public ResponseEntity<?> enrollUser(int userId, int courseId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Course> courseOpt = courseRepository.findById(courseId);

        if (userOpt.isEmpty() || courseOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Người dùng hoặc khóa học không tồn tại.");
        }

        User user = userOpt.get();
        Course course = courseOpt.get();

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(LocalDateTime.now());

        enrollmentRepository.save(enrollment);

        // Tạo LessonProgress cho mỗi bài học
        List<LessonProgress> progressList = course.getLessons().stream()
                .map(lesson -> {
                    LessonProgress progress = new LessonProgress();
                    progress.setEnrollment(enrollment);
                    progress.setLesson(lesson);
                    progress.setCompleted(false);
                    progress.setCompletedAt(null); // chưa hoàn thành thì null
                    return progress;
                }).toList();

        lessonProgressRepository.saveAll(progressList);

        return ResponseEntity.ok("Đăng ký khóa học và lưu tiến trình thành công.");
    }
}
