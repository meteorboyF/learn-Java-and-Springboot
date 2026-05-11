// BUG 5: @ControllerAdvice instead of @RestControllerAdvice
// Symptom: Even though the exception IS caught by the handler, Spring still
//          returns 500 Internal Server Error to the client (not 404 as expected).
//          The handler method runs, but its return value is treated as a VIEW NAME.
// Location: The GlobalExceptionHandler class annotation.

// BUG: @ControllerAdvice (WRONG for REST APIs)
@ControllerAdvice  // ← THIS IS THE BUG
public class GlobalExceptionHandler {

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<String> handleNotFound(StudentNotFoundException ex) {
        // This method IS called when StudentNotFoundException is thrown.
        // BUT: @ControllerAdvice doesn't add @ResponseBody by default.
        // Spring MVC thinks: "this controller might be returning a view name."
        // It tries to resolve "Student not found: 5" as a Thymeleaf/JSP template name.
        // No template with that name exists → Spring falls back to 500 error page.
        //
        // The handler runs but the response is NOT what we expect.
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        // ^^ This ResponseEntity IS returned, but without @ResponseBody on the class,
        // Spring might not serialize it correctly for all request types.
        // The safest fix is always: use @RestControllerAdvice.
    }
}

// WHAT THE CLIENT SEES:
// Expected: HTTP 404 + {"message": "Student not found: 5"}
// Actual:   HTTP 500 + Spring's default error page
// The test asserting status == 404 → FAILS.
