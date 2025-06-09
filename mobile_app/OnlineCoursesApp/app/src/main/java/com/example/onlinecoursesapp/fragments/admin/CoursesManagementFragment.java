package com.example.onlinecoursesapp.fragments.admin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.adapter.CourseManagementAdapter;
import com.example.onlinecoursesapp.adapter.UserAdapter;
import com.example.onlinecoursesapp.api.ApiClient;
import com.example.onlinecoursesapp.api.CourseApiService;
import com.example.onlinecoursesapp.data.CourseManagementRepository;
import com.example.onlinecoursesapp.data.UserRepository;
import com.example.onlinecoursesapp.models.Course;
import com.example.onlinecoursesapp.models.UserProgress;
import com.example.onlinecoursesapp.utils.CourseCallback;
import com.example.onlinecoursesapp.utils.CourseListCallback;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoursesManagementFragment extends Fragment {

    private RecyclerView recyclerView;
    private CourseManagementAdapter adapter;
    private FloatingActionButton fabAddCourse;

    private static final String ARG_CATEGORY_ID = "category_id";
    private CourseManagementRepository repository;

    private List<Course> listCourse = new ArrayList<>();
    private int categoryId;
    private ImageButton btnback;

    public static CoursesManagementFragment newInstance(int categoryId) {
        CoursesManagementFragment fragment = new CoursesManagementFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_ID, categoryId);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_courses_management, container, false);
        repository =CourseManagementRepository.getInstance(requireContext());
        initViews(view);
        setupRecyclerView();
        loadCoursesFromApi();
        setupListeners();
        return view;
    }


    private void loadCoursesFromApi() {
        categoryId = requireArguments().getInt(ARG_CATEGORY_ID);
        repository.getCoursesByCategoryId(categoryId, new CourseListCallback() {
            @Override
            public void onSuccess(List<Course> courseList) {
                adapter.updateCourses(courseList);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getContext(), "Lỗi tải dữ liệu: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("WrongViewCast")
    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewCourses);
        fabAddCourse = view.findViewById(R.id.fabAddCourse);
        btnback = view.findViewById(R.id.btnBack);
    }

    private void setupRecyclerView() {
        adapter = new CourseManagementAdapter(new ArrayList<>(), new CourseManagementAdapter.OnCourseItemClickListener() {
            @Override
            public void onEditClick(Course course) {
                CourseDialogFragment dialog = new CourseDialogFragment((courseUpdate, isEdit) -> {
                    if (isEdit) {
                        adapter.updateCourses(courseUpdate);
                    }
                }, course,categoryId);
                dialog.show(getParentFragmentManager(), "EditCourseDialog");
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
                Fragment lessonManagementFragment = new LessonManagementFragment();
                Bundle args = new Bundle();
                args.putInt("course_id", course.getId());
                lessonManagementFragment.setArguments(args);

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, lessonManagementFragment)
                        .addToBackStack(null)
                        .commit();
            }

        });

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }

    private void deleteCourse(int id) {
       repository.deleteCourse(id, new CourseCallback() {
            @Override
            public void onSuccess(Course course) {
                // Cập nhật giao diện, hiển thị thông báo xóa thành công, etc.
                Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                // Ví dụ: load lại danh sách khóa học
                loadCoursesFromApi();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getContext(), "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setupListeners() {
        System.out.println("Vào listener");
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
        // Add user button
        fabAddCourse.setOnClickListener(v -> {
            CourseDialogFragment dialog = new CourseDialogFragment((course, isEdit) -> {
                if (!isEdit) {
                    listCourse.add(course);
                    adapter.updateCourses(listCourse);
                }
            }, null, categoryId);
            dialog.show(getParentFragmentManager(), "AddCourseDialog");
        });
    }
}

//