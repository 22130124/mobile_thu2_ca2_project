package com.example.onlinecoursesapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.activities.dashboard.DashboardActivity;
import com.example.onlinecoursesapp.data.UserRepository;
import com.example.onlinecoursesapp.fragments.SettingsFragment;
import com.example.onlinecoursesapp.models.UserProgress;
import com.example.onlinecoursesapp.utils.LoginCallback;

public class LoginActivity extends AppCompatActivity {
    UserRepository userRepository;
    private TextView titleTextView, errorTextView;
    private EditText nameEditText;
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
        signupButton = findViewById(R.id.signupButton);
        loginButton = findViewById(R.id.loginButton);

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

                        // Chuyển sang MainActivity hoặc màn hình chính
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
        });
    }
}
