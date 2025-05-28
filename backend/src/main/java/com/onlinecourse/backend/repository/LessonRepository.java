package com.onlinecourse.backend.repository;

import com.onlinecourse.backend.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findByTitleContainingIgnoreCase(String query);

    List<Lesson> findByCourseId(int courseId);
}
