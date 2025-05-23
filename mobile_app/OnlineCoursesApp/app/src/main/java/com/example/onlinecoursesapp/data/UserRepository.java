package com.example.onlinecoursesapp.data;

import android.content.Context;

import com.example.onlinecoursesapp.api.UserAPIService;
import com.example.onlinecoursesapp.models.UserProgress;
import com.example.onlinecoursesapp.api.ApiClient;
import com.example.onlinecoursesapp.utils.RegisterCallback;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private static UserRepository instance;
    private final UserAPIService apiService;

    private UserRepository(Context context) {
        apiService = ApiClient.getUserApiService();
    }

    public static UserRepository getInstance(Context context) {
        if (instance == null) {
            instance = new UserRepository(context);
        }
        return instance;
    }

    public void registerUser(UserProgress user, RegisterCallback callback) {
        apiService.registerUser(user).enqueue(new Callback<UserProgress>() {
            @Override
            public void onResponse(Call<UserProgress> call, Response<UserProgress> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        callback.onFailure(response.errorBody().string());
                    } catch (IOException e) {
                        callback.onFailure("Lỗi không xác định");
                    }
                }
            }

            @Override
            public void onFailure(Call<UserProgress> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
//
//    public void login(String email, String password, LoginCallback callback) {
//        // Tùy backend, có thể dùng DTO như LoginRequest
//        apiService.login(email, password).enqueue(new Callback<UserProgress>() {
//            @Override
//            public void onResponse(Call<UserProgress> call, Response<UserProgress> response) {
//                if (response.isSuccessful()) {
//                    callback.onSuccess(response.body());
//                } else {
//                    callback.onFailure("Đăng nhập thất bại");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserProgress> call, Throwable t) {
//                callback.onFailure(t.getMessage());
//            }
//        });
//    }
//
//    public void changePassword(int userId, String oldPassword, String newPassword, PasswordChangeCallback callback) {
//        apiService.changePassword(userId, oldPassword, newPassword).enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    callback.onSuccess();
//                } else {
//                    callback.onFailure("Đổi mật khẩu thất bại");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                callback.onFailure(t.getMessage());
//            }
//        });
//    }
}
