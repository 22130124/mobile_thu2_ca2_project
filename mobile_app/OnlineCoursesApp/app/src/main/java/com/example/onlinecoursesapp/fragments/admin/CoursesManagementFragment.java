package com.example.onlinecoursesapp.fragments.admin;

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
import android.widget.EditText;
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

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewCourses);
        fabAddCourse = view.findViewById(R.id.fabAddCourse);
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

//    @Override
//    public void onDeleteClick(Course course) {
//        new AlertDialog.Builder(requireContext())
//                .setTitle("Xác nhận xoá")
//                .setMessage("Bạn có chắc muốn xoá khoá học này?")
//                .setPositiveButton("Xoá", (dialog, which) -> {
//                    repository.deleteCourse(course.getId(), new CourseCallback() {
//                        @Override
//                        public void onSuccess(Course course) {
//                            Toast.makeText(requireContext(), "Đã xoá", Toast.LENGTH_SHORT).show();
//                            loadCoursesFromApi(); // Tải lại danh sách
//                        }
//
//                        @Override
//                        public void onFailure(String errorMessage) {
//                            Toast.makeText(requireContext(), "Xoá thất bại: " + errorMessage, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                })
//                .setNegativeButton("Huỷ", null)
//                .show();
//    }


//
//    private void showAddCourseDialog() {
//        View dialogView = getLayoutInflater().inflate(R.layout.fragment_course_management_diaolog, null);
//        EditText editTextTitle = dialogView.findViewById(R.id.editTextTitle);
//        EditText editTextDescription = dialogView.findViewById(R.id.editTextDescription);
//        EditText editTextNumberOfLessons = dialogView.findViewById(R.id.editTextNumberOfLessons);
//        Spinner spinnerDifficulty = dialogView.findViewById(R.id.spinnerDifficulty);
//        ImageView imagePreview = dialogView.findViewById(R.id.iv_course_image);
//        View btnUploadImage = dialogView.findViewById(R.id.btn_upload_image);
//
//        ArrayAdapter<Course.Difficulty> difficultyAdapter = new ArrayAdapter<>(
//                requireContext(),
//                android.R.layout.simple_spinner_item,
//                Course.Difficulty.values()
//        );
//        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerDifficulty.setAdapter(difficultyAdapter);
//        btnUploadImage.setOnClickListener(v -> pickImageLauncher.launch("image/*"));
//        new AlertDialog.Builder(requireContext())
//                .setTitle("Thêm khóa học mới")
//                .setView(dialogView)
//                .setPositiveButton("Thêm", (dialog, which) -> {
//                    String title = editTextTitle.getText().toString().trim();
//                    String description = editTextDescription.getText().toString().trim();
//                    String numberOfLessonsStr = editTextNumberOfLessons.getText().toString().trim();
//                    if (title.isEmpty() || description.isEmpty() || numberOfLessonsStr.isEmpty() || selectedImageUri == null) {
//                        Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin và chọn ảnh", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    try {
//                        int numberOfLessons = Integer.parseInt(numberOfLessonsStr);
//
//                        Course newCourse = new Course();
//                        newCourse.setTitle(title);
//                        newCourse.setDescription(description);
//                        newCourse.setNumberOfLessons(numberOfLessons);
//                        newCourse.setDifficulty((Course.Difficulty) spinnerDifficulty.getSelectedItem());
//                        uploadImageAndAddCourse(newCourse);
//                    } catch (NumberFormatException e) {
//                        Toast.makeText(getContext(), "Số bài học không hợp lệ", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .setNegativeButton("Hủy", null)
//                .show();
//    }
//    private void uploadImageAndAddCourse(Course newCourse) {
//        try {
//            File imageFile = getFileFromUri(selectedImageUri);
//            RequestBody requestFile = RequestBody.create(MediaType.parse(requireContext().getContentResolver().getType(selectedImageUri)), imageFile);
//            MultipartBody.Part body = MultipartBody.Part.createFormData("file", imageFile.getName(), requestFile);
//
//            courseApiService.uploadCourseImage(body).enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    if (response.isSuccessful() && response.body() != null) {
//                        try {
//                            String imageUrl = response.body().string();
//                            newCourse.setImagePath(imageUrl);
//                            addCourse(newCourse);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            Toast.makeText(getContext(), "Lỗi xử lý phản hồi từ server", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(getContext(), "Lỗi tải ảnh lên", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(getContext(), "Lỗi xử lý file ảnh", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private File getFileFromUri(Uri uri) throws IOException {
//        InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
//        if (inputStream == null) {
//            throw new IOException("Unable to get InputStream from URI");
//        }
//        String fileName = "upload_" + System.currentTimeMillis();
//        File tempFile = File.createTempFile(fileName, null, requireContext().getCacheDir());
//        tempFile.deleteOnExit();
//
//        FileOutputStream outputStream = new FileOutputStream(tempFile);
//        byte[] buffer = new byte[1024];
//        int bytesRead;
//        while ((bytesRead = inputStream.read(buffer)) != -1) {
//            outputStream.write(buffer, 0, bytesRead);
//        }
//        outputStream.close();
//        inputStream.close();
//
//        return tempFile;
//    }
//    private void showEditCourseDialog(Course course) {
//        View dialogView = getLayoutInflater().inflate(R.layout.edit_course_management, null);
//        EditText editTextTitle = dialogView.findViewById(R.id.editTextTitle);
//        EditText editTextDescription = dialogView.findViewById(R.id.editTextDescription);
//        EditText editTextNumberOfLessons = dialogView.findViewById(R.id.editTextNumberOfLessons);
//        Spinner spinnerDifficulty = dialogView.findViewById(R.id.spinnerDifficulty);
//        EditText editTextImagePath = dialogView.findViewById(R.id.editTextImagePath);
//
//        editTextTitle.setText(course.getTitle());
//        editTextDescription.setText(course.getDescription());
//        editTextNumberOfLessons.setText(String.valueOf(course.getNumberOfLessons()));
//        editTextImagePath.setText(course.getImagePath());
//
//        ArrayAdapter<Course.Difficulty> difficultyAdapter = new ArrayAdapter<>(
//                requireContext(),
//                android.R.layout.simple_spinner_item,
//                Course.Difficulty.values()
//        );
//        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerDifficulty.setAdapter(difficultyAdapter);
//        spinnerDifficulty.setSelection(course.getDifficulty().ordinal());
//
//        new AlertDialog.Builder(requireContext())
//                .setTitle("Chỉnh sửa khóa học")
//                .setView(dialogView)
//                .setPositiveButton("Lưu", (dialog, which) -> {
//                    try {
//                        course.setTitle(editTextTitle.getText().toString().trim());
//                        course.setDescription(editTextDescription.getText().toString().trim());
//                        course.setNumberOfLessons(Integer.parseInt(editTextNumberOfLessons.getText().toString().trim()));
//                        course.setDifficulty((Course.Difficulty) spinnerDifficulty.getSelectedItem());
//                        course.setImagePath(editTextImagePath.getText().toString().trim());
//                        updateCourse(course.getId(), course);
//                    } catch (NumberFormatException e) {
//                        Toast.makeText(getContext(), "Số bài học không hợp lệ", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .setNegativeButton("Hủy", null)
//                .show();
//    }
//@Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_courses_management, container, false);
//
//        repository= CourseManagementRepository.getInstance()
//
//        recyclerView = view.findViewById(R.id.recyclerViewCourses);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        adapter = new CourseManagementAdapter(getContext(), this);
//        recyclerView.setAdapter(adapter);
//
//        fabAddCourse = view.findViewById(R.id.fabAddCourse);
//        fabAddCourse.setOnClickListener(v -> showAddCourseDialog());
//        pickImageLauncher = registerForActivityResult(
//                new ActivityResultContracts.GetContent(),
//                uri -> {
//                    if (uri != null) {
//                        selectedImageUri = uri;
//                        // Update the image preview
//                        ImageView imagePreview = requireView().findViewById(R.id.iv_course_image);
//                        Glide.with(this)
//                                .load(uri)
//                                .placeholder(R.drawable.placeholder_image)
//                                .error(R.drawable.placeholder_image)
//                                .into(imagePreview);
//                    }
//                }
//        );
//        loadCoursesFromApi();
//
//        return view;
//    }


