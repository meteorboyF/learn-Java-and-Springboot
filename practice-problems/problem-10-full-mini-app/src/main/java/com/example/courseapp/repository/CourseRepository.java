package com.example.courseapp.repository;

import com.example.courseapp.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// JpaRepository<Course, Long> — Course is the entity, Long is the @Id type.
// Spring generates the implementation automatically.
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // Derived query: findByCode → SELECT * FROM courses WHERE code = ?
    // Returns Optional because code is UNIQUE (0 or 1 result).
    Optional<Course> findByCode(String code);
}
