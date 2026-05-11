// Example: A complete @Entity with every annotation and explanation.

package com.example.app.model;

import jakarta.persistence.*;

// @Entity — registers this class with Hibernate as a managed entity.
// Hibernate creates/updates the DB table based on this class.
@Entity
// @Table — specifies the exact DB table name.
// Without: Hibernate uses lowercase class name ('student' for Student class).
// EXAM: Always include @Table with explicit name to avoid surprises.
@Table(name = "students")
public class Student {

    // @Id — primary key. Every @Entity needs exactly one.
    @Id
    // @GeneratedValue — DB assigns the id automatically.
    // IDENTITY: uses DB's native auto-increment (MySQL AUTO_INCREMENT, H2 IDENTITY).
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Long (wrapper) not long (primitive) — must be nullable for new objects

    // @Column — customize this field's DB column.
    // nullable = false → NOT NULL constraint.
    // length = 80 → VARCHAR(80) instead of default VARCHAR(255).
    @Column(nullable = false, length = 80)
    private String name;

    // unique = true → UNIQUE INDEX (no two rows can have the same value).
    // name = "student_id" → explicit column name (avoids camelCase ambiguity).
    @Column(name = "student_id", unique = true, nullable = false)
    private String studentId;

    // precision = total digits. scale = digits after decimal.
    // precision=4, scale=2: stores values like 3.75, 99.99.
    // No nullable=false → this column is nullable (gpa is optional).
    @Column(precision = 4, scale = 2)
    private Double gpa;  // Double (nullable) vs double (primitive, can't be null)

    // No @Column → uses defaults: column name = 'major', VARCHAR(255), nullable.
    private String major;

    // REQUIRED by JPA — Hibernate uses this to create blank instances.
    public Student() {}

    // Convenience constructor — id NOT included (DB assigns it).
    public Student(String name, String studentId, Double gpa, String major) {
        this.name = name;
        this.studentId = studentId;
        this.gpa = gpa;
        this.major = major;
    }

    // Getters — Jackson uses these to serialize to JSON.
    // Setters — Jackson uses these to deserialize from JSON.
    // Both required for JPA and Jackson to work.
    public Long   getId()        { return id; }
    public String getName()      { return name; }
    public void   setName(String n)  { this.name = n; }
    public String getStudentId() { return studentId; }
    public void   setStudentId(String s) { this.studentId = s; }
    public Double getGpa()       { return gpa; }
    public void   setGpa(Double g)   { this.gpa = g; }
    public String getMajor()     { return major; }
    public void   setMajor(String m) { this.major = m; }
}
