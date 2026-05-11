// FIX 1: Use ResponseEntity.created(uri) or ResponseEntity.status(CREATED) for POST
// The fix: replace .ok() with .created(location) to return 201 instead of 200.

@PostMapping
public ResponseEntity<Student> create(
        @PathVariable String studentId,  // needed to build the Location header URI
        @RequestBody Student student) {
    Student saved = service.createStudent(student);

    // Build the Location header URI pointing to the newly created resource.
    URI location = URI.create("/api/" + studentId + "/students/" + saved.getId());

    // FIXED: ResponseEntity.created(location) sets:
    //   - Status: 201 Created  ← correct for POST
    //   - Location header: the URI of the new resource
    // .body(saved) adds the created student as the response body.
    return ResponseEntity.created(location).body(saved);  // 201 + Location + body

    // Alternative (also correct, no Location header):
    // return ResponseEntity.status(HttpStatus.CREATED).body(saved);  // 201 + body
}

// WHY THIS MATTERS:
// HTTP 200 = "OK, request processed"
// HTTP 201 = "Created — a new resource was created at [Location]"
// The REST spec says POST → 201. Tests check for this exact status code.
