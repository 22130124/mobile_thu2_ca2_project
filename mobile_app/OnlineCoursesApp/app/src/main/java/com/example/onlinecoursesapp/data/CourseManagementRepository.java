package com.example.onlinecoursesapp.data;

import android.content.Context;

import com.example.onlinecoursesapp.api.ApiClient;
import com.example.onlinecoursesapp.api.CourseApiService;
import com.example.onlinecoursesapp.models.Course;
import com.example.onlinecoursesapp.utils.CourseCallback;
import com.example.onlinecoursesapp.utils.CourseListCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseManagementRepository {
    private static CourseManagementRepository instance;
    private final CourseApiService apiService;

    private CourseManagementRepository(Context context) {
        this.apiService = ApiClient.getCourseApiService();
    }

    public static CourseManagementRepository getInstance(Context context) {
        if (instance == null) {
            instance = new CourseManagementRepository(context);
        }
        return instance;
    }

    // Admin - Lấy tất cả khóa học
    public void getAllCourses(CourseListCallback callback) {
        apiService.getAllCourses().enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Lỗi " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });


    }

    // Admin - Lấy khóa học theo ID
    public void getManagementCourseById(int courseId, CourseCallback callback) {
        apiService.getManagementCourseById(courseId).enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, Response<Course> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Lỗi " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    // Admin - Thêm khóa học mới
    public void addCourse(Course course, CourseCallback callback) {
        apiService.addCourse(course).enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, Response<Course> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess((response.body()));
                } else {
                    callback.onFailure("Lỗi " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    // Admin - Cập nhật khóa học
    public void updateCourse(int courseId, Course course, CourseCallback callback) {
        apiService.updateCourse(courseId, course).enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, Response<Course> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Lỗi " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    // Admin - Xóa khóa học
    public void deleteCourse(int courseId, CourseCallback callback) {
        apiService.deleteCourse(courseId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
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

    // Admin - Lấy khóa học theo category ID
    public void getCoursesByCategoryId(int categoryId, CourseListCallback callback) {
        apiService.getCourseByCategoryId(categoryId).enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Lỗi " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
}
