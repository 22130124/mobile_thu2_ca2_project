package com.example.onlinecoursesapp.api;

import com.example.onlinecoursesapp.models.Course;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIService {
    @GET("/courses")
    Call<List<Course>> getCourses();




}
