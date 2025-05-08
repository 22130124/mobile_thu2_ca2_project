package com.onlinecourse.backend.dto;

public class StatisticsResponse {
    private int totalLearningTimeMinutes;
    private int totalCoursesEnrolled;
    private int completedCourses;
    private int inProgressCourses;
    private float averageCompletionRate;
    private int totalAchievements;

    public int getTotalLearningTimeMinutes() {
        return totalLearningTimeMinutes;
    }

    public void setTotalLearningTimeMinutes(int totalLearningTimeMinutes) {
        this.totalLearningTimeMinutes = totalLearningTimeMinutes;
    }

    public int getTotalCoursesEnrolled() {
        return totalCoursesEnrolled;
    }

    public void setTotalCoursesEnrolled(int totalCoursesEnrolled) {
        this.totalCoursesEnrolled = totalCoursesEnrolled;
    }

    public int getCompletedCourses() {
        return completedCourses;
    }

    public void setCompletedCourses(int completedCourses) {
        this.completedCourses = completedCourses;
    }

    public int getInProgressCourses() {
        return inProgressCourses;
    }

    public void setInProgressCourses(int inProgressCourses) {
        this.inProgressCourses = inProgressCourses;
    }

    public float getAverageCompletionRate() {
        return averageCompletionRate;
    }

    public void setAverageCompletionRate(float averageCompletionRate) {
        this.averageCompletionRate = averageCompletionRate;
    }

    public int getTotalAchievements() {
        return totalAchievements;
    }

    public void setTotalAchievements(int totalAchievements) {
        this.totalAchievements = totalAchievements;
    }
}
