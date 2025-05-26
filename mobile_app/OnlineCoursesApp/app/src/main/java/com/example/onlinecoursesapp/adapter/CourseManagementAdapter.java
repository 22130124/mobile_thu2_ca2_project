package com.example.onlinecoursesapp.adapter;

import android.content.Context;
import android.util.Log;
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

public class CourseManagementAdapter extends RecyclerView.Adapter<CourseManagementAdapter.ViewHolder> {

    public interface OnCourseItemClickListener {
        void onEditClick(Course course);
        void onDeleteClick(Course course);
        void onCourseClick(Course course);
    }

    private Context context;
    private List<Course> courseList;
    private OnCourseItemClickListener listener;

    public CourseManagementAdapter(Context context, OnCourseItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.courseList = new ArrayList<>();
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = (courseList != null) ? courseList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_course_management, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.textTitle.setText(course.getTitle());
        holder.textDesc.setText(course.getDescription());
        holder.textNumLessons.setText(String.valueOf(course.getNumberOfLessons()));

        String imageUrl = ApiClient.getBaseUrl() + course.getImagePath();
        Glide.with(context).load(imageUrl).into(holder.imageCourse);

        holder.iconEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onEditClick(course);
                }
            }
        });

        holder.iconDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDeleteClick(course);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCourseClick(course);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (courseList != null) ? courseList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageCourse, iconEdit, iconDelete;
        TextView textNumLessons, textTitle, textDesc;

        public ViewHolder(View itemView) {
            super(itemView);
            imageCourse = itemView.findViewById(R.id.imageCourse);
            textNumLessons = itemView.findViewById(R.id.textNumLessons);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDesc = itemView.findViewById(R.id.textDesc);
            iconEdit = itemView.findViewById(R.id.iconEdit);
            iconDelete = itemView.findViewById(R.id.iconDelete);

        }
    }
}
