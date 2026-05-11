// SOLUTION — DTO Pattern
// Three logical files shown here: StudentRequest, StudentResponse, and updated StudentService.

// ════════════════════════════════════════════════════════════
// FILE 1: StudentRequest.java — what the CLIENT sends
// Location: src/main/java/com/example/app/dto/StudentRequest.java
// ════════════════════════════════════════════════════════════
package com.example.app.dto;

// StudentRequest — represents the data the client sends in a POST/PUT body.
// KEY RULE: NO 'id' field.
//   The client should never set the id — the database assigns it.
//   If the entity is used directly as @RequestBody, a malicious client could
//   send {"id":1,"role":"ADMIN",...} and overwrite another user's data.
//   A Request DTO without 'id' makes this impossible.
public class StudentRequest {
    private String name;
    private String studentId;  // university-assigned ID (client provides this)
    private Double gpa;
    private String major;

    // No-arg constructor — Jackson needs this to create the DTO during deserialization.
    // Process: Jackson calls new StudentRequest(), then setName(...), setGpa(...), etc.
    public StudentRequest() {}

    // Optional convenience constructor for tests.
    public StudentRequest(String name, String studentId, Double gpa, String major) {
        this.name = name; this.studentId = studentId; this.gpa = gpa; this.major = major;
    }

    // BOTH getters AND setters — Jackson needs setters for deserialization (JSON → DTO).
    public String getName()      { return name; }
    public void   setName(String name)           { this.name = name; }
    public String getStudentId() { return studentId; }
    public void   setStudentId(String studentId) { this.studentId = studentId; }
    public Double getGpa()       { return gpa; }
    public void   setGpa(Double gpa)             { this.gpa = gpa; }
    public String getMajor()     { return major; }
    public void   setMajor(String major)         { this.major = major; }
}

// ════════════════════════════════════════════════════════════
// FILE 2: StudentResponse.java — what the SERVER returns
// Location: src/main/java/com/example/app/dto/StudentResponse.java
// ════════════════════════════════════════════════════════════
package com.example.app.dto;

// StudentResponse — represents the data the server sends back to the client.
// KEY RULE: NO 'gpa' field / getter — gpa is intentionally hidden.
//   Jackson serializes ONLY fields with public getters.
//   Since there is NO getGpa() method, gpa never appears in the JSON response.
//   This demonstrates how DTOs control data visibility without changing the entity.
public class StudentResponse {
    private Long   id;        // included — client needs this to reference the resource
    private String name;      // included — display purposes
    private String studentId; // included — the public university ID
    private String major;     // included — useful public info
    // gpa is EXCLUDED intentionally — pretend it's sensitive academic data

    // Constructor — all fields set at once (immutable DTO).
    public StudentResponse(Long id, String name, String studentId, String major) {
        this.id = id; this.name = name; this.studentId = studentId; this.major = major;
    }

    // Getters ONLY — Response DTOs are never deserialized (no setters needed).
    // Jackson uses these getters to produce the JSON output.
    // EXAM: No getGpa() → no gpa in JSON. This is how you hide fields.
    public Long   getId()        { return id; }
    public String getName()      { return name; }
    public String getStudentId() { return studentId; }
    public String getMajor()     { return major; }
}

// ════════════════════════════════════════════════════════════
// FILE 3: Updated StudentService.java — with DTO conversion methods
// Location: src/main/java/com/example/app/service/StudentService.java
// ════════════════════════════════════════════════════════════
package com.example.app.service;

import com.example.app.dto.StudentRequest;
import com.example.app.dto.StudentResponse;
import com.example.app.exception.StudentNotFoundException;
import com.example.app.model.Student;
import com.example.app.repository.StudentRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // ─── Conversion: Request DTO → Entity ────────────────────────────────────
    // Called when client creates/updates a student.
    // Converts the incoming request DTO into a Student entity for saving.
    // 'private' because it's an implementation detail of this service.
    private Student toEntity(StudentRequest request) {
        // Notice: id is NOT set here — the database assigns it.
        return new Student(
                request.getName(),
                request.getStudentId(),
                request.getGpa(),
                request.getMajor()
        );
    }

    // ─── Conversion: Entity → Response DTO ────────────────────────────────────
    // Called when returning student data to the client.
    // Converts a Student entity into a StudentResponse DTO.
    // Notice: gpa is NOT included → it won't appear in JSON responses.
    private StudentResponse toResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getName(),
                student.getStudentId(),
                student.getMajor()
                // student.getGpa() is intentionally NOT passed → hidden from response
        );
    }

    // ─── getAllStudents() with DTO ─────────────────────────────────────────────
    // Returns List<StudentResponse> (not List<Student>).
    // Uses stream + map to convert each Student entity to a StudentResponse DTO.
    // this::toResponse = method reference shorthand for student -> toResponse(student)
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::toResponse)          // convert each entity to response DTO
                .collect(Collectors.toList());  // collect stream back to a List
    }

    // ─── createStudent() with DTO ──────────────────────────────────────────────
    // Accepts StudentRequest (from client), returns StudentResponse (to client).
    // The Student entity is an implementation detail that never leaves this service.
    public StudentResponse createStudent(StudentRequest request) {
        Student entity = toEntity(request);        // 1. convert request DTO → entity
        Student saved  = studentRepository.save(entity);  // 2. INSERT → gets assigned id
        return toResponse(saved);                  // 3. convert saved entity → response DTO
    }
}

// ════════════════════════════════════════════════════════════
// FILE 4: Updated Controller — uses DTOs instead of Student entity directly
// Only the POST and GET methods are shown (others are similar).
// ════════════════════════════════════════════════════════════

// Updated controller POST endpoint:
//   @PostMapping
//   public ResponseEntity<StudentResponse> create(
//           @PathVariable String studentId,
//           @RequestBody StudentRequest request) {    // ← takes StudentRequest DTO
//       StudentResponse saved = service.createStudent(request);
//       URI loc = URI.create("/api/" + studentId + "/students/" + saved.getId());
//       return ResponseEntity.created(loc).body(saved);  // ← returns StudentResponse DTO
//   }
//
// Updated controller GET all endpoint:
//   @GetMapping
//   public ResponseEntity<List<StudentResponse>> getAll() {  // ← List<StudentResponse>
//       return ResponseEntity.ok(service.getAllStudents());
//   }
