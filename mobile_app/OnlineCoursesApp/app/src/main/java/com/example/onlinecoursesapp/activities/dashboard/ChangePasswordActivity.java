package com.example.onlinecoursesapp.activities.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.activities.LoginActivity;
import com.example.onlinecoursesapp.data.UserRepository;
import com.example.onlinecoursesapp.fragments.admin.SettingsFragment;
import com.example.onlinecoursesapp.utils.ChangePasswordCallback;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText newPassword, confirmPassword;
    private Button btnChangePassword;
    private UserRepository userRepository;
    private int userId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        userRepository = UserRepository.getInstance(this);

        Intent intent = getIntent();
        boolean isReset = intent.getBooleanExtra("isReset", false);
        String email = intent.getStringExtra("email");

        Log.d("Email", email);

        btnChangePassword.setOnClickListener(v -> {
            String newPass = newPassword.getText().toString().trim();
            String confirmPass = confirmPassword.getText().toString().trim();

            if (newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPass.equals(confirmPass)) {
                Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            userRepository.resetPassword(email, newPass, new ChangePasswordCallback() {
                @Override
                public void onSuccess(String message) {
                    runOnUiThread(() -> {
                        Toast.makeText(ChangePasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    });
                }

                @Override
                public void onFailure(String error) {
                    runOnUiThread(() -> {
                        Toast.makeText(ChangePasswordActivity.this, error, Toast.LENGTH_SHORT).show();
                    });
                }
            });
        });
    }
}
