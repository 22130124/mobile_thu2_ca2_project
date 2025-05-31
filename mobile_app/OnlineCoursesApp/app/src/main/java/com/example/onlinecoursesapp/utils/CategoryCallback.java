package com.example.onlinecoursesapp.utils;

import com.example.onlinecoursesapp.models.Category;

public interface CategoryCallback {
    void onSuccess(Category category);
    void onFailure(String message);
}
