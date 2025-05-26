package com.onlinecourse.backend.repository;

import com.onlinecourse.backend.model.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonProgressRepository extends JpaRepository<LessonProgress,Integer> {
}
