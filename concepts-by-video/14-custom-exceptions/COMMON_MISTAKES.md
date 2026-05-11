# Common Mistakes — Custom Exceptions

## Mistake 1: Extending Exception (checked) instead of RuntimeException (unchecked)
**Wrong:** `public class StudentNotFoundException extends Exception { ... }`
**Problem:** Every method that might throw this must declare `throws StudentNotFoundException` OR wrap it in try-catch. This spreads through the entire codebase.
**Fix:** `extends RuntimeException` — unchecked, no boilerplate needed anywhere.

## Mistake 2: Forgetting to call super(message)
**Wrong:**
```java
public class StudentNotFoundException extends RuntimeException {
    private String msg;
    public StudentNotFoundException(String message) { this.msg = message; } // wrong!
}
```
**Problem:** `ex.getMessage()` returns null! The GlobalExceptionHandler uses `ex.getMessage()` to build the error response — it will say "null" instead of your message.
**Fix:** Always call `super(message)` to pass the message to `RuntimeException`.

## Mistake 3: Catching the custom exception in the service or controller (swallows it)
**Wrong:**
```java
public Student getById(Long id) {
    try {
        return repo.findById(id).orElseThrow(...);
    } catch (StudentNotFoundException e) {
        return null;  // swallows the exception! Controller returns 200 with null body.
    }
}
```
**Problem:** The exception never reaches GlobalExceptionHandler — it's caught here and returns null. Client gets 200 with empty body instead of 404.
**Fix:** Don't catch custom exceptions in service methods. Let them propagate to GlobalExceptionHandler.
