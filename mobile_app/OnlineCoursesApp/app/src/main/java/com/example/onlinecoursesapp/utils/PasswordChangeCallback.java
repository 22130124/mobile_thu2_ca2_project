package com.example.onlinecoursesapp.utils;

public interface PasswordChangeCallback {
    void onSuccess();
    void onFailure(String message);
}