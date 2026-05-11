// SOLUTION — Two files: StudentNotFoundException.java and StudentService.java
// In a real project these are separate files. Shown together here for convenience.

// ════════════════════════════════════════════════════════════
// FILE 1: StudentNotFoundException.java
// Location: src/main/java/com/example/app/exception/StudentNotFoundException.java
// ════════════════════════════════════════════════════════════
package com.example.app.exception;

// extends RuntimeException (UNCHECKED) — not Exception (CHECKED).
// WHY: Unchecked exceptions do NOT require try-catch or 'throws' declarations.
//      They bubble up automatically to the GlobalExceptionHandler.
//      EXAM: This choice is explicitly tested — know why RuntimeException, not Exception.
public class StudentNotFoundException extends RuntimeException {

    // Constructor 1: custom message
    public StudentNotFoundException(String message) {
        super(message); // stores message — accessible via ex.getMessage()
    }

    // Constructor 2: convenience — builds message from id
    public StudentNotFoundException(Long id) {
        super("Student not found with id: " + id);
    }
}

// ════════════════════════════════════════════════════════════
// FILE 2: StudentService.java
// Location: src/main/java/com/example/app/service/StudentService.java
// ════════════════════════════════════════════════════════════
package com.example.app.service;

import com.example.app.exception.StudentNotFoundException;
import com.example.app.model.Student;
import com.example.app.repository.StudentRepository;
import org.springframework.stereotype.Service;
import java.util.List;

// @Service — marks this as a Spring service bean (singleton).
// Communicates intent: "this class contains business logic."
@Service
public class StudentService {

    // private final — immutable reference. Cannot be changed after construction.
    // This ensures the service always has a valid repo and is thread-safe.
    private final StudentRepository studentRepository;

    // Constructor injection — single constructor → @Autowired is optional.
    // EXAM: Prefer this over @Autowired field injection.
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // ─── Part (a): Get all students ──────────────────────────────────────
    // repo.findAll() → SELECT * FROM students → returns List<Student>
    // Returns empty List (never null) if no students exist.
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // ─── Part (b): Get by database primary key (Long id) ─────────────────
    // findById(id) returns Optional<Student>.
    // .orElseThrow() → if Optional is empty, run the lambda and throw the exception.
    // EXAM: NEVER use .get() — it throws NoSuchElementException (not caught by handler → 500).
    //       ALWAYS use .orElseThrow() — it throws YOUR exception (caught by handler → 404).
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    // ─── Part (c): Get by university studentId field ─────────────────────
    // Uses custom repo method. Same orElseThrow pattern.
    // Note: 'id' (Long) ≠ 'studentId' (String) — different fields entirely.
    public Student getStudentByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student not found with studentId: " + studentId));
    }

    // ─── Part (d): Create a new student ──────────────────────────────────
    // repo.save() detects id == null → generates INSERT SQL.
    // Returns the SAVED entity — now has an auto-generated id.
    // EXAM: Return the saved entity, not the input — the input doesn't have an id yet.
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    // ─── Part (e): Update existing student ──────────────────────────────
    // Pattern: find → modify → save
    //
    // Step 1: getStudentById(id) — reuses our method above (throws if not found).
    //         We don't duplicate the "not found" logic.
    //
    // Step 2: Update ONLY fields the client should be able to change.
    //         NEVER update: id (DB key), studentId (immutable university ID).
    //         UPDATE: name, gpa, major (user-modifiable profile data).
    //
    // Step 3: repo.save(existing) — existing has an id → Spring generates UPDATE SQL.
    //
    // EXAM: Never trust the request body's id field. Always use the URL path variable.
    public Student updateStudent(Long id, Student updatedData) {
        Student existing = getStudentById(id);              // Step 1: find or throw
        existing.setName(updatedData.getName());            // Step 2: update only safe fields
        existing.setGpa(updatedData.getGpa());
        existing.setMajor(updatedData.getMajor());
        return studentRepository.save(existing);            // Step 3: UPDATE in DB
    }

    // ─── Part (f): Delete a student ─────────────────────────────────────
    // Pattern: check existence → then delete.
    //
    // WHY check first?
    //   In Spring Boot 3.x, deleteById() silently does nothing if id not found.
    //   Without the check, deleting id=999 (non-existent) returns 200 instead of 404.
    //   That's incorrect HTTP behavior — 404 should be returned for non-existent resources.
    //
    // WHY existsById() and not getStudentById()?
    //   existsById() runs: SELECT COUNT(*) WHERE id=? → just a count, no object loaded.
    //   getStudentById() fetches the full Student object → wasted DB work just to delete it.
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {   // efficient existence check
            throw new StudentNotFoundException(id); // throw before attempting delete
        }
        studentRepository.deleteById(id);           // DELETE WHERE id=?
    }
}
