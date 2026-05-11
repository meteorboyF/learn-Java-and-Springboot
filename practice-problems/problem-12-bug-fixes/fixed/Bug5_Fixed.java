// FIX 5: Replace @ControllerAdvice with @RestControllerAdvice
// The fix: one annotation change — @ControllerAdvice → @RestControllerAdvice.

// FIXED: @RestControllerAdvice (correct for REST APIs)
@RestControllerAdvice  // ← THE FIX: was @ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<String> handleNotFound(StudentNotFoundException ex) {
        // Now this return value IS serialized to JSON and written to the HTTP response body.
        // The client receives: HTTP 404 + "Student not found: 5" as the response body.
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred.");
    }
}

// WHY THIS MATTERS:
// @ControllerAdvice:
//   - Designed for @Controller classes (HTML/MVC apps)
//   - Does NOT add @ResponseBody to handler methods
//   - Return values are treated as VIEW NAMES (like "error.html")
//   - Without a matching view template, Spring returns 500
//
// @RestControllerAdvice:
//   - = @ControllerAdvice + @ResponseBody
//   - Designed for @RestController classes (REST/JSON APIs)
//   - Return values ARE serialized to JSON automatically
//   - Returns the correct status code and JSON body
//
// EXAM: This is THE most subtle bug — the exception IS caught in both cases,
//       but only @RestControllerAdvice returns the correct HTTP response.
