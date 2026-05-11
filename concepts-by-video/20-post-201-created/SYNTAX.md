# SYNTAX — POST Requests and 201 Created

## All Valid Ways to Return 201

```java
// Option 1 (BEST): 201 + Location header + body
URI location = URI.create("/students/" + saved.getId());
return ResponseEntity.created(location).body(saved);

// Option 2: 201 + body, no Location header
return ResponseEntity.status(HttpStatus.CREATED).body(saved);

// Option 3: 201 + body using integer status
return ResponseEntity.status(201).body(saved);

// Option 4: 201, no body (less common for POST)
return ResponseEntity.created(location).build();
```

## Building the Location URI

```java
// Simple path
URI location = URI.create("/students/" + saved.getId());

// With API prefix and path variable
URI location = URI.create("/api/" + studentId + "/students/" + saved.getId());

// Using UriComponentsBuilder (more robust, handles encoding)
import org.springframework.web.util.UriComponentsBuilder;
URI location = UriComponentsBuilder
    .fromPath("/api/{studentId}/students/{id}")
    .buildAndExpand(studentId, saved.getId())
    .toUri();
```

## Complete POST Method Pattern

```java
@PostMapping
public ResponseEntity<Student> create(
        @PathVariable String studentId,
        @RequestBody StudentRequest request) {

    Student saved = service.create(request);

    URI location = URI.create(
        "/api/" + studentId + "/students/" + saved.getId()
    );

    return ResponseEntity.created(location).body(saved);
}
```

## Status Code Reference for All Methods

```java
GET    → return body directly, or ResponseEntity.ok(body)      // 200
POST   → ResponseEntity.created(uri).body(saved)               // 201
PUT    → ResponseEntity.ok(updated)                            // 200
DELETE → ResponseEntity.noContent().build()                    // 204
```

## EXAM Traps

```java
// ❌ Bug 1: returns 200, not 201
return ResponseEntity.ok(saved);

// ❌ Also 200 — bare return without ResponseEntity
return saved;

// ✅ Correct: 201
return ResponseEntity.status(HttpStatus.CREATED).body(saved);
return ResponseEntity.created(location).body(saved);
```
