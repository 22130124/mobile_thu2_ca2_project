package com.example.onlinecoursesapp.utils;

import com.example.onlinecoursesapp.models.dashboard.DashboardStats;
import com.example.onlinecoursesapp.models.dashboard.RegistrationData;
import com.example.onlinecoursesapp.models.dashboard.StudentStatusData;

import java.util.List;

public class DashboardCallback {
    public interface DashboardStatsCallback {
        void onSuccess(DashboardStats stats);
        void onFailure(String errorMessage);
    }
    public interface RegistrationDataCallback {
        void onSuccess(List<RegistrationData.DailyRegistration> data);
        void onFailure(String errorMessage);
    }
    public interface StudentStatusDataCallback {
        void onSuccess(List<StudentStatusData.Status> data);
        void onFailure(String errorMessage);
    }
}
