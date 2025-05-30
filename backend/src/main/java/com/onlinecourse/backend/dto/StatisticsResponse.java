package com.onlinecourse.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsResponse {
    private double totalLearningTimeMinutes;
    private int totalCoursesEnrolled;
    private int completedCourses;
    private int inProgressCourses;
    private float averageCompletionRate;
    private int totalAchievements;
}