package com.example.onlinecoursesapp.utils;

import com.example.onlinecoursesapp.models.UserProgress;

public interface LoginCallback {
    void onSuccess(UserProgress user);
    void onFailure(String message);
}
