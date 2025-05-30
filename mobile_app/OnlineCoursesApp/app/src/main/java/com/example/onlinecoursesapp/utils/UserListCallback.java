package com.example.onlinecoursesapp.utils;

import com.example.onlinecoursesapp.models.UserProgress;
import java.util.List;

public interface UserListCallback {
    void onSuccess(List<UserProgress> users);
    void onFailure(String errorMessage);
}