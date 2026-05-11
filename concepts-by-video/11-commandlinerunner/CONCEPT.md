# CommandLineRunner

## What is it?
`CommandLineRunner` is a Spring Boot interface with one method: `run(String... args)`. Spring Boot automatically calls `run()` after the entire application context is fully initialized and started.

## Why Does It Exist?
Sometimes you need to execute code after the app starts — seed the database, check external services, load configuration from a file. `CommandLineRunner.run()` is the correct hook for this.

## When to Use It
- **Database seeding** — insert initial data after JPA is ready and tables exist
- **Startup verification** — check that required data/services are available
- **Cache warmup** — pre-load frequently accessed data

## Why NOT use @PostConstruct or static initializers?
- `@PostConstruct` runs when the BEAN is initialized — before JPA is fully ready, before tables exist
- Static initializers run before Spring even starts — no beans, no JPA, nothing works
- `CommandLineRunner.run()` runs LAST — after everything is ready — perfect for DB operations

## Implementation
```java
@Component
public class DataSeeder implements CommandLineRunner {
    private final StudentRepository repo;
    
    public DataSeeder(StudentRepository repo) { this.repo = repo; }
    
    @Override
    public void run(String... args) throws Exception {
        if (repo.count() > 0) return;  // EXAM: duplicate guard!
        repo.save(new Student("Alice", "STU20240001", 3.85, "CS"));
        System.out.println("Seeded!");
    }
}
```

## EXAM: Two Critical Points
1. The duplicate guard `repo.count() > 0` prevents re-seeding on restart
2. The required `studentId` value ("STU20240001") must match exactly what the quiz specifies
