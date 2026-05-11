// SOLUTION — StudentController.java
// Location: src/main/java/com/example/app/controller/StudentController.java

package com.example.app.controller;

import com.example.app.model.Student;
import com.example.app.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

// @RestController — marks this class as a REST API controller.
// = @Controller (Spring registers it) + @ResponseBody (return values → JSON automatically).
// Without @ResponseBody, Spring tries to find an HTML view template for each return value.
// EXAM: @RestController vs @Controller — REST APIs always use @RestController.
@RestController

// @RequestMapping — base URL for ALL methods in this class.
// {studentId} is a PATH VARIABLE TEMPLATE — it matches ANY value in that position.
//   /api/STU20240001/students → studentId = "STU20240001"
//   /api/12345/students       → studentId = "12345"
//
// EXAM TRAP: This {studentId} is the URL PREFIX variable (your student number),
// NOT the student's database primary key (Long id). Both are called "studentId" here
// by coincidence. They are different things at different levels of the URL.
@RequestMapping("/api/{studentId}/students")
public class StudentController {

    private final StudentService studentService;

    // Constructor injection — no @Autowired needed (single constructor).
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // ─── Part (b): GET all students → 200 OK ────────────────────────────────
    // @GetMapping with no path argument maps to the base URL:
    //   GET /api/{studentId}/students
    // ResponseEntity.ok(body) creates a 200 OK response with the list as JSON body.
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    // ─── Part (c): GET one student → 200 OK or 404 ──────────────────────────
    // @GetMapping("/{id}") adds /{id} to the base path.
    // Full URL: GET /api/{studentId}/students/{id}
    //
    // @PathVariable Long id — extracts {id} from the URL, converts to Long automatically.
    //   Example: /api/STU001/students/5 → id = 5L
    // EXAM: @PathVariable extracts from the path. @RequestParam extracts from query string (?key=val).
    //
    // 404 case: if getStudentById throws StudentNotFoundException,
    //   GlobalExceptionHandler intercepts it and returns 404 — no try-catch needed here.
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    // ─── Part (d, g): POST create → 201 Created + Location header ────────────
    // @PostMapping — maps HTTP POST to this method.
    // @PathVariable String studentId — captures the URL prefix variable.
    //   We need this here ONLY to build the Location header URI.
    //   If you don't need the Location header, you wouldn't need this parameter.
    // @RequestBody Student student — Spring/Jackson deserializes the JSON body.
    //   Client sends: {"name":"Alice","studentId":"STU001","gpa":3.5,"major":"CS"}
    //   Spring creates: new Student() and calls setName("Alice"), setGpa(3.5), etc.
    //
    // Returns 201, NOT 200:
    //   201 = "I created a new resource."
    //   200 = "I processed your request."
    //   EXAM: This is Bug 1 in Problem 12. POST → 201, GET/PUT → 200, DELETE → 204.
    @PostMapping
    public ResponseEntity<Student> createStudent(
            @PathVariable String studentId,     // URL prefix variable
            @RequestBody Student student) {      // JSON body → Student object

        Student saved = studentService.createStudent(student);  // INSERT

        // Build the Location header URI pointing to the newly created resource.
        // Format: /api/{urlPrefix}/students/{newDatabaseId}
        // EXAM: ResponseEntity.created(uri) automatically:
        //   1. Sets status to 201 Created
        //   2. Adds "Location: <uri>" header to the response
        URI location = URI.create("/api/" + studentId + "/students/" + saved.getId());

        return ResponseEntity
                .created(location)   // sets 201 + Location header
                .body(saved);        // sets response body to the saved student (with its new id)
    }

    // ─── Part (e): PUT update → 200 OK or 404 ────────────────────────────────
    // @PutMapping("/{id}") — HTTP PUT for updating an existing resource.
    // Full URL: PUT /api/{studentId}/students/{id}
    // @PathVariable Long id — the database PK of the student to update.
    // @RequestBody Student updatedData — the new values from the client.
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable Long id,
            @RequestBody Student updatedData) {
        return ResponseEntity.ok(studentService.updateStudent(id, updatedData));
    }

    // ─── Part (f): DELETE → 204 No Content or 404 ────────────────────────────
    // @DeleteMapping("/{id}") — HTTP DELETE.
    // Full URL: DELETE /api/{studentId}/students/{id}
    //
    // Returns 204 No Content (not 200):
    //   204 = "Deletion succeeded. Nothing to return."
    //   200 = "Here is the deleted resource." (unusual for DELETE)
    //
    // ResponseEntity<Void> — Void means there is no response body.
    // .noContent() — sets status to 204.
    // .build() — creates the ResponseEntity without a body.
    // EXAM: Remember the pattern: .noContent().build() for 204 responses.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);          // throws StudentNotFoundException if not found
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // ─── BONUS: @RequestParam example ────────────────────────────────────────
    // URL: GET /api/{studentId}/students/search?major=CS
    // @RequestParam("major") → reads the 'major' query parameter from the URL.
    // Difference from @PathVariable:
    //   @PathVariable → /students/{id}   → part of the URL path (mandatory, structural)
    //   @RequestParam  → /students?major=CS → query string (often optional, for filtering)
    @GetMapping("/search")
    public ResponseEntity<List<Student>> searchByMajor(
            @RequestParam("major") String major) {
        return ResponseEntity.ok(studentService.getAllStudents().stream()
                .filter(s -> major.equalsIgnoreCase(s.getMajor()))
                .toList());
    }
}
