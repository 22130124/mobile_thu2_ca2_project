package com.example.onlinecoursesapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.activities.course_progress.CourseProgressActivity;
import com.example.onlinecoursesapp.data.UserRepository;

public class ProfileActivity extends AppCompatActivity {
    UserRepository userRepository;
    private ImageView imgView;
    private TextView nameText, emailText;
    private Button myCoursesButton, logoutButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Ánh xạ các View từ layout XML
        imgView = findViewById(R.id.imgView);
        nameText = findViewById(R.id.nameText);
        emailText = findViewById(R.id.emailText);
        myCoursesButton = findViewById(R.id.myCoursesButton);
        logoutButton = findViewById(R.id.logoutButton);

        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String name = sharedPref.getString("userName", "Không rõ");
        String email = sharedPref.getString("userEmail", "Không có email");

        nameText.setText(name);
        emailText.setText(email);

        myCoursesButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, CourseProgressActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> {
            sharedPref.edit().clear().apply();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
