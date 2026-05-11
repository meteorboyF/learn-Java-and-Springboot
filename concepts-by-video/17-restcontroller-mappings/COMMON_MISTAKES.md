# Common Mistakes — @RestController and Mappings

## Mistake 1: Using @Controller instead of @RestController
**Wrong:**
```java
@Controller
public class StudentController {
    @GetMapping("/students")
    public List<Student> getAll() { return service.getAll(); }  // Spring tries to find a view!
}
```
**Problem:** `@Controller` without `@ResponseBody` treats return values as view names. Spring looks for a template named `[Alice, Bob, ...]` — fails with 500.
**Fix:** Use `@RestController`. Or add `@ResponseBody` to every method (verbose — don't do this).

---

## Mistake 2: Missing `@RequestBody` on POST method
**Wrong:**
```java
@PostMapping
public ResponseEntity<Student> create(Student student) {  // no annotation!
    ...
}
```
**Problem:** Spring doesn't know to read the JSON body. `student` will have all fields as null.
**Fix:** Add `@RequestBody`: `public ResponseEntity<Student> create(@RequestBody Student student)`

---

## Mistake 3: Returning wrong status code from POST
**Wrong:**
```java
@PostMapping
public ResponseEntity<Student> create(@RequestBody Student student) {
    return ResponseEntity.ok(service.create(student));  // returns 200!
}
```
**Problem:** POST should return 201 (Created), not 200. Tests checking status code will fail.
**Fix:**
```java
return ResponseEntity.status(HttpStatus.CREATED).body(saved);  // 201
// OR:
return ResponseEntity.created(locationUri).body(saved);  // 201 + Location header
```
