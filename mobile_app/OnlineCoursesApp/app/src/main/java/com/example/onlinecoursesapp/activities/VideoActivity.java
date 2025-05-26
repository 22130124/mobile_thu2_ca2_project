package com.example.onlinecoursesapp.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.api.ApiClient;
import com.example.onlinecoursesapp.api.LessonProgressApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Thuy - Tich Hop API Youtube
public class VideoActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar loadingIndicator;
    private Button btnComplete;
    private int userId, lessonId;
    int courseId = 1; // gia su

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        webView = findViewById(R.id.youtubeWebView);
        loadingIndicator = findViewById(R.id.loadingIndicator);
        btnComplete = findViewById(R.id.btnComplete);

        // Lấy videoId và thông tin người dùng
        String videoId = getIntent().getStringExtra("videoId");
        lessonId = getIntent().getIntExtra("lessonId", -1);

        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);

        if (videoId == null || userId == -1 || courseId == -1 || lessonId == -1) {
            Toast.makeText(this, "Thiếu thông tin cần thiết", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                loadingIndicator.setVisibility(View.GONE);
            }
        });

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        String html = "<html><body style='margin:0'><iframe width='100%' height='100%' src='https://www.youtube.com/embed/" +
                videoId + "?autoplay=1&enablejsapi=1' frameborder='0' allowfullscreen></iframe></body></html>";
        webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);

        // Xử lý nút hoàn thành bài học
        btnComplete.setOnClickListener(v -> markLessonAsCompleted());
    }

    private void markLessonAsCompleted() {
        LessonProgressApiService apiService = ApiClient.getLessonProgressApiService();

        apiService.completeLesson(userId, courseId, lessonId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(VideoActivity.this, "🎉 Bài học đã được đánh dấu hoàn thành!", Toast.LENGTH_SHORT).show();
                    btnComplete.setEnabled(false);
                    btnComplete.setText("Đã hoàn thành");
                } else {
                    Toast.makeText(VideoActivity.this, "❌ Không thể cập nhật tiến trình", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(VideoActivity.this, "⚠ Lỗi kết nối đến server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
