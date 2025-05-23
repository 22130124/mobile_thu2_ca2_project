package com.example.onlinecoursesapp.models;

// Request gửi lại mã xác minh
public class ResendCodeRequest {
    private String email;

    public ResendCodeRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
