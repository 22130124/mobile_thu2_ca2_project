package com.onlinecourse.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CourseOverview {
    private int courseId;
    private String title;
    private String description;
    private String imagePath;
    private int numberOfLessons;
    private List<LessonOverview> lessons;
    private String totalFormattedDuration;

    // Constructor đầy đủ
    public CourseOverview(int courseId, String title, String description, String imagePath,
                          int numberOfLessons, List<LessonOverview> lessons, String totalFormattedDuration) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
        this.numberOfLessons = numberOfLessons;
        this.lessons = lessons;
        this.totalFormattedDuration = totalFormattedDuration;
    }

    @JsonProperty("totalFormattedDuration")
    public String getTotalFormattedDuration() {
        return totalFormattedDuration;
    }

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


}
