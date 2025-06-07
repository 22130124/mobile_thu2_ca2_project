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
import com.example.onlinecoursesapp.api.ApiClient;
import com.example.onlinecoursesapp.models.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseManagementAdapter extends RecyclerView.Adapter<CourseManagementAdapter.CourseViewHolder> {

    public interface OnCourseItemClickListener {
        void onEditClick(Course course);
        void onDeleteClick(Course course);
        void onCourseClick(Course course);
    }

    private List<Course> courseList;
    private final OnCourseItemClickListener listener;

    public CourseManagementAdapter(List<Course> courseList, OnCourseItemClickListener listener) {
        this.courseList = (courseList != null) ? courseList : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course_management, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.bind(course);
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public void updateCourses(List<Course> newCourses) {
        this.courseList = (newCourses != null) ? newCourses : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void updateCourses(Course updatedCourse) {
        for (int i = 0; i < courseList.size(); i++) {
            if (courseList.get(i).getId() == updatedCourse.getId()) {
                courseList.set(i, updatedCourse);
                notifyItemChanged(i);
                break;
            }
        }
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageCourse, iconEdit, iconDelete;
        private final TextView textTitle, textDesc, textNumLessons, textLevel;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCourse = itemView.findViewById(R.id.imageCourse);
            iconEdit = itemView.findViewById(R.id.iconEdit);
            iconDelete = itemView.findViewById(R.id.iconDelete);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDesc = itemView.findViewById(R.id.textDesc);
            textNumLessons = itemView.findViewById(R.id.textNumLessons);
            textLevel = itemView.findViewById(R.id.textLevel);
        }

        public void bind(Course course) {
            textTitle.setText(course.getTitle());
            textDesc.setText(course.getDescription());
            textNumLessons.setText("Số bài học: " + course.getNumberOfLessons());
            textLevel.setText("Độ khó: " + course.getDifficulty());

            String imageUrl = ApiClient.getBaseUrl() + course.getImagePath();
            Glide.with(itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_image) // thay bằng ảnh placeholder của bạn
                    .error(R.drawable.placeholder_image)
                    .into(imageCourse);

            iconEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(course);
                }
            });

            iconDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(course);
                }
            });

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCourseClick(course);
                }
            });
        }
    }
}
