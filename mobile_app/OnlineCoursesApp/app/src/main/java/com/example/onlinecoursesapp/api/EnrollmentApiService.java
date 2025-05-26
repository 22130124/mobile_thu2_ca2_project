package com.example.onlinecoursesapp.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EnrollmentApiService {

    // Thuy - Ghi danh người dùng (khi bấm nút Đăng ký học trong trang Tổng quan khóa học)
    @POST("/enrollments/enrollUser")
    Call<Void> enrollUser(
            @Query("userId") int userId,
            @Query("courseId") int courseId
    );

    // Thuy - Kiem tra user đã đăng ký học hay chưa
    @GET("/enrollments/checkEnrollment")
    Call<Boolean> checkEnrollment(
            @Query("userId") int userId,
            @Query("courseId") int courseId
    );
}
