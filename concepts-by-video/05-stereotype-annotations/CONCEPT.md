# Stereotype Annotations: @Component, @Service, @Repository, @RestController

## What Are They?
Spring stereotype annotations mark classes as Spring-managed beans. Spring discovers them during component scanning and creates one instance (singleton) of each.

## The Four Main Stereotypes

| Annotation | Used On | Purpose |
|---|---|---|
| `@Component` | Any class | Generic bean — use when no other fits |
| `@Service` | Business logic class | Communicates intent: "this has business rules" |
| `@Repository` | Data access class/interface | Enables DB exception translation |
| `@Controller` | MVC controller | Returns view names |
| `@RestController` | REST controller | = `@Controller` + `@ResponseBody` |

## Functionally: `@Service` = `@Repository` = `@Component`
All three make a class a singleton Spring bean. The difference is **semantic** (communication to developers reading the code) and **Spring features**:
- `@Repository` adds automatic exception translation (DB exceptions → Spring exceptions)
- `@RestController` adds `@ResponseBody` (return values → JSON)
- `@Service` and `@Component` are functionally identical

## EXAM: Which annotation goes where?
- `Student.java` (entity) → no annotation (it's `@Entity`)
- `StudentRepository.java` → `@Repository`
- `StudentService.java` → `@Service`
- `StudentController.java` → `@RestController`
- `DataSeeder.java` → `@Component`
- `GlobalExceptionHandler.java` → `@RestControllerAdvice`
