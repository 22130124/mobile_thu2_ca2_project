package com.example.onlinecoursesapp.utils;

public interface ImageUploadCallback {
    void onSuccess(String imageUrl);
    void onFailure(String message);
}