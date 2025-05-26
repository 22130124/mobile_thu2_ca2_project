package com.onlinecourse.backend.service;

import com.onlinecourse.backend.dto.course_progress.CourseProgress;
import com.onlinecourse.backend.dto.course_progress.StatisticsResponse;
import com.onlinecourse.backend.model.Course;
import com.onlinecourse.backend.model.Enrollment;
import com.onlinecourse.backend.model.Lesson;
import com.onlinecourse.backend.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
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
}
