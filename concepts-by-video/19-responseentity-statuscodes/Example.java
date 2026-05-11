// Example: ResponseEntity patterns for all HTTP operations.

package com.example.app.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/{studentId}/students")
public class StudentController {

    // ─── 200 OK — GET all ────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<Student>> getAll() {
        List<Student> students = service.getAllStudents();
        return ResponseEntity.ok(students);  // 200 + JSON array body
        // Equivalent: ResponseEntity.status(HttpStatus.OK).body(students)
        // Equivalent: ResponseEntity.status(200).body(students)
    }

    // ─── 200 OK — GET one ────────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<Student> getById(@PathVariable Long id) {
        Student student = service.getStudentById(id);  // throws 404 if not found
        return ResponseEntity.ok(student);  // 200 + student JSON
    }

    // ─── 201 Created — POST ───────────────────────────────────────────────────
    // EXAM: POST → 201, NEVER 200. This is Bug 1 in Problem 12.
    @PostMapping
    public ResponseEntity<Student> create(
            @PathVariable String studentId,
            @RequestBody Student student) {
        Student saved = service.createStudent(student);

        // Option A: 201 + Location header + body (BEST PRACTICE)
        URI location = URI.create("/api/" + studentId + "/students/" + saved.getId());
        return ResponseEntity.created(location).body(saved);

        // Option B: 201 + body, no Location header (also acceptable)
        // return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        // Option C: 201 only (no body) — rare
        // return ResponseEntity.created(location).build();
    }

    // ─── 200 OK — PUT ────────────────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<Student> update(
            @PathVariable Long id,
            @RequestBody Student data) {
        Student updated = service.updateStudent(id, data);
        return ResponseEntity.ok(updated);  // 200 + updated student JSON
    }

    // ─── 204 No Content — DELETE ──────────────────────────────────────────────
    // EXAM: DELETE → 204 (no body), NEVER 200 (with body).
    // ResponseEntity<Void> — Void means "no response body type."
    // .noContent() = 204 status.
    // .build() = create the ResponseEntity with no body.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteStudent(id);  // throws 404 if not found
        return ResponseEntity.noContent().build();  // 204, no body
    }

    // ─── Adding custom headers ─────────────────────────────────────────────────
    @GetMapping("/with-header/{id}")
    public ResponseEntity<Student> getWithHeader(@PathVariable Long id) {
        Student student = service.getStudentById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Student-Major", student.getMajor());  // custom header
        return ResponseEntity.ok()             // 200
                .headers(headers)              // add custom headers
                .body(student);               // set body
    }
}
