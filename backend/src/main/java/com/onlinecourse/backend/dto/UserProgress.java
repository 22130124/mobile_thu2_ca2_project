package com.onlinecourse.backend.dto;

public class UserProgress {
    private int id;
    private String name;
    private String email;
    private String password;
    private String role;
    private boolean isActive;

    // Constructor mặc định (THÊM CÁI NÀY VÀO)
    public UserProgress() {
    }

    // Constructor cho việc tạo mới user (khi đăng ký)
    public UserProgress(String name, String email, String password, String role, boolean isActive) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isActive = isActive;
    }

    public UserProgress(int id, String name, String email, String role, boolean isActive) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
