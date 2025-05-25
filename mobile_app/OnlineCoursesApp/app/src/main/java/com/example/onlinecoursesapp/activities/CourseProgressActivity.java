package com.example.onlinecoursesapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.adapter.CourseProgressAdapter;
import com.example.onlinecoursesapp.data.CourseRepository;
import com.example.onlinecoursesapp.models.CourseProgress;
import com.example.onlinecoursesapp.models.StatisticsResponse;
import com.example.onlinecoursesapp.utils.FormatTime;
import com.example.onlinecoursesapp.utils.ProgressCallback;
import com.example.onlinecoursesapp.utils.StatisticsCallback;

import java.util.List;

public class CourseProgressActivity extends AppCompatActivity implements CourseProgressAdapter.OnCourseClickListener {
    private CourseRepository courseRepository;
    private TextView tvTotalLearningTime;
    private TextView tvTotalCoursesEnrolled;
    private TextView tvMostPopularCourse;
    private RecyclerView rvCourseProgress;
    private CourseProgressAdapter courseProgressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_progress);

        // Initialize repository
        courseRepository = CourseRepository.getInstance(this);

        // Initialize views
        initViews();
        System.out.println("Load xong init view");
        // Load course progress data
        loadCourseProgressData();
        System.out.println("Load xong danh sách thông tin và khoas học");
    }

    private void initViews() {
        tvTotalLearningTime = findViewById(R.id.tv_total_learning_time);
        tvTotalCoursesEnrolled = findViewById(R.id.tv_total_courses_enrolled);
        tvMostPopularCourse = findViewById(R.id.tv_completed_courses);
        rvCourseProgress = findViewById(R.id.rv_course_progress);

        // Setup RecyclerView
        rvCourseProgress.setLayoutManager(new LinearLayoutManager(this));
        courseProgressAdapter = new CourseProgressAdapter();
        courseProgressAdapter.setOnCourseClickListener(this);
        rvCourseProgress.setAdapter(courseProgressAdapter);
    }

    private void loadCourseProgressData() {
        System.out.println("Vào pt load khoá học r nè");
        CourseRepository courseRepository = CourseRepository.getInstance(this);

        // Gọi API lấy thống kê học tập
        courseRepository.fetchUserStatistics(new StatisticsCallback() {
            @Override
            public void onStatisticsLoaded(StatisticsResponse statistics) {
                runOnUiThread(() -> {
                    // Tổng thời gian học
                    double totalMinutes = statistics.getTotalLearningTimeMinutes();
                    System.out.println("Total minutes: " + totalMinutes);
                    tvTotalLearningTime.setText(FormatTime.formatDuration(totalMinutes));

                    // Tổng số khóa học đã đăng ký
                    int totalCourses = statistics.getTotalCoursesEnrolled();
                    tvTotalCoursesEnrolled.setText(String.valueOf(totalCourses));

                    // Số khóa học đã hoàn thành
                    int completedCourses = statistics.getCompletedCourses();
                    tvMostPopularCourse.setText(String.valueOf(completedCourses));
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                runOnUiThread(() -> {
                    Log.e("API_ERROR", "Statistics error: " + errorMessage);
                    Toast.makeText(CourseProgressActivity.this, "Lỗi tải thống kê", Toast.LENGTH_SHORT).show();
                });
            }
        });

        // Gọi API lấy tiến độ từng khóa học
        courseRepository.fetchCourseProgress(new ProgressCallback() {
            @Override
            public void onSuccess(List<CourseProgress> progressList) {
                runOnUiThread(() -> courseProgressAdapter.setProgressList(progressList));
            }

            @Override
            public void onFailure(String errorMessage) {
                runOnUiThread(() -> {
                    // Hiển thị thông báo lỗi nếu cần
                });
            }
        });
    }


    @Override
    public void onCourseClick(CourseProgress courseProgress) {
        Intent intent = CourseProgressDetailActivity.newIntent(this, courseProgress.getCourseId());
        startActivity(intent);
    }
}