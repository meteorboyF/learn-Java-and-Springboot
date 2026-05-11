// Example: Complete GlobalExceptionHandler with @RestControllerAdvice.

package com.example.app.handler;

import com.example.app.exception.StudentNotFoundException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

// @RestControllerAdvice — intercepts exceptions from all @RestController classes.
// = @ControllerAdvice + @ResponseBody
// EXAM: Use @RestControllerAdvice for REST APIs. @ControllerAdvice is for HTML/MVC apps.
// EXAM: This is Bug 5 in Problem 12 — using @ControllerAdvice here causes 500 responses.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ─── Handler 1: StudentNotFoundException → 404 ────────────────────────────
    // @ExceptionHandler(StudentNotFoundException.class):
    //   "When ANY @RestController throws StudentNotFoundException, call ME."
    // Spring picks this over the Exception handler below because it's MORE SPECIFIC.
    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(StudentNotFoundException ex) {
        // ex.getMessage() = the message from: throw new StudentNotFoundException("msg")
        ErrorResponse body = new ErrorResponse(404, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    // ─── Handler 2: IllegalArgumentException → 400 ────────────────────────────
    // Throw this in your service when client input is invalid:
    //   if (gpa < 0) throw new IllegalArgumentException("GPA cannot be negative");
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {
        ErrorResponse body = new ErrorResponse(400, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // ─── Handler 3: Exception catch-all → 500 ─────────────────────────────────
    // Catches ANYTHING not caught by the more specific handlers above.
    // In production: don't expose internal exception details (security risk).
    // The order this is declared doesn't matter — specificity determines selection.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        ErrorResponse body = new ErrorResponse(500,
                "An unexpected error occurred. Please try again later.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}

// ─── Supporting DTO ────────────────────────────────────────────────────────────
class ErrorResponse {
    private int    status;
    private String message;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    // Getters — Jackson needs these to serialize to JSON.
    // Without getters: fields don't appear in the JSON response!
    public int    getStatus()  { return status; }
    public String getMessage() { return message; }
}
