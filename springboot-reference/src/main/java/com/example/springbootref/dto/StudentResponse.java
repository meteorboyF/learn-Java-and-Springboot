// ============================================================
// StudentResponse.java — DTO for outgoing server responses
// Represents what the SERVER returns to the client (HTTP response body).
// Controls WHICH fields the client can see — you may hide sensitive ones.
//
// WHY A SEPARATE RESPONSE DTO?
//   Data hiding: The Student entity might have sensitive fields (e.g., gpa,
//   internal flags, audit fields). By returning a Response DTO instead of the
//   entity, you control exactly what the client sees.
//   In this example: gpa is INTENTIONALLY excluded from StudentResponse.
//
//   API stability: If you rename a DB column, the entity changes, but the
//   Response DTO can stay the same — clients don't need to update.
//
//   EXAM: The Response DTO demonstrates the "expose only what you need to" principle.
// ============================================================
package com.example.springbootref.dto;

public class StudentResponse {

    // These are the fields the client CAN see in the response.
    // Notice: NO 'gpa' field — intentionally hidden from the API response.
    private Long   id;        // included: client needs this to reference the resource
    private String name;      // included: client needs to display the name
    private String studentId; // included: the university ID is public info
    private String major;     // included: useful to display
    // gpa: EXCLUDED — pretend it's sensitive academic data not for public API

    // Constructor — sets all fields at once.
    // No no-arg constructor needed here because we always create from entity data.
    // (No Jackson deserialization needed for response DTOs.)
    public StudentResponse(Long id, String name, String studentId, String major) {
        this.id        = id;
        this.name      = name;
        this.studentId = studentId;
        this.major     = major;
    }

    // Getters — Jackson uses these to serialize to JSON.
    // CRITICAL: Because there is NO 'getGpa()' method,
    //   Jackson will NOT include gpa in the JSON output — even if the underlying
    //   Student entity has a gpa value. This is how you hide fields.
    // EXAM: No getter = no JSON field in the response.
    public Long   getId()        { return id; }
    public String getName()      { return name; }
    public String getStudentId() { return studentId; }
    public String getMajor()     { return major; }
}
