# Dependency Injection (DI)

## What is it?
Dependency Injection is a design pattern where an object's dependencies (the other objects it needs) are **provided to it** rather than the object creating them itself. Spring's IoC (Inversion of Control) container manages this automatically.

## Why Does It Exist?
Without DI, classes create their own dependencies: `new StudentRepository()`. This makes classes tightly coupled — hard to test, hard to swap implementations, hard to configure. With DI, the container creates and provides dependencies — classes are loosely coupled.

## When to Use It
In Spring, DI is used everywhere. Every `@Service`, `@Controller`, and `@Component` that needs another Spring bean uses DI to get it.

## How It Works in Spring
1. Spring scans your packages at startup
2. Finds classes annotated with `@Service`, `@Repository`, `@Component`, `@RestController`
3. Creates one instance (singleton) of each
4. Looks at constructors/fields to find dependencies
5. Provides those dependencies automatically ("injects" them)

## Three Types of Injection

### 1. Constructor Injection (PREFERRED — EXAM: know why)
```java
@Service
public class StudentService {
    private final StudentRepository repo;  // final = immutable

    public StudentService(StudentRepository repo) {  // Spring calls this
        this.repo = repo;
    }
}
```

### 2. Field Injection (AVOID in production)
```java
@Service
public class StudentService {
    @Autowired
    private StudentRepository repo;  // Spring injects this directly
    // Can't be final. Hard to test. Hidden dependency.
}
```

### 3. Setter Injection (rarely used)
```java
@Service
public class StudentService {
    private StudentRepository repo;

    @Autowired
    public void setRepo(StudentRepository repo) {
        this.repo = repo;
    }
}
```

## EXAM: Why Constructor Injection is Best
- `private final` = immutable = thread-safe
- Dependencies are explicit (visible in constructor)
- Testable without Spring: `new StudentService(mockRepo)`
- No `@Autowired` needed when there's only ONE constructor
