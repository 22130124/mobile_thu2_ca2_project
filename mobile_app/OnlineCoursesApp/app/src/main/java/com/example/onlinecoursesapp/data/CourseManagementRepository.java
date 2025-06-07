package com.example.onlinecoursesapp.data;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.onlinecoursesapp.api.ApiClient;
import com.example.onlinecoursesapp.api.CourseApiService;
import com.example.onlinecoursesapp.models.Course;
import com.example.onlinecoursesapp.utils.CourseCallback;
import com.example.onlinecoursesapp.utils.CourseListCallback;
import com.example.onlinecoursesapp.utils.ImageUploadCallback;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseManagementRepository {
    private static CourseManagementRepository instance;
    private final CourseApiService apiService;
    private final Context context;

    private CourseManagementRepository(Context context) {
        this.context = context.getApplicationContext();
        this.apiService = ApiClient.getCourseApiService();
    }

    public static CourseManagementRepository getInstance(Context context) {
        if (instance == null) {
            instance = new CourseManagementRepository(context);
        }
        return instance;
    }

    // Admin - Lấy tất cả khóa học
    public void getAllCourses(CourseListCallback callback) {
        apiService.getAllCourses().enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Lỗi " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });


    }

    // Admin - Lấy khóa học theo ID
    public void getManagementCourseById(int courseId, CourseCallback callback) {
        apiService.getManagementCourseById(courseId).enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, Response<Course> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Lỗi " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void addCourse(Course course, CourseCallback callback) {
        apiService.addCourse(course).enqueue(new Callback<Course>() {
            @Override
            public void onResponse(@NonNull Call<Course> call, @NonNull Response<Course> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : null;
                        if (errorBody != null) {
                            JSONObject jsonObject = new JSONObject(errorBody);
                            String message = jsonObject.optString("message", "Failed to create course");
                            callback.onFailure(message);
                        } else {
                            callback.onFailure("Failed to create course");
                        }
                    } catch (Exception e) {
                        callback.onFailure("Error processing server response");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Course> call, @NonNull Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    public void updateCourse(int courseId, Course course, CourseCallback callback) {
        apiService.updateCourse(courseId, course).enqueue(new Callback<Course>() {
            @Override
            public void onResponse(@NonNull Call<Course> call, @NonNull Response<Course> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : null;
                        if (errorBody != null) {
                            JSONObject jsonObject = new JSONObject(errorBody);
                            String message = jsonObject.optString("message", "Failed to update course");
                            callback.onFailure(message);
                        } else {
                            callback.onFailure("Failed to update course");
                        }
                    } catch (Exception e) {
                        callback.onFailure("Error processing server response");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Course> call, @NonNull Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }
    // Admin - Xóa khóa học
    public void deleteCourse(int courseId, CourseCallback callback) {
        apiService.deleteCourse(courseId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);  // Không có body trả về
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : null;
                        if (errorBody != null) {
                            JSONObject jsonObject = new JSONObject(errorBody);
                            String message = jsonObject.optString("message", "Failed to delete course");
                            callback.onFailure(message);
                        } else {
                            callback.onFailure("Failed to delete course");
                        }
                    } catch (Exception e) {
                        callback.onFailure("Error processing server response");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    // Admin - Lấy khóa học theo category ID
    public void getCoursesByCategoryId(int categoryId, CourseListCallback callback) {
        apiService.getCourseByCategoryId(categoryId).enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Lỗi " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void uploadCourseImage(int courseId, Uri imageUri, ImageUploadCallback callback) {
        try {
            File file = getFileFromUri(imageUri);

            RequestBody requestFile = RequestBody.create(MediaType.parse(Objects.requireNonNull(context.getContentResolver().getType(imageUri))), file);

            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            System.out.println("repository "+ courseId);

            apiService.uploadCourseImage(courseId, body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String imageUrl = response.body().string();
                            System.out.println("link ảnh " +imageUrl );
                            callback.onSuccess(imageUrl);
                        } catch (IOException e) {
                            e.printStackTrace();
                            callback.onFailure("Error reading upload response");
                        }
                    } else {
                        String errorBody = null;
                        try {
                            if (response.errorBody() != null) {
                                errorBody = response.errorBody().string();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        callback.onFailure("Upload failed: " + response.code() + " " + errorBody);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    callback.onFailure("Upload error: " + t.getMessage());
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            callback.onFailure("Error processing image file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            callback.onFailure("Invalid image URI: " + e.getMessage());
        }
    }

    private File getFileFromUri(Uri uri) throws IOException {
        if (context == null) {
            throw new IllegalStateException("Context is not available in CourseRepository");
        }
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        if (inputStream == null) {
            throw new IOException("Unable to get InputStream from URI");
        }
        String fileName = "upload_" + System.currentTimeMillis();
        File tempFile = File.createTempFile(fileName, null, context.getCacheDir());
        tempFile.deleteOnExit();

        FileOutputStream outputStream = new FileOutputStream(tempFile);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.close();
        inputStream.close();

        return tempFile;
    }



}
