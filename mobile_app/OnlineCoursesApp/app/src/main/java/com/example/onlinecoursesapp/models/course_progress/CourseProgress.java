package com.example.onlinecoursesapp.models.course_progress;

import com.example.onlinecoursesapp.utils.FormatTime;

import java.io.Serializable;

public class CourseProgress implements Serializable {
    private int courseId;
    private String courseTitle;
    private String courseImagePath;
    private int enrollmentId;
    private int totalLessons;
    private int completedLessons;
    private double totalDurationMinutes;
    private double completedDurationMinutes;
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

    public double getTotalDurationMinutes() {
        return totalDurationMinutes;
    }

    public void setTotalDurationMinutes(double totalDurationMinutes) {
        this.totalDurationMinutes = totalDurationMinutes;
    }

    public double getCompletedDurationMinutes() {
        return completedDurationMinutes;
    }

    public void setCompletedDurationMinutes(double completedDurationMinutes) {
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
        return FormatTime.formatDuration(totalDurationMinutes);
    }

    public String getFormattedCompletedDuration() {
        return FormatTime.formatDuration(completedDurationMinutes);
    }

    public String getFormattedProgressText() {
        return completedLessons + "/" + totalLessons + " bài học";
    }

    public String getFormattedCompletionPercentage() {
        return Math.round(completionPercentage) + "%";
    }
}
