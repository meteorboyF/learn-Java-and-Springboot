# Custom Exceptions

## What is it?
Domain-specific exception classes that communicate exactly what went wrong (e.g., `StudentNotFoundException` vs. a generic `Exception`). They extend `RuntimeException` for unchecked behavior.

## Why Does It Exist?
- **Specificity:** `StudentNotFoundException` tells the handler exactly what happened — return 404.
- **Messages:** You control the error message: "Student not found with id: 5" vs. "NoSuchElementException".
- **Handler mapping:** `GlobalExceptionHandler` maps each exception type to an HTTP status code.

## The Two Constructor Pattern
```java
public class StudentNotFoundException extends RuntimeException {
    // For text messages: throw new StudentNotFoundException("Not found: STU001")
    public StudentNotFoundException(String message) {
        super(message);
    }
    
    // For ID-based lookups: throw new StudentNotFoundException(5L)
    public StudentNotFoundException(Long id) {
        super("Student not found with id: " + id);
    }
}
```

## Why RuntimeException (unchecked) NOT Exception (checked)?
- **Unchecked:** No try-catch or `throws` required in callers. Exception bubbles up automatically.
- **Checked:** EVERY method in the call chain needs `throws StudentNotFoundException` or try-catch.
- For "not found" domain errors: always unchecked (RuntimeException).
- EXAM: "Why RuntimeException?" is tested. Know the answer.

## Naming Convention
```
EntityNotFoundException: StudentNotFoundException, CourseNotFoundException
ServiceException: PaymentFailedException
ValidationException: InvalidGpaException
```
