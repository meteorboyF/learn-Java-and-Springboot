# Explanation — Problem 06: Service Layer

## Why `RuntimeException` and Not `Exception`?

This is explicitly tested. The answer has three parts:

1. **No forced try-catch:** Unchecked (`RuntimeException`) doesn't require callers to catch it. Your controller methods stay clean — no `throws StudentNotFoundException` or `try { } catch { }` blocks needed.

2. **Auto-bubbling to the handler:** The exception travels up through the call stack automatically (Controller → Spring Dispatcher → GlobalExceptionHandler). The handler catches it and returns 404. If you used checked `Exception`, every method in the chain would need a `throws` declaration.

3. **Convention:** Domain-specific "not found" exceptions are always unchecked in Spring. `EntityNotFoundException` in JPA itself is unchecked.

## Why `.orElseThrow()` and Never `.get()`?

```java
// BAD — do not do this:
Student s = repo.findById(id).get();
// If no student with that id exists:
//   .get() throws: NoSuchElementException (no message)
//   GlobalExceptionHandler has NO handler for NoSuchElementException
//   → Fallback handler catches it → returns 500 Internal Server Error
//   → The test expects 404 → TEST FAILS

// GOOD — always do this:
Student s = repo.findById(id)
    .orElseThrow(() -> new StudentNotFoundException(id));
// If no student exists:
//   .orElseThrow() throws: StudentNotFoundException("Student not found with id: 5")
//   GlobalExceptionHandler catches StudentNotFoundException → returns 404
//   → The test expects 404 → TEST PASSES
```

**This is Bug 2 in Problem 12.** It is one of the most commonly lost marks on the exam.

## The `updateStudent` Pattern

```java
// Pattern: find → modify → save
Student existing = getStudentById(id);       // Step 1: find or throw 404
existing.setName(updatedData.getName());     // Step 2: modify ONLY safe fields  
existing.setGpa(updatedData.getGpa());
existing.setMajor(updatedData.getMajor());
return repo.save(existing);                  // Step 3: save triggers UPDATE
```

### Fields we DO NOT update:
- `id` — this is the database primary key, set permanently at creation
- `studentId` — this is the university-issued ID, should be immutable once set

### Why `repo.save(existing)` does UPDATE not INSERT:
`save()` checks if the entity has an id. If yes → UPDATE. If id is null → INSERT. Since `existing` was loaded from the database, it has an id → `save()` generates `UPDATE`.

## The `deleteStudent` Pattern

```java
// Why check BEFORE deleting?
if (!repo.existsById(id)) {          // SELECT COUNT(*) WHERE id=?
    throw new StudentNotFoundException(id);
}
repo.deleteById(id);                  // DELETE WHERE id=?
```

In Spring Boot 3.x, `deleteById()` with a non-existent id does nothing silently. Without the check, deleting `id=999` (which doesn't exist) would return `204 No Content` — wrong, it should return `404`. The check ensures correct HTTP semantics.

## Layer Architecture Reminder

```
HTTP Request
    ↓
StudentController  (handles HTTP in/out only)
    ↓
StudentService     (contains all business logic ← you are here)
    ↓
StudentRepository  (handles database access)
    ↓
Database
```

**Rule:** Controllers never call repositories directly. Services never handle HTTP. Each layer has one responsibility.
