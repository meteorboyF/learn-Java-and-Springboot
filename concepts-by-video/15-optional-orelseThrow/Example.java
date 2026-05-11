// Example: Optional and orElseThrow in a Spring service context.

package com.example.app.service;

import java.util.Optional;

public class OptionalExamples {

    // ─── Scenario 1: orElseThrow (USE THIS) ──────────────────────────────────
    // The lambda () -> new StudentNotFoundException(id) is only executed if the Optional is EMPTY.
    // If the Optional has a value, the lambda is never called (lazy evaluation).
    public Student getById_Correct(Long id) {
        return studentRepository.findById(id)   // returns Optional<Student>
                .orElseThrow(() ->              // if empty: run this lambda
                        new StudentNotFoundException("Student not found with id: " + id));
        // If Optional has a Student: that Student is returned.
        // If Optional is empty: StudentNotFoundException is thrown.
        // GlobalExceptionHandler catches StudentNotFoundException → 404 response.
    }

    // ─── Scenario 2: .get() (NEVER DO THIS) ──────────────────────────────────
    public Student getById_Wrong(Long id) {
        return studentRepository.findById(id).get();  // DANGEROUS!
        // If Optional has a Student: that Student is returned. (works)
        // If Optional is empty: throws NoSuchElementException (NOT StudentNotFoundException)
        //   → GlobalExceptionHandler doesn't catch NoSuchElementException
        //   → Falls through to catch-all Exception handler
        //   → Returns 500 Internal Server Error
        //   → Test expects 404, gets 500 → TEST FAILS
    }

    // ─── Scenario 3: orElse — return a default value ─────────────────────────
    public Student getByIdOrDefault(Long id) {
        Student defaultStudent = new Student("Unknown", "N/A", null, "Unknown");
        return studentRepository.findById(id).orElse(defaultStudent);
        // If found: returns the DB student.
        // If not found: returns the defaultStudent (no exception thrown).
        // Use when: a missing value is acceptable and you have a sensible default.
    }

    // ─── Scenario 4: isPresent / ifPresent ───────────────────────────────────
    public boolean studentExists(String studentId) {
        Optional<Student> opt = studentRepository.findByStudentId(studentId);
        return opt.isPresent();  // true if found, false if not
    }

    public void processIfFound(Long id) {
        studentRepository.findById(id)
                .ifPresent(student -> {       // lambda runs ONLY if value present
                    System.out.println("Found: " + student.getName());
                });
        // If not found: nothing happens (no exception, no null check needed)
    }

    // ─── Scenario 5: map — transform the value inside Optional ───────────────
    public String getNameById(Long id) {
        return studentRepository.findById(id)
                .map(Student::getName)        // if present: extract the name
                .orElse("Unknown");           // if empty: return "Unknown"
        // Avoids: if (student != null) { return student.getName(); } else { return "Unknown"; }
    }
}
