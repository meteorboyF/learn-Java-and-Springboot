# Problem 04 — Spring Data Repository with Custom Query Methods

**Source:** CSE 2218 Advanced OOP Lab — Section 2, Question 4

## The Question

Given the `Student` entity from Problem 03, create a `StudentRepository` that:

a) Extends `JpaRepository` with the correct type parameters  
b) Adds a method to find all students by `major`  
c) Adds a method to find a student by their `studentId` field  
d) Adds a method to find students whose GPA is >= a given value  
e) Adds a custom `@Query` to search by name (case-insensitive, partial match)

## What You Need to Know Before Solving

- `JpaRepository<EntityType, IdType>` — always two type parameters
- **Derived query methods**: Spring reads the method name and writes the SQL for you
- `findBy<Field>` → `WHERE field = ?`
- `findBy<Field>GreaterThanEqual` → `WHERE field >= ?`
- **Return type matters**: `Optional<T>` for 0-or-1 results, `List<T>` for 0-or-many
- `@Query` with JPQL uses class/field names (not table/column names)

## Exam Tips

- `JpaRepository<Student, Long>` → Second type must match the `@Id` field type exactly
- `findByStudentId()` returns `Optional` because studentId is UNIQUE (max 1 result)
- `findByMajor()` returns `List` because many students can have the same major
- In `@Query`: `Student` = Java class name, NOT the `students` table name
- `@Param("name")` must match the `:name` placeholder in the `@Query` string
