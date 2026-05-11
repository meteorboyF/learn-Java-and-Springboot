// Example: Correct layer structure — each layer doing ONLY its job.
// Reading top-to-bottom shows the full request/response lifecycle.

package com.example.app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.*;
import java.util.List;

// ─── MODEL LAYER — data structure, no logic ──────────────────────────────────
@Entity
@Table(name = "courses")
class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;

    public Course() {}  // required

    // Getters and setters
    public Long getId()       { return id; }
    public String getName()   { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode()   { return code; }
    public void setCode(String code) { this.code = code; }
}

// ─── REPOSITORY LAYER — database access only ─────────────────────────────────
// @Repository is implied by extending JpaRepository.
// This layer ONLY talks to the database. No HTTP, no business rules.
interface CourseRepository extends JpaRepository<Course, Long> {
    // JpaRepository provides: save, findById, findAll, deleteById, existsById, count
}

// ─── EXCEPTION — belongs in exception/ package ───────────────────────────────
class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(Long id) {
        super("Course not found with id: " + id);
    }
}

// ─── SERVICE LAYER — business logic only ─────────────────────────────────────
// @Service makes this a Spring bean. Constructor injection for the repo.
// This layer knows NOTHING about HTTP. No @RequestBody, no ResponseEntity, no status codes.
@Service
class CourseService {

    private final CourseRepository repo; // inject via constructor — no @Autowired needed

    public CourseService(CourseRepository repo) {
        this.repo = repo;
    }

    // Business logic lives here — e.g., "you can't save a course without a code"
    public Course create(Course course) {
        if (course.getCode() == null || course.getCode().isBlank()) {
            throw new IllegalArgumentException("Course code is required"); // 400 Bad Request
        }
        return repo.save(course);
    }

    public List<Course> findAll() {
        return repo.findAll();
    }

    public Course findById(Long id) {
        return repo.findById(id)
                   .orElseThrow(() -> new CourseNotFoundException(id)); // 404 if missing
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) throw new CourseNotFoundException(id);
        repo.deleteById(id);
    }
}

// ─── CONTROLLER LAYER — HTTP only ────────────────────────────────────────────
// @RestController handles HTTP. It ONLY calls the service and wraps results in ResponseEntity.
// This layer knows NOTHING about SQL or business rules.
@RestController
@RequestMapping("/api/{studentId}/courses")
class CourseController {

    private final CourseService service; // inject service, NOT repository directly

    public CourseController(CourseService service) {
        this.service = service;
    }

    @GetMapping
    public List<Course> getAll() {
        return service.findAll(); // delegate to service
    }

    @GetMapping("/{id}")
    public Course getById(@PathVariable Long id) {
        return service.findById(id); // service throws 404 if not found
    }

    @PostMapping
    public ResponseEntity<Course> create(
            @PathVariable String studentId,
            @RequestBody Course course) {
        Course saved = service.create(course);
        return ResponseEntity
                .created(java.net.URI.create("/api/" + studentId + "/courses/" + saved.getId()))
                .body(saved); // 201 Created
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}

// ─── LAYER VIOLATION EXAMPLE (what NOT to do) ────────────────────────────────
/*
@RestController
class BadController {
    @Autowired
    private CourseRepository repo; // ← WRONG: controller should never inject a repository

    @GetMapping("/courses/{id}")
    public Course get(@PathVariable Long id) {
        return repo.findById(id).get(); // ← WRONG: business logic (get vs orElseThrow) in controller
    }
}
*/
