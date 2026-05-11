// Example: @ExceptionHandler for 404 Not Found — the complete end-to-end flow.

package com.example.app;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

// ─── Step 1: Custom Exception ─────────────────────────────────────────────────
// Extends RuntimeException so it's UNCHECKED — no try-catch needed in callers.
class StudentNotFoundException extends RuntimeException {
    // Convenience constructor: builds message from the id
    public StudentNotFoundException(Long id) {
        super("Student not found with id: " + id);
    }
    // Constructor for custom messages
    public StudentNotFoundException(String message) {
        super(message);
    }
}

// ─── Step 2: Error Response DTO ───────────────────────────────────────────────
// The JSON structure that gets sent to the client when an error occurs.
class ErrorBody {
    private int status;
    private String message;

    public ErrorBody(int status, String message) {
        this.status = status;
        this.message = message;
    }

    // Getters are REQUIRED for Jackson to serialize these fields to JSON.
    // EXAM: Missing getters → empty JSON {} (Common Mistake 2 in 21-restcontrolleradvice).
    public int getStatus()     { return status; }
    public String getMessage() { return message; }
}

// ─── Step 3: Global Exception Handler ─────────────────────────────────────────
// @RestControllerAdvice = @ControllerAdvice + @ResponseBody
// Intercepts exceptions from ALL @RestController classes in the application.
// EXAM: Must be @RestControllerAdvice, not @ControllerAdvice (Bug 5).
@RestControllerAdvice
class GlobalExceptionHandler {

    // @ExceptionHandler(X.class) — "handle exceptions of type X from any controller"
    // Spring calls this method when StudentNotFoundException is thrown.
    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ErrorBody> handleNotFound(StudentNotFoundException ex) {
        // ex.getMessage() returns the message from: throw new StudentNotFoundException(id)
        ErrorBody body = new ErrorBody(404, ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // 404
                .body(body);                  // {"status":404,"message":"Student not found..."}
    }

    // Catch-all for unexpected exceptions — prevents raw stack traces reaching clients.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorBody> handleGeneric(Exception ex) {
        ErrorBody body = new ErrorBody(500, "An unexpected error occurred.");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
                .body(body);
    }
}

// ─── Step 4: Controller — throws exception, doesn't catch it ─────────────────
// The controller does NOT use try-catch. It just calls the service.
// If service throws StudentNotFoundException, it automatically propagates to the handler.
@RestController
@RequestMapping("/api/{studentId}/students")
class StudentController {

    @GetMapping("/{id}")
    public String getById(
            @PathVariable String studentId,
            @PathVariable Long id
    ) {
        // Simulating: student with id=99 doesn't exist
        if (id == 99) {
            throw new StudentNotFoundException(id);
            // ↑ Spring catches this automatically via @ExceptionHandler above.
            // No try-catch needed here — that's the whole point.
        }
        return "Student " + id;
    }
}
