package com.example.onlinecoursesapp.models;

public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;

    public ChangePasswordRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
