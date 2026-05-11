# Common Mistakes — Stereotype Annotations

## Mistake 1: Using @Service on a @RestController or vice versa
**Wrong:** `@Service public class StudentController { @GetMapping... }`
**Problem:** The HTTP mapping annotations won't be registered for URL routing — the class will never receive HTTP requests.
**Fix:** Use the correct stereotype for each layer: controllers get `@RestController`, services get `@Service`.

## Mistake 2: Not annotating the class at all
**Wrong:** `public class StudentService { ... }` (no annotation)
**Problem:** Spring doesn't know about this class — it's not in the container, can't be injected anywhere.
**Fix:** Add `@Service`. Without a stereotype annotation, Spring ignores the class entirely.

## Mistake 3: Putting @Entity annotation on a service or controller
**Wrong:** `@Entity @Service public class StudentService { ... }`
**Problem:** JPA tries to create a database table for `StudentService` — crashes at startup.
**Fix:** `@Entity` only goes on domain model classes (data containers). `@Service` goes on classes with business logic.
