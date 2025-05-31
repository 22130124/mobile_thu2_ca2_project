package com.example.onlinecoursesapp.utils;

import com.example.onlinecoursesapp.models.Course;

public interface CourseCallback {
    void onSuccess(Course course);
    void onFailure(String error);
}
