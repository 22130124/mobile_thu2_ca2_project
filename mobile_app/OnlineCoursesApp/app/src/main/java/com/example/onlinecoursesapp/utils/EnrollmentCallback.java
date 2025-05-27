package com.example.onlinecoursesapp.utils;

public interface EnrollmentCallback {
    void onSuccess();
    void onFailure(String message);
    void onResult(boolean isEnrolled);
}
