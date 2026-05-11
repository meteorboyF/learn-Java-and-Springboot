# Common Mistakes — JPA Repository

## Mistake 1: Wrong type parameters in JpaRepository
**Wrong:** `extends JpaRepository<Student, Integer>` when Student has `private Long id`
**Problem:** Spring Data can't match the ID type — save/findById/deleteById won't work correctly.
**Fix:** The second type parameter MUST match the @Id field type: `private Long id` → `JpaRepository<Student, Long>`

## Mistake 2: Using a class instead of an interface
**Wrong:** `public class StudentRepository extends JpaRepository<Student, Long>`
**Problem:** JpaRepository is an interface — you can't extend an interface with a class. Compilation error.
**Fix:** `public interface StudentRepository extends JpaRepository<Student, Long>`

## Mistake 3: Naming derived query methods incorrectly
**Wrong:** `findByStudentId(Long id)` when the field is `String studentId`
**Problem:** Spring tries to use `Long` for a `String` field — type mismatch, query fails.
**Fix:** Match the parameter type to the field type. `String studentId` → `findByStudentId(String studentId)`. The method name must use the exact FIELD name (camelCase), not the DB column name.
