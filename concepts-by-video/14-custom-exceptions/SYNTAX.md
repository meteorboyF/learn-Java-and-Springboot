# Syntax — Custom Exceptions

## Minimal Custom Exception
```java
public class MyNotFoundException extends RuntimeException {
    public MyNotFoundException(String message) {
        super(message);
    }
}
```

## With Multiple Constructors
```java
public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String message) { super(message); }
    public StudentNotFoundException(Long id) { super("Student not found: " + id); }
}
```

## Checked vs Unchecked
```java
// UNCHECKED (preferred for domain exceptions):
public class MyException extends RuntimeException { ... }
// No try-catch required. Bubbles up to GlobalExceptionHandler automatically.

// CHECKED (avoid for domain exceptions):
public class MyException extends Exception { ... }
// Every caller must: throws MyException OR try-catch.
// Clutters service/controller signatures.
```

## Throwing Custom Exceptions
```java
// In service:
throw new StudentNotFoundException(id);                   // Long id
throw new StudentNotFoundException("Not found: " + s);   // String message
throw new IllegalArgumentException("GPA must be 0-4.0"); // built-in

// In repository (rarely):
// Use orElseThrow in service instead:
repo.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
```

## Exception Hierarchy (EXAM)
```
Throwable
└── Exception                          (checked)
    ├── IOException
    └── RuntimeException               (unchecked) ← extend this
        ├── IllegalArgumentException   (built-in → 400)
        ├── NullPointerException       (built-in → don't catch in handler)
        └── StudentNotFoundException   (custom → 404)
```
