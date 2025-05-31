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

import java.util.List;

public class TutorialAdapter extends RecyclerView.Adapter<TutorialAdapter.TutorialViewHolder> {

    private Context context;
    private List<Course> courseList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Course course);
    }

    public TutorialAdapter(Context context, List<Course> courseList) {
        this.context = context;
        this.courseList = courseList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TutorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tutorial, parent, false);
        return new TutorialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorialViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.bind(course);
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    class TutorialViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvDescription;
        private ImageView ivIcon;
        private ImageView ivArrow;

        public TutorialViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views according to your XML layout
            tvTitle = itemView.findViewById(R.id.tvTutorialTitle);
            tvDescription = itemView.findViewById(R.id.tvTutorialDescription);
            ivIcon = itemView.findViewById(R.id.ivTutorialIcon);
            ivArrow = itemView.findViewById(R.id.ivArrow);

            // Set click listener for the entire item
            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onItemClick(courseList.get(getAdapterPosition()));
                }
            });
        }

        public void bind(Course course) {
            // Set course title
            tvTitle.setText(course.getTitle());

            // Set course description
            tvDescription.setText(course.getDescription());

            // Load course icon using Glide
            if (course.getImagePath() != null && !course.getImagePath().isEmpty()) {
                Glide.with(context)
                        .load(ApiClient.BASE_URL + course.getImagePath())
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image)
                        .into(ivIcon);
            } else {
                ivIcon.setImageResource(R.drawable.placeholder_image);
            }


            // You can add click animation or different arrow states if needed
            ivArrow.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(course);
                }
            });
        }

    }

    // Method to update the course list
    public void updateCourses(List<Course> newCourses) {
        this.courseList.clear();
        this.courseList.addAll(newCourses);
        notifyDataSetChanged();
    }

    // Method to add new courses
    public void addCourses(List<Course> newCourses) {
        int startPosition = courseList.size();
        courseList.addAll(newCourses);
        notifyItemRangeInserted(startPosition, newCourses.size());
    }
}