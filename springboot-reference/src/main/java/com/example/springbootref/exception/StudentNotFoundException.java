// ============================================================
// StudentNotFoundException.java — Custom Exception
// A domain-specific exception that says exactly WHAT went wrong.
// "Student not found" is far more useful than a generic error.
// ============================================================
package com.example.springbootref.exception;

// WHY extends RuntimeException (unchecked) instead of Exception (checked)?
//
// CHECKED Exception (extends Exception):
//   - Every method that might throw it MUST declare 'throws StudentNotFoundException'
//   - Every CALLER must have try-catch OR also declare 'throws StudentNotFoundException'
//   - Result: throws declarations spread through every layer (service, controller, etc.)
//   - This is messy, verbose, and pollutes your method signatures.
//
// UNCHECKED Exception (extends RuntimeException):
//   - No try-catch or 'throws' declaration required anywhere.
//   - The exception bubbles up through ALL layers automatically.
//   - Your @RestControllerAdvice catches it at the top level and returns 404.
//   - Controllers stay clean — no try-catch needed.
//   - EXAM: Custom exceptions for "not found" scenarios ALWAYS extend RuntimeException.
//
// REAL-WORLD RULE: Use checked exceptions for RECOVERABLE errors (file not found → ask user).
//                  Use unchecked exceptions for PROGRAMMING ERRORS or DOMAIN violations.
// EXAM: "Why RuntimeException?" is a common exam/interview question.
public class StudentNotFoundException extends RuntimeException {

    // Constructor 1: takes a custom message string.
    // 'super(message)' passes the message up to RuntimeException's constructor,
    // which stores it internally. ex.getMessage() then returns this message.
    // EXAM: Always call super(message) so the exception carries a useful description.
    public StudentNotFoundException(String message) {
        super(message);  // stores 'message' — accessible via getMessage()
    }

    // Constructor 2: convenience constructor that accepts a Long id.
    // Builds a descriptive message automatically.
    // This avoids repeating the message format in every service method.
    // Usage: throw new StudentNotFoundException(5L);
    //   → message: "Student not found with id: 5"
    public StudentNotFoundException(Long id) {
        super("Student not found with id: " + id);
    }
}
