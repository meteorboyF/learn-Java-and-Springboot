// ============================================================
// Student.java — The JPA Entity (Domain Model)
// An @Entity class = a Java class mapped to a database TABLE.
// Every row in the 'students' table corresponds to one Student object.
// ============================================================
package com.example.springbootref.model;

// Jakarta Persistence API (JPA) annotations — these come from the
// jakarta.persistence package in Spring Boot 3.x.
// (In Spring Boot 2.x, it was javax.persistence — EXAM: know the difference.)
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// @Entity — REQUIRED. Marks this class as a JPA entity.
// Hibernate (the JPA implementation) reads this annotation and knows:
//   "I need to create and manage a database table for this class."
// Without @Entity, Hibernate ignores the class completely.
// EXAM: @Entity is always required on model classes that map to a DB table.
@Entity

// @Table(name = "students") — specifies the EXACT table name in the database.
// Without @Table, Hibernate uses the class name as the table name:
//   - Class 'Student' → table 'student' (lowercase, no @Table)
// Best practice: always use @Table so the DB schema is explicit and predictable.
// EXAM: If the question says "table named 'students'", you MUST include @Table.
@Table(name = "students")
public class Student {

    // --------------------------------------------------------
    // PRIMARY KEY FIELD
    // --------------------------------------------------------

    // @Id — marks this field as the PRIMARY KEY of the table.
    // Every @Entity MUST have exactly one @Id field.
    // EXAM: If you forget @Id, the app crashes with "No identifier specified for entity".
    @Id

    // @GeneratedValue — tells JPA how to auto-generate the primary key value.
    // strategy = GenerationType.IDENTITY:
    //   The DATABASE generates the ID using its native auto-increment mechanism:
    //   - MySQL: AUTO_INCREMENT column
    //   - H2: auto-increment (compatible with IDENTITY behavior)
    //   - PostgreSQL: SERIAL or BIGSERIAL column
    // Other strategies (for reference):
    //   GenerationType.SEQUENCE  — uses a DB sequence object (PostgreSQL preferred)
    //   GenerationType.AUTO      — JPA picks the best strategy for the DB (can be unpredictable)
    //   GenerationType.TABLE     — uses a dedicated table to track IDs (slow, avoid)
    // EXAM: GenerationType.IDENTITY is the most common choice for H2/MySQL.
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    // Long (capital L, not primitive long):
    //   - A new, unsaved entity has id = null (no database ID yet).
    //   - Primitive 'long' cannot hold null, causing NullPointerException.
    //   - EXAM: Always use wrapper types (Long, Integer) for @Id, not primitives.
    private Long id;

    // --------------------------------------------------------
    // NAME FIELD
    // --------------------------------------------------------

    // @Column — customises the database column for this field.
    // Without @Column, Hibernate uses the field name as the column name and allows NULL.
    //
    // nullable = false → adds NOT NULL constraint at the DB level.
    //   If you try to save a Student with name=null, the DB rejects it.
    //   EXAM: nullable=false is the JPA way to enforce NOT NULL.
    //
    // length = 80 → sets the column to VARCHAR(80).
    //   Default without length is VARCHAR(255).
    //   EXAM: If the question specifies a max length, use the length attribute.
    @Column(nullable = false, length = 80)
    private String name;

    // --------------------------------------------------------
    // STUDENT ID FIELD (the university-assigned student number)
    // --------------------------------------------------------

    // name = "student_id" — renames the DB column.
    //   Without this, the field 'studentId' would become column 'student_id' automatically
    //   (Hibernate converts camelCase to snake_case), but being explicit avoids surprises.
    //
    // unique = true → adds a UNIQUE index on this column.
    //   No two students can have the same studentId.
    //   EXAM: unique=true is how you enforce uniqueness at the DB level.
    //
    // nullable = false → this field must always have a value (NOT NULL).
    // EXAM: studentId is the "business key" — the university ID number, not the DB primary key.
    //       The DB primary key (id) is auto-generated. studentId is assigned by the university.
    @Column(name = "student_id", unique = true, nullable = false)
    private String studentId;

