package com.example.onlinecoursesapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.models.Lesson;

import java.util.List;

public class LessonManagementAdapter extends RecyclerView.Adapter<LessonManagementAdapter.ViewHolder> {

    private final Context context;
    private final List<Lesson> lessonList;
    private final OnLessonActionListener listener;
    public interface OnLessonActionListener {
        void onEdit(Lesson lesson);
        void onDelete(Lesson lesson);
    }
    public LessonManagementAdapter(Context context, List<Lesson> lessonList, OnLessonActionListener listener) {
        this.context = context;
        this.lessonList = lessonList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LessonManagementAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lesson_management, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonManagementAdapter.ViewHolder holder, int position) {
        Lesson lesson = lessonList.get(position);

        holder.tvTitle.setText(lesson.getTitle());
        holder.tvContent.setText(lesson.getContent());
        holder.tvYoutubeUrl.setText("Link video: " + lesson.getYoutubeVideoUrl());
        holder.tvDuration.setText("Thời lượng: " + lesson.getDurationMinutes() + " phút");

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(lesson));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(lesson));
    }

    @Override
    public int getItemCount() {
        return lessonList != null ? lessonList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvContent, tvYoutubeUrl, tvDuration, tvCreatedAt;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvLessonTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvYoutubeUrl = itemView.findViewById(R.id.tvYoutubeUrl);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            btnEdit = itemView.findViewById(R.id.iconEdit);
            btnDelete = itemView.findViewById(R.id.iconDelete);}
    }
}
