// com.example.onlinecoursesapp.adapter.LessonAdapter
package com.example.onlinecoursesapp.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.fragments.VideoFragment;
import com.example.onlinecoursesapp.models.LessonOverview;

import java.util.List;

// Thuy - Hien danh sach bai tuong ung voi khoa hoc
public class LessonOverviewAdapter extends RecyclerView.Adapter<LessonOverviewAdapter.LessonViewHolder> {

    private List<LessonOverview> lessonList;
    private boolean isEnrolled;
    private int courseId;
    public LessonOverviewAdapter(List<LessonOverview> lessonList, boolean isEnrolled, int courseId) {
        this.lessonList = lessonList;
        this.isEnrolled = isEnrolled;
        this.courseId = courseId;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        LessonOverview lesson = lessonList.get(position);
        holder.lessonTitle.setText(lesson.getTitle());
        holder.lessonDuration.setText(lesson.getFormattedDuration());

        holder.itemView.setOnClickListener(v -> {
            if (!isEnrolled) {
                Toast.makeText(v.getContext(), "Bạn cần đăng ký khóa học để xem bài học!", Toast.LENGTH_SHORT).show();
                return;
            }

            String videoId = extractVideoId(lesson.getYoutubeVideoUrl());
            VideoFragment videoFragment = VideoFragment.newInstance(videoId, lesson.getId(), courseId);

            FragmentActivity activity = (FragmentActivity) v.getContext();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, videoFragment) // Đảm bảo bạn có FrameLayout với id này
                    .addToBackStack(null)
                    .commit();
        });

    }

    @Override
    public int getItemCount() {
        return lessonList != null ? lessonList.size() : 0;
    }

    public static class LessonViewHolder extends RecyclerView.ViewHolder {
        TextView lessonTitle, lessonDuration;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            lessonTitle = itemView.findViewById(R.id.lessonTitle);
            lessonDuration = itemView.findViewById(R.id.lessonDuration);
        }
    }

    private static String extractVideoId(String youtubeUrl) {
        if (youtubeUrl == null || !youtubeUrl.contains("v=")) return "";
        Uri uri = Uri.parse(youtubeUrl);
        return uri.getQueryParameter("v");
    }
}
