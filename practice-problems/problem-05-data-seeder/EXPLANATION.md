# Explanation — Problem 05: Data Seeder with CommandLineRunner

## Why CommandLineRunner?

Spring Boot calls `CommandLineRunner.run()` at a very specific moment: **after** the entire application context is fully initialized. This means:

1. All Spring beans are created (`@Service`, `@Repository`, etc.)
2. The database connection pool is established
3. Hibernate has already run `ddl-auto=update` — the `students` table exists
4. JPA transactions are ready

If you tried to insert data in a `static` block or a constructor, you'd get errors because JPA isn't ready yet. `CommandLineRunner` is the correct "startup hook."

## The Duplicate Guard — `repo.count() > 0`

```java
if (studentRepository.count() > 0) {
    return;
}
```

### Why this line is critical:

| Storage type | Without guard | With guard |
|---|---|---|
| H2 in-memory | Fine (table always empty on start) | Fine (skips, but no harm) |
| H2 file-based | ❌ Adds 3 students per restart | ✅ Inserts only once |
| MySQL | ❌ Adds 3 students per restart | ✅ Inserts only once |

**This is Bug 4 in Problem 12.** Forgetting the guard is a common and explicitly tested mistake.

### A more robust guard (alternative):
Instead of checking count > 0, check for the specific required record:
```java
if (studentRepository.findByStudentId("STU20240001").isPresent()) {
    return; // already seeded
}
```
This is safer because count > 0 could be triggered by other data, but finding the specific required student is a precise check.

## Why the Specific studentId Value Matters

The exam JUnit test typically looks like this:
```java
@Test
void testSeededStudentExists() {
    Optional<Student> student = studentRepository.findByStudentId("STU20240001");
    assertThat(student).isPresent();
    assertThat(student.get().getName()).isEqualTo("Alice Johnson");
}
```

If your seeder uses `"STU001"` instead of `"STU20240001"`, **the test fails** even though everything else is perfectly correct. Always read the exam prompt for the exact required values.

## Constructor Injection vs. `@Autowired`

```java
// ✅ CORRECT — constructor injection
public DataSeeder(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
}

// ❌ AVOID — field injection
@Autowired
private StudentRepository studentRepository;
```

**Why constructor injection is better:**
1. `private final` is possible → field is immutable after construction
2. The class is testable without Spring: `new DataSeeder(mockRepo)` works
3. Dependencies are explicit — you can't accidentally create a `DataSeeder` without a repo

## What `repo.save(new Student(...))` Does

1. Creates a `Student` object with `id = null`
2. Calls `repo.save(student)` → Spring Data JPA sees `id == null` → generates `INSERT`
3. The database assigns the auto-incremented id
4. The returned `Student` object now has its `id` set
5. The row is committed to the database

## Execution Order in Spring Boot Startup

```
1. Spring starts
2. Reads application.properties
3. Creates beans (@Service, @Repository, @Component...)
4. Hibernate creates/updates tables (ddl-auto=update)
5. ← DataSeeder.run() is called HERE
6. Embedded Tomcat starts
7. App is ready to receive HTTP requests
```
