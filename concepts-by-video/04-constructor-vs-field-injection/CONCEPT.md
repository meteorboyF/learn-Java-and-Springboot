# Constructor vs Field Injection

## Constructor Injection (PREFERRED)
```java
@Service
public class StudentService {
    private final StudentRepository repo;  // final = immutable
    
    public StudentService(StudentRepository repo) {
        this.repo = repo;  // injected at construction time
    }
}
```
**Pros:** `private final` possible, testable without Spring, explicit dependencies, no @Autowired needed for single constructor.

## Field Injection (AVOID)
```java
@Service
public class StudentService {
    @Autowired
    private StudentRepository repo;  // injected via reflection after construction
}
```
**Cons:** Cannot be `final`, harder to test, hidden dependency, @Autowired required.

## EXAM: Why Constructor Injection is ALWAYS preferred
1. `private final` → thread-safe, guaranteed non-null after construction
2. Unit testing: `new StudentService(mockRepo)` — no Spring context needed
3. Dependencies are visible in the constructor signature
4. Single constructor → no `@Autowired` annotation needed
5. Spring itself recommends constructor injection in official documentation

## When is @Autowired Required?
- Field injection: always required
- Setter injection: always required  
- Constructor injection: only when multiple constructors exist
