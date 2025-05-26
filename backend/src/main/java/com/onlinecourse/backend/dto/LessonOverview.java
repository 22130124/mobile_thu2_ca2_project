package com.onlinecourse.backend.dto;

import com.onlinecourse.backend.model.Lesson;

public class LessonOverview {
    private int id;
    private String title;
    private String content;
    private String youtubeVideoUrl;
    private double durationMinutes;

    public LessonOverview(Lesson lesson) {
        this.id = lesson.getId();
        this.title = lesson.getTitle();
        this.content = lesson.getContent();
        this.youtubeVideoUrl = lesson.getYoutubeVideoUrl();
        this.durationMinutes = lesson.getDurationMinutes();
    }

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

    @com.fasterxml.jackson.annotation.JsonProperty("formattedDuration")
    public String getFormattedDuration() {
        int minutes = (int) durationMinutes;
        int seconds = (int) ((durationMinutes - minutes) * 60);
        return String.format("%d:%02d", minutes, seconds);
    }

}
