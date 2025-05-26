package com.example.onlinecoursesapp.data;

import android.content.Context;

import com.example.onlinecoursesapp.api.ApiClient;
import com.example.onlinecoursesapp.api.EnrollmentApiService;
import com.example.onlinecoursesapp.utils.EnrollmentCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnrollmentRepository {
    private static EnrollmentRepository instance;
    private final EnrollmentApiService apiService;

    EnrollmentRepository(Context context) {
        this.apiService = ApiClient.getEnrollmentApiService();
    }

    public static EnrollmentRepository getInstance(Context context) {
        if (instance == null) {
            instance = new EnrollmentRepository(context);
        }
        return instance;
    }

    // Thuy - Ghi danh người dùng (khi bấm nút Đăng ký học trong trang Tổng quan khóa học)
    public void enrollUser(int userId, int courseId, EnrollmentCallback callback) {
        apiService.enrollUser(userId, courseId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onFailure("Lỗi " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }


    // Thuy - Kiem tra user đã đăng ký học hay chưa
    public void checkEnrollment(int userId, int courseId, EnrollmentCallback callback) {
        apiService.checkEnrollment(userId, courseId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(response.body());
                } else {
                    callback.onResult(false); // Mặc định false nếu lỗi
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                callback.onResult(false);
            }
        });
    }
}
