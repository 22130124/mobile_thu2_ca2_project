package com.example.onlinecoursesapp.utils;

import com.example.onlinecoursesapp.models.UserProgress;

public interface RegisterCallback {
    void onSuccess(UserProgress user);
    void onFailure(String message);
}
