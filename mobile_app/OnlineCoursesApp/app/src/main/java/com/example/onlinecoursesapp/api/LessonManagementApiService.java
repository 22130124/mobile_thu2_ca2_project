package com.example.onlinecoursesapp.api;


import com.example.onlinecoursesapp.models.Lesson;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LessonManagementApiService {
    // Lấy danh sách bài học theo ID khóa học
    @GET("/management/lesson/course/{courseId}")
    Call<List<Lesson>> getLessonsByCourseId(@Path("courseId") int courseId);

    // Lấy bài học theo ID
    @GET("/management/lesson/{id}")
    Call<Lesson> getLessonById(@Path("id") int id);

    // Tìm kiếm bài học theo từ khóa
    @GET("/management/lesson/search")
    Call<List<Lesson>> searchLessons(@Query("query") String query);

    // Cập nhật bài học
    @PUT("/management/lesson/{id}")
    Call<Lesson> updateLesson(@Path("id") int id, @Body Lesson lesson);

    // Xóa bài học
    @DELETE("/management/lesson/{id}")
    Call<String> deleteLesson(@Path("id") int id);

    // Thêm bài học mới
    @POST("/management/lesson")
    Call<Lesson> addLesson(@Body Lesson lesson);

}
