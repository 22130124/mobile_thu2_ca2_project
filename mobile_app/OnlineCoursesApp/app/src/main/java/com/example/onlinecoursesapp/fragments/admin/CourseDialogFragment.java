package com.example.onlinecoursesapp.fragments.admin;

import android.annotation.SuppressLint;
import android.app.Dialog;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.DialogFragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.data.CourseManagementRepository;
import com.example.onlinecoursesapp.models.Course;
import com.example.onlinecoursesapp.utils.CourseCallback;
import com.example.onlinecoursesapp.utils.ImageUploadCallback;

import org.json.JSONException;
import org.json.JSONObject;


@SuppressLint("ValidFragment")
public class CourseDialogFragment extends DialogFragment {
    private EditText edtTitle, edtDescription;
    private Spinner spinnerDifficulty;
    private Button btnUploadImg, btnSave;
    private ImageView ivCourseImage;
    private int categoryId;

    public interface CourseDialogListener {
        void onSaved(Course course, boolean isEdit);
    }

    private Course courseToEdit;
    private CourseDialogFragment.CourseDialogListener listener;
    private ActivityResultLauncher<String> pickImageLauncher;
    private Uri selectedImageUri = null;

    public CourseDialogFragment(CourseDialogFragment.CourseDialogListener listener, @Nullable Course courseToEdit, int categoryId) {
        this.listener = listener;
        this.courseToEdit = courseToEdit;
        this.categoryId= categoryId;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9); // 90% màn hình
            getDialog().getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedImageUri = uri;

