package com.example.onlinecoursesapp.models;

public class LessonOverview {
    private int id;
    private String title;
    private String content;
    private String youtubeVideoUrl;
    private double durationMinutes;
    private String formattedDuration;


    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getYoutubeVideoUrl() { return youtubeVideoUrl; }
    public void setYoutubeVideoUrl(String youtubeVideoUrl) { this.youtubeVideoUrl = youtubeVideoUrl; }

    public double getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(double durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getFormattedDuration() { return formattedDuration; }
    public void setFormattedDuration(String formattedDuration) { this.formattedDuration = formattedDuration; }

}
