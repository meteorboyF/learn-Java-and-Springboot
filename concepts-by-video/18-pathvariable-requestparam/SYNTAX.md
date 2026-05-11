# SYNTAX — @PathVariable and @RequestParam

## @PathVariable

```java
// Basic — placeholder name matches parameter name exactly
@GetMapping("/{id}")
public Student getById(@PathVariable Long id) { ... }

// With explicit name — when Java name differs from URL placeholder
@GetMapping("/{studentId}")
public Student getByStudentId(@PathVariable("studentId") String sid) { ... }

// Multiple path variables in one method
@GetMapping("/{courseId}/enrollment/{studentId}")
public String getEnrollment(
    @PathVariable Long courseId,
    @PathVariable Long studentId
) { ... }

// From class-level @RequestMapping — the {studentId} placeholder is accessible too
@RestController
@RequestMapping("/api/{studentId}/students")
public class StudentController {
    @GetMapping("/{id}")
    public String get(
        @PathVariable String studentId,  // from class @RequestMapping
        @PathVariable Long id            // from method @GetMapping
    ) { ... }
}
```

## @RequestParam

```java
// Required (default) — client MUST provide ?name=value or Spring returns 400
@GetMapping("/search")
public List<Student> search(@RequestParam String major) { ... }
// URL: /search?major=CS

// Optional with default
@GetMapping("/filter")
public List<Student> filter(
    @RequestParam(required = false, defaultValue = "0.0") Double minGpa
) { ... }
// URL: /filter          → minGpa=0.0
// URL: /filter?minGpa=3 → minGpa=3.0

// Explicit name mapping (URL param name differs from Java name)
@GetMapping("/byId")
public Student getByStudentId(@RequestParam("sid") String studentId) { ... }
// URL: /byId?sid=STU001 → studentId="STU001"

// Multiple request params
@GetMapping("/advanced")
public List<Student> advanced(
    @RequestParam String major,
    @RequestParam(required = false, defaultValue = "0.0") Double minGpa,
    @RequestParam(required = false, defaultValue = "10") Integer limit
) { ... }
// URL: /advanced?major=CS&minGpa=3.0&limit=5
```

## Quick Decision Rule

```
Is the value part of the URL path itself?   → @PathVariable
Is the value in the ?key=value query string? → @RequestParam
Is the value required to identify a resource? → @PathVariable
Is the value optional / a filter?            → @RequestParam
```
