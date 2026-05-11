package com.example.courseapp.controller;

import com.example.courseapp.model.Course;
import com.example.courseapp.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

// @RestController — REST API controller (auto-JSON responses).
@RestController
// Base URL: /api/{studentId}/courses
// {studentId} = your student number (e.g., STU20240001) used as URL prefix.
@RequestMapping("/api/{studentId}/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // GET /api/{studentId}/courses → 200 OK + all courses
    @GetMapping
    public ResponseEntity<List<Course>> getAll() {
        return ResponseEntity.ok(courseService.getAll());
    }

    // GET /api/{studentId}/courses/code/{code} → 200 or 404
    // Example: GET /api/STU20240001/courses/code/CSE2218
    @GetMapping("/code/{code}")
    public ResponseEntity<Course> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(courseService.getByCode(code));
    }

    // POST /api/{studentId}/courses → 201 Created + Location header
    // EXAM: POST must return 201 (not 200). ResponseEntity.created(uri) handles this.
    @PostMapping
    public ResponseEntity<Course> create(
            @PathVariable String studentId,  // URL prefix for Location header
            @RequestBody Course course) {
        Course saved = courseService.create(course);
        // Location header: where the new resource can be found
        URI location = URI.create("/api/" + studentId + "/courses/" + saved.getId());
        return ResponseEntity.created(location).body(saved);  // 201 + Location + body
    }

    // DELETE /api/{studentId}/courses/{id} → 204 No Content or 404
    // EXAM: DELETE returns 204 (no body), not 200. ResponseEntity<Void> + .noContent().build()
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();  // 204 No Content
    }
}
