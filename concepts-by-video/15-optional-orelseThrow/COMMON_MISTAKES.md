# Common Mistakes — Optional and orElseThrow

## Mistake 1: Using `.get()` instead of `.orElseThrow()` — THE #1 MISTAKE
**Wrong:** `return repo.findById(id).get();`
**Error:** If not found → `NoSuchElementException` → GlobalExceptionHandler doesn't catch it → 500 response
**Fix:** `return repo.findById(id).orElseThrow(() -> new StudentNotFoundException(id));`
This is Bug 2 in Problem 12. It's tested on the exam.

---

## Mistake 2: Not using Optional for unique-field queries
**Wrong:**
```java
Student findByStudentId(String studentId);  // returns null if not found!
```
**Problem:** JPA auto-generated queries return null when nothing matches, causing NullPointerException downstream.

**Fix:**
```java
Optional<Student> findByStudentId(String studentId);  // explicit: might be empty
```
Then use `.orElseThrow()` in the service.

---

## Mistake 3: Creating an Optional with `.of()` when the value might be null
**Wrong:** `Optional.of(someValue)` — throws `NullPointerException` if `someValue` is null!
**Fix:** Use `Optional.ofNullable(someValue)` when the value might be null.
- `Optional.of(value)` — use when you KNOW value is not null
- `Optional.ofNullable(value)` — use when value MIGHT be null
- In Spring Data JPA, repository methods return proper Optionals — you rarely create them manually.
