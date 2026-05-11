# Syntax Reference — Optional and orElseThrow

## Creating Optional
```java
Optional<Student> full  = Optional.of(student);       // non-null value (throws if null!)
Optional<Student> safe  = Optional.ofNullable(value); // null → empty Optional
Optional<Student> empty = Optional.empty();            // always empty
```

## Consuming Optional — All Methods
```java
Optional<Student> opt = repo.findById(id);

// EXAM: Use these:
opt.orElseThrow(() -> new MyException("msg"))  // throw custom exception if empty
opt.orElseThrow(MyException::new)              // shorter if constructor takes no args
opt.orElse(defaultValue)                       // return default if empty
opt.orElseGet(() -> computeDefault())          // compute default lazily if empty

// Check presence:
opt.isPresent()   // true if has value
opt.isEmpty()     // true if empty (Java 11+)

// Get value (USE WITH EXTREME CARE):
opt.get()  // ❌ throws NoSuchElementException if empty — almost never correct

// Act on value:
opt.ifPresent(v -> doSomething(v))   // run lambda only if present
opt.ifPresentOrElse(
    v -> doIfPresent(v),
    () -> doIfEmpty()
)

// Transform:
opt.map(Student::getName)            // Optional<String> with the name, or empty
opt.flatMap(s -> repo.findByName(s.getName()))  // for methods that return Optional
opt.filter(s -> s.getGpa() >= 3.5)  // empty if predicate fails
```

## The orElseThrow Lambda — Detailed
```java
// Long form (readable):
.orElseThrow(() -> new StudentNotFoundException("Not found: " + id))

// Method reference (only when exception constructor takes the right args):
.orElseThrow(StudentNotFoundException::new)  // calls new StudentNotFoundException()

// The lambda is LAZY — only executed if Optional is empty:
.orElseThrow(() -> {
    System.out.println("Student not found!");  // only runs if empty
    return new StudentNotFoundException(id);
})
```
