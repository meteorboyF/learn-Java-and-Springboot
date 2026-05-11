# Explanation — Problem 11: DTO Pattern

## Why DTOs Exist — Three Reasons

### 1. Security: Prevent Mass Assignment
If you use the `@Entity` directly as `@RequestBody`:
```java
@PostMapping
public ResponseEntity<Student> create(@RequestBody Student student) { ... }
```
A malicious client could send:
```json
{"name":"Alice","studentId":"STU001","gpa":4.0,"major":"CS","id":1}
```
Now the client is setting the `id` — potentially overwriting another student's record!

With `StudentRequest` (no `id` field), the client cannot send an `id` — it's simply not in the DTO.

### 2. Data Hiding
If you return the `@Entity` directly:
```java
public ResponseEntity<Student> getById(Long id) { ... }
// Jackson serializes ALL fields with getters → gpa appears in JSON!
```
The client sees gpa even though it's supposed to be private.

With `StudentResponse` (no `getGpa()` method), gpa never appears in any JSON response.

### 3. API Stability
The API contract (what fields the client sees) is decoupled from the database schema. You can:
- Rename a DB column without changing the API
- Add a sensitive DB field without it appearing in responses
- Change the API shape without touching the database

## The Conversion Flow

```
Client sends JSON →  StudentRequest DTO  →  (toEntity)  →  Student Entity  →  DB (INSERT)
                                                                                    ↓
Client receives JSON ← StudentResponse DTO ← (toResponse) ← Student Entity ← DB (SELECT)
```

## Jackson's Serialization Rule (EXAM: critical)

**Jackson only serializes fields that have public getter methods.**

```java
public class StudentResponse {
    private Long id;
    private String name;
    private Double gpa;  // field exists, but...
    
    public Long   getId()   { return id; }     // → "id" appears in JSON ✅
    public String getName() { return name; }   // → "name" appears in JSON ✅
    // No getGpa() method                       // → "gpa" MISSING from JSON ✅
}
```

Response JSON: `{"id":1,"name":"Alice","studentId":"STU001","major":"CS"}`
gpa is NOT present — hidden by the absence of a getter.

## `this::toResponse` — Method Reference

```java
// Long form (lambda):
.map(student -> toResponse(student))

// Short form (method reference) — equivalent:
.map(this::toResponse)
```

Both do the same thing. The method reference is cleaner.

## When to Use DTOs vs Entity Directly

| Scenario | Use Entity directly? | Use DTOs? |
|---|---|---|
| Quick quiz, simple app | ✅ OK (less code) | Optional |
| Production REST API | ❌ Security risk | ✅ Required |
| Exam asks for DTO pattern | ❌ Wrong answer | ✅ Required |
| Sensitive fields in entity | ❌ Data leak | ✅ Required |

For the exam: if the question says "use DTOs" or shows separate Request/Response classes, use them.

## The Three-Layer Data Flow with DTOs

```
HTTP layer (Controller):  @RequestBody StudentRequest  →  StudentService
Service layer (Service):  StudentRequest  →  toEntity()  →  Student (for DB)
Service layer (Service):  Student  →  toResponse()  →  StudentResponse (for API)
HTTP layer (Controller):  StudentResponse  →  ResponseEntity<StudentResponse>
```

The `Student` entity never crosses the HTTP boundary — it stays inside the service layer.
