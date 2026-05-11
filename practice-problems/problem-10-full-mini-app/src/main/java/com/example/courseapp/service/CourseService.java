package com.example.courseapp.service;

import com.example.courseapp.exception.CourseNotFoundException;
import com.example.courseapp.model.Course;
import com.example.courseapp.repository.CourseRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    // Constructor injection — single constructor, no @Autowired needed.
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // Get all courses → SELECT * FROM courses.
    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    // Get by course code (e.g., "CSE2218").
    // .orElseThrow() — throws CourseNotFoundException if code not found.
    // EXAM: Never use .get() — always .orElseThrow().
    public Course getByCode(String code) {
        return courseRepository.findByCode(code)
                .orElseThrow(() -> new CourseNotFoundException(
                        "Course not found with code: " + code));
    }

    // Create a new course — INSERT.
    public Course create(Course course) {
        return courseRepository.save(course);
    }

    // Delete by database id — check first, then delete.
    public void delete(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new CourseNotFoundException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }
}
