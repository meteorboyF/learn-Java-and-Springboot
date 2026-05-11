# Problem 10 — Complete Mini-App: Course Management (Exam Simulation)

**Source:** CSE 2218 Advanced OOP Lab — Section 5, Question 10

## The Question

Build a **complete Spring Boot mini-app** for managing university courses.

**Entity: `Course`**
| Field | Type | Constraint |
|---|---|---|
| `id` | `Long` | Auto-generated primary key |
| `code` | `String` | UNIQUE + NOT NULL |
| `title` | `String` | NOT NULL |
| `credits` | `int` | Nullable (no constraint) |
| `instructor` | `String` | Nullable |

**Requirements:**
- H2 in-memory DB, `ddl-auto=update`
- Seed **3 courses** on startup — one must have `code='CSE2218'`
- Base URL: `/api/{yourStudentId}/courses`
- `GET` all courses → 200
- `GET` course by code → 200 or 404
- `POST` new course → 201 + Location header
- `DELETE` course by id → 204 or 404
- Global handler: `CourseNotFoundException` → 404
- All components use **constructor injection**

## Files to Create (7 required)

1. `application.properties`
2. `Course.java` (Entity)
3. `CourseRepository.java`
4. `CourseNotFoundException.java`
5. `CourseService.java`
6. `DataSeeder.java`
7. `CourseController.java`
8. `GlobalExceptionHandler.java`

## Exam Checklist

- [ ] `Course` entity has no-arg constructor
- [ ] Seeder has duplicate guard (`repo.count() > 0`)
- [ ] Seeder includes a course with `code='CSE2218'`
- [ ] Base URL is `/api/{studentId}/courses`
- [ ] POST returns 201 (not 200)
- [ ] DELETE returns 204 (not 200)
- [ ] `CourseNotFoundException` extends `RuntimeException`
- [ ] `@RestControllerAdvice` (not `@ControllerAdvice`)
