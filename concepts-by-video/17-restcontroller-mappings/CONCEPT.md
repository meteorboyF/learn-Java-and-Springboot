# @RestController and HTTP Mapping Annotations

## What is it?
`@RestController` marks a class as a REST API controller. The mapping annotations (`@GetMapping`, `@PostMapping`, etc.) map specific HTTP method + URL combinations to Java methods.

## Why Does It Exist?
Without these annotations, Spring has no way to know which Java method to call when an HTTP request arrives. The mapping annotations create the routing table: "When GET /students arrives, call `getAllStudents()`."

## When to Use Each Annotation

| Annotation | HTTP Method | Operation | Status |
|---|---|---|---|
| `@GetMapping` | GET | Read | 200 |
| `@PostMapping` | POST | Create | 201 |
| `@PutMapping` | PUT | Full update | 200 |
| `@PatchMapping` | PATCH | Partial update | 200 |
| `@DeleteMapping` | DELETE | Delete | 204 |

## `@RestController` vs `@Controller`
- `@Controller` — for MVC apps (returns HTML view names)
- `@RestController` — for REST APIs (returns JSON automatically) = `@Controller` + `@ResponseBody`
- **EXAM:** Always use `@RestController` for REST APIs

## `@RequestMapping` Levels
```java
@RestController
@RequestMapping("/api/{studentId}/students")  // class-level: base path for ALL methods
public class StudentController {

    @GetMapping                    // maps: GET /api/{studentId}/students
    public ResponseEntity<...> getAll() { ... }

    @GetMapping("/{id}")           // maps: GET /api/{studentId}/students/{id}
    public ResponseEntity<...> getById(@PathVariable Long id) { ... }

    @PostMapping                   // maps: POST /api/{studentId}/students
    public ResponseEntity<...> create(...) { ... }

    @PutMapping("/{id}")           // maps: PUT /api/{studentId}/students/{id}
    public ResponseEntity<...> update(...) { ... }

    @DeleteMapping("/{id}")        // maps: DELETE /api/{studentId}/students/{id}
    public ResponseEntity<...> delete(...) { ... }
}
```

## EXAM: The URL Prefix Pattern
The quiz uses: `/api/{studentId}/students`
- `{studentId}` is YOUR student number as a URL path variable
- It makes each student's API URL unique in the lab
- The actual student entity's `studentId` field is different — both happen to have the same name by coincidence
