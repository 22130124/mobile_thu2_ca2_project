package com.example.onlinecoursesapp.utils;

import com.example.onlinecoursesapp.models.StatisticsResponse;

public interface StatisticsCallback {
    void onStatisticsLoaded(StatisticsResponse statistics);
}
