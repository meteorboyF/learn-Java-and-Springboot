// SOLUTION — StudentRepository.java
// Location: src/main/java/com/example/app/repository/StudentRepository.java

package com.example.app.repository;

import com.example.app.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

// @Repository — marks as a Spring Data bean.
// Spring auto-generates an implementation of this interface at startup.
// You write the interface with method signatures — Spring writes the SQL.
// EXAM: This is an INTERFACE, not a class. No @Override, no implementation body.
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // JpaRepository<Student, Long>:
    //   Student = the entity this repo manages (the @Entity class)
    //   Long    = the type of the @Id field in Student
    // EXAM: These two type parameters are always required and always tested.
    //
    // FREE METHODS from JpaRepository (zero code needed):
    //   save(Student s)        → INSERT or UPDATE
    //   findById(Long id)      → Optional<Student>
    //   findAll()              → List<Student>
    //   deleteById(Long id)    → DELETE
    //   existsById(Long id)    → boolean
    //   count()                → long (row count)

    // ─── Part (b): Find by major ────────────────────────────────────────────
    // Method name formula: findBy + FieldName(exact match)
    // Spring generates: SELECT * FROM students WHERE major = ?
    // Returns List because many students can share the same major.
    List<Student> findByMajor(String major);

    // ─── Part (c): Find by studentId (Optional return) ─────────────────────
    // Returns Optional<Student> because studentId is UNIQUE:
    //   - 0 or 1 results possible (never more due to unique constraint).
    //   - Optional forces the caller to handle the "not found" case explicitly.
    //   - Never return null — callers would need null checks everywhere.
    // EXAM: unique field → Optional. Non-unique field → List.
    Optional<Student> findByStudentId(String studentId);

    // ─── Part (d): Find by GPA >= threshold ────────────────────────────────
    // 'GreaterThanEqual' is a Spring Data keyword that maps to: WHERE gpa >= ?
    // Other keywords: LessThan, GreaterThan, Between, IsNull, IsNotNull, Like, Containing
    // EXAM: Keyword list is tested — know at least: LessThan, GreaterThan, GreaterThanEqual,
    //       LessThanEqual, Between, Containing, In, IsNull, IsNotNull
    List<Student> findByGpaGreaterThanEqual(Double minGpa);

    // ─── Part (e): @Query with JPQL (case-insensitive, partial match) ───────
    // When method names get too complex, use @Query to write JPQL directly.
    //
    // JPQL (Java Persistence Query Language) uses:
    //   CLASS names → "Student" (not "students" table)
    //   FIELD names → "s.name" (not "name" column)
    //   (Hibernate translates JPQL to SQL for you)
    //
    // LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))
    //   LOWER(...) on both sides = case-insensitive comparison
    //   CONCAT('%', :name, '%') = builds the LIKE pattern: %searchTerm%
    //   :name = named parameter placeholder
    //
    // @Param("name") String name → binds the method parameter to :name in the query.
    //   The string in @Param must EXACTLY match the :placeholder name.
    // EXAM: JPQL vs SQL — which uses class names vs table names?
    @Query("SELECT s FROM Student s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Student> searchByName(@Param("name") String name);

    // ─── BONUS: Native SQL example ──────────────────────────────────────────
    // nativeQuery = true → write raw SQL (uses table/column names, not Java names).
    // Use when: you need DB-specific functions or complex SQL not expressible in JPQL.
    @Query(value = "SELECT * FROM students WHERE gpa IS NOT NULL ORDER BY gpa DESC",
           nativeQuery = true)
    List<Student> findAllWithGpaOrderedDesc();

    // ─── BONUS: Combined condition (AND) ────────────────────────────────────
    // Spring reads: findBy + Major + And + Gpa + GreaterThanEqual
    // Generates: SELECT * FROM students WHERE major = ? AND gpa >= ?
    // Parameter order must match the method name order.
    List<Student> findByMajorAndGpaGreaterThanEqual(String major, Double minGpa);
}
