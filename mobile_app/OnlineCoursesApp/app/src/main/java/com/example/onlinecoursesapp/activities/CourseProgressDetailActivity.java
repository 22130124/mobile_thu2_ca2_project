package com.example.onlinecoursesapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.api.ApiClient;
import com.example.onlinecoursesapp.data.CourseRepository;
import com.example.onlinecoursesapp.models.course_progress.CourseProgress;
import com.example.onlinecoursesapp.utils.FormatTime;
import com.example.onlinecoursesapp.utils.ProgressCallback;

public class CourseProgressDetailActivity extends AppCompatActivity {
    private static final String EXTRA_COURSE_ID = "EXTRA_COURSE_ID";

    private CourseRepository courseRepository;
    private int courseId;
    private Toolbar toolbar;
    private ImageView imgCourse;
    private TextView tvCourseTitle;
    private TextView tvLessonCount;
    private TextView tvTimeRemaining;
    private ProgressBar progressBar;
    private TextView tvProgressPercentage;
    private TextView tvCompletedTime;
    private TextView tvTotalTime;
    private Button btnContinueLearning;

    public static Intent newIntent(Context context, int courseId) {
        Intent intent = new Intent(context, CourseProgressDetailActivity.class);
        intent.putExtra(EXTRA_COURSE_ID, courseId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_progress_detail);

        // Get course ID from intent
        courseId = getIntent().getIntExtra(EXTRA_COURSE_ID, -1);
        if (courseId == -1) {
            finish();
            return;
        }

        // Initialize repository
        courseRepository = CourseRepository.getInstance(this);

        // Initialize views
        initViews();

        // Load course progress data
        loadCourseProgressData();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chi tiết tiến độ khóa học");

        imgCourse = findViewById(R.id.img_course);
        tvCourseTitle = findViewById(R.id.tv_course_title);
        tvLessonCount = findViewById(R.id.tv_lesson_count);
        tvTimeRemaining = findViewById(R.id.tv_time_remaining);
        progressBar = findViewById(R.id.progress_bar);
        tvProgressPercentage = findViewById(R.id.tv_progress_percentage);
        tvCompletedTime = findViewById(R.id.tv_completed_time);
        tvTotalTime = findViewById(R.id.tv_total_time);

        btnContinueLearning = findViewById(R.id.btn_continue_learning);
        btnContinueLearning.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

    }

    private void loadCourseProgressData() {
        courseRepository.fetchCourseProgressById(courseId, new ProgressCallback.SingleCourse() {
            @Override
            public void onSuccess(CourseProgress progress) {
                updateUIWithProgress(progress);
            }

            @Override
            public void onFailure(String message) {
                finish(); // Đóng activity nếu có lỗi
            }
        });
    }

    private void updateUIWithProgress(CourseProgress progress) {
        tvCourseTitle.setText(progress.getCourseTitle());

        String lessonCountText = progress.getCompletedLessons() + " / " + progress.getTotalLessons() + " bài học đã hoàn thành";
        tvLessonCount.setText(lessonCountText);

        double remainingMinutes = progress.getTotalDurationMinutes() - progress.getCompletedDurationMinutes();
        if (remainingMinutes < 0) remainingMinutes = 0;
        String timeRemainingText = FormatTime.formatDuration(remainingMinutes) + " còn lại";
        tvTimeRemaining.setText(timeRemainingText);

        int percentage = Math.round(progress.getCompletionPercentage());
        progressBar.setProgress(percentage);
        String percentageText=percentage+"%";
        tvProgressPercentage.setText(percentageText);

        tvCompletedTime.setText(progress.getFormattedCompletedDuration());
        tvTotalTime.setText(progress.getFormattedTotalDuration());

        Glide.with(imgCourse.getContext())
                .load(ApiClient.getBaseUrl() + progress.getCourseImagePath())
                .placeholder(R.drawable.placeholder_image) // ảnh mặc định nếu đang tải
                .error(R.drawable.error_image) // ảnh nếu load thất bại
                .into(imgCourse);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
