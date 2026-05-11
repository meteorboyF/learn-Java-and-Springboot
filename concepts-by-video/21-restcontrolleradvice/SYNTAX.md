# Syntax Reference — @RestControllerAdvice

## Class Annotation
```java
@RestControllerAdvice                  // applies to ALL controllers globally (preferred)
@RestControllerAdvice(basePackages = "com.example.app")  // scope to specific package
@RestControllerAdvice(assignableTypes = {StudentController.class})  // scope to specific class
```

## @ExceptionHandler — All Forms
```java
// Handle one exception type:
@ExceptionHandler(StudentNotFoundException.class)
public ResponseEntity<ErrorResponse> handle(StudentNotFoundException ex) { ... }

// Handle multiple exception types in one method:
@ExceptionHandler({StudentNotFoundException.class, CourseNotFoundException.class})
public ResponseEntity<ErrorResponse> handleNotFound(Exception ex) { ... }

// Catch-all:
@ExceptionHandler(Exception.class)
public ResponseEntity<ErrorResponse> handleAll(Exception ex) { ... }
```

## Method Signature Options
```java
// Receives the exception:
public ResponseEntity<T> handle(MyException ex) { ... }

// Also receives request info:
public ResponseEntity<T> handle(MyException ex, HttpServletRequest request) { ... }
public ResponseEntity<T> handle(MyException ex, WebRequest request) { ... }

// Return types (all valid):
ResponseEntity<ErrorResponse>   // structured JSON body
ResponseEntity<String>          // plain string body
ResponseEntity<Map<String, Object>>  // map as JSON
String                          // just a string (less common)
void                            // no body
```

## Complete Minimal Template
```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(YourNotFoundException.class)
    public ResponseEntity<String> handleNotFound(YourNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception ex) {
        return ResponseEntity.internalServerError().body("Unexpected error occurred.");
    }
}
```
