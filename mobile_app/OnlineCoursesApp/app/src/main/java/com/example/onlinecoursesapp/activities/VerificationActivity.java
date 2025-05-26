package com.example.onlinecoursesapp.activities;

import android.content.Intent;
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
import com.example.onlinecoursesapp.api.ApiClient;
import com.example.onlinecoursesapp.api.UserAPIService;
import com.example.onlinecoursesapp.models.GenericResponse;
import com.example.onlinecoursesapp.models.ResendCodeRequest;
import com.example.onlinecoursesapp.models.VerifyCodeRequest;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationActivity extends AppCompatActivity {

    private EditText codeEditText;
    private TextView errorTextView, resendCodeTextView;
    private Button verifyButton;

    private String userEmail; // Email người dùng (cần truyền từ màn đăng ký hoặc đăng nhập)

    private UserAPIService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        codeEditText = findViewById(R.id.codeEditText);
        errorTextView = findViewById(R.id.errorTextView);
        resendCodeTextView = findViewById(R.id.resendCodeTextView);
        verifyButton = findViewById(R.id.verifyButton);

        userEmail = getIntent().getStringExtra("email");

        apiService = ApiClient.getUserApiService();

        verifyButton.setOnClickListener(v -> {
            String code = codeEditText.getText().toString().trim();
            if (TextUtils.isEmpty(code)) {
                errorTextView.setText("Vui lòng nhập mã xác minh");
                errorTextView.setVisibility(View.VISIBLE);
                return;
            }
            errorTextView.setVisibility(View.GONE);
            verifyCode(userEmail, code);
        });

        resendCodeTextView.setOnClickListener(v -> {
            resendVerificationCode(userEmail);
        });
    }

    private void verifyCode(String email, String code) {
        Log.d("Email", email);
        Log.d("Code", code);
        verifyButton.setEnabled(false);

        VerifyCodeRequest request = new VerifyCodeRequest(email, code);

        apiService.verifyEmailCode(request).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                verifyButton.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(VerificationActivity.this, "Xác minh thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(VerificationActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    errorTextView.setText("Mã xác minh không đúng hoặc đã hết hạn");
                    errorTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                verifyButton.setEnabled(true);
                errorTextView.setText("Lỗi kết nối: " + t.getMessage());
                errorTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void resendVerificationCode(String email) {
        resendCodeTextView.setEnabled(false);

        Map<String, String> payload = new HashMap<>();
        payload.put("email", email);

        apiService.resendVerificationCode(payload).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                resendCodeTextView.setEnabled(true);
                if (response.isSuccessful()) {
                    Toast.makeText(VerificationActivity.this, "Mã xác minh đã được gửi lại", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VerificationActivity.this, "Không thể gửi lại mã", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                resendCodeTextView.setEnabled(true);
                Toast.makeText(VerificationActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

