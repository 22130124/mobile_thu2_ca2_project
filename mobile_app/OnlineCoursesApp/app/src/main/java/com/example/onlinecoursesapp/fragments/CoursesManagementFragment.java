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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.adapter.CourseManagementAdapter;
import com.example.onlinecoursesapp.api.ApiClient;
import com.example.onlinecoursesapp.api.CourseApiService;
import com.example.onlinecoursesapp.models.Course;
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
    private static final String ARG_CATEGORY_ID = "category_id";
    private int categoryId;

    public static CoursesManagementFragment newInstance(int categoryId) {
        CoursesManagementFragment fragment = new CoursesManagementFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_ID, categoryId);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_courses_management, container, false);

        courseApiService = ApiClient.getCourseApiService();

        recyclerView = view.findViewById(R.id.recyclerViewCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CourseManagementAdapter(getContext(), this);
        recyclerView.setAdapter(adapter);

        fabAddCourse = view.findViewById(R.id.fabAddCourse);
        fabAddCourse.setOnClickListener(v -> showAddCourseDialog());

        loadCoursesFromApi();

        return view;
    }

    private void loadCoursesFromApi() {
        int categoryId = requireArguments().getInt(ARG_CATEGORY_ID);
        courseApiService.getCourseByCategoryId(categoryId).enqueue(new Callback<List<Course>>() {
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
                    String title = editTextTitle.getText().toString().trim();
                    String description = editTextDescription.getText().toString().trim();
                    String numberOfLessonsStr = editTextNumberOfLessons.getText().toString().trim();
                    String imagePath = editTextImagePath.getText().toString().trim();

                    if (title.isEmpty() || description.isEmpty() || numberOfLessonsStr.isEmpty() || imagePath.isEmpty()) {
                        Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        int numberOfLessons = Integer.parseInt(numberOfLessonsStr);

                        Course newCourse = new Course();
                        newCourse.setTitle(title);
                        newCourse.setDescription(description);
                        newCourse.setNumberOfLessons(numberOfLessons);
                        newCourse.setDifficulty((Course.Difficulty) spinnerDifficulty.getSelectedItem());
                        newCourse.setImagePath(imagePath);

                        addCourse(newCourse);
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Số bài học không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
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

        editTextTitle.setText(course.getTitle());
        editTextDescription.setText(course.getDescription());
        editTextNumberOfLessons.setText(String.valueOf(course.getNumberOfLessons()));
        editTextImagePath.setText(course.getImagePath());

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
                    try {
                        course.setTitle(editTextTitle.getText().toString().trim());
                        course.setDescription(editTextDescription.getText().toString().trim());
                        course.setNumberOfLessons(Integer.parseInt(editTextNumberOfLessons.getText().toString().trim()));
                        course.setDifficulty((Course.Difficulty) spinnerDifficulty.getSelectedItem());
                        course.setImagePath(editTextImagePath.getText().toString().trim());
                        updateCourse(course.getId(), course);
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Số bài học không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void addCourse(Course newCourse) {
        courseApiService.addCourse(newCourse).enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, Response<Course> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Thêm khóa học thành công", Toast.LENGTH_SHORT).show();
                    loadCoursesFromApi();
                } else {
                    Toast.makeText(getContext(), "Lỗi thêm khóa học", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCourse(int courseId, Course updatedCourse) {
        courseApiService.updateCourse(courseId, updatedCourse).enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, Response<Course> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Cập nhật khóa học thành công", Toast.LENGTH_SHORT).show();
                    loadCoursesFromApi();
                } else {
                    Toast.makeText(getContext(), "Lỗi cập nhật khóa học", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteCourse(int courseId) {
        courseApiService.deleteCourse(courseId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Xóa khóa học thành công", Toast.LENGTH_SHORT).show();
                    loadCoursesFromApi();
                } else {
                    Toast.makeText(getContext(), "Lỗi xóa khóa học", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
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
