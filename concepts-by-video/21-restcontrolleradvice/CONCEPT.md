# @RestControllerAdvice and Global Exception Handling

## What is it?
`@RestControllerAdvice` marks a class as a global exception handler for ALL `@RestController` classes in the application. Combined with `@ExceptionHandler` methods, it converts exceptions into proper HTTP error responses automatically.

## Why Does It Exist?
Without it, you'd need try-catch blocks in EVERY controller method. One missing try-catch = a 500 error reaches the client. `@RestControllerAdvice` centralises error handling in ONE place.

## When to Use It
Every Spring Boot REST API project should have exactly ONE `GlobalExceptionHandler` class annotated with `@RestControllerAdvice`.

## `@ControllerAdvice` vs `@RestControllerAdvice`
| | `@ControllerAdvice` | `@RestControllerAdvice` |
|---|---|---|
| For | MVC apps (HTML) | REST APIs (JSON) |
| `@ResponseBody` included? | ❌ No | ✅ Yes |
| Return values serialized to JSON? | ❌ No (treated as view names) | ✅ Yes (automatically) |
| EXAM: Use for REST APIs? | ❌ Wrong | ✅ Correct |

**This is Bug 5 in Problem 12 — using `@ControllerAdvice` instead of `@RestControllerAdvice`.**

## How Exception Matching Works
Spring picks the **most specific** `@ExceptionHandler` for a thrown exception:

```
StudentNotFoundException thrown
    ↓
Spring checks all @ExceptionHandler methods:
    @ExceptionHandler(StudentNotFoundException.class) ← MATCHES (most specific)
    @ExceptionHandler(RuntimeException.class)          ← would also match, but less specific
    @ExceptionHandler(Exception.class)                 ← would also match, but least specific
    ↓
Spring calls the MOST SPECIFIC match → handleNotFound() is called → returns 404
```

## EXAM: Handler order in class doesn't matter — specificity wins, not declaration order.
