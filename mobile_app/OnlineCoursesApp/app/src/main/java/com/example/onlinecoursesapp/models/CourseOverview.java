package com.example.onlinecoursesapp.models;

import java.util.List;

// Thuy
public class CourseOverview {
    private int courseId;
    private String title;
    private String description;
    private String imagePath;
    private int numberOfLessons;
    private List<LessonOverview> lessons;
    private String totalFormattedDuration;

    // Getter và Setter
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getNumberOfLessons() {
        return numberOfLessons;
    }

    public void setNumberOfLessons(int numberOfLessons) {
        this.numberOfLessons = numberOfLessons;
    }

    public List<LessonOverview> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonOverview> lessons) {
        this.lessons = lessons;
    }

    public String getTotalFormattedDuration() {
        return totalFormattedDuration;
    }

    public void setTotalFormattedDuration(String totalFormattedDuration) {
        this.totalFormattedDuration = totalFormattedDuration;
    }
}