package com.example.onlinecoursesapp.utils;

import com.example.onlinecoursesapp.models.Lesson;

public interface LessonCallback {
    void onSuccess(Lesson lesson);
    void onFailure(String message);
}
