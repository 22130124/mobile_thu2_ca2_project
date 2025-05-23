package com.example.onlinecoursesapp.api;

import com.example.onlinecoursesapp.models.GenericResponse;
import com.example.onlinecoursesapp.models.LoginRequest;
import com.example.onlinecoursesapp.models.LoginResponse;
import com.example.onlinecoursesapp.models.RegisterResponse;
import com.example.onlinecoursesapp.models.ResendCodeRequest;
import com.example.onlinecoursesapp.models.UserProgress;
import com.example.onlinecoursesapp.models.VerifyCodeRequest;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserAPIService {
    @POST("users/signup")
    Call<RegisterResponse> registerUser(@Body UserProgress userProgress);

    @POST("/users/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("users/verify")
    Call<GenericResponse> verifyEmailCode(@Body VerifyCodeRequest request);

    @POST("users/resend")
    Call<GenericResponse> resendVerificationCode(@Body Map<String, String> emailPayload);
}
