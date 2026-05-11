// BUG 1: POST endpoint returns 200 instead of 201
// Symptom: POST /api/STU001/students returns HTTP 200 OK instead of HTTP 201 Created.
//          JUnit tests that check for status 201 FAIL.
// Location: The @PostMapping method in the controller.

@PostMapping
public ResponseEntity<Student> create(@RequestBody Student student) {
    Student saved = service.createStudent(student);
    return ResponseEntity.ok(saved);  // BUG: .ok() always returns 200, not 201!
    // ResponseEntity.ok() = 200 OK
    // POST should always return 201 Created — this is an HTTP standard rule.
    // The JUnit test does: assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED)
    // Using .ok() → status is 200 → assertion fails → TEST FAILS.
}
