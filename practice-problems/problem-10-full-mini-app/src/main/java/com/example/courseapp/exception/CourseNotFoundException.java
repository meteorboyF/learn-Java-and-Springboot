package com.example.courseapp.exception;

// extends RuntimeException — unchecked. No try-catch needed in callers.
// GlobalExceptionHandler catches this and returns 404.
public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(String message) {
        super(message);
    }
}
