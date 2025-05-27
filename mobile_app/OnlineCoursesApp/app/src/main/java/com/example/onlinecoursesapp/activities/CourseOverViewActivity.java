package com.example.onlinecoursesapp.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

        //        t
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String username = prefs.getString("username", "Khách");
        //   int userId = prefs.getInt("userId", -1);
        int userId = 4;

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        tvWelcome.setText("Xin chào, " + username + "!" + userId);
        //        t

        courseRepository = CourseRepository.getInstance(this);

        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        numberOfLessons = findViewById(R.id.numberOfLessons);
        totalDuration = findViewById(R.id.totalDuration);
        imagePath = findViewById(R.id.imagePath);
        lessonsRecyclerView = findViewById(R.id.lessons);
        btnRegister = findViewById(R.id.btnRegister);

        getCourseById(1);
        int courseId = 1;
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
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(CourseOverViewActivity.this, "Đăng ký thất bại: " + message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResult(boolean isEnrolled) {

            }
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
