package com.example.onlinecoursesapp.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.activities.CourseOverViewActivity;
import com.example.onlinecoursesapp.adapter.TutorialAdapter;
import com.example.onlinecoursesapp.api.ApiClient;
import com.example.onlinecoursesapp.api.CourseApiService;
import com.example.onlinecoursesapp.models.Course;
import com.example.onlinecoursesapp.models.course_progress.CourseProgress;
import com.example.onlinecoursesapp.models.course_progress.StatisticsResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    // UI Components
    private RecyclerView recyclerViewTutorials;
    private ImageView Search;
    private TextView tvContinueCourseProgress;
    private TutorialAdapter tutorialAdapter;
    // Data
    private List<Course> courseList;
    private CourseApiService apiService;
    private int currentUserId = 1;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize API service
        apiService = ApiClient.getCourseApiService();
        courseList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initViews(view);
        setupRecyclerView();
        loadData();

        return view;
    }

    private void initViews(View view) {
        recyclerViewTutorials = view.findViewById(R.id.recyclerViewTutorials);
        Search = view.findViewById(R.id.Search);

        Search .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // In your MainActivity or wherever you want to show the search
                SearchFragment searchFragment = SearchFragment.newInstance();
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, searchFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    private void setupRecyclerView() {
        tutorialAdapter = new TutorialAdapter(getContext(), courseList);
        recyclerViewTutorials.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewTutorials.setAdapter(tutorialAdapter);

        // Set item click listener
        tutorialAdapter.setOnItemClickListener(new TutorialAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Course course) {
                // Handle tutorial item click
                Toast.makeText(getContext(), "Opening: " + course.getTitle(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), CourseOverViewActivity.class);
                intent.putExtra("courseId", course.getId());
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        loadAllCourses();
        loadUserProgress();
        loadUserStatistics();
    }

    private void loadAllCourses() {
        Call<List<Course>> call = apiService.getAllCourses();
        call.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Course> courses = response.body();
                    courseList.clear();
                    courseList.addAll(courses);
                    tutorialAdapter.notifyDataSetChanged();

                    Log.d(TAG, "Loaded " + courseList.size() + " courses");
                } else {
                    Log.e(TAG, "Failed to load courses: " + response.code());
                    showError("Failed to load courses");
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                Log.e(TAG, "Error loading courses", t);
                showError("Network error while loading courses");
            }
        });
    }

    private void loadUserProgress() {
        Call<List<CourseProgress>> call = apiService.getUserCourseProgress(currentUserId);
        call.enqueue(new Callback<List<CourseProgress>>() {
            @Override
            public void onResponse(Call<List<CourseProgress>> call, Response<List<CourseProgress>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CourseProgress> progressList = response.body();

                    // Find the most recent course in progress
                    CourseProgress latestProgress = findLatestCourseInProgress(progressList);
                    if (latestProgress != null) {
                        updateContinueCourseCard(latestProgress);
                    }

                    Log.d(TAG, "Loaded user progress for " + progressList.size() + " courses");
                } else {
                    Log.e(TAG, "Failed to load user progress: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<CourseProgress>> call, Throwable t) {
                Log.e(TAG, "Error loading user progress", t);
            }
        });
    }

    private void loadUserStatistics() {
        Call<StatisticsResponse> call = apiService.getUserStatistics(currentUserId);
        call.enqueue(new Callback<StatisticsResponse>() {
            @Override
            public void onResponse(Call<StatisticsResponse> call, Response<StatisticsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StatisticsResponse stats = response.body();
                    Log.d(TAG, "Loaded user statistics");
                    // You can update UI with statistics here if needed
                } else {
                    Log.e(TAG, "Failed to load user statistics: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<StatisticsResponse> call, Throwable t) {
                Log.e(TAG, "Error loading user statistics", t);
            }
        });
    }

    /**
     * Find the course with highest progress that's not completed (0 < progress < 100%)
     */
    private CourseProgress findLatestCourseInProgress(List<CourseProgress> progressList) {
        CourseProgress mostRecentProgress = null;
        float highestProgress = 0;

        for (CourseProgress progress : progressList) {
            // Find courses that are in progress (not completed and not just started)
            if (progress.getCompletionPercentage() > 0 && progress.getCompletionPercentage() < 100) {
                // Select the one with highest progress (most recently worked on)
                if (progress.getCompletionPercentage() > highestProgress) {
                    highestProgress = progress.getCompletionPercentage();
                    mostRecentProgress = progress;
                }
            }
        }

        return mostRecentProgress;
    }

    /**
     * Update the continue course card with the latest progress
     */
    private void updateContinueCourseCard(CourseProgress progress) {

        Log.d(TAG, "Updated continue course card: " + progress.getCourseTitle() +
                " (" + progress.getFormattedCompletionPercentage() + " completed)");
        Log.d(TAG, "Progress details: " + progress.getFormattedProgressText() +
                " - " + progress.getFormattedCompletedDuration() + "/" + progress.getFormattedTotalDuration());
    }

    private void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    public void refreshData() {
        loadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }
}