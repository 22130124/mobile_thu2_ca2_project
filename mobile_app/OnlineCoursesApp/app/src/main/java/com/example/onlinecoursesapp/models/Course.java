package com.example.onlinecoursesapp.models;

import java.time.LocalDateTime;
import java.util.List;

public class Course {
    private int id;
    private String title;
    private String description;
    private int categoryId;
    private String imagePath;
    private int numberOfLessons;
    private Difficulty difficulty;
    private List<Integer> createdAt;

    public String getCreatedAtFormatted() {
        if (createdAt != null && createdAt.size() >= 3) {
            return String.format("%02d/%02d/%d", createdAt.get(2), createdAt.get(1), createdAt.get(0));
        }
        return "Không rõ";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public List<Integer> getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(List<Integer> createdAt) {
        this.createdAt = createdAt;
    }


    public enum Difficulty {
        EASY, MEDIUM, HARD
    }
}
