package com.example.onlinecoursesapp.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.api.ApiClient;
import com.example.onlinecoursesapp.api.LessonProgressApiService;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Thuy - Tich hop API Youtube
public class VideoActivity extends AppCompatActivity {
    private YouTubePlayerView youtubePlayerView;
    private ProgressBar loadingIndicator;
    private Button btnComplete;
    private int userId, lessonId, courseId;

    private String videoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        youtubePlayerView = findViewById(R.id.youtubePlayerView);
        loadingIndicator = findViewById(R.id.loadingIndicator);
        btnComplete = findViewById(R.id.btnComplete);

        getLifecycle().addObserver(youtubePlayerView);

        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);

        videoId = getIntent().getStringExtra("videoId");
        lessonId = getIntent().getIntExtra("lessonId", -1);
        courseId = getIntent().getIntExtra("courseId", -1);

        if (videoId == null || userId == -1 || courseId == -1 || lessonId == -1) {
            Toast.makeText(this, "Thiếu thông tin cần thiết", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupYouTubePlayer(videoId);
        checkIfLessonCompleted();
        btnComplete.setOnClickListener(v -> markLessonAsCompleted());
    }

    private void setupYouTubePlayer(String videoId) {
        loadingIndicator.setVisibility(View.VISIBLE);

        youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                loadingIndicator.setVisibility(View.GONE);
                youTubePlayer.loadVideo(videoId, 0);
            }
        });
    }

    private void checkIfLessonCompleted() {
        LessonProgressApiService apiService = ApiClient.getLessonProgressApiService();
        apiService.checkLessonCompletion(userId, courseId, lessonId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null && response.body()) {
                    btnComplete.setEnabled(false);
                    btnComplete.setText("Đã hoàn thành");
                } else {
                    btnComplete.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(VideoActivity.this, "Không thể kiểm tra tiến trình", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Xử lý nút Hoàn thành bài học
    private void markLessonAsCompleted() {
        LessonProgressApiService apiService = ApiClient.getLessonProgressApiService();
        apiService.completeLesson(userId, courseId, lessonId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(VideoActivity.this, "Đã hoàn thành bài học", Toast.LENGTH_SHORT).show();
                    btnComplete.setEnabled(false);
                    btnComplete.setText("Đã hoàn thành");
                } else {
                    Toast.makeText(VideoActivity.this, "Không thể cập nhật tiến trình", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(VideoActivity.this, "Lỗi kết nối đến server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
