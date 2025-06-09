package com.example.onlinecoursesapp.fragments.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.api.ApiClient;
import com.example.onlinecoursesapp.api.LessonProgressApiService;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoFragment extends Fragment {
    private YouTubePlayerView youtubePlayerView;
    private ProgressBar loadingIndicator;
    private Button btnComplete;
    private int userId, lessonId, courseId;
    private String videoId;
    public static VideoFragment newInstance(String videoId, int lessonId, int courseId) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putString("videoId", videoId);
        args.putInt("lessonId", lessonId);
        args.putInt("courseId", courseId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        youtubePlayerView = view.findViewById(R.id.youtubePlayerView);
        loadingIndicator = view.findViewById(R.id.loadingIndicator);
        btnComplete = view.findViewById(R.id.btnComplete);

        getLifecycle().addObserver(youtubePlayerView);

        SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserPrefs", 0);
        userId = sharedPref.getInt("userId", -1);

        if (getArguments() != null) {
            videoId = getArguments().getString("videoId");
            lessonId = getArguments().getInt("lessonId", -1);
            courseId = getArguments().getInt("courseId", -1);
        }

        if (videoId == null || userId == -1 || courseId == -1 || lessonId == -1) {
            Toast.makeText(getContext(), "Thiếu thông tin cần thiết", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
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
                if (response.isSuccessful() && Boolean.TRUE.equals(response.body())) {
                    btnComplete.setEnabled(false);
                    btnComplete.setText("Đã hoàn thành");
                } else {
                    btnComplete.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getContext(), "Không thể kiểm tra tiến trình", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void markLessonAsCompleted() {
        LessonProgressApiService apiService = ApiClient.getLessonProgressApiService();
        apiService.completeLesson(userId, courseId, lessonId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Đã hoàn thành bài học", Toast.LENGTH_SHORT).show();
                    btnComplete.setEnabled(false);
                    btnComplete.setText("Đã hoàn thành");
                } else {
                    Toast.makeText(getContext(), "Không thể cập nhật tiến trình", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối đến server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
