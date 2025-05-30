package com.example.onlinecoursesapp.data;

import android.content.Context;

import com.example.onlinecoursesapp.api.ApiClient;
import com.example.onlinecoursesapp.api.CategoryApiService;
import com.example.onlinecoursesapp.models.Category;
import com.example.onlinecoursesapp.utils.CategoryCallback;
import com.example.onlinecoursesapp.utils.CategoryListCallback;

import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoryRepository {
    private static CategoryRepository instance;
    private final CategoryApiService apiService;

    CategoryRepository(Context context) {
        this.apiService = ApiClient.getCategoryApiService();
    }

    public static CategoryRepository getInstance(Context context) {
        if (instance == null) {
            instance = new CategoryRepository(context);
        }
        return instance;
    }

    // Lấy tất cả category
    public void getAllCategories(CategoryListCallback callback) {
        apiService.getAllCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Lỗi " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    // Lấy category theo id
    public void getCategoryById(int id, CategoryCallback callback) {
        apiService.getCategoryById(id).enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Lỗi " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    // Tạo category mới
    public void createCategory(Category category, CategoryCallback callback) {
        apiService.createCategory(category).enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Lỗi " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    // Cập nhật category
    public void updateCategory(int id, Category updateCategory, CategoryCallback callback) {
        apiService.updateCategory(id, updateCategory).enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Lỗi " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    // Xóa category
    public void deleteCategory(int id, CategoryCallback callback) {
        apiService.deleteCategory(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onFailure("Lỗi " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
}




