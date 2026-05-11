// BUG 2: Using .get() on Optional instead of .orElseThrow()
// Symptom: When student is not found, Spring returns 500 Internal Server Error
//          instead of 404 Not Found.
//          JUnit tests checking for 404 FAIL — they get 500 instead.
// Location: The service method that fetches a student by ID.

public Student getStudentById(Long id) {
    return studentRepository.findById(id).get();
    // BUG: .get() on an empty Optional throws NoSuchElementException!
    // NoSuchElementException is NOT StudentNotFoundException.
    // GlobalExceptionHandler has @ExceptionHandler(StudentNotFoundException.class)
    //   — it does NOT catch NoSuchElementException.
    // The catch-all @ExceptionHandler(Exception.class) catches it → returns 500.
    // The test expects 404 → gets 500 → TEST FAILS.
}
