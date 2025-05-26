package com.example.onlinecoursesapp.utils;

import com.example.onlinecoursesapp.models.CourseOverview;

import java.util.List;

public interface CourseOverviewCallback {
    void onSuccess(List<CourseOverview> courseOverviewList);

    void onFailure(String errorMessage);

    interface SingleCourse {
        void onSuccess(CourseOverview courseOverviewList);
        void onFailure(String message);
    }
}
