package com.example.onlinecoursesapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.models.Course;

import java.util.ArrayList;
import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private Context context;
    private List<Course> courses;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Course course);
    }

    public SearchResultAdapter(Context context) {
        this.context = context;
        this.courses = new ArrayList<>();
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = courses.get(position);

        holder.tvTitle.setText(course.getTitle());
        holder.tvLessons.setText(course.getNumberOfLessons() + " lessons");

        // Set difficulty
        String difficulty = "";
        int difficultyColor = R.color.primary;

        holder.tvDifficulty.setText(difficulty);
        holder.tvDifficulty.setTextColor(context.getResources().getColor(difficultyColor));

        // Load image
        if (course.getImagePath() != null && !course.getImagePath().isEmpty()) {
            Glide.with(context)
                    .load(course.getImagePath())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .centerCrop()
                    .into(holder.ivCourse);
        } else {
            holder.ivCourse.setImageResource(R.drawable.placeholder_image);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(course);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courses != null ? courses.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCourse;
        TextView tvTitle;
        TextView tvLessons;
        TextView tvDifficulty;

        ViewHolder(View itemView) {
            super(itemView);
            ivCourse = itemView.findViewById(R.id.ivCourse);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvLessons = itemView.findViewById(R.id.tvLessons);
            tvDifficulty = itemView.findViewById(R.id.tvDifficulty);
        }
    }
}