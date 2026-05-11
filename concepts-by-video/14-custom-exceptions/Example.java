// Example: Custom exception classes for a Spring Boot app.
package com.example.app.exception;

// ─── StudentNotFoundException (the most common pattern) ──────────────────────
// extends RuntimeException = UNCHECKED = no try-catch required in callers.
// GlobalExceptionHandler catches this and returns HTTP 404.
public class StudentNotFoundException extends RuntimeException {

    // Constructor 1: any custom message
    public StudentNotFoundException(String message) {
        super(message);  // passes message to RuntimeException — accessible via getMessage()
    }

    // Constructor 2: convenience — builds message from Long id
    public StudentNotFoundException(Long id) {
        super("Student not found with id: " + id);
        // Usage: throw new StudentNotFoundException(5L);
        // Message: "Student not found with id: 5"
    }
}

// ─── CourseNotFoundException ─────────────────────────────────────────────────
class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(String message) { super(message); }
}

// ─── ProductNotFoundException (for in-memory service variant) ────────────────
class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) { super(message); }
}

// ─── DuplicateStudentIdException (for 409 Conflict) ─────────────────────────
// Handler maps this to 409 Conflict (duplicate resource).
class DuplicateStudentIdException extends RuntimeException {
    public DuplicateStudentIdException(String studentId) {
        super("Student with studentId '" + studentId + "' already exists.");
    }
}

// ─── Using these exceptions in a service ─────────────────────────────────────
// throw new StudentNotFoundException(id);               // in service → caught → 404
// throw new StudentNotFoundException("Not found: " + s); // in service → caught → 404
// throw new IllegalArgumentException("GPA invalid");   // built-in → caught → 400
// throw new DuplicateStudentIdException("STU001");      // custom → caught → 409
