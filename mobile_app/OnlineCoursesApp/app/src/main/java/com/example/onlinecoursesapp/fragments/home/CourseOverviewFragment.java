package com.example.onlinecoursesapp.fragments.home;

import android.content.Context;
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

public class CourseOverviewFragment extends Fragment {
    private final String baseUrl = "http://10.0.2.2:8080";
    private TextView title, description, numberOfLessons, totalDuration, tvWelcome;
    private ImageView imagePath, Search, imgAvatar;
    private RecyclerView lessonsRecyclerView;
    private Button btnRegister;
    private boolean isEnrolled;
    private int courseId;
    private CourseRepository courseRepository;

    public static CourseOverviewFragment newInstance(int courseId) {
        CourseOverviewFragment fragment = new CourseOverviewFragment();
        Bundle args = new Bundle();
        args.putInt("courseId", courseId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        courseRepository = CourseRepository.getInstance(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_overview, container, false); // dùng chung layout
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);
        String username = prefs.getString("userName", "Khách");
        String img = prefs.getString("img", "");

        tvWelcome = view.findViewById(R.id.tvWelcome);
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);
        numberOfLessons = view.findViewById(R.id.numberOfLessons);
        totalDuration = view.findViewById(R.id.totalDuration);
        imagePath = view.findViewById(R.id.imagePath);
        lessonsRecyclerView = view.findViewById(R.id.lessons);
        btnRegister = view.findViewById(R.id.btnRegister);
        Search = view.findViewById(R.id.Search);
        imgAvatar = view.findViewById(R.id.imgAvatar);

        tvWelcome.setText("Xin chào, " + username);
        if (!img.isEmpty()) {
            Glide.with(this)
                    .load(baseUrl + img)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .circleCrop()
                    .into(imgAvatar);
        } else {
            imgAvatar.setImageResource(R.drawable.placeholder_image);
        }

        if (getArguments() != null) {
            courseId = getArguments().getInt("courseId", -1);
            if (courseId != -1) {
                getCourseById(courseId);
            } else {
                Toast.makeText(getContext(), "Không tìm thấy khóa học", Toast.LENGTH_SHORT).show();
            }
        }

        btnRegister.setOnClickListener(v -> handleEnrollment(userId, courseId));

        Search.setOnClickListener(v -> {
            SearchFragment searchFragment = SearchFragment.newInstance();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, searchFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void handleEnrollment(int userId, int courseId) {
        if (userId == -1) {
            Toast.makeText(getContext(), "Vui lòng đăng nhập trước khi đăng ký khóa học", Toast.LENGTH_SHORT).show();
            return;
        }

        EnrollmentRepository enrollmentRepo = EnrollmentRepository.getInstance(getContext());
        enrollmentRepo.enrollUser(userId, courseId, new EnrollmentCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "Đăng ký khóa học thành công!", Toast.LENGTH_SHORT).show();
                enrollmentRepo.checkEnrollment(userId, courseId, new EnrollmentCallback() {
                    @Override public void onSuccess() {}
                    @Override public void onFailure(String message) {}
                    @Override public void onResult(boolean enrolled) {
                        isEnrolled = enrolled;
                        getCourseById(courseId);
                    }
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
                        .load(baseUrl + courseOverview.getImagePath())
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(imagePath);

                EnrollmentRepository enrollmentRepo = EnrollmentRepository.getInstance(requireContext());
                int userId = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                        .getInt("userId", -1);

                enrollmentRepo.checkEnrollment(userId, courseId, new EnrollmentCallback() {
                    @Override public void onSuccess() {}
                    @Override public void onFailure(String message) {}

                    @Override
                    public void onResult(boolean enrolled) {
                        isEnrolled = enrolled;
                        btnRegister.setVisibility(enrolled ? View.GONE : View.VISIBLE);
                        LessonOverviewAdapter adapter = new LessonOverviewAdapter(courseOverview.getLessons(), isEnrolled, courseId);
                        lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        lessonsRecyclerView.setAdapter(adapter);
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                Log.e("CourseOverviewFragment", "Failed to load course: " + message);
            }
        });
    }
}
