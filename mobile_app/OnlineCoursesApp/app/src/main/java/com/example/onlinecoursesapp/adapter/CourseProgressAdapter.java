package com.example.onlinecoursesapp.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.api.ApiClient;
import com.example.onlinecoursesapp.models.course_progress.CourseProgress;

import java.util.ArrayList;
import java.util.List;

public class CourseProgressAdapter extends RecyclerView.Adapter<CourseProgressAdapter.ProgressViewHolder> {
    private List<CourseProgress> progressList = new ArrayList<>();
    private OnCourseClickListener listener;

    public interface OnCourseClickListener {
        void onCourseClick(CourseProgress courseProgress);
    }

    public void setOnCourseClickListener(OnCourseClickListener listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setProgressList(List<CourseProgress> progressList) {
        this.progressList = progressList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProgressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course_progress, parent, false);
        return new ProgressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressViewHolder holder, int position) {
        CourseProgress progress = progressList.get(position);
        holder.bind(progress);
    }

    @Override
    public int getItemCount() {
        return progressList.size();
    }

    class ProgressViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgCourse;
        private final TextView tvCourseTitle;
        private final TextView tvProgress;
        private final ProgressBar progressBar;
        private final TextView tvCompletionPercentage;
        private final TextView tvDuration;

        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCourse = itemView.findViewById(R.id.img_course);
            tvCourseTitle = itemView.findViewById(R.id.tv_course_title);
            tvProgress = itemView.findViewById(R.id.tv_progress);
            progressBar = itemView.findViewById(R.id.progress_bar);
            tvCompletionPercentage = itemView.findViewById(R.id.tv_completion_percentage);
            tvDuration = itemView.findViewById(R.id.tv_duration);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onCourseClick(progressList.get(position));
                }
            });
        }

        public void bind(CourseProgress progress) {
            tvCourseTitle.setText(progress.getCourseTitle());
            tvProgress.setText(progress.getFormattedProgressText());

            // Set progress percentage
            int percentage = Math.round(progress.getCompletionPercentage());
            progressBar.setProgress(percentage);
            tvCompletionPercentage.setText(progress.getFormattedCompletionPercentage());

            // Set duration text
            String durationText = progress.getFormattedCompletedDuration() + " / " +
                    progress.getFormattedTotalDuration();
            tvDuration.setText(durationText);

            Glide.with(imgCourse.getContext())
                    .load(ApiClient.getBaseUrl() + progress.getCourseImagePath())
                    .placeholder(R.drawable.placeholder_image) // ảnh mặc định nếu đang tải
                    .error(R.drawable.error_image) // ảnh nếu load thất bại
                    .into(imgCourse);

        }
    }
}
