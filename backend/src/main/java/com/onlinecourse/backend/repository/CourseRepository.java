package com.onlinecourse.backend.repository;

import com.onlinecourse.backend.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    List<Course> findByTitleContainingIgnoreCase(String query);
}
