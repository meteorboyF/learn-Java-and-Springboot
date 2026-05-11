# SYNTAX — REST API Basics

## HTTP Method Annotations

```java
@GetMapping                    // GET /base
@GetMapping("/{id}")           // GET /base/{id}
@PostMapping                   // POST /base
@PutMapping("/{id}")           // PUT /base/{id}
@DeleteMapping("/{id}")        // DELETE /base/{id}
@PatchMapping("/{id}")         // PATCH /base/{id} (partial update, rarely used in exams)
```

## Class-Level Base URL

```java
@RequestMapping("/api/{studentId}/students")   // all methods share this prefix
@RequestMapping("/students")                   // simpler variant without path variable
```

## Return Status Codes

```java
// 200 OK — default for GET and PUT when returning a body
return ResponseEntity.ok(body);
return body; // shorthand — Spring picks 200 automatically

// 201 Created — REQUIRED for POST
return ResponseEntity.status(HttpStatus.CREATED).body(saved);
return ResponseEntity.created(locationUri).body(saved); // also sets Location header

// 204 No Content — for DELETE (success, no response body)
return ResponseEntity.noContent().build();

// 404 Not Found
return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody);
return ResponseEntity.notFound().build(); // no body version
```

## CRUD → HTTP → Status Code Table

| Operation | HTTP Method | URL            | Success Code |
|-----------|-------------|----------------|--------------|
| Create    | POST        | /students       | 201 Created  |
| Read all  | GET         | /students       | 200 OK       |
| Read one  | GET         | /students/{id}  | 200 OK       |
| Update    | PUT         | /students/{id}  | 200 OK       |
| Delete    | DELETE      | /students/{id}  | 204 No Content |

## URL Design — RESTful vs Non-RESTful

```
CORRECT (nouns):               WRONG (verbs):
/students                      /getAllStudents
/students/5                    /getStudent?id=5
/students  (POST)              /createStudent
/students/5  (PUT)             /updateStudent/5
/students/5  (DELETE)          /deleteStudent?id=5
```

## JSON — Automatic with @RestController

```java
// @RestController handles this automatically — no extra code needed:
// Client sends: {"name":"Alice","gpa":3.9}
// @RequestBody deserializes it → Java object
// Return value serialized → {"id":1,"name":"Alice","gpa":3.9}
```
