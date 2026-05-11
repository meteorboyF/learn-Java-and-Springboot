# Explanation — Problem 04: Repository Query Methods

## The Core Concept: Derived Query Methods

Spring Data JPA reads your method name and **generates the SQL automatically**. You write a method signature — Spring writes the database query. This is called "query derivation."

### The Formula
```
findBy<FieldName><OptionalKeyword>(<Parameters>)
```

### Keyword Reference Table (EXAM: memorise these)

| Method name part | SQL equivalent |
|---|---|
| `findByName` | `WHERE name = ?` |
| `findByNameContaining` | `WHERE name LIKE '%?%'` |
| `findByGpaGreaterThan` | `WHERE gpa > ?` |
| `findByGpaGreaterThanEqual` | `WHERE gpa >= ?` |
| `findByGpaLessThan` | `WHERE gpa < ?` |
| `findByGpaLessThanEqual` | `WHERE gpa <= ?` |
| `findByGpaBetween` | `WHERE gpa BETWEEN ? AND ?` |
| `findByMajorIsNull` | `WHERE major IS NULL` |
| `findByMajorIsNotNull` | `WHERE major IS NOT NULL` |
| `findByMajorAndGpa` | `WHERE major = ? AND gpa = ?` |
| `findByMajorOrGpa` | `WHERE major = ? OR gpa = ?` |
| `findByNameOrderByGpaDesc` | `WHERE name = ? ORDER BY gpa DESC` |

## Why `Optional<Student>` for `findByStudentId()`?

`studentId` has `unique = true` on the `@Column`. This means the database can return at most **one** result. `Optional<T>` perfectly models "zero or one":

- `Optional.isPresent()` = found
- `Optional.isEmpty()` = not found
- `Optional.orElseThrow()` = get the value or throw an exception

**Never return `null` from a repository method.** Callers would need `if (result != null)` everywhere, and forgetting that check causes `NullPointerException`. `Optional` forces explicit handling.

## Why `List<Student>` for `findByMajor()`?

`major` is not unique — many students can share the same major. The result can be 0, 1, or many students. `List<T>` models this correctly. It returns an **empty list** (not null) when no students are found — callers don't need null checks.

## `@Query` — When to Use It

Use `@Query` when the method name approach would be too long or impossible:

| Scenario | Use derived method? | Use `@Query`? |
|---|---|---|
| Simple field match: `WHERE major = ?` | ✅ Yes | Works either way |
| Case-insensitive search with LIKE | ❌ Awkward | ✅ Use `@Query` |
| Multi-table JOIN | ❌ Not possible | ✅ Use `@Query` |
| DB-specific function (MySQL's `RAND()`) | ❌ Not possible | ✅ Use native SQL |

## JPQL vs Native SQL

| | JPQL | Native SQL |
|---|---|---|
| Uses | Java class/field names | DB table/column names |
| Example entity reference | `Student s` | `students s` |
| Example field reference | `s.studentId` | `s.student_id` |
| DB portable? | ✅ Yes (Hibernate translates) | ❌ No (DB-specific) |
| `nativeQuery` attribute | `false` (default) | `true` |

## `@Param` Annotation

Links a method parameter to a `:placeholder` in the `@Query` string:
```java
@Query("SELECT s FROM Student s WHERE s.major = :major AND s.gpa >= :minGpa")
List<Student> findByMajorAndMinGpa(@Param("major") String major, @Param("minGpa") Double minGpa);
```
The string in `@Param("...")` must **exactly** match the `:placeholder` in the query string.
