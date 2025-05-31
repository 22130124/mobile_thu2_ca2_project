package com.example.onlinecoursesapp.api;

import com.example.onlinecoursesapp.models.Category;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CategoryApiService {
    // Lay tat ca category - Huong
    @GET("/categories")
    Call<List<Category>> getAllCategories();

    // Lay category theo id - Huong
    @GET("/categories/{id}")
    Call<Category> getCategoryById(@Path("id") int id);

    // Them category- Huong
    @POST("/categories")
    Call<Category> createCategory(@Body Category category);

    // Cap nhat category - Huong
    @PUT("/categories/{id}")
    Call<Category> updateCategory(@Path("id") int id, @Body Category updateCategory);

    // Xoa category - Huong
    @DELETE("/categories/{id}")
    Call<Void> deleteCategory(@Path("id") int id);

}



