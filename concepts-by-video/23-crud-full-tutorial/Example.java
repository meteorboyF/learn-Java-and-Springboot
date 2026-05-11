// Example: Complete CRUD — all 5 operations wired together across 3 layers.
// This is a self-contained file showing the essential code for each layer.

package com.example.app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.*;
import java.net.URI;
import java.util.List;
import java.util.Optional;

// ═══════════════════════════════════════════════════════════════════════════════
// LAYER 1: ENTITY
// ═══════════════════════════════════════════════════════════════════════════════
@Entity
@Table(name = "students")
class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment
    private Long id;

    private String name;
    private String major;
    private Double gpa;

    public Student() {} // required no-arg constructor — Bug 3 if missing

    // Getters and setters (Jackson + JPA both need them)
    public Long getId()       { return id; }
    public String getName()   { return name; }
    public void setName(String name) { this.name = name; }
    public String getMajor()  { return major; }
    public void setMajor(String major) { this.major = major; }
    public Double getGpa()    { return gpa; }
    public void setGpa(Double gpa) { this.gpa = gpa; }
}

// ═══════════════════════════════════════════════════════════════════════════════
// LAYER 2: REPOSITORY
// ═══════════════════════════════════════════════════════════════════════════════
// Extending JpaRepository<Student, Long> gives us for free:
// save(), findById(), findAll(), deleteById(), existsById(), count(), and more.
interface StudentRepository extends JpaRepository<Student, Long> {
    // No extra methods needed for basic CRUD
}

// Custom exception for 404 responses
class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(Long id) {
        super("Student not found with id: " + id);
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// LAYER 3: SERVICE
// ═══════════════════════════════════════════════════════════════════════════════
@Service
class StudentService {

    private final StudentRepository repo;

    // Constructor injection — Spring injects the repo automatically.
    public StudentService(StudentRepository repo) {
        this.repo = repo;
    }

    // CREATE — save new student, return with generated id
    public Student create(Student student) {
        return repo.save(student); // save() returns the saved entity (id is now populated)
    }

    // READ ALL — return list of all students
    public List<Student> findAll() {
        return repo.findAll();
    }

    // READ ONE — find by id, throw 404 if not found
    public Student findById(Long id) {
        // orElseThrow: if Optional is empty, throws the given exception.
        // EXAM: Never use .get() here — use .orElseThrow() (Bug 2 if .get() is used).
        return repo.findById(id)
                   .orElseThrow(() -> new StudentNotFoundException(id));
    }

    // UPDATE — find first (throws 404 if missing), modify, save
    public Student update(Long id, Student updated) {
        Student existing = findById(id);   // reuse findById — throws 404 automatically
        existing.setName(updated.getName()); // only update the fields that should change
        existing.setMajor(updated.getMajor());
        existing.setGpa(updated.getGpa());
        return repo.save(existing);        // save() updates because id already exists
    }

    // DELETE — confirm exists first, then delete
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new StudentNotFoundException(id); // throw 404 if not found
        }
        repo.deleteById(id); // remove the row from the database
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// LAYER 4: CONTROLLER
// ═══════════════════════════════════════════════════════════════════════════════
@RestController
@RequestMapping("/api/{studentId}/students")
class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    // GET /api/{studentId}/students — all students, 200 OK
    @GetMapping
    public List<Student> getAll() {
        return service.findAll();
    }

    // GET /api/{studentId}/students/5 — one student, 200 OK or 404
    @GetMapping("/{id}")
    public Student getById(@PathVariable Long id) {
        return service.findById(id); // throws StudentNotFoundException → caught by @RestControllerAdvice
    }

    // POST /api/{studentId}/students — create, 201 Created
    @PostMapping
    public ResponseEntity<Student> create(
            @PathVariable String studentId,
            @RequestBody Student student) {
        Student saved = service.create(student);
        URI location = URI.create("/api/" + studentId + "/students/" + saved.getId());
        return ResponseEntity.created(location).body(saved); // 201
    }

    // PUT /api/{studentId}/students/5 — update, 200 OK or 404
    @PutMapping("/{id}")
    public ResponseEntity<Student> update(
            @PathVariable Long id,
            @RequestBody Student updated) {
        Student saved = service.update(id, updated);
        return ResponseEntity.ok(saved); // 200
    }

    // DELETE /api/{studentId}/students/5 — delete, 204 No Content or 404
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
