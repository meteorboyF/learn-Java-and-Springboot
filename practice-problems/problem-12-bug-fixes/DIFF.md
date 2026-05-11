# DIFF — Problem 12: All 5 Bugs and Their Fixes

## Bug 1: Wrong POST Status Code

**Symptom:** POST returns HTTP 200 instead of HTTP 201.

**Buggy line:**
```java
return ResponseEntity.ok(saved);  // .ok() = 200
```

**Fixed line:**
```java
URI location = URI.create("/api/" + studentId + "/students/" + saved.getId());
return ResponseEntity.created(location).body(saved);  // .created() = 201
```

**Why:** The HTTP spec says POST (create) → 201 Created. `.ok()` always returns 200. `ResponseEntity.created(uri)` returns 201 AND adds the Location header. JUnit tests check the status code numerically — 200 ≠ 201 → test fails.

---

## Bug 2: `.get()` on Optional

**Symptom:** GET /students/999 (non-existent) returns HTTP 500 instead of 404.

**Buggy line:**
```java
return studentRepository.findById(id).get();  // throws NoSuchElementException
```

**Fixed line:**
```java
return studentRepository.findById(id)
        .orElseThrow(() -> new StudentNotFoundException("Student not found: " + id));
```

**Why:** `.get()` on an empty Optional throws `NoSuchElementException`. The `GlobalExceptionHandler` only handles `StudentNotFoundException` → returns 404. `NoSuchElementException` falls through to the `Exception` catch-all → returns 500. `.orElseThrow()` throws your custom exception → GlobalExceptionHandler maps it to 404.

---

## Bug 3: Missing No-Arg Constructor in `@Entity`

**Symptom:** App crashes when any data is loaded from the database. Error: `No default constructor for entity: Student`

**Buggy code:**
```java
@Entity
public class Student {
    // Only parameterised constructor defined:
    public Student(String name, String studentId, Double gpa, String major) { ... }
    // Missing: public Student() {}
}
```

**Fixed code:**
```java
@Entity
public class Student {
    public Student() {}  // ← ADDED: required by JPA
    public Student(String name, String studentId, Double gpa, String major) { ... }
}
```

**Why:** JPA uses Java reflection to create entity objects when loading from DB. It calls the no-arg constructor first, then fills fields via setters. If you define any constructor in Java, the compiler stops auto-generating the no-arg one — you must add it explicitly.

---

## Bug 4: Seeder Without Duplicate Guard

**Symptom:** Every restart inserts 3 more students. With `unique` constraints on `studentId`, the second restart throws `ConstraintViolationException` and the app fails to start.

**Buggy code:**
```java
@Override
public void run(String... args) throws Exception {
    // No guard — always inserts:
    repo.save(new Student("Alice", "STU20240001", 3.85, "CS"));
    ...
}
```

**Fixed code:**
```java
@Override
public void run(String... args) throws Exception {
    if (repo.count() > 0) return;  // ← ADDED: guard against duplicate seeding
    repo.save(new Student("Alice", "STU20240001", 3.85, "CS"));
    ...
}
```

**Why:** With `ddl-auto=update` and persistent storage (H2 file-based, MySQL), tables are NOT dropped on restart — data persists. Without the guard, every restart inserts duplicates. The `unique=true` constraint on `studentId` makes the second insert fail with a `ConstraintViolationException`.

---

## Bug 5: `@ControllerAdvice` Instead of `@RestControllerAdvice`

**Symptom:** Exception handler runs but client still receives 500. The exception IS caught but the response is wrong.

**Buggy code:**
```java
@ControllerAdvice  // ← WRONG for REST APIs
public class GlobalExceptionHandler {
    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<String> handleNotFound(StudentNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }
}
```

**Fixed code:**
```java
@RestControllerAdvice  // ← CORRECT: adds @ResponseBody automatically
public class GlobalExceptionHandler {
    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<String> handleNotFound(StudentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
```

**Why:** `@ControllerAdvice` is for MVC apps returning HTML views. It does NOT add `@ResponseBody` — return values are treated as view names. For REST APIs, use `@RestControllerAdvice` which equals `@ControllerAdvice + @ResponseBody`, ensuring return values are serialized to JSON. This is the most subtle of the 5 bugs because the handler code RUNS — the annotation makes the output format wrong, not the logic.

---

## Summary Table (EXAM: memorise these)

| Bug | Root Cause | Fix | Effect |
|---|---|---|---|
| 1 | `.ok()` used for POST | Use `.created(uri)` | 200 → 201 |
| 2 | `.get()` on Optional | Use `.orElseThrow()` | 500 → 404 |
| 3 | No no-arg constructor | Add `public Entity() {}` | crash → works |
| 4 | No seeder guard | Add `if (count > 0) return;` | duplicates → once |
| 5 | `@ControllerAdvice` | Use `@RestControllerAdvice` | 500 → 404 |
