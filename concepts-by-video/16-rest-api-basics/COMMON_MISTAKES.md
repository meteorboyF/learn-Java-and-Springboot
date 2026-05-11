# Common Mistakes — REST API Basics

## Mistake 1: Returning 200 OK from a POST (Bug 1 in Problem 12)
**Wrong:** `return ResponseEntity.ok(saved);` inside `@PostMapping`
**Problem:** POST must return 201 Created per REST convention. Returning 200 is technically "works" but is semantically incorrect and will fail exam tests that check status codes.
**Fix:** `return ResponseEntity.status(HttpStatus.CREATED).body(saved);`

---

## Mistake 2: Using Verbs in URL Paths
**Wrong:**
```
@GetMapping("/getAllStudents")
@PostMapping("/createStudent")
@DeleteMapping("/deleteStudentById")
```
**Problem:** REST URLs identify THINGS (resources), not actions. Verbs in URLs break the REST constraint.
**Fix:** Use nouns only — the HTTP method IS the verb:
```
@GetMapping            → "get all students"
@PostMapping           → "create a student"
@DeleteMapping("/{id}")→ "delete student with this id"
```

---

## Mistake 3: Confusing @Controller with @RestController
**Wrong:** `@Controller public class StudentController { ... }` returning objects
**Problem:** `@Controller` alone treats return values as view (HTML template) names. The object is NOT serialized to JSON. The response will be a 500 error or a "view not found" error.
**Fix:** Use `@RestController` for REST APIs. It combines `@Controller + @ResponseBody`, which tells Jackson to serialize return values to JSON automatically.
