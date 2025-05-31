package com.example.onlinecoursesapp.utils;

import com.example.onlinecoursesapp.models.Category;
import com.example.onlinecoursesapp.models.Course;

import java.util.List;

public interface CourseListCallback {
    void onSuccess(List<Course> courses);
    void onFailure(String error);
}
