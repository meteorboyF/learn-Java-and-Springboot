# Common Mistakes — CommandLineRunner

## Mistake 1: Missing the duplicate guard (Bug 4)
**Wrong:** Seeder without `if (repo.count() > 0) return;`
**Problem:** Every restart inserts more data. With unique constraints, the second restart crashes.
**Fix:** Always add the guard as the FIRST line of `run()`.

## Mistake 2: Using @PostConstruct for database seeding
**Wrong:** `@PostConstruct public void seed() { repo.save(...); }`
**Problem:** @PostConstruct runs during bean initialization — before JPA/Hibernate is ready. The table might not exist yet.
**Fix:** Use `CommandLineRunner.run()` which runs AFTER JPA is fully ready and tables exist.

## Mistake 3: Forgetting to implement the interface
**Wrong:**
```java
@Component
public class DataSeeder {  // missing 'implements CommandLineRunner'
    public void run(String... args) { ... }  // never called by Spring!
}
```
**Problem:** Spring doesn't know this class is a CommandLineRunner — it never calls `run()`.
**Fix:** `public class DataSeeder implements CommandLineRunner { ... }` — the interface is required.