    // --------------------------------------------------------
    // GPA FIELD
    // --------------------------------------------------------

    // @Column with precision and scale — controls decimal storage.
    //   precision = 4 → total number of significant digits (e.g., 3.75 = 3 digits total)
    //   scale = 2     → digits AFTER the decimal point (e.g., .75 = 2 decimal places)
    //   Together: stores values from -99.99 to 99.99.
    //   A GPA of 3.85 has 3 significant digits with 2 decimal places — fits perfectly.
    //
    // No nullable=false → this column IS nullable (gpa is optional).
    // EXAM: Double (capital D) used because null means "no GPA recorded" — primitives can't be null.
    @Column(precision = 4, scale = 2)
    private Double gpa;  // nullable — some students may not have a GPA yet

    // --------------------------------------------------------
    // MAJOR FIELD
    // --------------------------------------------------------

    // No @Column annotation at all — uses Hibernate defaults:
    //   - Column name: "major" (same as field name, no camelCase conversion needed)
    //   - Type: VARCHAR(255) (default for String)
    //   - Nullable: true (default — this field can be null)
    // EXAM: Fields without @Column are still mapped to the DB — just with defaults.
    private String major;  // optional — not every student declares a major

    // --------------------------------------------------------
    // CONSTRUCTORS
    // --------------------------------------------------------

    // NO-ARG CONSTRUCTOR — REQUIRED by JPA.
    // When Hibernate loads a row from the database, it:
    //   1. Calls this no-arg constructor to create a blank Student object.
    //   2. Uses reflection (setters or direct field access) to fill in the values.
    // If you don't provide a no-arg constructor, JPA crashes with:
    //   "No default constructor for entity: Student"
    // EXAM: ALWAYS include a no-arg constructor in every @Entity class.
    // NOTE: If you define ANY constructor, Java stops auto-generating the no-arg one.
    //       You must add it explicitly.
    public Student() {}  // required by JPA — do not remove this!

    // PARAMETERISED CONSTRUCTOR — convenience for creating Student objects in your code.
    // Notice id is NOT a parameter — the database assigns id automatically.
    // EXAM: The seeder and tests use this to create students without knowing the ID yet.
    public Student(String name, String studentId, Double gpa, String major) {
        this.name      = name;       // 'this.name' = field, 'name' = parameter
        this.studentId = studentId;  // disambiguates when field and parameter have same name
        this.gpa       = gpa;
        this.major     = major;
    }

    // --------------------------------------------------------
    // GETTERS AND SETTERS
    // --------------------------------------------------------
    // WHY GETTERS AND SETTERS ARE REQUIRED:
    //   1. JPA/Hibernate uses getters/setters to read and write field values.
    //   2. Jackson (the JSON library) uses getters to convert the object to JSON.
    //      If you remove a getter, that field disappears from the JSON response!
    //   3. Setters are needed for update operations and JSON deserialization.
    // EXAM: Always include getters and setters for ALL fields in an @Entity.

    // id getter — no setter for id because the DB assigns it; clients should not set it.
    public Long getId() { return id; }

    public String getName()      { return name; }
    public void   setName(String name)           { this.name = name; }

    public String getStudentId() { return studentId; }
    public void   setStudentId(String studentId) { this.studentId = studentId; }

    public Double getGpa()       { return gpa; }
    public void   setGpa(Double gpa)             { this.gpa = gpa; }

    public String getMajor()     { return major; }
    public void   setMajor(String major)         { this.major = major; }

    // toString() — helpful for debugging (prints object details in logs).
    // Not required by JPA or Jackson, but good practice.
    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + name + "', studentId='" + studentId
                + "', gpa=" + gpa + ", major='" + major + "'}";
    }
}
