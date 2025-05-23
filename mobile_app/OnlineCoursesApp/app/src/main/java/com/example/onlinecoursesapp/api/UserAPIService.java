package com.example.onlinecoursesapp.api;

import com.example.onlinecoursesapp.models.UserProgress;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserAPIService {
    @POST("users")
    Call<UserProgress> registerUser(@Body UserProgress userProgress);
}
