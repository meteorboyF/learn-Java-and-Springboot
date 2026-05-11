# Syntax Reference — @RestController and Mapping Annotations

## @RestController
```java
@RestController  // class-level only, no attributes
public class MyController { ... }
```

## @RequestMapping — All Forms
```java
// Class-level (base path):
@RequestMapping("/students")
@RequestMapping("/api/{studentId}/students")
@RequestMapping(value = "/students", produces = "application/json")

// Method-level (adds to class path):
@RequestMapping(value = "/{id}", method = RequestMethod.GET)
```

## HTTP Method Shortcuts
```java
@GetMapping             // = @RequestMapping(method = GET)
@GetMapping("/{id}")    // with path suffix
@GetMapping(value = "/{id}", produces = "application/json")

@PostMapping
@PostMapping(consumes = "application/json")

@PutMapping("/{id}")
@PatchMapping("/{id}")
@DeleteMapping("/{id}")
```

## Combining Class + Method Paths
```java
@RequestMapping("/api")  // class: base = /api
public class Controller {
    @GetMapping("/students")      // full: GET /api/students
    @PostMapping("/students")     // full: POST /api/students
    @GetMapping("/students/{id}") // full: GET /api/students/{id}
    @DeleteMapping("/courses/{id}") // full: DELETE /api/courses/{id}
}
```

## @RequestMapping Attributes
```java
@RequestMapping(
    value = "/students",        // URL path (or use path = "/students")
    method = RequestMethod.GET, // HTTP method
    produces = "application/json",  // response content type
    consumes = "application/json"   // request body content type
)
```
