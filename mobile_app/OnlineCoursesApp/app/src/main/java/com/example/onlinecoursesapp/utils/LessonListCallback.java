package com.example.onlinecoursesapp.utils;

import com.example.onlinecoursesapp.models.Lesson;

import java.util.List;

public interface LessonListCallback {
    void onSuccess(List<Lesson> lessons);
    void onFailure(String message);
}
