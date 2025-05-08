package com.onlinecourse.backend.dto;

public class CourseProgress {
    private int courseId;
    private String courseTitle;
    private String courseImagePath;
    private int enrollmentId;
    private int totalLessons;
    private int completedLessons;
    private int totalDurationMinutes;
    private int completedDurationMinutes;
    private float completionPercentage;

    // Constructors
    public CourseProgress() {}

    public CourseProgress(int courseId, String courseTitle, String courseImagePath, int enrollmentId,
                          int totalLessons, int completedLessons,
                          int totalDurationMinutes, int completedDurationMinutes, float completionPercentage) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseImagePath = courseImagePath;
        this.enrollmentId = enrollmentId;
        this.totalLessons = totalLessons;
        this.completedLessons = completedLessons;
        this.totalDurationMinutes = totalDurationMinutes;
        this.completedDurationMinutes = completedDurationMinutes;
        this.completionPercentage = completionPercentage;
    }

    // Getters & Setters

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseImagePath() {
        return courseImagePath;
    }

    public void setCourseImagePath(String courseImagePath) {
        this.courseImagePath = courseImagePath;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public int getTotalLessons() {
        return totalLessons;
    }

    public void setTotalLessons(int totalLessons) {
        this.totalLessons = totalLessons;
    }

    public int getCompletedLessons() {
        return completedLessons;
    }

    public void setCompletedLessons(int completedLessons) {
        this.completedLessons = completedLessons;
    }

    public int getTotalDurationMinutes() {
        return totalDurationMinutes;
    }

    public void setTotalDurationMinutes(int totalDurationMinutes) {
        this.totalDurationMinutes = totalDurationMinutes;
    }

    public int getCompletedDurationMinutes() {
        return completedDurationMinutes;
    }

    public void setCompletedDurationMinutes(int completedDurationMinutes) {
        this.completedDurationMinutes = completedDurationMinutes;
    }

    public float getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(float completionPercentage) {
        this.completionPercentage = completionPercentage;
    }
}