                        // Hiển thị ảnh tạm trước, chờ upload sau khi thêm xong khóa học
                        Glide.with(CourseDialogFragment.this)
                                .load(uri)
                                .placeholder(R.drawable.placeholder_image)
                                .into(ivCourseImage);
                    }
                }
        );

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_course_management_diaolog, null);
        edtTitle = view.findViewById(R.id.editTextTitle);
        edtDescription = view.findViewById(R.id.editTextDescription);
        spinnerDifficulty = view.findViewById(R.id.spinnerDifficulty);
        ivCourseImage = view.findViewById(R.id.iv_course_image);

        // Tạo adapter cho Spinner
        ArrayAdapter<Course.Difficulty> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                Course.Difficulty.values()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapter);

        if (courseToEdit != null) {
            edtTitle.setText(courseToEdit.getTitle());
            edtDescription.setText(courseToEdit.getDescription());
            spinnerDifficulty.setSelection(adapter.getPosition(courseToEdit.getDifficulty()));

            if (courseToEdit.getImagePath() != null && !courseToEdit.getImagePath().isEmpty()) {
                Glide.with(this)
                        .load(courseToEdit.getImagePath())
                        .placeholder(R.drawable.placeholder_image)
                        .into(ivCourseImage);
            }
        } else {
            // Nếu courseToEdit == null thì có thể set ảnh mặc định
            Glide.with(this)
                    .load(R.drawable.placeholder_image)
                    .into(ivCourseImage);
        }

        btnUploadImg = view.findViewById(R.id.btn_upload_image);
        btnSave = view.findViewById(R.id.btn_save);

        btnSave.setText(courseToEdit == null ? "Thêm" : "Cập nhật");

        btnUploadImg.setOnClickListener(v -> {
            pickImageLauncher.launch("image/*");
        });

        btnSave.setOnClickListener(v -> {
            String title = edtTitle.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();
            Course.Difficulty difficulty = (Course.Difficulty) spinnerDifficulty.getSelectedItem();

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin ", Toast.LENGTH_SHORT).show();
                return;
            }

            Course course = courseToEdit != null ? courseToEdit : new Course();
            course.setTitle(title);
            course.setDescription(description);
            course.setDifficulty(difficulty);

            CourseManagementRepository repo = CourseManagementRepository.getInstance(getContext());

            if (courseToEdit == null) {
                // 👉 Trường hợp thêm mới
                course.setCategoryId(categoryId);
                course.setNumberOfLessons(0);
                repo.addCourse(course, new CourseCallback() {
                    @Override
                    public void onSuccess(Course addedCourse) {
                        if (selectedImageUri != null) {
                            // Nếu có ảnh, tiến hành upload ảnh
                            repo.uploadCourseImage(addedCourse.getId(), selectedImageUri, new ImageUploadCallback() {
                                @Override
                                public void onSuccess(String imageUrl) {
                                    System.out.println("Link trong dialog "+ imageUrl);
                                    String imagePath;
                                    JSONObject json = null;
                                    try {
                                        json = new JSONObject(imageUrl);
                                        imagePath = json.getString("imagePath");
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }


                                    addedCourse.setImagePath(imagePath);
                                    // Cập nhật khóa học sau khi có ảnh
                                    repo.updateCourse(addedCourse.getId(), addedCourse, new CourseCallback() {
                                        @Override
                                        public void onSuccess(Course updatedCourse) {
                                            listener.onSaved(updatedCourse, false);
                                            dismiss();
                                        }

                                        @Override
                                        public void onFailure(String errorMessage) {
                                            Toast.makeText(getContext(), "Cập nhật ảnh thất bại: " + errorMessage, Toast.LENGTH_SHORT).show();
                                            listener.onSaved(addedCourse, false); // vẫn cho lưu
                                            dismiss();
                                        }
                                    });
                                }

                                @Override
                                public void onFailure(String errorMessage) {
                                    Toast.makeText(getContext(), "Tải ảnh thất bại: " + errorMessage, Toast.LENGTH_SHORT).show();
                                    listener.onSaved(addedCourse, false); // vẫn cho lưu
                                    dismiss();
                                }
                            });
                        } else {
                            listener.onSaved(addedCourse, false);
                            dismiss();
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // 👉 Trường hợp cập nhật
                if (selectedImageUri != null) {
                    repo.uploadCourseImage(course.getId(), selectedImageUri, new ImageUploadCallback() {
                        @Override
                        public void onSuccess(String imageUrl) {
                            try {
                                JSONObject json = new JSONObject(imageUrl);
                                String imagePath = json.getString("imagePath");
                                course.setImagePath(imagePath);
                            } catch (JSONException e) {
                                Toast.makeText(getContext(), "Lỗi xử lý ảnh", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Sau khi upload ảnh thành công, gọi updateCourse
                            repo.updateCourse(course.getId(), course, new CourseCallback() {
                                @Override
                                public void onSuccess(Course updatedCourse) {
                                    listener.onSaved(updatedCourse, true);
                                    dismiss();
                                }

                                @Override
                                public void onFailure(String errorMessage) {
                                    Toast.makeText(getContext(), "Cập nhật thất bại: " + errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Toast.makeText(getContext(), "Tải ảnh thất bại: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Nếu không có ảnh mới → cập nhật bình thường
                    course.setImagePath(courseToEdit.getImagePath()); // vẫn giữ lại ảnh cũ
                    repo.updateCourse(course.getId(), course, new CourseCallback() {
                        @Override
                        public void onSuccess(Course updatedCourse) {
                            listener.onSaved(updatedCourse, true);
                            dismiss();
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(view);
        dialog.setTitle(courseToEdit == null ? "Thêm khóa học" : "Sửa khóa học");
        return dialog;
    }


    private void uploadImage(Uri imageUri) {
        CourseManagementRepository repo = CourseManagementRepository.getInstance(getContext());
        if (courseToEdit == null || courseToEdit.getId() <= 0) {
            // Hiển thị ảnh mặc định
            Glide.with(CourseDialogFragment.this)
                    .load(R.drawable.placeholder_image) // hoặc placeholder_image tùy bạn
                    .into(ivCourseImage);

            Toast.makeText(getContext(), "Khóa học chưa được lưu, sử dụng ảnh mặc định", Toast.LENGTH_SHORT).show();
            return;
        }
        int courseId = courseToEdit.getId();
        repo.uploadCourseImage(courseId, imageUri, new ImageUploadCallback() {
            @Override
            public void onSuccess(String imageUrl) {
                if (courseToEdit == null) {
                    courseToEdit = new Course();
                }
                courseToEdit.setImagePath(imageUrl);

                Glide.with(CourseDialogFragment.this)
                        .load(imageUrl)
                        .placeholder(R.drawable.placeholder_image)
                        .into(ivCourseImage);

                Toast.makeText(getContext(), "Ảnh tải lên thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getContext(), "Tải ảnh thất bại: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
