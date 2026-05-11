// ============================================================
// StudentController.java — The Web Layer (REST Controller)
// Handles HTTP requests and returns HTTP responses.
// The controller is the "front door" of your application.
// It should contain NO business logic — only HTTP in/out + delegation to service.
// ============================================================
package com.example.springbootref.controller;

import com.example.springbootref.model.Student;
import com.example.springbootref.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

// @RestController — combines TWO annotations:
//   1. @Controller      — marks this class as a Spring MVC controller (Spring registers it)
//   2. @ResponseBody    — EVERY method's return value is automatically serialized to JSON
//                         and written to the HTTP response body.
// Without @ResponseBody (i.e., using plain @Controller), Spring tries to find
// a view template (HTML file) for the return value — not what REST APIs want.
// EXAM: @RestController is ALWAYS used for REST APIs. @Controller for MVC/HTML apps.
@RestController

// @RequestMapping — sets the BASE URL PREFIX for ALL methods in this controller.
// Every endpoint in this class will start with /api/{studentId}/students
//
// {studentId} in the URL path is a TEMPLATE VARIABLE — a placeholder that
// matches any value. Example:
//   /api/STU20240001/students → studentId = "STU20240001"
//   /api/12345/students       → studentId = "12345"
//
// EXAM: The quiz uses YOUR student ID number as the URL prefix.
//       This is just a convention to make each student's API unique.
//       The {studentId} here is the URL prefix variable, NOT the student's ID field!
//       Both happen to be called "studentId" in this example — be careful not to confuse them.
@RequestMapping("/api/{studentId}/students")
public class StudentController {

    // private final — always use this for injected dependencies (same reasoning as Service).
    private final StudentService studentService;

    // Constructor injection — Spring automatically calls this with the StudentService bean.
    // No @Autowired needed (single constructor rule).
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // ============================================================
    // GET /api/{studentId}/students → 200 OK + list of all students
    // ============================================================
    // @GetMapping — maps HTTP GET requests to this method.
    // No path specified here because @RequestMapping already defines the base path.
    // ResponseEntity<List<Student>> — the response contains a list of Student objects.
    // EXAM: GET = READ. No request body. Returns 200 OK on success.
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();

        // ResponseEntity.ok(body) — creates a 200 OK response with 'students' as the JSON body.
        // Jackson serializes List<Student> to: [{"id":1,"name":"Alice",...}, {...}]
        // EXAM: ResponseEntity.ok() = status 200 + body.
        return ResponseEntity.ok(students);
    }

    // ============================================================
    // GET /api/{studentId}/students/{id} → 200 OK or 404 if not found
    // ============================================================
    // @GetMapping("/{id}") — adds /{id} to the base path.
    // Full URL: /api/{studentId}/students/{id}
    //           studentId = URL prefix variable, id = the student's database primary key.
    //
    // @PathVariable Long id:
    //   Extracts the {id} segment from the URL and converts it to a Long.
    //   Example: /api/STU001/students/5 → id = 5L
    //   Spring handles the String-to-Long conversion automatically.
    //   EXAM: @PathVariable extracts values FROM the URL path (between slashes).
    //
    // 404 case: If getStudentById throws StudentNotFoundException,
    //           GlobalExceptionHandler catches it and returns 404 — no try-catch needed here.
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);  // 200 OK + student JSON
    }

    // ============================================================
    // GET /api/{studentId}/students/byStudentId?sid=STU001
    // Shows @RequestParam — reading query string parameters
    // ============================================================
    // @RequestParam reads values from the URL QUERY STRING (after the ?).
    // URL: /api/ABC/students/byStudentId?sid=STU20240001
    //   → sid = "STU20240001"
    //
    // Difference from @PathVariable:
    //   @PathVariable → reads from path: /students/{id}  → /students/5
    //   @RequestParam  → reads from query: /students?sid=STU001 → sid="STU001"
    // EXAM: Know when to use each one.
    //   Path variable: identifies a SPECIFIC resource (mandatory, part of the route).
    //   Query param: optional filtering, sorting, pagination.
    @GetMapping("/byStudentId")
    public ResponseEntity<Student> getByStudentId(@RequestParam("sid") String studentId) {
        // @RequestParam("sid") — the URL parameter name is "sid",
        // mapped to the Java parameter 'studentId'.
        Student student = studentService.getStudentByStudentId(studentId);
        return ResponseEntity.ok(student);
    }

    // ============================================================
    // POST /api/{studentId}/students → 201 Created + new student
    // ============================================================
    // @PostMapping — maps HTTP POST requests (used to CREATE new resources).
    // @RequestBody Student student — deserializes the JSON request body to a Student object.
    //   Client sends: {"name":"Alice","studentId":"STU001","gpa":3.5,"major":"CS"}
    //   Spring/Jackson converts it to a Student object automatically.
    //   EXAM: @RequestBody reads the HTTP body. Only POST/PUT have a request body.
    //
    // @PathVariable String studentId — captures the URL prefix variable.
    //   We need this here to build the Location header pointing to the new resource.
    //
    // Returns 201 Created (NOT 200 OK):
    //   201 = "I successfully created a new resource."
    //   200 = "I successfully processed your request (but didn't necessarily create anything)."
    //   EXAM: POST always returns 201, not 200. This is THE most common exam mistake.
    @PostMapping
    public ResponseEntity<Student> createStudent(
            @PathVariable String studentId,  // the URL prefix (e.g., "STU20240001")
            @RequestBody Student student) {  // the JSON body becomes a Student object

        Student saved = studentService.createStudent(student);  // save → gets db id

        // Location header: tells the client WHERE to find the newly created resource.
        // Best practice per HTTP spec (RFC 7231): POST response should include Location.
        // Example: Location: /api/STU20240001/students/7
        //   where 7 is the database-generated id of the new student.
        // EXAM: ResponseEntity.created(uri) automatically sets:
        //   - Status: 201 Created
        //   - Location header: the URI you pass in
        URI location = URI.create(
                "/api/" + studentId + "/students/" + saved.getId());

        return ResponseEntity
                .created(location)  // 201 + Location header
                .body(saved);       // + JSON body of the saved student (with its new id)
    }

    // ============================================================
    // PUT /api/{studentId}/students/{id} → 200 OK or 404
    // ============================================================
    // @PutMapping — maps HTTP PUT requests (used to UPDATE/REPLACE an existing resource).
    // PUT = full replacement (send ALL fields, even unchanged ones).
    // PATCH = partial update (send only changed fields) — not covered here.
    // EXAM: PUT returns 200 (with updated body) or 404 (if resource not found).
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable Long id,           // database PK from URL: /students/5
            @RequestBody Student updatedData) {  // JSON body with new values

        Student updated = studentService.updateStudent(id, updatedData);
        return ResponseEntity.ok(updated);  // 200 OK + updated student
    }

    // ============================================================
    // DELETE /api/{studentId}/students/{id} → 204 No Content or 404
    // ============================================================
    // @DeleteMapping — maps HTTP DELETE requests.
    // 204 No Content = "I successfully deleted it. There is nothing to return."
    //   204 has NO body — hence ResponseEntity<Void> (Void = no body type).
    //   EXAM: DELETE returns 204 (no body), NOT 200 (with body). Common mistake.
    //
    // .build() — creates the ResponseEntity without a body.
    //   ResponseEntity.ok(body) has a body.
    //   ResponseEntity.noContent().build() has NO body. EXAM: know both forms.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);      // throws StudentNotFoundException if not found
        return ResponseEntity.noContent().build();  // 204 No Content
    }
}
