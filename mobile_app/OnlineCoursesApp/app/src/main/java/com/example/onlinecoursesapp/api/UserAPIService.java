package com.example.onlinecoursesapp.api;

import com.example.onlinecoursesapp.models.ChangePasswordRequest;
import com.example.onlinecoursesapp.models.GenericResponse;
import com.example.onlinecoursesapp.models.LoginRequest;
import com.example.onlinecoursesapp.models.LoginResponse;
import com.example.onlinecoursesapp.models.RegisterResponse;
import com.example.onlinecoursesapp.models.ResetPasswordRequest;
import com.example.onlinecoursesapp.models.UserProgress;
import com.example.onlinecoursesapp.models.VerifyCodeRequest;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserAPIService {
    @POST("users/signup")
    Call<RegisterResponse> registerUser(@Body UserProgress userProgress);

    @POST("/users/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("users/verify")
    Call<GenericResponse> verifyEmailCode(@Body VerifyCodeRequest request);

    @POST("users/resend")
    Call<GenericResponse> resendVerificationCode(@Body Map<String, String> emailPayload);

    @PUT("users/{id}/change-password")
    Call<GenericResponse> changePassword(@Path("id") int userId, @Body ChangePasswordRequest request);

    @Multipart
    @POST("users/{id}/upload-image")
    Call<ResponseBody> uploadUserImage(@Path("id") int userId, @Part MultipartBody.Part file);

    @GET("users/{id}")
    Call<UserProgress> getUserProfile(@Path("id") int userId);

    @PUT("users/{id}")
    Call<UserProgress> updateUserProfile(@Path("id") int userId, @Body UserProgress user);

    //Quản ly user
    @GET("users")
    Call<List<UserProgress>> getAllUsers();

    @GET("users/byId{id}")
    Call<UserProgress> getUserById(@Path("id") int id);

    @POST("users/addUser")
    Call<UserProgress> addUser(@Body UserProgress user);

    @PUT("users/updateId{id}")
    Call<UserProgress> updateUser(@Path("id") int id, @Body UserProgress user);

    @PUT("users/updateId{id}/status")
    Call<UserProgress> updateUserStatus(@Path("id") int id, @Query("is_active") boolean active);

    @GET("users/search")
    Call<List<UserProgress>> searchUsers(@Query("query") String query);

    @GET("users/filter")
    Call<List<UserProgress>> filterUsersByStatus(@Query("is_active") boolean active);

    @PUT("users/reset-password")
    Call<GenericResponse> resetPassword(@Body ResetPasswordRequest request);
}
