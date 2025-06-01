package com.example.onlinecoursesapp.data;

import android.content.Context;

import com.example.onlinecoursesapp.api.ApiClient;
import com.example.onlinecoursesapp.api.LessonManagementApiService;
import com.example.onlinecoursesapp.models.Lesson;
import com.example.onlinecoursesapp.utils.LessonCallback;
import com.example.onlinecoursesapp.utils.LessonListCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LessonManagementRepository {
    private static LessonManagementRepository instance;
    private final LessonManagementApiService apiService;

    LessonManagementRepository(Context context) {
        this.apiService = ApiClient.getLessonManagementApiService();
    }

    public static LessonManagementRepository getInstance(Context context) {
        if (instance == null) {
            instance = new LessonManagementRepository(context);
        }
        return instance;
    }

    // Lấy danh sách bài học theo khóa học
    public void getLessonsByCourseId(int courseId, LessonListCallback callback) {
        apiService.getLessonsByCourseId(courseId).enqueue(new Callback<List<Lesson>>() {
            @Override
            public void onResponse(Call<List<Lesson>> call, Response<List<Lesson>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Lỗi " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Lesson>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    // Lấy bài học theo ID
    public void getLessonById(int id, LessonCallback callback) {
        apiService.getLessonById(id).enqueue(new Callback<Lesson>() {
            @Override
            public void onResponse(Call<Lesson> call, Response<Lesson> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Lỗi " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Lesson> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    // Tìm kiếm bài học theo từ khóa
    public void searchLessons(String query, LessonListCallback callback) {
        apiService.searchLessons(query).enqueue(new Callback<List<Lesson>>() {
            @Override
            public void onResponse(Call<List<Lesson>> call, Response<List<Lesson>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Lỗi " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Lesson>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    // Thêm bài học mới
    public void addLesson(Lesson lesson, LessonCallback callback) {
        apiService.addLesson(lesson).enqueue(new Callback<Lesson>() {
            @Override
            public void onResponse(Call<Lesson> call, Response<Lesson> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Lỗi " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Lesson> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    // Cập nhật bài học
    public void updateLesson(int id, Lesson lesson, LessonCallback callback) {
        apiService.updateLesson(id, lesson).enqueue(new Callback<Lesson>() {
            @Override
            public void onResponse(Call<Lesson> call, Response<Lesson> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Lỗi " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Lesson> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    // Xóa bài học
    public void deleteLesson(int id, LessonCallback callback) {
        apiService.deleteLesson(id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null); // Không trả lesson cụ thể
                } else {
                    callback.onFailure("Lỗi " + response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
}
