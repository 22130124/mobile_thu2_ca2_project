package com.example.onlinecoursesapp.api;

import com.example.onlinecoursesapp.models.Course;
import com.example.onlinecoursesapp.models.CourseProgress;
import com.example.onlinecoursesapp.models.StatisticsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface CourseApiService {
        /**
         * Lấy tất cả khóa học từ server
         */
        @GET("/courses")
        Call<List<Course>> getAllCourses();

        /**
         * Lấy khóa học theo ID
         */
        @GET("/courses/{id}")
        Call<Course> getCourseById(@Path("id") int courseId);

        /**
         * Lấy tiến độ khóa học của người dùng
         */
        @GET("/enrollments/{userId}/progress")
        Call<List<CourseProgress>> getUserCourseProgress(@Path("userId") int userId);

        /**
         * Tìm kiếm khóa học theo từ khóa
         */
        @GET("/courses/search")
        Call<List<Course>> searchCourses(@Query("query") String query);

        /**
         * Lấy thống kê của người dùng
         */
        @GET("/enrollments/{userId}/statistics")
        Call<StatisticsResponse> getUserStatistics(@Path("userId") int userId);

        /**
         * Lấy tiến độ học tập của user cho một khóa học cụ thể
         */
        @GET("/enrollments/{userId}/progress/{courseId}")
        Call<CourseProgress> getCourseProgressByUserAndCourse(@Path("userId") int userId,@Path("courseId") int courseId);


}
