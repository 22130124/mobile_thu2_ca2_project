package com.example.onlinecoursesapp.data;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.onlinecoursesapp.api.UserAPIService;
import com.example.onlinecoursesapp.models.ChangePasswordRequest;
import com.example.onlinecoursesapp.models.GenericResponse;
import com.example.onlinecoursesapp.models.LoginRequest;
import com.example.onlinecoursesapp.models.LoginResponse;
import com.example.onlinecoursesapp.models.RegisterResponse;
import com.example.onlinecoursesapp.models.UserProgress;
import com.example.onlinecoursesapp.api.ApiClient;
import com.example.onlinecoursesapp.utils.ChangePasswordCallback;
import com.example.onlinecoursesapp.utils.ImageUploadCallback;
import com.example.onlinecoursesapp.utils.LoginCallback;
import com.example.onlinecoursesapp.utils.ProfileUpdateCallback;
import com.example.onlinecoursesapp.utils.RegisterCallback;
import com.example.onlinecoursesapp.utils.UserCallback;
import com.example.onlinecoursesapp.utils.UserListCallback;

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

public class UserRepository {
    private static UserRepository instance;
    private final UserAPIService apiService;
    private final Context context;
    private UserRepository(Context context) {
        this.context = context.getApplicationContext();
        apiService = ApiClient.getUserApiService();
    }

    public static UserRepository getInstance(Context context) {
        if (instance == null) {
            instance = new UserRepository(context);
        }
        return instance;
    }

