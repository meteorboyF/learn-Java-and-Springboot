# @ExceptionHandler and 404 Not Found

## The Problem It Solves
When a user requests a student that doesn't exist, what should happen?

**Without @ExceptionHandler:** Spring returns a generic 500 Internal Server Error with a stack trace — useless to the client, potentially a security risk.

**With @ExceptionHandler:** You control exactly what the client receives: a clean 404 with a helpful message.

## How @ExceptionHandler Works
`@ExceptionHandler` is placed on a method inside a `@RestControllerAdvice` class (the `GlobalExceptionHandler`). It tells Spring: "when THIS exception type is thrown from any `@RestController`, call THIS method to handle it."

```
@RestController method throws StudentNotFoundException
    ↓
Spring catches it automatically
    ↓
Spring finds @ExceptionHandler(StudentNotFoundException.class)
    ↓
Calls the handler method
    ↓
Returns the ResponseEntity it builds (e.g. 404 with JSON error body)
```

## The Flow for a 404
1. Client sends `GET /api/STU001/students/99` (id=99 doesn't exist)
2. Controller calls `service.findById(99)`
3. Service calls `repo.findById(99).orElseThrow(() -> new StudentNotFoundException(99))`
4. `orElseThrow` triggers because the Optional is empty
5. `StudentNotFoundException` propagates up through service → controller
6. Spring's `@ExceptionHandler` intercepts it before it reaches the client
7. Handler returns `ResponseEntity.status(404).body(new ErrorResponse(404, "Student not found with id: 99"))`
8. Client receives 404 JSON — not a 500

## EXAM: Three Key Points
1. `@ExceptionHandler` goes on a METHOD inside `@RestControllerAdvice`.
2. The most specific handler wins when multiple handlers could match.
3. This is the correct alternative to try-catch in every controller method.
