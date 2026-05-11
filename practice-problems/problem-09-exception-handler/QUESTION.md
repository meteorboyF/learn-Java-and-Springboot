# Problem 09 — Global Exception Handler with Structured Error Response

**Source:** CSE 2218 Advanced OOP Lab — Section 4, Question 9

## The Question

Create a `GlobalExceptionHandler` that:

a) Catches `StudentNotFoundException` → 404 Not Found  
b) Catches `IllegalArgumentException` → 400 Bad Request  
c) Catches any other `Exception` → 500 Internal Server Error  
d) Returns a structured JSON error body with: `status` (int), `message` (String), `timestamp` (String)  
e) Also create the `ErrorResponse` DTO class used as the return body

## What You Need to Know Before Solving

- `@RestControllerAdvice` = `@ControllerAdvice` + `@ResponseBody` → use this for REST APIs
- `@ExceptionHandler(ExceptionClass.class)` marks a method as a handler for that exception type
- Spring picks the **most specific** matching handler (StudentNotFoundException wins over Exception)
- The `ErrorResponse` DTO needs **getters** so Jackson can serialize it to JSON
- Without `GlobalExceptionHandler`, Spring returns generic 500 errors for all exceptions

## Exam Tips

- `@ControllerAdvice` (wrong) vs `@RestControllerAdvice` (correct) — this is Bug 5 in Problem 12
- The order of handlers doesn't matter — Spring always uses the most specific match
- `ex.getMessage()` returns the message you put in `throw new StudentNotFoundException("...")`
- `LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))` for timestamps
- **One GlobalExceptionHandler per application** — it covers ALL `@RestController` classes
