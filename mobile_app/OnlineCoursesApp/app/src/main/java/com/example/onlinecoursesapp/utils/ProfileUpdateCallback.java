package com.example.onlinecoursesapp.utils;

import com.example.onlinecoursesapp.models.UserProgress;

public interface ProfileUpdateCallback {
    void onSuccess(UserProgress updatedUser);
    void onFailure(String message);
}