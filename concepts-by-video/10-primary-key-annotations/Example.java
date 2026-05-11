// Example: All @Id + @GeneratedValue patterns.
package com.example.app.model;

import jakarta.persistence.*;

@Entity @Table(name = "students")
public class Student {

    // ─── PATTERN 1: IDENTITY (standard — use for H2 and MySQL) ───────────────
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Long (wrapper), never long (primitive)
    // DB assigns: 1, 2, 3, 4... automatically

    // ─── PATTERN 2: SEQUENCE (for PostgreSQL) ────────────────────────────────
    // @Id
    // @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_seq")
    // @SequenceGenerator(name = "student_seq", sequenceName = "student_sequence", allocationSize = 1)
    // private Long id;

    // ─── PATTERN 3: AUTO (JPA decides — avoid this) ──────────────────────────
    // @Id
    // @GeneratedValue(strategy = GenerationType.AUTO)
    // private Long id;
    // Unpredictable: might use SEQUENCE in PostgreSQL, IDENTITY in H2 — hard to control.

    // ─── PATTERN 4: No @GeneratedValue (you assign manually) ─────────────────
    // @Id
    // private Long id;
    // You must set id manually before saving. Rare — usually for business keys.

    // ─── PATTERN 5: UUID primary key ─────────────────────────────────────────
    // @Id
    // @GeneratedValue(strategy = GenerationType.UUID)  // Java 17+ / Hibernate 6+
    // private UUID id;
    // Useful for distributed systems where you don't want predictable IDs.

    // --- rest of entity ---
    private String name;
    public Student() {}
    public Student(String name) { this.name = name; }
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String n) { this.name = n; }
}
