# Common Mistakes — ResponseEntity and Status Codes

## Mistake 1: POST returns 200 instead of 201
**Wrong:** `return ResponseEntity.ok(saved);`
**Fix:** `return ResponseEntity.created(location).body(saved);`
**Why:** POST (create) → 201. `ok()` always returns 200. This is Bug 1 in Problem 12 and THE most common exam mistake.

---

## Mistake 2: DELETE returns 200 with a body instead of 204 with no body
**Wrong:**
```java
@DeleteMapping("/{id}")
public ResponseEntity<Student> delete(@PathVariable Long id) {
    Student deleted = service.deleteStudent(id);
    return ResponseEntity.ok(deleted);  // 200 with body — wrong!
}
```
**Fix:**
```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.deleteStudent(id);
    return ResponseEntity.noContent().build();  // 204, no body
}
```
**Why:** DELETE → 204 No Content. There's nothing meaningful to return after deletion. Returning the deleted resource is unusual and non-standard.

---

## Mistake 3: Calling `.build()` when a body IS needed (or vice versa)
**Wrong:**
```java
return ResponseEntity.ok().build();        // 200, no body — user gets empty response
return ResponseEntity.noContent().body(x); // won't compile — noContent() doesn't have body()
```
**Rule:**
- `.build()` — creates response with NO body (correct for 204)
- `.body(obj)` — creates response WITH a body (correct for 200, 201)
- `ResponseEntity.noContent()` returns a `HeadersBuilder`, not a `BodyBuilder` — it has `.build()` but not `.body()`
