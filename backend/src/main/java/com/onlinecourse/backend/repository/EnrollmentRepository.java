package com.onlinecourse.backend.repository;

import com.onlinecourse.backend.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Integer> {
    List<Enrollment> findByUserId(int userId);

    boolean existsByUserIdAndCourseId(int userId, int courseId);

    Optional<Enrollment> findByUserIdAndCourseId(int userId, int courseId);
}
