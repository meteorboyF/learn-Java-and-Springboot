# Common Mistakes — @PathVariable and @RequestParam

## Mistake 1: Mismatched Placeholder and Parameter Name
**Wrong:**
```java
@GetMapping("/{id}")
public Student get(@PathVariable Long studentId) { ... }
//                              ↑ name is "studentId" but URL says "id"
```
**Problem:** Spring can't match `{id}` in the URL to `studentId` in Java → 500 error at startup or runtime: `Missing URI template variable 'studentId'`.
**Fix:** Either match names exactly (`@PathVariable Long id`) or use explicit mapping (`@PathVariable("id") Long studentId`).

---

## Mistake 2: Using @RequestParam When You Should Use @PathVariable (and vice versa)
**Wrong:**
```java
// DELETE /students?id=5   ← wrong, id should be in path
@DeleteMapping
public void delete(@RequestParam Long id) { ... }
```
**Problem:** REST convention says resource identity goes in the path (`/students/5`), not query string. Also, `DELETE /students` looks like "delete all students" — dangerous and confusing.
**Fix:**
```java
// DELETE /students/5   ← correct
@DeleteMapping("/{id}")
public ResponseEntity<Void> delete(@PathVariable Long id) { ... }
```

---

## Mistake 3: Forgetting `required=false` for Optional Params Causes 400 Errors
**Wrong:**
```java
@GetMapping("/search")
public List<Student> search(@RequestParam String major) { ... }
// Client calls: GET /search    ← no ?major= provided
```
**Problem:** `@RequestParam` is `required=true` by default. If the client doesn't provide `?major=...`, Spring returns `400 Bad Request` automatically with error "Required request parameter 'major' ... is not present."
**Fix:** Mark it optional if you want to allow missing params:
```java
@RequestParam(required = false, defaultValue = "CS") String major
```
