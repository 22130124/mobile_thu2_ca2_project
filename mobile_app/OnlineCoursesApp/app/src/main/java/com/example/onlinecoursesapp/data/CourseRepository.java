package com.example.onlinecoursesapp.data;

import android.content.Context;

import com.example.onlinecoursesapp.api.ApiClient;
import com.example.onlinecoursesapp.api.CourseApiService;
import com.example.onlinecoursesapp.models.CourseOverview;
import com.example.onlinecoursesapp.models.CourseProgress;
import com.example.onlinecoursesapp.models.StatisticsResponse;
import com.example.onlinecoursesapp.utils.CourseOverviewCallback;
import com.example.onlinecoursesapp.utils.ProgressCallback;
import com.example.onlinecoursesapp.utils.StatisticsCallback;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseRepository {
    private static CourseRepository instance;
    private final CourseApiService apiService;
    private final int userId = 1; // Hoặc lấy từ SharedPreferences nếu đã login
    private StatisticsResponse statisticsCache;

    CourseRepository(Context context) {
        apiService = ApiClient.getCourseApiService();
    }

    public static CourseRepository getInstance(Context context) {
        if (instance == null) {
            instance = new CourseRepository(context);
        }
        return instance;
    }

    // Lấy ra thống kể người dùng
    public void fetchUserStatistics(StatisticsCallback callback) {
        System.out.println("Đã vào pt fetch User statistics");
        apiService.getUserStatistics(userId).enqueue(new Callback<StatisticsResponse>() {
            @Override
            public void onResponse(Call<StatisticsResponse> call, Response<StatisticsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    System.out.println("Trả về response của statistic: "+response.body());
                    statisticsCache = response.body();
                    callback.onStatisticsLoaded(statisticsCache); // Gọi callback
                }

                System.out.println("Không thành công load statistic");
            }

            @Override
            public void onFailure(Call<StatisticsResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //Lấy ra các tiến độ khoá học của user
    public void fetchCourseProgress(ProgressCallback callback) {
        System.out.println("Vào pt fetch Course Progress");
        apiService.getUserCourseProgress(userId).enqueue(new Callback<List<CourseProgress>>() {
            @Override
            public void onResponse(Call<List<CourseProgress>> call, Response<List<CourseProgress>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    System.out.println("Trả về response của course progress: "+response.body());
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

    // Lấy khóa học qua id
    public void fetchGetCourseById(int courseId, CourseOverviewCallback.SingleCourse callback) {
        apiService.getCourseById(courseId).enqueue(new Callback<CourseOverview>() {
            @Override
            public void onResponse(Call<CourseOverview> call, Response<CourseOverview> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Không lấy được thông tin khóa học.");
                }
            }

            @Override
            public void onFailure(Call<CourseOverview> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }


}

