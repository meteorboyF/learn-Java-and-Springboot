# Common Mistakes — Full Project with H2

## Mistake 1: Wrong ddl-auto Value Destroys Data
**Wrong in development:**
```properties
spring.jpa.hibernate.ddl-auto=create
```
**Problem:** `create` drops ALL tables and recreates them every time the application starts. Any data you inserted manually or through the API is gone. If you forgot the seeder guard (`if (count() > 0) return`), you'll also get duplicate seed data on every restart.
**Fix:** Use `update` for development:
```properties
spring.jpa.hibernate.ddl-auto=update
```
`update` only creates missing tables or adds missing columns. It never drops existing data.

---

## Mistake 2: H2 Console Not Working (Can't Connect)
**Wrong configuration:**
```properties
# Missing these lines:
spring.h2.console.enabled=true
spring.datasource.url=jdbc:h2:mem:testdb
```
**Problem:** Without `h2.console.enabled=true`, Spring Security (if present) or Spring Boot blocks the `/h2-console` path. Without the `datasource.url`, Spring may auto-configure a different database name, and connecting with `testdb` fails.
**Fix:** Ensure both lines are in `application.properties`, and use the exact same URL string in the H2 Console login form that you have in your properties file.

---

## Mistake 3: All 5 Exam Bugs in One Summary
These are the bugs from Problem 12 — the most common mistakes in a full project:

```
Bug 1 — Controller:  ResponseEntity.ok(saved) in @PostMapping   → fix: ResponseEntity.created(uri).body(saved)
Bug 2 — Service:     repo.findById(id).get()                    → fix: .orElseThrow(() -> new NotFoundException(id))
Bug 3 — Entity:      missing public Student() {}                 → fix: add the no-arg constructor
Bug 4 — Seeder:      no guard before inserting seed data         → fix: if (repo.count() > 0) return;
Bug 5 — Handler:     @ControllerAdvice on GlobalExceptionHandler → fix: @RestControllerAdvice
```

Any one of these bugs will cause tests to fail even though the rest of the code is correct — always double-check all five before submitting.
