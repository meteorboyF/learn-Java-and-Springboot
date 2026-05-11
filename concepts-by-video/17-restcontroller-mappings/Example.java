// Example: All HTTP mapping annotations demonstrated in one controller.

package com.example.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// @RestController — REST API controller. Return values are automatically JSON.
// EXAM: = @Controller + @ResponseBody. Use for all REST APIs.
@RestController

// @RequestMapping — sets the base URL for ALL methods in this class.
// Every endpoint starts with /api/{studentId}/students.
@RequestMapping("/api/{studentId}/students")
public class StudentController {

    // @GetMapping — handles HTTP GET requests.
    // No path argument → maps to the base path: GET /api/{studentId}/students
    // Returns: list of all students (200 OK).
    @GetMapping
    public ResponseEntity<List<Student>> getAll() {
        return ResponseEntity.ok(service.getAllStudents());
    }

    // @GetMapping("/{id}") — adds path suffix.
    // Full path: GET /api/{studentId}/students/{id}
    // @PathVariable Long id — extracts {id} from the URL.
    @GetMapping("/{id}")
    public ResponseEntity<Student> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getStudentById(id));
    }

    // @PostMapping — handles HTTP POST (create).
    // Returns: 201 Created (EXAM: not 200!).
    @PostMapping
    public ResponseEntity<Student> create(
            @PathVariable String studentId,   // the URL prefix variable
            @RequestBody Student student) {   // JSON body → Student object
        Student saved = service.createStudent(student);
        return ResponseEntity.status(201).body(saved);
    }

    // @PutMapping — handles HTTP PUT (full update).
    // Returns: 200 OK + updated resource.
    @PutMapping("/{id}")
    public ResponseEntity<Student> update(
            @PathVariable Long id,
            @RequestBody Student data) {
        return ResponseEntity.ok(service.updateStudent(id, data));
    }

    // @DeleteMapping — handles HTTP DELETE.
    // Returns: 204 No Content (EXAM: not 200!). No response body.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteStudent(id);
        return ResponseEntity.noContent().build();  // 204 + no body
    }

    // ─── Long form of mapping annotations (all equivalent) ───────────────────
    // These three are identical in effect:
    // @GetMapping("/test")
    // @RequestMapping(value = "/test", method = RequestMethod.GET)
    // The short forms (@GetMapping etc.) are preferred — less verbose.
}
