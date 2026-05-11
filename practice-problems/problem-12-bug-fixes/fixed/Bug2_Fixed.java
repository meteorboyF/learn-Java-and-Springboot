// FIX 2: Replace .get() with .orElseThrow() on Optional
// The fix: never call .get() on an Optional. Always use .orElseThrow().

public Student getStudentById(Long id) {
    // FIXED: .orElseThrow() throws StudentNotFoundException when Optional is empty.
    // StudentNotFoundException IS caught by GlobalExceptionHandler → returns 404.
    return studentRepository.findById(id)
            .orElseThrow(() -> new StudentNotFoundException(
                    "Student not found with id: " + id));
}

// WHY THIS MATTERS:
// Optional.get() when empty → throws NoSuchElementException (generic, uncaught → 500)
// Optional.orElseThrow()   → throws StudentNotFoundException (specific, caught → 404)
//
// The GlobalExceptionHandler has:
//   @ExceptionHandler(StudentNotFoundException.class) → returns 404
//   @ExceptionHandler(Exception.class)               → returns 500 (catch-all)
//
// .get() throws NoSuchElementException → hits the Exception catch-all → 500
// .orElseThrow() throws StudentNotFoundException → hits the specific handler → 404
//
// RULE: NEVER call .get() on Optional in production code.
//       ALWAYS use one of:
//         .orElseThrow(() -> new YourException(...))   // throw custom exception
//         .orElse(defaultValue)                         // return a default
//         .orElseGet(() -> computeDefault())            // compute a default lazily
//         .ifPresent(value -> doSomething(value))       // act only if present
