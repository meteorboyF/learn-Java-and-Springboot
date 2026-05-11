# POST Requests and 201 Created

## Why 201 and Not 200?
HTTP status codes communicate MEANING. `200 OK` means "the request succeeded and here's the result." `201 Created` means "the request succeeded AND a new resource was created." For a POST that creates a database row, `201` is the semantically correct response.

**EXAM: This is Bug 1 in Problem 12 — using `ResponseEntity.ok()` inside `@PostMapping` returns 200 instead of 201.**

## The Location Header
When a POST creates a resource, the response should include a `Location` header — the URL where the new resource can be found. This follows REST conventions and lets clients immediately fetch the resource they just created.

```
HTTP/1.1 201 Created
Location: /api/STU001/students/42
Content-Type: application/json

{"id": 42, "name": "Alice", ...}
```

## How ResponseEntity.created() Works
`ResponseEntity.created(URI)` is a builder that:
1. Sets the status to `201 Created`
2. Adds the `Location` header with the URI you provide
3. Then `.body(savedObject)` adds the JSON response body

## The Full Pattern for a POST Endpoint

```java
@PostMapping
public ResponseEntity<Student> create(@RequestBody StudentRequest req) {
    Student saved = service.create(req);               // 1. save to DB
    URI location = URI.create(                         // 2. build Location URI
        "/api/" + studentId + "/students/" + saved.getId()
    );
    return ResponseEntity.created(location).body(saved); // 3. 201 + Location + body
}
```

## EXAM Summary
| What you write | Status code | Correct for POST? |
|---|---|---|
| `ResponseEntity.ok(saved)` | 200 | ❌ No (Bug 1) |
| `return saved;` | 200 | ❌ No |
| `ResponseEntity.status(201).body(saved)` | 201 | ✅ Yes |
| `ResponseEntity.created(uri).body(saved)` | 201 + Location | ✅ Best |
