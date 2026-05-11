// ============================================================
// StudentRequest.java — DTO for incoming client requests
// Represents what the CLIENT sends to the server (HTTP request body).
// This is different from the Student @Entity — it's just a data carrier.
//
// WHY A SEPARATE REQUEST DTO?
//   Security: The Student entity has an 'id' field. If you accept the entity
//   directly in @RequestBody, a malicious client could include id=1 in the JSON
//   and potentially overwrite another student's data.
//   StudentRequest has NO id field — the client cannot influence the id.
//
//   Flexibility: The API contract (what the client sends) is decoupled from the
//   database schema (what Hibernate manages). You can change one without the other.
//
//   EXAM: DTOs are tested. Know the DTO pattern: Request DTO in, Entity in DB,
//         Response DTO out.
// ============================================================
package com.example.springbootref.dto;

public class StudentRequest {

    // These fields are what the CLIENT sends in the POST/PUT request body.
    // NO 'id' field — the database assigns the id; the client has no say.
    private String name;
    private String studentId;  // the university ID number the student provides
    private Double gpa;
    private String major;

    // No-arg constructor — required by Jackson for JSON deserialization.
    // When Jackson reads: {"name":"Alice","gpa":3.5}
    // It creates a new StudentRequest() and then calls setName("Alice"), setGpa(3.5).
    // Without this constructor, Jackson throws an error.
    public StudentRequest() {}

    // Convenience constructor (optional — useful in tests).
    public StudentRequest(String name, String studentId, Double gpa, String major) {
        this.name      = name;
        this.studentId = studentId;
        this.gpa       = gpa;
        this.major     = major;
    }

    // Getters — Jackson uses these to READ values when converting DTO to JSON.
    // Setters — Jackson uses these to WRITE values when converting JSON to DTO.
    // EXAM: You need BOTH getters and setters for Jackson to work correctly.
    public String getName()      { return name; }
    public void   setName(String name)           { this.name = name; }
    public String getStudentId() { return studentId; }
    public void   setStudentId(String studentId) { this.studentId = studentId; }
    public Double getGpa()       { return gpa; }
    public void   setGpa(Double gpa)             { this.gpa = gpa; }
    public String getMajor()     { return major; }
    public void   setMajor(String major)         { this.major = major; }
}
