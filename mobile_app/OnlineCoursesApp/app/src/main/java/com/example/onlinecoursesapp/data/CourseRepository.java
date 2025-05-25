package com.example.onlinecoursesapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.onlinecoursesapp.api.ApiClient;
import com.example.onlinecoursesapp.api.CourseApiService;
import com.example.onlinecoursesapp.models.CourseProgress;
import com.example.onlinecoursesapp.models.StatisticsResponse;
import com.example.onlinecoursesapp.utils.ProgressCallback;
import com.example.onlinecoursesapp.utils.StatisticsCallback;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseRepository {
    private static CourseRepository instance;
    private final CourseApiService apiService;
    private StatisticsResponse statisticsCache;
    private final int userId;
    private final Context context;

    CourseRepository(Context context) {
        apiService = ApiClient.getCourseApiService();
        this.context= context.getApplicationContext();
        this.userId=getUserIdFromPrefs();
    }

    public static CourseRepository getInstance(Context context) {
        if (instance == null) {
            instance = new CourseRepository(context);
        }
        return instance;
    }

    private int getUserIdFromPrefs() {
        SharedPreferences prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return prefs.getInt("userId", -1);
    }

    // Lấy ra thống kể người dùng
    public void fetchUserStatistics(StatisticsCallback callback) {
        if (userId == -1) {
            callback.onFailure("Không tìm thấy thông tin người dùng.");
            return;
        }
        apiService.getUserStatistics(userId).enqueue(new Callback<StatisticsResponse>() {
            @Override
            public void onResponse(Call<StatisticsResponse> call, Response<StatisticsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    statisticsCache = response.body();
                    callback.onStatisticsLoaded(statisticsCache); // Gọi callback
                }
            }

            @Override
            public void onFailure(Call<StatisticsResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //Lấy ra các tiến độ khoá học của user
    public void fetchCourseProgress(ProgressCallback callback) {
        if (userId == -1) {
            callback.onFailure("Không tìm thấy thông tin người dùng.");
            return;
        }
        apiService.getUserCourseProgress(userId).enqueue(new Callback<List<CourseProgress>>() {
            @Override
            public void onResponse(Call<List<CourseProgress>> call, Response<List<CourseProgress>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Không lấy được tiến độ học.");
                }
            }

            @Override
            public void onFailure(Call<List<CourseProgress>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    // Lấy tiến độ học tập cho một khóa học cụ thể
    public void fetchCourseProgressById(int courseId, ProgressCallback.SingleCourse callback) {
        if (userId == -1) {
            callback.onFailure("Không tìm thấy thông tin người dùng.");
            return;
        }
        apiService.getCourseProgressByUserAndCourse(userId, courseId).enqueue(new Callback<CourseProgress>() {
            @Override
            public void onResponse(Call<CourseProgress> call, Response<CourseProgress> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Không lấy được tiến độ khóa học.");
                }
            }

            @Override
            public void onFailure(Call<CourseProgress> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }



}

