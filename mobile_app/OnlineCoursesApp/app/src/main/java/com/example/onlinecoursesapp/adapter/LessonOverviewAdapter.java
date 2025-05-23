// com.example.onlinecoursesapp.adapter.LessonAdapter
package com.example.onlinecoursesapp.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.models.LessonOverview;

import java.util.List;

public class LessonOverviewAdapter extends RecyclerView.Adapter<LessonOverviewAdapter.LessonViewHolder> {

    private List<LessonOverview> lessonList;

    public LessonOverviewAdapter(List<LessonOverview> lessonList) {
        this.lessonList = lessonList;
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
            String videoId = extractVideoId(lesson.getYoutubeVideoUrl());

            Intent intent = new Intent(v.getContext(), VideoActivity.class);
            intent.putExtra("videoId", videoId);
            v.getContext().startActivity(intent);
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
