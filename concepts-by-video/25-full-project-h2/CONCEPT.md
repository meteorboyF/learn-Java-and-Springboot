# Full Project with H2 Database

## What This Covers
A complete, runnable Spring Boot project using H2 in-memory database. This is the capstone concept — everything from previous videos combined into one working application.

## What H2 In-Memory Gives You
- **No installation required** — H2 is a Java database bundled as a JAR
- **Starts fresh on every run** — data is lost when the app stops (good for dev/testing)
- **H2 Console** — a browser-based SQL query tool at `http://localhost:8080/h2-console`
- **ddl-auto=update** — Hibernate auto-creates/updates database tables from your `@Entity` classes

## The Complete File Checklist

```
src/main/java/com/example/app/
├── AppApplication.java              ← main entry point
├── model/Student.java               ← @Entity
├── repository/StudentRepository.java ← JpaRepository
├── exception/StudentNotFoundException.java
├── service/StudentService.java      ← @Service
├── controller/StudentController.java ← @RestController
├── handler/GlobalExceptionHandler.java ← @RestControllerAdvice
└── seeder/DataSeeder.java           ← CommandLineRunner

src/main/resources/
└── application.properties           ← H2 config

pom.xml                              ← Maven dependencies
```

## Startup Sequence
When you run `mvn spring-boot:run` or press Run in your IDE, Spring Boot:

1. Scans for `@Component`, `@Service`, `@Repository`, `@RestController` → creates beans
2. Connects to H2 in-memory database (creates the database in RAM)
3. Hibernate reads `@Entity` classes → generates `CREATE TABLE` SQL → runs it
4. `DataSeeder.run()` executes → inserts initial rows (if `count() == 0`)
5. HTTP server starts on port 8080 → your endpoints are live

## Testing with H2 Console
1. Start the app
2. Open `http://localhost:8080/h2-console` in a browser
3. Set JDBC URL to `jdbc:h2:mem:testdb`, leave username/password as `sa`/`(empty)`
4. Click Connect → run SQL directly against your tables

## EXAM: The 5 Bugs to Know for Problem 12
| Bug | Wrong | Right |
|---|---|---|
| Bug 1 | `ResponseEntity.ok(saved)` in POST | `ResponseEntity.created(uri).body(saved)` |
| Bug 2 | `repo.findById(id).get()` | `repo.findById(id).orElseThrow(...)` |
| Bug 3 | No `public Student() {}` in entity | Add the no-arg constructor |
| Bug 4 | No guard in seeder | `if (repo.count() > 0) return;` |
| Bug 5 | `@ControllerAdvice` | `@RestControllerAdvice` |
