package com.example.onlinecoursesapp.api;

import com.example.onlinecoursesapp.models.LoginRequest;
import com.example.onlinecoursesapp.models.LoginResponse;
import com.example.onlinecoursesapp.models.UserProgress;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserAPIService {
    @POST("users/signup")
    Call<UserProgress> registerUser(@Body UserProgress userProgress);

    @POST("/users/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}
