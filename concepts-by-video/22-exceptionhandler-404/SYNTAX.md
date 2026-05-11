# SYNTAX — @ExceptionHandler and 404 Not Found

## @ExceptionHandler Method

```java
// Inside a @RestControllerAdvice class:

// Handle one exception type
@ExceptionHandler(StudentNotFoundException.class)
public ResponseEntity<ErrorResponse> handleNotFound(StudentNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                         .body(new ErrorResponse(404, ex.getMessage()));
}

// Handle multiple exception types in one method
@ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
public ResponseEntity<ErrorResponse> handleBadInput(Exception ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                         .body(new ErrorResponse(400, ex.getMessage()));
}

// Catch-all fallback
@ExceptionHandler(Exception.class)
public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                         .body(new ErrorResponse(500, "Unexpected error"));
}
```

## Throwing Exceptions in Service/Controller

```java
// In a service method (preferred place to throw):
public Student findById(Long id) {
    return repo.findById(id)
               .orElseThrow(() -> new StudentNotFoundException(id));
}

// Manual check and throw:
public Student findById(Long id) {
    if (!repo.existsById(id)) {
        throw new StudentNotFoundException(id);
    }
    return repo.findById(id).get();
}
```

## Custom Exception Class

```java
// Unchecked (extends RuntimeException) — no try-catch needed by callers
public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(Long id) {
        super("Student not found with id: " + id);
    }
    public StudentNotFoundException(String message) {
        super(message);
    }
}
```

## HTTP Status Codes Enum

```java
HttpStatus.OK                    // 200
HttpStatus.CREATED               // 201
HttpStatus.NO_CONTENT            // 204
HttpStatus.BAD_REQUEST           // 400
HttpStatus.NOT_FOUND             // 404
HttpStatus.INTERNAL_SERVER_ERROR // 500
```

## Specificity Rule (EXAM)

```
Most specific handler wins:
StudentNotFoundException → @ExceptionHandler(StudentNotFoundException.class) ← WINS
                        → @ExceptionHandler(RuntimeException.class)
                        → @ExceptionHandler(Exception.class)

Declaration order inside the class does NOT matter — only type specificity matters.
```
