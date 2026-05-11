# SYNTAX — Full Project with H2

## application.properties — H2 In-Memory Config

```properties
# H2 in-memory database (data lost on restart)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.h2.console.enabled=true
```

## application.properties — H2 File-Based Config

```properties
# H2 file-based (data persists on restart, stored in ./data/db.mv.db)
spring.datasource.url=jdbc:h2:file:./data/db
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.h2.console.enabled=true
```

## ddl-auto Values

```
update       → add missing tables/columns. Safe for dev. Never removes data.
create       → drops all tables on startup, recreates. Destroys existing data!
create-drop  → like create, also drops on shutdown. Good for tests only.
validate     → checks schema matches entities. No changes. Crashes if mismatch.
none         → does nothing. You manage the schema manually.
```

## Main Class

```java
@SpringBootApplication
public class AppApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }
}
```

## Full Layer Quick-Reference

```java
// Entity
@Entity @Table(name="students") class Student {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    public Student() {} // required!
}

// Repository
interface StudentRepository extends JpaRepository<Student, Long> {}

// Service
@Service class StudentService {
    private final StudentRepository repo;
    StudentService(StudentRepository repo) { this.repo = repo; }
    Student findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }
}

// Controller
@RestController @RequestMapping("/api/{studentId}/students")
class StudentController {
    private final StudentService service;
    StudentController(StudentService service) { this.service = service; }
    @GetMapping("/{id}") Student get(@PathVariable Long id) { return service.findById(id); }
    @PostMapping ResponseEntity<Student> create(@PathVariable String studentId, @RequestBody Student s) {
        Student saved = service.create(s);
        URI loc = URI.create("/api/" + studentId + "/students/" + saved.getId());
        return ResponseEntity.created(loc).body(saved);
    }
    @DeleteMapping("/{id}") ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id); return ResponseEntity.noContent().build();
    }
}

// Exception Handler
@RestControllerAdvice class GlobalExceptionHandler {
    @ExceptionHandler(StudentNotFoundException.class)
    ResponseEntity<ErrorResponse> handle(StudentNotFoundException ex) {
        return ResponseEntity.status(404).body(new ErrorResponse(404, ex.getMessage()));
    }
}

// Seeder
@Component class DataSeeder implements CommandLineRunner {
    private final StudentRepository repo;
    DataSeeder(StudentRepository repo) { this.repo = repo; }
    public void run(String... args) {
        if (repo.count() > 0) return;  // guard — don't insert twice
        repo.save(new Student(...));
    }
}
```

## H2 Console Access

```
URL:      http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (leave empty)
```
