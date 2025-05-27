package com.example.onlinecoursesapp.api;

import com.example.onlinecoursesapp.models.dashboard.DashboardStats;
import com.example.onlinecoursesapp.models.dashboard.RegistrationData;
import com.example.onlinecoursesapp.models.dashboard.StudentStatusData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DashboardApiService {
    //    Lấy ra thông tin statistic của trang overview
    @GET("/dashboard/stats")
    Call<DashboardStats> getDashboardStats();

    //Lấy ra thông tin đăng ky hàng tuần của trang overview
    @GET("/dashboard/registrations")
    Call<List<RegistrationData.DailyRegistration>> getRegistrationData();

    //Lấy ra thông tin trang thai hoc của user của trang overview
    @GET("/dashboard/student-status")
    Call<List<StudentStatusData.Status>> getStudentStatusData();
}

