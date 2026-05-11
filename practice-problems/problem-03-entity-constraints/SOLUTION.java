// SOLUTION — Student.java (JPA Entity with all constraints)
// This file would be at: src/main/java/com/example/app/model/Student.java

package com.example.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// @Entity — REQUIRED. Tells Hibernate: "Create and manage a DB table for this class."
// Without @Entity, Hibernate completely ignores this class.
// EXAM: Every model class that maps to a DB table needs @Entity.
@Entity

// @Table(name = "students") — sets the exact DB table name.
// Without @Table, Hibernate uses the class name: 'Student' → table 'student' (lowercase).
// EXAM: Always include @Table if the question specifies the table name.
@Table(name = "students")
public class Student {

    // @Id — marks this field as the PRIMARY KEY. Every @Entity needs exactly one @Id.
    // EXAM: Missing @Id → app crashes with "No identifier specified for entity".
    @Id

    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    //   → DB auto-increments this column (MySQL AUTO_INCREMENT, H2 IDENTITY).
    //   → You don't set this value — the DB assigns it after INSERT.
    // EXAM: GenerationType.IDENTITY is the standard choice for H2 and MySQL.
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    // Long (wrapper, not primitive long) because:
    //   - New entities have id = null before they are saved.
    //   - Primitive 'long' cannot be null → NullPointerException.
    // EXAM: Always use Long, Integer, etc. (wrapper types) for @Id.
    private Long id;

    // @Column(nullable = false, length = 80)
    //   nullable = false → NOT NULL constraint in the DB.
    //   length = 80 → VARCHAR(80) column. Default without length = VARCHAR(255).
    // EXAM: nullable=false is how you enforce NOT NULL at the JPA level.
    @Column(nullable = false, length = 80)
    private String name;

    // @Column(name = "student_id", unique = true, nullable = false)
    //   name = "student_id" → the DB column is named student_id (snake_case).
    //   unique = true → UNIQUE INDEX on this column. No two rows can share a value.
    //   nullable = false → NOT NULL constraint.
    // EXAM: unique=true is how you prevent duplicate studentIds at the DB level.
    @Column(name = "student_id", unique = true, nullable = false)
    private String studentId;  // university-assigned ID (e.g., "STU20240001")

    // @Column(precision = 4, scale = 2)
    //   precision = 4 → total number of significant digits stored.
    //   scale = 2 → digits after the decimal point.
    //   Together: stores values like 3.85 (valid: up to 99.99, minimum step: 0.01).
    //   No nullable=false → this column IS NULLABLE (gpa is optional).
    // EXAM: precision and scale control decimal/numeric columns. Know the math.
    @Column(precision = 4, scale = 2)
    private Double gpa;  // Double (nullable) — null means "no GPA recorded"

    // No @Column annotation → Hibernate uses field name as column name ('major')
    // and makes it nullable VARCHAR(255) by default.
    // This column is optional — students don't need to declare a major.
    private String major;

    // ─────────────────────────────────────────────────────────
    // CONSTRUCTORS
    // ─────────────────────────────────────────────────────────

    // No-arg constructor — REQUIRED by JPA.
    // Hibernate calls this when loading rows from the DB (via reflection).
    // If you omit this, Hibernate throws: "No default constructor for entity: Student"
    // EXAM: This is Bug 3 in Problem 12 — forgetting the no-arg constructor.
    public Student() {}

    // Parameterised constructor — for creating new Student objects in code.
    // Notice: id is NOT a parameter (the DB assigns it automatically).
    public Student(String name, String studentId, Double gpa, String major) {
        this.name      = name;
        this.studentId = studentId;
        this.gpa       = gpa;
        this.major     = major;
    }

    // ─────────────────────────────────────────────────────────
    // GETTERS AND SETTERS
    // ─────────────────────────────────────────────────────────
    // Both getters AND setters required:
    //   - JPA/Hibernate uses them to read/write field values.
    //   - Jackson uses getters to serialize to JSON (response).
    //   - Jackson uses setters to deserialize from JSON (request).
    // EXAM: Missing getters → fields disappear from JSON responses.

    public Long   getId()        { return id; }
    // No setId() — the DB assigns the id; we don't let callers set it.

    public String getName()      { return name; }
    public void   setName(String name)           { this.name = name; }

    public String getStudentId() { return studentId; }
    public void   setStudentId(String studentId) { this.studentId = studentId; }

    public Double getGpa()       { return gpa; }
    public void   setGpa(Double gpa)             { this.gpa = gpa; }

    public String getMajor()     { return major; }
    public void   setMajor(String major)         { this.major = major; }

    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + name + "', studentId='"
                + studentId + "', gpa=" + gpa + ", major='" + major + "'}";
    }
}
