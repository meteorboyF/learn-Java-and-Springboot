// SOLUTION — Two files: ErrorResponse.java and GlobalExceptionHandler.java

// ════════════════════════════════════════════════════════════
// FILE 1: ErrorResponse.java
// Location: src/main/java/com/example/app/dto/ErrorResponse.java
// ════════════════════════════════════════════════════════════
package com.example.app.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// ErrorResponse — the JSON body returned for every error.
// Without this DTO, we'd return plain String bodies.
// With this DTO, the client gets structured JSON:
//   { "status": 404, "message": "Student not found", "timestamp": "2024-05-11 10:22:01" }
// EXAM: Jackson requires GETTERS to serialize fields. No getter = no JSON field.
public class ErrorResponse {
    private int    status;    // HTTP status code (404, 400, 500)
    private String message;   // error description
    private String timestamp; // when the error occurred

    public ErrorResponse(int status, String message) {
        this.status    = status;
        this.message   = message;
        // Auto-generate timestamp so callers don't need to provide it.
        this.timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // Getters — REQUIRED for Jackson to include these fields in the JSON response.
    // If you remove any getter, that field disappears from the JSON. EXAM: know this.
    public int    getStatus()    { return status; }
    public String getMessage()   { return message; }
    public String getTimestamp() { return timestamp; }
}

// ════════════════════════════════════════════════════════════
// FILE 2: GlobalExceptionHandler.java
// Location: src/main/java/com/example/app/handler/GlobalExceptionHandler.java
// ════════════════════════════════════════════════════════════
package com.example.app.handler;

import com.example.app.dto.ErrorResponse;
import com.example.app.exception.StudentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// @RestControllerAdvice — interceptor for exceptions from ALL @RestController classes.
//
// EXAM: @ControllerAdvice (WRONG for REST) vs @RestControllerAdvice (CORRECT for REST):
//   @ControllerAdvice: return values treated as VIEW NAMES (for HTML/MVC apps). No JSON.
//   @RestControllerAdvice: = @ControllerAdvice + @ResponseBody → return values → JSON.
//   Using @ControllerAdvice for a REST API means the exception IS caught, but the
//   String return value is treated as an HTML view name → Spring returns 500 instead of 404.
//   This is EXACTLY Bug 5 in Problem 12.
// EXAM: This annotation is always required in REST API projects. Know it cold.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ─── Handler 1: 404 for StudentNotFoundException ────────────────────────
    // @ExceptionHandler(StudentNotFoundException.class):
    //   When any @RestController throws StudentNotFoundException,
    //   Spring calls this method INSTEAD of propagating the exception to the client.
    //
    // HOW IT WORKS:
    //   1. StudentService throws new StudentNotFoundException("Student not found: 5")
    //   2. StudentController doesn't catch it → propagates up
    //   3. Spring's DispatcherServlet catches it
    //   4. Spring checks all @ExceptionHandler methods in @RestControllerAdvice
    //   5. Finds this method (StudentNotFoundException matches)
    //   6. Calls this method → returns 404 JSON to client
    //
    // SPECIFICITY: Spring always picks the most specific matching handler.
    //   StudentNotFoundException → uses Handler 1 (most specific match)
    //   IllegalArgumentException → uses Handler 2
    //   NullPointerException     → uses Handler 3 (most general)
    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(StudentNotFoundException ex) {
        // ex.getMessage() = "Student not found with id: 5" (the message from the throw)
        ErrorResponse body = new ErrorResponse(404, ex.getMessage());
        // HttpStatus.NOT_FOUND = 404. Using the enum is clearer than the integer.
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    // ─── Handler 2: 400 for IllegalArgumentException ───────────────────────
    // 400 Bad Request = "The client sent invalid data."
    // Throw this in your service for validation failures:
    //   if (gpa < 0 || gpa > 4.0) throw new IllegalArgumentException("GPA must be 0-4.0");
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {
        ErrorResponse body = new ErrorResponse(400, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // ─── Handler 3: 500 for anything else ──────────────────────────────────
    // CATCH-ALL: If no more specific handler matches, this catches everything else.
    // Examples: NullPointerException, database connection failure, etc.
    // In production: don't expose the raw exception message (security risk).
    // For the quiz: including ex.getMessage() is acceptable.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        ErrorResponse body = new ErrorResponse(500,
                "An unexpected error occurred. Please try again later.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
