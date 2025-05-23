package com.example.onlinecoursesapp.models;

import java.time.LocalDateTime;

public class Lesson {
    private int id;
    private Course course;
    private String title;
    private String content;
    private String youtubeVideoUrl;
    private double durationMinutes;
    private LocalDateTime createdAt;

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getYoutubeVideoUrl() { return youtubeVideoUrl; }
    public void setYoutubeVideoUrl(String youtubeVideoUrl) { this.youtubeVideoUrl = youtubeVideoUrl; }

    public double getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(double durationMinutes) { this.durationMinutes = durationMinutes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
