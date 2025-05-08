package com.example.onlinecoursesapp.models;

import java.io.Serializable;

public class CourseProgress implements Serializable {
    private int courseId;
    private String courseTitle;
    private String courseImagePath;
    private int enrollmentId;
    private int totalLessons;
    private int completedLessons;
    private int totalDurationMinutes;
    private int completedDurationMinutes;
    private float completionPercentage;

    public CourseProgress() {
        // Empty constructor
    }

    // Getters and setters
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

    // Helper methods
    public String getFormattedTotalDuration() {
        int hours = totalDurationMinutes / 60;
        int minutes = totalDurationMinutes % 60;

        if (hours > 0) {
            return hours + "h " + minutes + "m";
        } else {
            return minutes + "m";
        }
    }

    public String getFormattedCompletedDuration() {
        int hours = completedDurationMinutes / 60;
        int minutes = completedDurationMinutes % 60;

        if (hours > 0) {
            return hours + "h " + minutes + "m";
        } else {
            return minutes + "m";
        }
    }

    public String getFormattedProgressText() {
        return completedLessons + "/" + totalLessons + " bài học";
    }

    public String getFormattedCompletionPercentage() {
        return Math.round(completionPercentage) + "%";
    }
}
