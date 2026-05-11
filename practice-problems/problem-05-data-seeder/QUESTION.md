# Problem 05 — Data Seeding with CommandLineRunner

**Source:** CSE 2218 Advanced OOP Lab — Section 2, Question 5

## The Question

Write a `DataSeeder` `@Component` that:

a) Implements `CommandLineRunner`  
b) Uses **constructor injection** to get the `StudentRepository`  
c) Seeds exactly **3 students** on app startup  
d) One student MUST have `studentId = 'STU20240001'` (the quiz anti-cheat requirement)  
e) Prints a **confirmation message** after seeding  
f) Does **NOT seed duplicates** if the app restarts (check before inserting)

## What You Need to Know Before Solving

- `CommandLineRunner` has one method: `run(String... args)`
- Spring calls `run()` AFTER the full application context is ready
- `repo.count()` returns the number of rows in the table — use it for the duplicate guard
- `@Component` (not `@Service`) is the correct annotation for utility classes like seeders
- Constructor injection: single constructor → no `@Autowired` needed

## Exam Tips

- **CRITICAL:** The quiz JUnit test checks for the specific studentId value. Wrong value = test fails.
- The duplicate guard `if (repo.count() > 0) return;` is essential for file-based and MySQL storage
- `repo.save(new Student(...))` = one INSERT per call
- `System.out.println(...)` for the confirmation message is fine — no logging framework needed
- **Why CommandLineRunner?** Because it runs AFTER JPA is ready and tables exist (unlike static initializers)
