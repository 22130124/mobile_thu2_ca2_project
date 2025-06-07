package com.onlinecourse.backend.controller;


import com.onlinecourse.backend.dto.CourseOverview;
import com.onlinecourse.backend.model.Course;
import com.onlinecourse.backend.model.User;
import com.onlinecourse.backend.repository.CourseRepository;
import com.onlinecourse.backend.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/courses")
@CrossOrigin
public class CourseContr {
    private final CourseRepository courseRepository;
    private final CourseService courseService;

    @Value("${file.upload-base-dir}")
    private String uploadDir;
    public CourseContr(CourseRepository courseRepository, CourseService courseService) {
        this.courseRepository = courseRepository;
        this.courseService = courseService;
    }

    // lay tat ca khoa hoc
    @GetMapping
    public List<Course> getAllCourses() {
        return this.courseRepository.findAll();
    }

    // Thủy - Lấy khoá học theo ID
    @GetMapping("/{id}")
    public ResponseEntity<CourseOverview> getCourseOverviewById(@PathVariable int id) {
        CourseOverview overview = courseService.getCourseOverviewById(id);
        return (overview != null) ? ResponseEntity.ok(overview) : ResponseEntity.notFound().build();
    }

    // Tìm kiếm khóa học theo từ khóa
    @GetMapping("/search")
    public List<Course> searchCourses(@RequestParam String query) {
        return courseRepository.findByTitleContainingIgnoreCase(query);
    }

    //Chỉnh sửa khóa học - Huong
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable int id, @RequestBody Course course) {
        System.out.println(course.toString());
        return courseRepository.findById(id)
                .map(existingCourse -> {
                   existingCourse.setCourse(course);
                    Course updated = courseRepository.save(existingCourse);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    //Xóa khóa học - Huong
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable int id) {
        if (!courseRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        courseRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // HTTP 204
    }

    //Thêm khóa học - Hương
    @PostMapping
    public ResponseEntity<Course> addCourse(@RequestBody Course course) {
        Course savedCourse = courseRepository.save(course);
        return ResponseEntity.status(201).body(savedCourse);
    }

    //Lấy khóa học theo category - Hương
    @GetMapping("/category/{id}")
    public List<Course> getCourseByCategoryId(@PathVariable("id") int categoryId) {
        return courseRepository.findByCategoryId(categoryId);
    }
    // Huong - Lấy khoá học theo ID
    @GetMapping("/management/{id}")
    public ResponseEntity<Course> getManagementCourseById(@PathVariable int id) {
        Optional<Course> course = courseRepository.findById(id);
        return course.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    // Upload ảnh khóa học
    @PostMapping("/upload-image/{id}")
    public ResponseEntity<?> uploadCourseImage(@PathVariable int id, @RequestParam("file") MultipartFile file) {
        try {
            Optional<Course> courseOpt = courseRepository.findById(id);
            if (courseOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
            }

            Course course = courseOpt.get();
            String courseImgDir = uploadDir + "/course_img";
            Path uploadPath = Paths.get(courseImgDir);

            // Tạo thư mục nếu chưa tồn tại
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Xoá ảnh cũ nếu có
            if (course.getImagePath() != null && !course.getImagePath().isEmpty()) {
                Path oldFilePath = Paths.get(uploadDir, course.getImagePath());
                Files.deleteIfExists(oldFilePath);
            }

            // Lưu ảnh mới
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Cập nhật đường dẫn ảnh
            String fileUrl = "/course_img/" + fileName;
            System.out.println(fileUrl);
            course.setImagePath(fileUrl);
            courseRepository.save(course);
            return ResponseEntity.ok().body(Map.of("imagePath", fileUrl));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        }
    }


}
