package com.example.onlinecoursesapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.activities.dashboard.DashboardActivity;
import com.example.onlinecoursesapp.api.ApiClient;
import com.example.onlinecoursesapp.data.UserRepository;
import com.example.onlinecoursesapp.models.GenericResponse;
import com.example.onlinecoursesapp.models.UserProgress;
import com.example.onlinecoursesapp.utils.LoginCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    UserRepository userRepository;
    private TextView titleTextView, forgotPasswordTextView, errorTextView;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signupButton;
    private Button loginButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userRepository = UserRepository.getInstance(this);

        // Ánh xạ các View từ layout XML
        titleTextView = findViewById(R.id.titleTextView);
        errorTextView = findViewById(R.id.errorTextView);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);
        signupButton = findViewById(R.id.signupButton);
        loginButton = findViewById(R.id.loginButton);

        forgotPasswordTextView.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                errorTextView.setText("Vui lòng nhập email để đặt lại mật khẩu");
                errorTextView.setVisibility(View.VISIBLE);
                return;
            }

            errorTextView.setVisibility(View.GONE);

            Map<String, String> payload = new HashMap<>();
            payload.put("email", email);
            payload.put("isPasswordReset", "true");

            // Gửi request đến server để gửi mã xác minh
            ApiClient.getUserApiService().resendVerificationCode(payload).enqueue(new Callback<GenericResponse>() {
                @Override
                public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                    if (response.isSuccessful()) {
                        // Chuyển sang màn hình xác minh
                        Intent intent = new Intent(LoginActivity.this, VerificationActivity.class);
                        intent.putExtra("email", email);
                        intent.putExtra("isPasswordReset", true);
                        startActivity(intent);
                    } else {
                        errorTextView.setText("Không thể gửi mã xác minh. Email không hợp lệ.");
                        try {
                            Log.e("API_ERROR", response.errorBody().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        errorTextView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<GenericResponse> call, Throwable t) {
                    errorTextView.setText("Lỗi kết nối: " + t.getMessage());
                    errorTextView.setVisibility(View.VISIBLE);
                }
            });
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    errorTextView.setText("Vui lòng điền đầy đủ thông tin");
                    errorTextView.setVisibility(View.VISIBLE);
                    return;
                }

                userRepository.loginUser(email, password, new LoginCallback() {
                    @Override
                    public void onSuccess(UserProgress user) {
                        // Lưu thông tin user vào SharedPreferences
                        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("userId", user.getId());
                        editor.putString("userName", user.getName());
                        editor.putString("userEmail", user.getEmail());
                        editor.putString("userRole", user.getRole());
                        editor.putBoolean("isActive", user.isActive());
                        editor.putString("img",user.getImg());

                        editor.apply();

                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                        Intent intent;
                        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                            intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        } else {
                            // Chuyển sang màn hình user thường
                            intent = new Intent(LoginActivity.this, MainActivity.class);
                        }
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(String message) {
                        errorTextView.setText(message);
                        errorTextView.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        signupButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}