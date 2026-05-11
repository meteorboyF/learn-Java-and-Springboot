# Optional and orElseThrow

## What is it?
`Optional<T>` is a Java container class that either holds a value (`Optional.of(value)`) or is empty (`Optional.empty()`). It's used as a return type instead of returning `null`, forcing callers to handle the "not found" case explicitly.

## Why Does It Exist?
Before `Optional`, methods returned `null` when a value wasn't found. Callers often forgot to check for null, causing `NullPointerException` — the most common Java runtime error. `Optional` makes the absence of a value EXPLICIT in the type system.

## When to Use It (in Spring)
Repository methods that return 0 or 1 results use `Optional`:
- `findById(Long id)` → `Optional<Student>`
- `findByStudentId(String id)` → `Optional<Student>` (unique field)
- Any custom query that might return no results but at most one

## The Four Key Methods

### `.orElseThrow()` — EXAM: most important
```java
Student s = repo.findById(id)
    .orElseThrow(() -> new StudentNotFoundException(id));
// If Optional is empty: throws StudentNotFoundException (caught by GlobalExceptionHandler → 404)
// If Optional has value: returns the Student
```

### `.orElse()` — return a default value
```java
Student s = repo.findById(id).orElse(new Student("Default", "N/A", null, null));
// If empty: returns the default Student
// If has value: returns the found Student
```

### `.isPresent()` / `.isEmpty()`
```java
Optional<Student> opt = repo.findByStudentId("STU001");
if (opt.isPresent()) { ... }   // true if value exists
if (opt.isEmpty())  { ... }   // true if empty (Java 11+)
```

### `.get()` — NEVER USE IN PRODUCTION
```java
Student s = repo.findById(id).get();  // ❌ WRONG
// If empty: throws NoSuchElementException — NOT your custom exception!
// Your GlobalExceptionHandler catches StudentNotFoundException, not NoSuchElementException.
// Result: 500 Internal Server Error instead of 404. Bug 2 in Problem 12.
```

## EXAM: The Critical Rule
**NEVER call `.get()` on an Optional.** ALWAYS use `.orElseThrow()`.
