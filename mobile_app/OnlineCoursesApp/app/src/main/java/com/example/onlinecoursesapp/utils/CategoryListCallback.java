package com.example.onlinecoursesapp.utils;

import com.example.onlinecoursesapp.models.Category;

import java.util.List;

public interface CategoryListCallback {
    void onSuccess(List<Category> categories);
    void onFailure(String errorMessage);
}