    public void registerUser(UserProgress userProgress, RegisterCallback callback) {
        apiService.registerUser(userProgress).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserProgress user = response.body().getUser();
                    callback.onSuccess(user);
                } else {
                    try {
                        // Đọc lỗi trả về từ server (ví dụ: "Email đã được sử dụng" hoặc "Không thể gửi email xác minh")
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : null;

                        if (errorBody != null) {
                            JSONObject jsonObject = new JSONObject(errorBody);
                            String message = jsonObject.optString("message", "Đăng ký thất bại không rõ lý do");
                            callback.onFailure(message);
                        } else {
                            callback.onFailure("Lỗi không rõ từ máy chủ");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure("Lỗi xử lý phản hồi từ máy chủ");
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                callback.onFailure("Lỗi kết nối mạng: " + t.getMessage());
            }
        });
    }

    public void loginUser(String email, String password, LoginCallback callback) {
        LoginRequest request = new LoginRequest(email, password);

        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getUser());
                } else {
                    try {
                        // Đọc thông báo lỗi từ JSON trả về
                        String errorJson = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorJson);
                        String errorMessage = jsonObject.optString("message", "Đăng nhập thất bại");

                        callback.onFailure(errorMessage);
                    } catch (Exception e) {
                        callback.onFailure("Lỗi phản hồi từ server");
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onFailure("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void changePassword(int userId, String oldPassword, String newPassword, ChangePasswordCallback callback) {
        ChangePasswordRequest request = new ChangePasswordRequest(oldPassword, newPassword);
        apiService.changePassword(userId, request).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getMessage());
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : null;
                        if (errorBody != null) {
                            JSONObject json = new JSONObject(errorBody);
                            callback.onFailure(json.optString("message", "Lỗi đổi mật khẩu"));
                        } else {
                            callback.onFailure("Lỗi không xác định");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure("Lỗi xử lý phản hồi");
                    }
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                callback.onFailure("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void uploadUserImage(int userId, Uri imageUri, ImageUploadCallback callback) {
        try {
            File file = getFileFromUri(imageUri);

            RequestBody requestFile = RequestBody.create(MediaType.parse(Objects.requireNonNull(context.getContentResolver().getType(imageUri))), file);

            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

            apiService.uploadUserImage(userId, body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String imageUrl = response.body().string();
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

    public void updateUserProfileData(int userId, UserProgress updatedUser, ProfileUpdateCallback callback) {
        apiService.updateUserProfile(userId, updatedUser).enqueue(new Callback<UserProgress>() {
            @Override
            public void onResponse(@NonNull Call<UserProgress> call, @NonNull Response<UserProgress> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    String errorBody = null;
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    callback.onFailure("Profile update failed: " + response.code() + " " + errorBody);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserProgress> call, @NonNull Throwable t) {
                callback.onFailure("Profile update error: " + t.getMessage());
            }
        });
    }

    private File getFileFromUri(Uri uri) throws IOException {
        if (context == null) {
            throw new IllegalStateException("Context is not available in UserRepository");
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

    // Method Quan ly user trong dashboard
    public void getAllUsers(UserListCallback callback) {
        apiService.getAllUsers().enqueue(new Callback<List<UserProgress>>() {
            @Override
            public void onResponse(@NonNull Call<List<UserProgress>> call, @NonNull Response<List<UserProgress>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : null;
                        if (errorBody != null) {
                            JSONObject jsonObject = new JSONObject(errorBody);
                            String message = jsonObject.optString("message", "Failed to load users");
                            callback.onFailure(message);
                        } else {
                            callback.onFailure("Failed to load users");
                        }
                    } catch (Exception e) {
                        callback.onFailure("Error processing server response");
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<UserProgress>> call, @NonNull Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    public void getUserById(int userId, UserCallback callback) {
        apiService.getUserById(userId).enqueue(new Callback<UserProgress>() {
            @Override
            public void onResponse(@NonNull Call<UserProgress> call, @NonNull Response<UserProgress> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : null;
                        if (errorBody != null) {
                            JSONObject jsonObject = new JSONObject(errorBody);
                            String message = jsonObject.optString("message", "Failed to get user details");
                            callback.onFailure(message);
                        } else {
                            callback.onFailure("Failed to get user details");
                        }
                    } catch (Exception e) {
                        callback.onFailure("Error processing server response");
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<UserProgress> call, @NonNull Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    public void addUser(UserProgress user, UserCallback callback) {
        apiService.addUser(user).enqueue(new Callback<UserProgress>() {
            @Override
            public void onResponse(@NonNull Call<UserProgress> call, @NonNull Response<UserProgress> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : null;
                        if (errorBody != null) {
                            JSONObject jsonObject = new JSONObject(errorBody);
                            String message = jsonObject.optString("message", "Failed to create user");
                            callback.onFailure(message);
                        } else {
                            callback.onFailure("Failed to create user");
                        }
                    } catch (Exception e) {
                        callback.onFailure("Error processing server response");
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<UserProgress> call, @NonNull Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    public void updateUser(int userId, UserProgress user, UserCallback callback) {
        apiService.updateUser(userId, user).enqueue(new Callback<UserProgress>() {
            @Override
            public void onResponse(@NonNull Call<UserProgress> call, @NonNull Response<UserProgress> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : null;
                        if (errorBody != null) {
                            JSONObject jsonObject = new JSONObject(errorBody);
                            String message = jsonObject.optString("message", "Failed to update user");
                            callback.onFailure(message);
                        } else {
                            callback.onFailure("Failed to update user");
                        }
                    } catch (Exception e) {
                        callback.onFailure("Error processing server response");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserProgress> call, @NonNull Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    public void updateUserStatus(int userId, boolean active, UserCallback callback) {
        apiService.updateUserStatus(userId, active).enqueue(new Callback<UserProgress>() {
            @Override
            public void onResponse(@NonNull Call<UserProgress> call, @NonNull Response<UserProgress> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : null;
                        if (errorBody != null) {
                            JSONObject jsonObject = new JSONObject(errorBody);
                            String message = jsonObject.optString("message", "Failed to update user status");
                            callback.onFailure(message);
                        } else {
                            callback.onFailure("Failed to update user status");
                        }
                    } catch (Exception e) {
                        callback.onFailure("Error processing server response");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserProgress> call, @NonNull Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    public void searchUsers(String query, UserListCallback callback) {
        apiService.searchUsers(query).enqueue(new Callback<List<UserProgress>>() {
            @Override
            public void onResponse(@NonNull Call<List<UserProgress>> call, @NonNull Response<List<UserProgress>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : null;
                        if (errorBody != null) {
                            JSONObject jsonObject = new JSONObject(errorBody);
                            String message = jsonObject.optString("message", "Failed to search users");
                            callback.onFailure(message);
                        } else {
                            callback.onFailure("Failed to search users");
                        }
                    } catch (Exception e) {
                        callback.onFailure("Error processing server response");
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<UserProgress>> call, @NonNull Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    public void filterUsersByStatus(boolean active, UserListCallback callback) {
        apiService.filterUsersByStatus(active).enqueue(new Callback<List<UserProgress>>() {
            @Override
            public void onResponse(@NonNull Call<List<UserProgress>> call, @NonNull Response<List<UserProgress>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    System.out.println("Kết quả cho lọc: ");
                    System.out.println(response.body());
                    callback.onSuccess(response.body());
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : null;
                        if (errorBody != null) {
                            JSONObject jsonObject = new JSONObject(errorBody);
                            String message = jsonObject.optString("message", "Failed to filter users");
                            callback.onFailure(message);
                        } else {
                            callback.onFailure("Failed to filter users");
                        }
                    } catch (Exception e) {
                        callback.onFailure("Error processing server response");
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<UserProgress>> call, @NonNull Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }
}
