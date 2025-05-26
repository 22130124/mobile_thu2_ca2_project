package com.onlinecourse.backend.service.dashboard.overview;

import com.onlinecourse.backend.dto.dashboard.overview.DailyRegistrationResponse;
import com.onlinecourse.backend.dto.dashboard.overview.DashboardStatsResponse;
import com.onlinecourse.backend.dto.dashboard.overview.StudentStatusResponse;
import com.onlinecourse.backend.model.Course;
import com.onlinecourse.backend.model.Enrollment;
import com.onlinecourse.backend.model.User;
import com.onlinecourse.backend.repository.CourseRepository;
import com.onlinecourse.backend.repository.EnrollmentRepository;
import com.onlinecourse.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public DashboardService(UserRepository userRepository,
                            CourseRepository courseRepository,
                            EnrollmentRepository enrollmentRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public DashboardStatsResponse getDashboardStats() {
        DashboardStatsResponse stats = new DashboardStatsResponse();

        // Get total user (roll user)
        List<User> users = userRepository.findByRole("USER");
        stats.setTotalStudents(users.size());

        // Get total courses
        List<Course> courses = courseRepository.findAll();
        stats.setTotalCourses(courses.size());

        // Calculate total learning hours
        int totalMinutes = courses.stream()
                .mapToInt(course -> course.getLessons().stream()
                        .mapToInt(lesson -> (int)lesson.getDurationMinutes())
                        .sum())
                .sum();
        stats.setTotalHours(totalMinutes / 60);

        // Calculate completion rate
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        double completionRate = enrollments.stream()
                .mapToDouble(enrollment -> {
                    int totalLessons = enrollment.getCourse().getLessons().size();
                    int completedLessons = enrollment.getCompletedLessonIds().size();
                    return totalLessons > 0 ? (double) completedLessons / totalLessons * 100 : 0;
                })
                .average()
                .orElse(0.0);
        stats.setCompletionRate(completionRate);

        // Get new registrations today
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        int newToday = (int) users.stream()
                .filter(user -> user.getCreatedAt().isAfter(startOfDay))
                .count();
        stats.setNewToday(newToday);

        // Get new registrations this week
        LocalDateTime startOfWeek = LocalDate.now().minusDays(7).atStartOfDay();
        int newWeek = (int) users.stream()
                .filter(user -> user.getCreatedAt().isAfter(startOfWeek))
                .count();
        stats.setNewWeek(newWeek);

        return stats;
    }

    public List<DailyRegistrationResponse> getRegistrationData() {
        LocalDateTime startOfWeek = LocalDate.now().minusDays(6).atStartOfDay();
        List<User> newUsers = userRepository.findByCreatedAtAfter(startOfWeek);
        return LocalDate.now().minusDays(6).datesUntil(LocalDate.now().plusDays(1))
                .map(date -> {
                    LocalDateTime dayStart = date.atStartOfDay();
                    LocalDateTime dayEnd = date.plusDays(1).atStartOfDay();

                    int count = (int) newUsers.stream()
                            .filter(user -> user.getCreatedAt().isAfter(dayStart)
                                    && user.getCreatedAt().isBefore(dayEnd))
                            .count();

                    return new DailyRegistrationResponse(date.getDayOfWeek().toString().substring(0, 3),count);
                })
                .collect(Collectors.toList());
    }

    public List<StudentStatusResponse> getStudentStatus() {
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        int totalEnrollments = enrollments.size();
        if (totalEnrollments == 0) {
            return List.of();
        }

        int completed = 0;
        int inProgress = 0;
        int notStarted = 0;

        for (Enrollment enrollment : enrollments) {
            int totalLessons = enrollment.getCourse().getLessons().size();
            int completedLessons = enrollment.getCompletedLessonIds().size();
            if (completedLessons == 0) {
                notStarted++;
            } else if (completedLessons == totalLessons) {
                completed++;
            } else {
                inProgress++;
            }
        }

        return List.of(
                new StudentStatusResponse("Completed", (float) completed / totalEnrollments * 100),
                new StudentStatusResponse("In Progress", (float) inProgress / totalEnrollments * 100),
                new StudentStatusResponse("Not Started", (float) notStarted / totalEnrollments * 100)
        );
    }
}