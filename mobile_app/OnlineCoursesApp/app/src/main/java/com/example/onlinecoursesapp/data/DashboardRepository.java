package com.example.onlinecoursesapp.data;

import com.example.onlinecoursesapp.api.ApiClient;
import com.example.onlinecoursesapp.api.DashboardApiService;
import com.example.onlinecoursesapp.models.dashboard.DashboardStats;
import com.example.onlinecoursesapp.models.dashboard.RegistrationData;
import com.example.onlinecoursesapp.models.dashboard.StudentStatusData;
import com.example.onlinecoursesapp.utils.DashboardCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardRepository {
    private static DashboardRepository instance;
    private final DashboardApiService dashboardApiService;

    private DashboardRepository() {
        dashboardApiService = ApiClient.getDashboardApiService();
    }
    public static DashboardRepository getInstance() {
        if (instance == null) {
            instance = new DashboardRepository();
        }
        return instance;
    }

    //Lấy dữ liệu trả về từ server
    public void fetchDashboardStats(DashboardCallback.DashboardStatsCallback callback) {
        System.out.println("Vào fetch dashboard statistic");
        dashboardApiService.getDashboardStats().enqueue(new Callback<DashboardStats>() {
            @Override
            public void onResponse(Call<DashboardStats> call, Response<DashboardStats> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                    System.out.println("Response: "+response.body().toString());
                } else {
                    callback.onFailure("Không thể tải thống kê tổng quan.");
                }
            }
            @Override
            public void onFailure(Call<DashboardStats> call, Throwable t) {
                callback.onFailure("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    public void fetchRegistrationData(DashboardCallback.RegistrationDataCallback callback) {
        System.out.println("Vào fetch registration data");
        dashboardApiService.getRegistrationData().enqueue(new Callback<List<RegistrationData.DailyRegistration>>() {
            @Override
            public void onResponse(Call<List<RegistrationData.DailyRegistration>> call, Response<List<RegistrationData.DailyRegistration>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Không thể tải dữ liệu đăng ký.");
                }
            }
            @Override
            public void onFailure(Call<List<RegistrationData.DailyRegistration>> call, Throwable t) {
                callback.onFailure("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    public void fetchStudentStatusData(DashboardCallback.StudentStatusDataCallback callback) {
        System.out.println("Vào fetch student status");
        dashboardApiService.getStudentStatusData().enqueue(new Callback<List<StudentStatusData.Status>>() {
            @Override
            public void onResponse(Call<List<StudentStatusData.Status>> call, Response<List<StudentStatusData.Status>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Không thể tải dữ liệu trạng thái học viên.");
                }
            }
            @Override
            public void onFailure(Call<List<StudentStatusData.Status>> call, Throwable t) {
                callback.onFailure("Lỗi mạng: " + t.getMessage());
            }
        });
    }
}
