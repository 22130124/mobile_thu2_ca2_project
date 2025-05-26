package com.onlinecourse.backend.dto.course_progress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseProgress {
    private int courseId;
    private String courseTitle;
    private String courseImagePath;
    private int enrollmentId;
    private int totalLessons;
    private int completedLessons;
    private double totalDurationMinutes;
    private double completedDurationMinutes;
    private float completionPercentage;

}
