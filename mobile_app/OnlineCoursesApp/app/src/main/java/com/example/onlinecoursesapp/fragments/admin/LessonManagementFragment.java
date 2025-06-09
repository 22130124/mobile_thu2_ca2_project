package com.example.onlinecoursesapp.fragments.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.adapter.LessonManagementAdapter;
import com.example.onlinecoursesapp.data.CourseManagementRepository;
import com.example.onlinecoursesapp.data.LessonManagementRepository;
import com.example.onlinecoursesapp.models.Course;
import com.example.onlinecoursesapp.models.Lesson;
import com.example.onlinecoursesapp.utils.CourseListCallback;
import com.example.onlinecoursesapp.utils.LessonCallback;
import com.example.onlinecoursesapp.utils.LessonListCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class LessonManagementFragment extends Fragment {

    private RecyclerView recyclerView;
    private LessonManagementAdapter adapter;
    private List<Lesson> lessonList;
    private LessonManagementRepository lessonRepository;
    private CourseManagementRepository courseRepository;
    private FloatingActionButton fabAddLesson;
    private List<Course> courseList;
    private int selectedCourseId;
    private static final String ARG_COURSE_ID = "course_id";
    private ImageButton btnback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lesson_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewLessons);
        fabAddLesson = view.findViewById(R.id.fabAddLesson);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        btnback = view.findViewById(R.id.btnBack);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        lessonList = new ArrayList<>();
        adapter = new LessonManagementAdapter(getContext(), lessonList, new LessonManagementAdapter.OnLessonActionListener() {
            @Override
            public void onEdit(Lesson lesson) {
                showLessonForm(lesson);
            }

            @Override
            public void onDelete(Lesson lesson) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Xác nhận")
                        .setMessage("Bạn có chắc muốn xóa bài học này?")
                        .setPositiveButton("Đồng ý", (dialog, which) -> {
                            lessonRepository.deleteLesson(lesson.getId(), new LessonCallback() {
                                @Override
                                public void onSuccess(Lesson deletedLesson) {
                                    Toast.makeText(getContext(), "Xóa bài học thành công!", Toast.LENGTH_SHORT).show();
                                    loadLessonsFromApi(); // Tải lại danh sách bài học sau khi xóa
                                }

                                @Override
                                public void onFailure(String errorMessage) {
                                    Toast.makeText(getContext(), "Lỗi khi xóa bài học: " + errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            });
                        })
                        .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });

        recyclerView.setAdapter(adapter);

        lessonRepository = LessonManagementRepository.getInstance(getContext());
        courseRepository = CourseManagementRepository.getInstance(getContext());
        loadCourses();

        fabAddLesson.setOnClickListener(v -> showLessonForm(null));
    }

    private void loadCourses() {
        courseRepository.getAllCourses(new CourseListCallback() {
            @Override
            public void onSuccess(List<Course> courses) {
                courseList = courses;
                if (!courseList.isEmpty()) {
                    selectedCourseId = courseList.get(0).getId();
                    loadLessonsFromApi();
                } else {
                    lessonList.clear();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getContext(), "Lỗi tải khóa học: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadLessonsFromApi() {
        int courseId = requireArguments().getInt(ARG_COURSE_ID, -1);
        if (courseId == -1) return;

        lessonRepository.getLessonsByCourseId(courseId, new LessonListCallback() {
            @Override
            public void onSuccess(List<Lesson> lessons) {
                lessonList.clear();
                if (lessons != null && !lessons.isEmpty()) {
                    lessonList.addAll(lessons);
                }
                adapter.notifyDataSetChanged();

            }


            @Override
            public void onFailure(String errorMessage) {
                // Chỉ hiển thị lỗi nếu là lỗi kết nối/API, không phải do danh sách rỗng
                Toast.makeText(getContext(), "Chưa có bài học nào trong khóa học này.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLessonForm(@Nullable Lesson lesson) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.add_lesson_management, null);

        EditText etTitle = dialogView.findViewById(R.id.etTitle);
        EditText etContent = dialogView.findViewById(R.id.etContent);
        EditText etYoutubeUrl = dialogView.findViewById(R.id.etYoutubeUrl);
        Spinner courseSpinner = dialogView.findViewById(R.id.spinnerCourses);

        ArrayAdapter<String> courseAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, getCourseNames());
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(courseAdapter);

        // Nếu là sửa, thì load dữ liệu từ bài học
        if (lesson != null) {
            etTitle.setText(lesson.getTitle());
            etContent.setText(lesson.getContent());
            etYoutubeUrl.setText(lesson.getYoutubeVideoUrl());

            // Nếu lesson.getCourse() null
            if (lesson.getCourse() != null) {
                int position = getCoursePosition(lesson.getCourse().getId());
                courseSpinner.setSelection(position);
            }

            courseSpinner.setEnabled(false); // Khóa lại spinner khi sửa
        } else {
            // Nếu thêm mới, chọn course theo ARG_COURSE_ID từ arguments
            int courseId = requireArguments().getInt(ARG_COURSE_ID, -1);
            int position = getCoursePosition(courseId);
            courseSpinner.setSelection(position);
            courseSpinner.setEnabled(false); // Khóa spinner luôn khi thêm
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView)
                .setTitle(lesson == null ? "Thêm bài học mới" : "Chỉnh sửa bài học")
                .setPositiveButton("Lưu", null)
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String title = etTitle.getText().toString().trim();
                String content = etContent.getText().toString().trim();
                String youtubeUrl = etYoutubeUrl.getText().toString().trim();

                if (title.isEmpty() || content.isEmpty() || youtubeUrl.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                Course selectedCourse = courseList.get(courseSpinner.getSelectedItemPosition());

                new AlertDialog.Builder(getContext())
                        .setTitle("Xác nhận")
                        .setMessage(lesson == null ? "Bạn có chắc muốn thêm bài học này?" : "Bạn có chắc muốn cập nhật bài học này?")
                        .setPositiveButton("Đồng ý", (confirmDialog, whichButton) -> {
                            if (lesson == null) {
                                Lesson newLesson = new Lesson(title, content, youtubeUrl, 14.2);
                                newLesson.setCourse(selectedCourse);
                                lessonRepository.addLesson(newLesson, new LessonCallback() {
                                    @Override
                                    public void onSuccess(Lesson addedLesson) {
                                        Toast.makeText(getContext(), "Thêm bài học thành công!", Toast.LENGTH_SHORT).show();
                                        loadLessonsFromApi();
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onFailure(String errorMessage) {
                                        Toast.makeText(getContext(), "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                lesson.setTitle(title);
                                lesson.setContent(content);
                                lesson.setYoutubeVideoUrl(youtubeUrl);
                                lesson.setDurationMinutes(14.2);
                                lesson.setCourse(selectedCourse);

                                lessonRepository.updateLesson(lesson.getId(), lesson, new LessonCallback() {
                                    @Override
                                    public void onSuccess(Lesson updatedLesson) {
                                        Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                        loadLessonsFromApi();
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onFailure(String errorMessage) {
                                        Toast.makeText(getContext(), "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            });
        });

        dialog.show();
    }


    private List<String> getCourseNames() {
        List<String> courseNames = new ArrayList<>();
        if (courseList != null) {
            for (Course course : courseList) {
                courseNames.add(course.getTitle());
            }
        }
        return courseNames;
    }

    private int getCoursePosition(int courseId) {
        if (courseList != null) {
            for (int i = 0; i < courseList.size(); i++) {
                if (courseList.get(i).getId() == courseId) {
                    return i;
                }
            }
        }
        return 0;
    }
}
