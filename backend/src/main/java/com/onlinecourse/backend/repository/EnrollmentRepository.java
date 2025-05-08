package com.onlinecourse.backend.repository;

import com.onlinecourse.backend.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Integer> {
    List<Enrollment> findByUserId(int userId);
}
