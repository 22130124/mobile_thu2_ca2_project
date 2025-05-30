package com.example.onlinecoursesapp.utils;

import com.example.onlinecoursesapp.models.UserProgress;

public interface UserCallback {
    void onSuccess(UserProgress user);
    void onFailure(String errorMessage);
}