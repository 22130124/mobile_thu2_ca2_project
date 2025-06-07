package com.onlinecourse.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "category_id")
    private int categoryId;

    //Cho phép null vì khi add course sẽ thực hiện add trước
    // -> Nếu thành công mới setImg
    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "number_of_lessons")
    private int numberOfLessons;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Lesson> lessons;


    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

    /*
   Hương: Set course, chỉ cập nhật lại các giá trị khác null
    */
    public void setCourse(Course course) {
        if (course.getTitle() != null) {
            this.title = course.getTitle();
        }
        if (course.getDescription() != null) {
            this.description = course.getDescription();
        }
        if (course.getCategoryId() != 0) {
            this.categoryId = course.getCategoryId();
        }
        if (course.getImagePath() != null) {
            this.imagePath = course.getImagePath();
        }
        if (course.getNumberOfLessons() != 0) {
            this.numberOfLessons = course.getNumberOfLessons();
        }
        if (course.getDifficulty() != null) {
            this.difficulty = course.getDifficulty();
        }
    }

    /*
    Hương: Khi ngày có giá trị null, cập nhật giá trị là now
     */
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}

