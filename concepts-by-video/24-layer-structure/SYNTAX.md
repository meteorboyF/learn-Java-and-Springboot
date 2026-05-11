# SYNTAX — Layer Structure

## Annotation Per Layer

```java
// Entity (model layer) — maps to database table
@Entity
@Table(name = "students")
public class Student { ... }

// Repository layer — database access
public interface StudentRepository extends JpaRepository<Student, Long> { }

// Service layer — business logic
@Service
public class StudentService {
    private final StudentRepository repo;
    public StudentService(StudentRepository repo) { this.repo = repo; }
}

// Controller layer — HTTP endpoints
@RestController
@RequestMapping("/api/{studentId}/students")
public class StudentController {
    private final StudentService service;
    public StudentController(StudentService service) { this.service = service; }
}

// Exception handler — global error responses
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(StudentNotFoundException ex) { ... }
}
```

## Package Structure Convention

```
src/main/java/com/example/app/
├── AppApplication.java            ← @SpringBootApplication, main()
├── controller/
│   └── StudentController.java     ← @RestController
├── service/
│   └── StudentService.java        ← @Service
├── repository/
│   └── StudentRepository.java     ← extends JpaRepository
├── model/
│   └── Student.java               ← @Entity
├── exception/
│   └── StudentNotFoundException.java
├── handler/
│   └── GlobalExceptionHandler.java ← @RestControllerAdvice
├── dto/
│   ├── StudentRequest.java
│   └── StudentResponse.java
└── seeder/
    └── DataSeeder.java            ← @Component implements CommandLineRunner
```

## Dependency Injection (Constructor Pattern)

```java
// Service depends on Repository
@Service
public class StudentService {
    private final StudentRepository repo;
    public StudentService(StudentRepository repo) { this.repo = repo; }
}

// Controller depends on Service (never Repository)
@RestController
public class StudentController {
    private final StudentService service;
    public StudentController(StudentService service) { this.service = service; }
}
```

## What Each Layer Knows

```
Controller    HTTP ✅   Business Logic ❌   SQL ❌
Service       HTTP ❌   Business Logic ✅   SQL ❌
Repository    HTTP ❌   Business Logic ❌   SQL ✅ (via JPA)
```
