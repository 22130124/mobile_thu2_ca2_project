package com.example.onlinecoursesapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.data.UserRepository;
import com.example.onlinecoursesapp.models.UserProgress;
import com.example.onlinecoursesapp.utils.RegisterCallback;

public class SignUpActivity extends AppCompatActivity {
    UserRepository userRepository;
    private ImageView imgView;
    private TextView titleTextView, errorTextView;
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signupButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userRepository = UserRepository.getInstance(this);

        // Ánh xạ các View từ layout XML
        imgView = findViewById(R.id.imgView);
        titleTextView = findViewById(R.id.titleTextView);
        errorTextView = findViewById(R.id.errorTextView);
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signupButton = findViewById(R.id.signupButton);
        loginButton = findViewById(R.id.loginButton);

        // Xử lý sự kiện nút đăng ký
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Kiểm tra dữ liệu đầu vào
                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    errorTextView.setText("Vui lòng điền đầy đủ thông tin");
                    errorTextView.setVisibility(View.VISIBLE);
                    return;
                }

                if(!isValidEmail(email)){
                    errorTextView.setText("Email không hợp lệ");
                    errorTextView.setVisibility(View.VISIBLE);
                    return;
                }

                // Tạo đối tượng UserProgress (hoặc một lớp DTO khác cho request đăng ký)
                UserProgress newUser = new UserProgress(0, name, email, password, "user", true,"");

                newUser.setPassword(password);

                // Gửi dữ liệu đăng ký đến Backend API
                registerUser(newUser);
            }
        });

        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void registerUser(UserProgress newUser) {
        userRepository.registerUser(newUser, new RegisterCallback() {
            @Override
            public void onSuccess(UserProgress user) {
                Toast.makeText(SignUpActivity.this, "Đăng ký thành công! Vui lòng kiểm tra email để xác minh.", Toast.LENGTH_SHORT).show();
                errorTextView.setVisibility(View.GONE);

                // Chuyển sang VerificationActivity và truyền email
                Intent intent = new Intent(SignUpActivity.this, VerificationActivity.class);
                Log.d("UserEmail", user.getEmail() != null ? user.getEmail() : "email is null");
                intent.putExtra("email", user.getEmail());
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(String message) {
                errorTextView.setText(message);
                errorTextView.setVisibility(View.VISIBLE);
                System.out.println(message);
            }
        });
    }

    public boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email != null && email.matches(regex);
    }
}
