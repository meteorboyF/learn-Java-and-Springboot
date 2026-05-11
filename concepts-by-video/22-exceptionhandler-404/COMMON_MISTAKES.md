# Common Mistakes — @ExceptionHandler and 404 Not Found

## Mistake 1: Using Try-Catch in the Controller Instead of @ExceptionHandler
**Wrong:**
```java
@GetMapping("/{id}")
public Student get(@PathVariable Long id) {
    try {
        return service.findById(id);
    } catch (StudentNotFoundException e) {
        // Now what? You can't return a Student here...
        return null; // returns 200 with a null body — broken!
    }
}
```
**Problem:** Try-catch in the controller prevents the exception from reaching `@ExceptionHandler`. You end up with confused return types and can't easily return the right HTTP status code.
**Fix:** Don't use try-catch in controllers. Let the exception propagate — `@RestControllerAdvice` intercepts it and handles it in one central place.

---

## Mistake 2: Catching Generic Exception Before More Specific Ones in Service Methods
**Wrong:**
```java
// In StudentService.java:
public Student findById(Long id) {
    try {
        return repo.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    } catch (Exception e) {         // catches StudentNotFoundException too!
        return null;                // swallows the exception — GlobalExceptionHandler never sees it
    }
}
```
**Problem:** `StudentNotFoundException` is a subclass of `Exception`. The `catch (Exception e)` block intercepts it before it can reach `GlobalExceptionHandler`. Result: all 404s silently become 200 responses with `null` bodies.
**Fix:** Remove the try-catch from the service. Only use try-catch when you truly want to recover (e.g., retry logic), not to suppress or log exceptions.

---

## Mistake 3: @ExceptionHandler on a Method Outside @RestControllerAdvice
**Wrong:**
```java
@RestController
public class StudentController {
    @ExceptionHandler(StudentNotFoundException.class)  // ← placed inside a @RestController
    public ResponseEntity<ErrorResponse> handle(StudentNotFoundException ex) { ... }
}
```
**Problem:** `@ExceptionHandler` inside a `@RestController` only handles exceptions thrown by THAT controller class. It won't intercept exceptions from any other controller. This creates code duplication if you have multiple controllers.
**Fix:** Always put `@ExceptionHandler` methods inside a class annotated with `@RestControllerAdvice`. This makes them global — they intercept exceptions from ALL `@RestController` classes.
