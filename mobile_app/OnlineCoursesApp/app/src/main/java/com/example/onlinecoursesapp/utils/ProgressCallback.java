package com.example.onlinecoursesapp.utils;

import com.example.onlinecoursesapp.models.course_progress.CourseProgress;

import java.util.List;

public interface ProgressCallback {
    void onSuccess(List<CourseProgress> progressList);
    void onFailure(String errorMessage);

    interface SingleCourse {
        void onSuccess(CourseProgress progress);
        void onFailure(String message);
    }
}
