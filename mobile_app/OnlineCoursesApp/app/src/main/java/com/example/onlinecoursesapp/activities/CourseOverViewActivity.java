package com.example.onlinecoursesapp.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.adapter.LessonOverviewAdapter;
import com.example.onlinecoursesapp.data.CourseRepository;
import com.example.onlinecoursesapp.data.EnrollmentRepository;
import com.example.onlinecoursesapp.models.CourseOverview;
import com.example.onlinecoursesapp.utils.CourseOverviewCallback;
import com.example.onlinecoursesapp.utils.EnrollmentCallback;

// Thuy - Hien tong quan khoa hoc theo id
public class CourseOverViewActivity extends AppCompatActivity {
    private TextView title, description, numberOfLessons, totalDuration;

    private ImageView imagePath;
    private RecyclerView lessonsRecyclerView;
    private CourseRepository courseRepository;
    private boolean isEnrolled;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_overview);

        courseRepository = CourseRepository.getInstance(this);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = prefs.getString("userName", "Khách");
        int userId = prefs.getInt("userId", -1);
        int courseId = 1; // giả sử cố định là 1

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        numberOfLessons = findViewById(R.id.numberOfLessons);
        totalDuration = findViewById(R.id.totalDuration);
        imagePath = findViewById(R.id.imagePath);
        lessonsRecyclerView = findViewById(R.id.lessons);
        btnRegister = findViewById(R.id.btnRegister);


        tvWelcome.setText("Xin chào, " + username + " ! " + userId);

        getCourseById(courseId);

        btnRegister.setOnClickListener(v -> handleEnrollment(userId, courseId));
    }


    // Xử lý nút Đăng ký học
    private void handleEnrollment(int userId, int courseId) {
        if (userId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập trước khi đăng ký khóa học", Toast.LENGTH_SHORT).show();
            return;
        }

        EnrollmentRepository enrollmentRepo = EnrollmentRepository.getInstance(this);
        enrollmentRepo.enrollUser(userId, courseId, new EnrollmentCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(CourseOverViewActivity.this, "Đăng ký khóa học thành công!", Toast.LENGTH_SHORT).show();

                // Gọi lại kiểm tra user có đăng ký học
                enrollmentRepo.checkEnrollment(userId, courseId, new EnrollmentCallback() {
                    @Override
                    public void onResult(boolean enrolled) {
                        // Cập nhật lại trạng thái isEnrolled sau khi user đăng ký
                        isEnrolled = enrolled;
                        getCourseById(courseId);
                    }

                    @Override
                    public void onSuccess() {}

                    @Override
                    public void onFailure(String message) {}
                });
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(CourseOverViewActivity.this, "Đăng ký thất bại: " + message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResult(boolean isEnrolled) {}
        });
    }


    private void getCourseById(int courseId) {
        courseRepository.fetchGetCourseById(courseId, new CourseOverviewCallback.SingleCourse() {
            @Override
            public void onSuccess(CourseOverview courseOverview) {
                title.setText(courseOverview.getTitle());
                description.setText(courseOverview.getDescription());
                numberOfLessons.setText(String.valueOf(courseOverview.getNumberOfLessons()));
                totalDuration.setText(courseOverview.getTotalFormattedDuration());

                Glide.with(CourseOverViewActivity.this)
                        .load(courseOverview.getImagePath())
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(imagePath);

                EnrollmentRepository enrollmentRepo = EnrollmentRepository.getInstance(CourseOverViewActivity.this);
                SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
                int userId = prefs.getInt("userId", -1);

                enrollmentRepo.checkEnrollment(userId, courseId, new EnrollmentCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(String message) {

                    }

                    @Override
                    public void onResult(boolean enrolled) {
                        isEnrolled = enrolled;

                        btnRegister.setVisibility(enrolled ? View.GONE : View.VISIBLE);

                        LessonOverviewAdapter adapter = new LessonOverviewAdapter(courseOverview.getLessons(), isEnrolled);
                        lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(CourseOverViewActivity.this));
                        lessonsRecyclerView.setAdapter(adapter);
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                Log.e("ItemCourseActivity", "Failed to load course: " + message);
            }
        });
    }

}
