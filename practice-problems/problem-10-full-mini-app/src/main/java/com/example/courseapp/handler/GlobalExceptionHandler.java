package com.example.courseapp.handler;

import com.example.courseapp.exception.CourseNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// @RestControllerAdvice — CORRECT for REST APIs. @ControllerAdvice is WRONG (Bug 5).
// Catches exceptions from all @RestController classes globally.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // CourseNotFoundException → 404 Not Found
    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<String> handleNotFound(CourseNotFoundException ex) {
        // Returning a plain String body here (simpler than a DTO for this mini-app).
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Any other exception → 500 Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred.");
    }
}
