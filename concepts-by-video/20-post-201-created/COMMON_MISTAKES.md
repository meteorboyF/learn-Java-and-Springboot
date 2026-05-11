# Common Mistakes — POST Requests and 201 Created

## Mistake 1: Returning 200 Instead of 201 (Bug 1 in Problem 12)
**Wrong:**
```java
@PostMapping
public ResponseEntity<Student> create(@RequestBody StudentRequest req) {
    Student saved = service.create(req);
    return ResponseEntity.ok(saved); // ← 200 OK — WRONG for POST!
}
```
**Problem:** `ResponseEntity.ok()` always returns 200. POST must return 201 Created per HTTP and REST conventions. An exam test checking `response.getStatusCode() == 201` will fail.
**Fix:** Use `ResponseEntity.created(location).body(saved)` or `ResponseEntity.status(HttpStatus.CREATED).body(saved)`.

---

## Mistake 2: Returning Just the Object (Not a ResponseEntity)
**Wrong:**
```java
@PostMapping
public Student create(@RequestBody StudentRequest req) {
    return service.create(req); // ← Spring defaults to 200
}
```
**Problem:** When you return a bare object from a `@RestController`, Spring uses 200 OK automatically. For POST you need 201 — you can only control the status code by returning `ResponseEntity<T>`.
**Fix:**
```java
public ResponseEntity<Student> create(@RequestBody StudentRequest req) {
    Student saved = service.create(req);
    URI uri = URI.create("/students/" + saved.getId());
    return ResponseEntity.created(uri).body(saved);
}
```

---

## Mistake 3: Missing @RequestBody on the Method Parameter
**Wrong:**
```java
@PostMapping
public ResponseEntity<Student> create(StudentRequest req) { ... }
// No @RequestBody annotation ↑
```
**Problem:** Without `@RequestBody`, Spring tries to bind the request to query parameters or form fields — not the JSON body. `req` will be `null` or empty. You'll get a `NullPointerException` or validation failure.
**Fix:** Always annotate the parameter with `@RequestBody` to tell Spring to deserialize the JSON request body:
```java
public ResponseEntity<Student> create(@RequestBody StudentRequest req) { ... }
```
