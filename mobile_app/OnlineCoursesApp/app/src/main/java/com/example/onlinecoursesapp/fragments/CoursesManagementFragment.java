package com.example.onlinecoursesapp.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.adapter.CourseManagementAdapter;
import com.example.onlinecoursesapp.api.ApiClient;
import com.example.onlinecoursesapp.api.CourseApiService;
import com.example.onlinecoursesapp.models.Course;
import com.example.onlinecoursesapp.models.StatisticsResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoursesManagementFragment extends Fragment implements CourseManagementAdapter.OnCourseItemClickListener {

    private RecyclerView recyclerView;
    private CourseManagementAdapter adapter;
    private FloatingActionButton fabAddCourse;
    private CourseApiService courseApiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_courses_management, container, false);

        // Khởi tạo API service
        courseApiService = ApiClient.getCourseApiService();

        // Khởi tạo RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CourseManagementAdapter(getContext(), this);
        recyclerView.setAdapter(adapter);

        // Khởi tạo FloatingActionButton
        fabAddCourse = view.findViewById(R.id.fabAddCourse);
        fabAddCourse.setOnClickListener(v -> showAddCourseDialog());

        // Load danh sách khóa học
        loadCoursesFromApi();

        return view;
    }

    private void loadCoursesFromApi() {
        Call<List<Course>> call = courseApiService.getAllCourses();
        call.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setCourseList(response.body());
                } else {
                    Toast.makeText(getContext(), "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddCourseDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.add_course_management, null);
        EditText editTextTitle = dialogView.findViewById(R.id.editTextTitle);
        EditText editTextDescription = dialogView.findViewById(R.id.editTextDescription);
        EditText editTextNumberOfLessons = dialogView.findViewById(R.id.editTextNumberOfLessons);
        Spinner spinnerDifficulty = dialogView.findViewById(R.id.spinnerDifficulty);
        EditText editTextImagePath = dialogView.findViewById(R.id.editTextImagePath);

        // Setup spinner for difficulty levels
        ArrayAdapter<Course.Difficulty> difficultyAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                Course.Difficulty.values()
        );
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(difficultyAdapter);

        new AlertDialog.Builder(requireContext())
                .setTitle("Thêm khóa học mới")
                .setView(dialogView)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    Course newCourse = new Course();
                    newCourse.setTitle(editTextTitle.getText().toString());
                    newCourse.setDescription(editTextDescription.getText().toString());
                    newCourse.setNumberOfLessons(Integer.parseInt(editTextNumberOfLessons.getText().toString()));
                    newCourse.setDifficulty((Course.Difficulty) spinnerDifficulty.getSelectedItem());
                    newCourse.setImagePath(editTextImagePath.getText().toString());

                    String title = editTextTitle.getText().toString().trim();
                    String description = editTextDescription.getText().toString().trim();
                    String numberOfLessonsStr = editTextNumberOfLessons.getText().toString().trim();
                    String imagePath = editTextImagePath.getText().toString().trim();

                    if (title.isEmpty() || description.isEmpty() || numberOfLessonsStr.isEmpty() || imagePath.isEmpty()) {
                        Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    addCourse(newCourse);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showEditCourseDialog(Course course) {
        View dialogView = getLayoutInflater().inflate(R.layout.edit_course_management, null);
        EditText editTextTitle = dialogView.findViewById(R.id.editTextTitle);
        EditText editTextDescription = dialogView.findViewById(R.id.editTextDescription);
        EditText editTextNumberOfLessons = dialogView.findViewById(R.id.editTextNumberOfLessons);
        Spinner spinnerDifficulty = dialogView.findViewById(R.id.spinnerDifficulty);
        EditText editTextImagePath = dialogView.findViewById(R.id.editTextImagePath);

        // Fill current course data
        editTextTitle.setText(course.getTitle());
        editTextDescription.setText(course.getDescription());
        editTextNumberOfLessons.setText(String.valueOf(course.getNumberOfLessons()));
        editTextImagePath.setText(course.getImagePath());

        // Setup spinner for difficulty levels
        ArrayAdapter<Course.Difficulty> difficultyAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                Course.Difficulty.values()
        );
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(difficultyAdapter);
        spinnerDifficulty.setSelection(course.getDifficulty().ordinal());

        new AlertDialog.Builder(requireContext())
                .setTitle("Chỉnh sửa khóa học")
                .setView(dialogView)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    course.setTitle(editTextTitle.getText().toString());
                    course.setDescription(editTextDescription.getText().toString());
                    course.setNumberOfLessons(Integer.parseInt(editTextNumberOfLessons.getText().toString()));
                    course.setDifficulty((Course.Difficulty) spinnerDifficulty.getSelectedItem());
                    course.setImagePath(editTextImagePath.getText().toString());
                    updateCourse(course.getId(), course);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void addCourse(Course newCourse) {
        Call<StatisticsResponse> call = courseApiService.addCourse(newCourse);
        call.enqueue(new Callback<StatisticsResponse>() {
            @Override
            public void onResponse(Call<StatisticsResponse> call, Response<StatisticsResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Thêm khóa học thành công", Toast.LENGTH_SHORT).show();
                    loadCoursesFromApi();
                } else {
                    Toast.makeText(getContext(), "Lỗi thêm khóa học", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StatisticsResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCourse(int courseId, Course updatedCourse) {
        Call<StatisticsResponse> call = courseApiService.updateCourse(courseId, updatedCourse);
        call.enqueue(new Callback<StatisticsResponse>() {
            @Override
            public void onResponse(Call<StatisticsResponse> call, Response<StatisticsResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Cập nhật khóa học thành công", Toast.LENGTH_SHORT).show();
                    loadCoursesFromApi();
                } else {
                    Toast.makeText(getContext(), "Lỗi cập nhật khóa học", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StatisticsResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteCourse(int courseId) {
        Call<StatisticsResponse> call = courseApiService.deleteCourse(courseId);
        call.enqueue(new Callback<StatisticsResponse>() {
            @Override
            public void onResponse(Call<StatisticsResponse> call, Response<StatisticsResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Xóa khóa học thành công", Toast.LENGTH_SHORT).show();
                    loadCoursesFromApi();
                } else {
                    Toast.makeText(getContext(), "Lỗi xóa khóa học", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StatisticsResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEditClick(Course course) {
        showEditCourseDialog(course);
    }

    @Override
    public void onDeleteClick(Course course) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa khóa học: " + course.getTitle() + "?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteCourse(course.getId()))
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public void onCourseClick(Course course) {
        Toast.makeText(getContext(), "Chọn khóa học: " + course.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
