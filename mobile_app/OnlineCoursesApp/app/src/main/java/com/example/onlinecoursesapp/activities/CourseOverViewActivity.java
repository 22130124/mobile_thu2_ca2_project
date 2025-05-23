package com.example.onlinecoursesapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.adapter.LessonOverviewAdapter;
import com.example.onlinecoursesapp.data.CourseRepository;
import com.example.onlinecoursesapp.models.CourseOverview;
import com.example.onlinecoursesapp.utils.CourseOverviewCallback;

public class CourseOverViewActivity extends AppCompatActivity {

    private TextView title, description, numberOfLessons, totalDuration;

    private ImageView imagePath;
    private RecyclerView lessonsRecyclerView;
    private CourseRepository courseRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_overview);

        courseRepository = CourseRepository.getInstance(this);

        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        numberOfLessons = findViewById(R.id.numberOfLessons);
        totalDuration = findViewById(R.id.totalDuration);
        imagePath = findViewById(R.id.imagePath);
        lessonsRecyclerView = findViewById(R.id.lessons);

        getCourseById(1);
    }

    private void getCourseById(int courseId) {
        courseRepository.fetchGetCourseById(courseId, new CourseOverviewCallback.SingleCourse() {
            @Override
            public void onSuccess(CourseOverview courseOverview) {
                Log.d("DEBUG", "Title: " + courseOverview.getTitle());
                Log.d("DEBUG", "Description: " + courseOverview.getDescription());
                Log.d("DEBUG", "Description: " + courseOverview.getImagePath());

                title.setText(courseOverview.getTitle());
                description.setText(courseOverview.getDescription());
                numberOfLessons.setText(String.valueOf(courseOverview.getNumberOfLessons()));
                totalDuration.setText(courseOverview.getTotalFormattedDuration());
                // Load ảnh vào ShapeableImageView
                imagePath = findViewById(R.id.imagePath);
                Glide.with(CourseOverViewActivity.this)
                        .load(courseOverview.getImagePath())
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(imagePath);

                // Load danh sách bài học
                LessonOverviewAdapter lessonAdapter = new LessonOverviewAdapter(courseOverview.getLessons());
                lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(CourseOverViewActivity.this));
                lessonsRecyclerView.setAdapter(lessonAdapter);
            }

            @Override
            public void onFailure(String message) {
                Log.e("ItemCourseActivity", "Failed to load course: " + message);
            }
        });
    }
}
