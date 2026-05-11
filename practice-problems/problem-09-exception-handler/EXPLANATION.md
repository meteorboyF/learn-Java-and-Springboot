# Explanation — Problem 09: Global Exception Handler

## What Happens Without `GlobalExceptionHandler`?

Without the handler, Spring's default error handling returns this for a `StudentNotFoundException`:

```json
{
  "timestamp": "2024-05-11T10:22:01.000+00:00",
  "status": 500,
  "error": "Internal Server Error",
  "path": "/api/STU001/students/999"
}
```

Problems:
- Status is **500** (wrong — should be 404 for not-found)
- The message is generic — no information about what wasn't found
- The JUnit test checks for status 404 → **test fails**

With `GlobalExceptionHandler`:
```json
{
  "status": 404,
  "message": "Student not found with id: 999",
  "timestamp": "2024-05-11 10:22:01"
}
```
Status is **404** → **test passes**.

## `@RestControllerAdvice` vs `@ControllerAdvice`

This is **Bug 5 in Problem 12** and is explicitly tested:

| | `@ControllerAdvice` | `@RestControllerAdvice` |
|---|---|---|
| For | MVC apps returning HTML | REST APIs returning JSON |
| Return values | Treated as VIEW NAMES | Serialized to JSON automatically |
| `@ResponseBody` included? | ❌ No | ✅ Yes |
| What happens for REST APIs | Exception IS caught but response is broken | Exception caught and JSON returned correctly |

**The subtle bug:** When you use `@ControllerAdvice`, Spring DOES catch the exception. But then it tries to find an HTML view with the name matching your return value (like `"Student not found: 5"`). When no view is found, Spring returns a 500 error — not the 404 you wanted.

## Handler Specificity — Spring Always Picks the Most Specific Match

Exception inheritance:
```
Throwable
  └── Exception
        ├── RuntimeException
        │     └── StudentNotFoundException
        └── IllegalArgumentException → (extends RuntimeException too)
```

When `StudentNotFoundException` is thrown:
- Handler 1 matches: `@ExceptionHandler(StudentNotFoundException.class)` ← **chosen (most specific)**
- Handler 3 matches: `@ExceptionHandler(Exception.class)` (would also match, but less specific)

Spring always uses the most specific matching handler. The order you declare them in the class doesn't matter.

## The `ErrorResponse` DTO — Why Getters Matter

```java
public class ErrorResponse {
    private int status;
    private String message;

    public int getStatus() { return status; }
    // getTimestamp() is MISSING
}
```

Jackson serializes using getters. Without `getTimestamp()`, the JSON output is:
```json
{ "status": 404, "message": "Not found" }
// timestamp is MISSING from JSON!
```

This is why every field you want in the response needs a getter.

## Flow Diagram

```
Client sends GET /api/STU001/students/999
    ↓
StudentController.getStudentById(999)
    ↓
StudentService.getStudentById(999)
    ↓ repo.findById(999) returns empty Optional
    ↓ .orElseThrow() executes lambda
    ↓ throws StudentNotFoundException("Student not found with id: 999")
    ↓
[Exception bubbles up through call stack]
    ↓
Spring's DispatcherServlet catches it
    ↓
Spring finds @ExceptionHandler(StudentNotFoundException.class) in GlobalExceptionHandler
    ↓
handleNotFound(ex) is called
    ↓
Returns: HTTP 404 + {"status":404,"message":"Student not found with id: 999","timestamp":"..."}
    ↓
Client receives 404 response ✅
```
