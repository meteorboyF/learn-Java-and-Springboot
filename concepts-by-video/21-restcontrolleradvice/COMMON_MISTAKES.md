# Common Mistakes — @RestControllerAdvice

## Mistake 1: Using @ControllerAdvice instead of @RestControllerAdvice (Bug 5)
**Wrong:** `@ControllerAdvice`
**Fix:** `@RestControllerAdvice`
**Why:** `@ControllerAdvice` doesn't add `@ResponseBody` — return values are treated as view names, not JSON. The exception IS caught but the response is wrong (500 instead of 404). This is explicitly tested.

---

## Mistake 2: Missing getters in the ErrorResponse DTO
**Wrong:**
```java
public class ErrorResponse {
    private int status;
    private String message;
    // No getters!
}
```
**Problem:** Jackson can't serialize the fields → empty JSON `{}` is returned → test checking for "status" field fails.
**Fix:** Add public getters: `public int getStatus() { return status; }` and `public String getMessage() { return message; }`

---

## Mistake 3: Catching Exception before more specific exceptions
**Wrong (if using try-catch — avoid this pattern):**
```java
// In a service method (BAD approach):
try {
    return repo.findById(id).orElseThrow(...);
} catch (Exception e) {      // catches StudentNotFoundException too!
    return null;              // swallows the meaningful exception
}
```
**Problem:** The generic catch intercepts `StudentNotFoundException` before it can reach the `GlobalExceptionHandler`.
**Fix:** Don't use try-catch in service methods. Let the exception propagate to the `GlobalExceptionHandler` automatically. Only use try-catch in services for truly recoverable situations.
