package com.example.courseapp.model;

import jakarta.persistence.*;

// @Entity — Hibernate will create a 'courses' table for this class.
@Entity
// @Table(name="courses") — explicit table name. Without it: table would be 'course'.
@Table(name = "courses")
public class Course {

    // Auto-generated Long primary key.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // unique + nullable=false → UNIQUE + NOT NULL constraint on 'code' column.
    // 'code' is the course code (e.g., "CSE2218") — must be unique across all courses.
    @Column(unique = true, nullable = false)
    private String code;

    // nullable=false → NOT NULL. Every course must have a title.
    @Column(nullable = false)
    private String title;

    // No @Column — uses default (nullable int column named 'credits').
    // int (primitive) — credits are always a number, never null.
    private int credits;

    // Optional — instructor can be null (e.g., course not yet assigned).
    private String instructor;

    // No-arg constructor — REQUIRED by JPA for reflection-based loading.
    public Course() {}

    // Parameterised constructor — id NOT included (DB assigns it).
    public Course(String code, String title, int credits, String instructor) {
        this.code       = code;
        this.title      = title;
        this.credits    = credits;
        this.instructor = instructor;
    }

    // Getters and setters — required by JPA and Jackson.
    public Long   getId()          { return id; }
    public String getCode()        { return code; }
    public void   setCode(String c)         { this.code = c; }
    public String getTitle()       { return title; }
    public void   setTitle(String t)        { this.title = t; }
    public int    getCredits()     { return credits; }
    public void   setCredits(int c)         { this.credits = c; }
    public String getInstructor()  { return instructor; }
    public void   setInstructor(String i)   { this.instructor = i; }
}
