package com.example.onlinecoursesapp.api;

import retrofit2.Call;
import retrofit2.http.PUT;
import retrofit2.http.Query;


// t
public interface LessonProgressApiService {

    // Thuy - Cập nhật tiến trình isCompleted = 1 (khi bấm nút Hoàn thành)
    @PUT("/progress/complete")
    Call<Void> completeLesson(
            @Query("userId") int userId,
            @Query("courseId") int courseId,
            @Query("lessonId") int lessonId
    );

}