//private void addCourse(Course newCourse) {
//        courseApiService.addCourse(newCourse).enqueue(new Callback<Course>() {
//            @Override
//            public void onResponse(Call<Course> call, Response<Course> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(getContext(), "Thêm khóa học thành công", Toast.LENGTH_SHORT).show();
//                    loadCoursesFromApi();
//                } else {
//                    Toast.makeText(getContext(), "Lỗi thêm khóa học", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Course> call, Throwable t) {
//                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    private void updateCourse(int courseId, Course updatedCourse) {
//        courseApiService.updateCourse(courseId, updatedCourse).enqueue(new Callback<Course>() {
//            @Override
//            public void onResponse(Call<Course> call, Response<Course> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(getContext(), "Cập nhật khóa học thành công", Toast.LENGTH_SHORT).show();
//                    loadCoursesFromApi();
//                } else {
//                    Toast.makeText(getContext(), "Lỗi cập nhật khóa học", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Course> call, Throwable t) {
//                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    private void deleteCourse(int courseId) {
//        repository.deleteCourse(courseId).enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(getContext(), "Xóa khóa học thành công", Toast.LENGTH_SHORT).show();
//                    loadCoursesFromApi();
//                } else {
//                    Toast.makeText(getContext(), "Lỗi xóa khóa học", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    @Override
//    public void onEditClick(Course course) {
//        showEditCourseDialog(course);
//    }
//
//    @Override
//    public void onDeleteClick(Course course) {
//        new AlertDialog.Builder(requireContext())
//                .setTitle("Xác nhận xóa")
//                .setMessage("Bạn có chắc muốn xóa khóa học: " + course.getTitle() + "?")
//                .setPositiveButton("Xóa", (dialog, which) -> deleteCourse(course.getId()))
//                .setNegativeButton("Hủy", null)
//                .show();
//    }

//    @Override
//    public void onCourseClick(Course course) {
//        Fragment lessonManagementFragment = new LessonManagementFragment();
//        Bundle args = new Bundle();
//        args.putInt("course_id", course.getId());
//        lessonManagementFragment.setArguments(args);
//
//        requireActivity().getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container, lessonManagementFragment)
//                .addToBackStack(null)
//                .commit();
//    }

