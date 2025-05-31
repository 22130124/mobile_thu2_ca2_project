package com.example.onlinecoursesapp.fragments.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.adapter.LessonOverviewAdapter;
import com.example.onlinecoursesapp.data.CourseRepository;
import com.example.onlinecoursesapp.data.EnrollmentRepository;
import com.example.onlinecoursesapp.models.CourseOverview;
import com.example.onlinecoursesapp.utils.CourseOverviewCallback;
import com.example.onlinecoursesapp.utils.EnrollmentCallback;

public class CourseOverViewFragment extends Fragment {
    private TextView title, description, numberOfLessons, totalDuration;
    private ImageView imagePath;
    private RecyclerView lessonsRecyclerView;
    private CourseRepository courseRepository;
    private boolean isEnrolled;
    private Button btnRegister;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_over_view, container, false);

        courseRepository = CourseRepository.getInstance(requireContext());

        SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", 0);
        String username = prefs.getString("userName", "Khách");
        int userId = prefs.getInt("userId", -1);
        int courseId = 1; // giả sử cố định là 1

        TextView tvWelcome = view.findViewById(R.id.tvWelcome);
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);
        numberOfLessons = view.findViewById(R.id.numberOfLessons);
        totalDuration = view.findViewById(R.id.totalDuration);
        imagePath = view.findViewById(R.id.imagePath);
        lessonsRecyclerView = view.findViewById(R.id.lessons);
        btnRegister = view.findViewById(R.id.btnRegister);

        tvWelcome.setText("Xin chào, " + username + " ! " + userId);

        getCourseById(courseId);

        btnRegister.setOnClickListener(v -> handleEnrollment(userId, courseId));

        return view;
    }

    private void handleEnrollment(int userId, int courseId) {
        if (userId == -1) {
            Toast.makeText(getContext(), "Vui lòng đăng nhập trước khi đăng ký khóa học", Toast.LENGTH_SHORT).show();
            return;
        }

        EnrollmentRepository enrollmentRepo = EnrollmentRepository.getInstance(requireContext());
        enrollmentRepo.enrollUser(userId, courseId, new EnrollmentCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "Đăng ký khóa học thành công!", Toast.LENGTH_SHORT).show();

                enrollmentRepo.checkEnrollment(userId, courseId, new EnrollmentCallback() {
                    @Override
                    public void onResult(boolean enrolled) {
                        isEnrolled = enrolled;
                        getCourseById(courseId);
                    }

                    @Override
                    public void onSuccess() {}

                    @Override
                    public void onFailure(String message) {}
                });
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getContext(), "Đăng ký thất bại: " + message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResult(boolean isEnrolled) {}
        });
    }

    private void getCourseById(int courseId) {
        courseRepository.fetchGetCourseById(courseId, new CourseOverviewCallback.SingleCourse() {
            @Override
            public void onSuccess(CourseOverview courseOverview) {
                title.setText(courseOverview.getTitle());
                description.setText(courseOverview.getDescription());
                numberOfLessons.setText(String.valueOf(courseOverview.getNumberOfLessons()));
                totalDuration.setText(courseOverview.getTotalFormattedDuration());

                Glide.with(requireContext())
                        .load(courseOverview.getImagePath())
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(imagePath);

                EnrollmentRepository enrollmentRepo = EnrollmentRepository.getInstance(requireContext());
                SharedPreferences prefs = requireActivity().getSharedPreferences("UserSession", 0);
                int userId = prefs.getInt("userId", -1);

                enrollmentRepo.checkEnrollment(userId, courseId, new EnrollmentCallback() {
                    @Override
                    public void onSuccess() {}

                    @Override
                    public void onFailure(String message) {}

                    @Override
                    public void onResult(boolean enrolled) {
                        isEnrolled = enrolled;
                        btnRegister.setVisibility(enrolled ? View.GONE : View.VISIBLE);
                        LessonOverviewAdapter adapter = new LessonOverviewAdapter(courseOverview.getLessons(), isEnrolled);
                        lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                        lessonsRecyclerView.setAdapter(adapter);
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                Log.e("ItemCourseActivity", "Failed to load course: " + message);
            }
        });
    }
}
