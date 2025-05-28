package com.onlinecourse.backend.dto;

import lombok.Data;

@Data
public class UserProgress {
    private int id;
    private String name;
    private String email;
    private String password;
    private String role;
    private boolean isActive;
    private String img;

    public UserProgress() {}

    // Constructor cho việc tạo mới user (khi đăng ký)
    public UserProgress(String name, String email, String password, String role, boolean isActive, String img) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isActive = isActive;
        this.img=img;
    }

    public UserProgress(int id, String name, String email, String role, boolean isActive, String img) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.isActive = isActive;
        this.img= img;
    }
}
