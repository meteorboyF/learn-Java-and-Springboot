// ============================================================
// GlobalExceptionHandler.java — Centralized Exception Handling
// Catches exceptions thrown anywhere in ANY controller and
// converts them to the correct HTTP response automatically.
// Without this class, Spring would return a generic 500 error
// for everything — even a simple "not found" case.
// ============================================================
package com.example.springbootref.handler;

import com.example.springbootref.dto.ErrorResponse;
import com.example.springbootref.exception.StudentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// @RestControllerAdvice — a GLOBAL interceptor for exceptions from ALL @RestController classes.
//
// Difference between @ControllerAdvice and @RestControllerAdvice:
//
//   @ControllerAdvice:
//     - Intercepts exceptions from @Controller classes (MVC / HTML-rendering apps).
//     - Return values from @ExceptionHandler methods are treated as VIEW NAMES (HTML templates).
//     - Does NOT automatically serialize return values to JSON.
//     - WRONG choice for REST APIs.
//
//   @RestControllerAdvice:
//     - = @ControllerAdvice + @ResponseBody
//     - The @ResponseBody means: "serialize my return values to JSON automatically."
//     - CORRECT choice for REST APIs.
//     - EXAM: Bug 5 in problem-12 is exactly this mistake — using @ControllerAdvice for REST.
//
// EXAM: @RestControllerAdvice is tested. Always use it (not @ControllerAdvice) for REST exception handling.
// EXAM: You only need ONE GlobalExceptionHandler per Spring Boot application.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ============================================================
    // HANDLER 1: StudentNotFoundException → 404 Not Found
    // ============================================================
    // @ExceptionHandler(StudentNotFoundException.class) — tells Spring:
    //   "When any @RestController throws StudentNotFoundException,
    //    call THIS method instead of letting it propagate to the client."
    //
    // HOW IT WORKS:
    //   1. Controller method calls service.getStudentById(99).
    //   2. Service throws new StudentNotFoundException(99).
    //   3. Spring sees the exception, checks all @ExceptionHandler methods.
    //   4. Finds this handler (matches StudentNotFoundException).
    //   5. Calls handleNotFound(ex) with the thrown exception.
    //   6. Returns the ResponseEntity as the HTTP response — 404 + JSON body.
    //
    // SPECIFICITY RULE: Spring picks the MOST SPECIFIC matching handler.
    //   StudentNotFoundException extends RuntimeException extends Exception.
    //   If you have handlers for all three, Spring uses the StudentNotFoundException one.
    //   Only if there's no specific match does it fall through to the more general handlers.
    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(StudentNotFoundException ex) {
        // ex.getMessage() returns the message from new StudentNotFoundException("Student not found: 5")
        ErrorResponse body = new ErrorResponse(404, ex.getMessage());

        // ResponseEntity.status(HttpStatus.NOT_FOUND) = set status code to 404.
        // .body(body) = serialize 'body' to JSON and set as the response body.
        // Client receives: HTTP 404 + {"status":404,"message":"Student not found with id: 5","timestamp":"..."}
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    // ============================================================
    // HANDLER 2: IllegalArgumentException → 400 Bad Request
    // ============================================================
    // 400 Bad Request = "The client sent invalid input — their fault, not ours."
    // Examples of when a service might throw IllegalArgumentException:
    //   - Negative GPA value
    //   - Empty name field
    //   - Invalid date format
    // Throw it in your service: throw new IllegalArgumentException("GPA cannot be negative");
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {
        ErrorResponse body = new ErrorResponse(400, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);  // 400
    }

    // ============================================================
    // HANDLER 3: Exception (catch-all) → 500 Internal Server Error
    // ============================================================
    // This handler catches ANYTHING that wasn't caught by the more specific handlers above.
    // Examples: NullPointerException, database connection failure, OutOfMemoryError, etc.
    //
    // WHY PUT THIS LAST?
    //   Spring evaluates handlers from MOST SPECIFIC to LEAST SPECIFIC.
    //   If this were first, it would catch StudentNotFoundException before Handler 1 could.
    //   Exception is the most general type — it must be the last resort.
    //
    // EXAM: The fallback handler always catches Exception (not Throwable) for safety.
    //       This prevents accidentally catching OutOfMemoryError and other JVM errors.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        // Don't expose the raw exception message to clients in production —
        // it might contain sensitive stack trace info or internal class names.
        // For the quiz, including ex.getMessage() is fine.
        ErrorResponse body = new ErrorResponse(500,
                "An unexpected error occurred. Please try again later.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);  // 500
    }
}
