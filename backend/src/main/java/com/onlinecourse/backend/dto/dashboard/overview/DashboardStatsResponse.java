package com.onlinecourse.backend.dto.dashboard.overview;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatsResponse {
    private int totalStudents;
    private int totalCourses;
    private int totalHours;
    private double completionRate;
    private int newToday;
    private int newWeek;
}