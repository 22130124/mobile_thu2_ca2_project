package com.onlinecourse.backend.dto;

import com.onlinecourse.backend.model.Course;
import com.onlinecourse.backend.model.Lesson;
import java.time.LocalDateTime;

public class LessonManagement {
    private int id;
    private int courseId;
    private String title;
    private String content;
    private String youtubeVideoUrl;
    private double durationMinutes;
    private LocalDateTime createdAt;

    // Constructor chính (không có ID và createdAt)
    public LessonManagement(int courseId, String title, String content, String youtubeVideoUrl, double durationMinutes) {
        this.courseId = courseId;
        this.title = title;
        this.content = content;
        this.youtubeVideoUrl = youtubeVideoUrl;
        this.durationMinutes = durationMinutes;
    }

    // Constructor từ Entity Lesson
    public LessonManagement(Lesson lesson) {
        this.id = lesson.getId();
        this.courseId = lesson.getCourse().getId();
        this.title = lesson.getTitle();
        this.content = lesson.getContent();
        this.youtubeVideoUrl = lesson.getYoutubeVideoUrl();
        this.durationMinutes = lesson.getDurationMinutes();
        this.createdAt = lesson.getCreatedAt();
    }

    // Getter và Setter (ID và createdAt không có setter để đảm bảo chúng không bị thay đổi từ bên ngoài)
    public int getId() {
        return id;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getYoutubeVideoUrl() {
        return youtubeVideoUrl;
    }

    public void setYoutubeVideoUrl(String youtubeVideoUrl) {
        this.youtubeVideoUrl = youtubeVideoUrl;
    }

    public double getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(double durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


}
