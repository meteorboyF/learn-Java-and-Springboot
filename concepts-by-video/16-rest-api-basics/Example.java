// Example: REST API basics demonstrated with a minimal StudentController.
// This file shows how HTTP methods map to CRUD operations and what status codes to return.

package com.example.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @RestController = @Controller + @ResponseBody
// Every method return value is automatically serialized to JSON.
// EXAM: @RestController is required for REST APIs. @Controller alone won't serialize to JSON.
@RestController

// @RequestMapping sets the BASE URL for ALL methods in this class.
// Every @GetMapping, @PostMapping, etc. is APPENDED to this base.
// EXAM: URL pattern uses nouns (students), NOT verbs (getStudents, createStudent).
@RequestMapping("/api/{studentId}/students")
public class StudentRestExample {

    // ─── GET /api/{studentId}/students ────────────────────────────────────────
    // Returns a list of all students.
    // No extra path needed — the @RequestMapping base IS the full URL.
    // Returns 200 OK automatically when you return a list from @RestController.
    @GetMapping
    public List<String> getAllStudents() {
        // Normally: return service.findAll();
        return List.of("Alice", "Bob", "Charlie"); // fake data for illustration
    }

    // ─── GET /api/{studentId}/students/5 ──────────────────────────────────────
    // Returns one student. The "/{id}" is appended to the base "/api/{studentId}/students".
    // Full URL: /api/STU001/students/5
    // EXAM: @PathVariable binds the {id} placeholder in the URL to the method parameter.
    @GetMapping("/{id}")
    public String getOneStudent(@PathVariable Long id) {
        // Normally: return service.findById(id);
        return "Student with id " + id; // fake for illustration
    }

    // ─── POST /api/{studentId}/students ───────────────────────────────────────
    // Creates a new student. Client sends JSON body → Spring deserializes it.
    // @RequestBody: take the JSON request body and convert it to a Java object.
    // EXAM: POST should return 201 Created, NOT 200 OK. This is Bug 1 in Problem 12.
    @PostMapping
    public ResponseEntity<String> createStudent(@RequestBody String requestBody) {
        // Normally: Student saved = service.create(request);
        String saved = "NewStudent";

        // ResponseEntity.created() sets status to 201 and accepts the Location URI.
        // ResponseEntity.ok() would set 200 — WRONG for POST!
        return ResponseEntity
                .status(HttpStatus.CREATED)  // 201 Created
                .body(saved);
    }

    // ─── PUT /api/{studentId}/students/5 ──────────────────────────────────────
    // Updates an existing student. Needs both the id (URL) and new data (body).
    // Returns 200 OK with the updated resource.
    @PutMapping("/{id}")
    public ResponseEntity<String> updateStudent(
            @PathVariable Long id,           // which student to update
            @RequestBody String requestBody  // new data from client
    ) {
        String updated = "UpdatedStudent#" + id;
        return ResponseEntity.ok(updated); // 200 OK with body
    }

    // ─── DELETE /api/{studentId}/students/5 ───────────────────────────────────
    // Deletes a student. Returns 204 No Content — success with NO response body.
    // EXAM: DELETE returns 204, not 200. Use .noContent().build() to get 204 with no body.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        // Normally: service.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content, no body
    }
}
