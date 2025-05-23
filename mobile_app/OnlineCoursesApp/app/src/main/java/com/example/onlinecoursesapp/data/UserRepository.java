package com.example.onlinecoursesapp.data;

import android.content.Context;

import com.example.onlinecoursesapp.api.UserAPIService;
import com.example.onlinecoursesapp.models.LoginRequest;
import com.example.onlinecoursesapp.models.LoginResponse;
import com.example.onlinecoursesapp.models.RegisterResponse;
import com.example.onlinecoursesapp.models.UserProgress;
import com.example.onlinecoursesapp.api.ApiClient;
import com.example.onlinecoursesapp.utils.LoginCallback;
import com.example.onlinecoursesapp.utils.RegisterCallback;

import org.json.JSONObject;

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

    public void registerUser(UserProgress userProgress, RegisterCallback callback) {
        apiService.registerUser(userProgress).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserProgress user = response.body().getUser();
                    callback.onSuccess(user);
                } else {
                    try {
                        // Đọc lỗi trả về từ server (ví dụ: "Email đã được sử dụng" hoặc "Không thể gửi email xác minh")
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : null;

                        if (errorBody != null) {
                            JSONObject jsonObject = new JSONObject(errorBody);
                            String message = jsonObject.optString("message", "Đăng ký thất bại không rõ lý do");
                            callback.onFailure(message);
                        } else {
                            callback.onFailure("Lỗi không rõ từ máy chủ");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure("Lỗi xử lý phản hồi từ máy chủ");
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                callback.onFailure("Lỗi kết nối mạng: " + t.getMessage());
            }
        });
    }


    public void loginUser(String email, String password, LoginCallback callback) {
        LoginRequest request = new LoginRequest(email, password);

        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getUser());
                } else {
                    try {
                        // Đọc thông báo lỗi từ JSON trả về
                        String errorJson = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorJson);
                        String errorMessage = jsonObject.optString("message", "Đăng nhập thất bại");

                        callback.onFailure(errorMessage);
                    } catch (Exception e) {
                        callback.onFailure("Lỗi phản hồi từ server");
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onFailure("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

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
