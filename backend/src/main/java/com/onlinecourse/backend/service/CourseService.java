package com.onlinecourse.backend.service;

import com.onlinecourse.backend.dto.CourseOverview;
import com.onlinecourse.backend.dto.LessonOverview;
import com.onlinecourse.backend.model.Course;
import com.onlinecourse.backend.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public CourseOverview getCourseOverviewById(int courseId) {
        return courseRepository.findById(courseId)
                .map(course -> {
                    List<LessonOverview> lessonOverviews = course.getLessons()
                            .stream()
                            .map(LessonOverview::new)
                            .collect(Collectors.toList());

                    double totalMinutes = lessonOverviews.stream()
                            .mapToDouble(LessonOverview::getDurationMinutes)
                            .sum();

                    String formattedDuration = formatDurationFromMinutes(totalMinutes);

                    return new CourseOverview(
                            course.getId(),
                            course.getTitle(),
                            course.getDescription(),
                            course.getImagePath(),
                            course.getNumberOfLessons(),
                            lessonOverviews,
                            formattedDuration
                    );
                })
                .orElse(null);
    }

    private String formatDurationFromMinutes(double totalMinutes) {
        int hours = (int) (totalMinutes / 60);
        int minutes = (int) (Math.round(totalMinutes) % 60);

        return String.format("%dh%02d", hours, minutes);
    }

}
