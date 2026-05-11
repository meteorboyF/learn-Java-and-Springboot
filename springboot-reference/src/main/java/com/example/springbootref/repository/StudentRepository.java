// ============================================================
// StudentRepository.java — The Data Access Layer (Repository)
// This interface is the ONLY thing you need to write for database access.
// Spring Data JPA automatically generates the implementation at startup.
// You write an interface — Spring writes the SQL for you.
// ============================================================
package com.example.springbootref.repository;

import com.example.springbootref.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// @Repository — marks this interface as a Spring Data repository bean.
// Spring will create a proxy class that implements this interface.
// Also enables Spring's exception translation:
//   low-level SQL exceptions → Spring's DataAccessException hierarchy.
// EXAM: @Repository is optional here (JpaRepository already gets scanned),
//       but including it is best practice and makes intent clear.
// EXAM: This is an INTERFACE, not a class. You never write an implementation.
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // JpaRepository<Student, Long> — two type parameters:
    //   Student — the entity type this repository manages
    //   Long    — the type of the primary key (@Id field in Student is Long)
    // EXAM: The second type parameter MUST match the @Id field's type exactly.
    //
    // By extending JpaRepository, you get these methods for FREE (no code needed):
    //   save(Student s)              — INSERT or UPDATE (Spring detects which based on id)
    //   findById(Long id)            — SELECT WHERE id=? → returns Optional<Student>
    //   findAll()                    — SELECT * FROM students → returns List<Student>
    //   deleteById(Long id)          — DELETE WHERE id=?
    //   existsById(Long id)          — SELECT COUNT(*) > 0 WHERE id=?
    //   count()                      — SELECT COUNT(*) FROM students
    //   And many more...
    // EXAM: You get all of these without writing a single line of SQL or implementation.

    // ============================================================
    // STYLE 1: DERIVED QUERY METHOD — method name IS the query
    // ============================================================
    // Spring reads the method name and generates SQL automatically.
    // 'findBy' + 'Major' → Spring generates: SELECT * FROM students WHERE major = ?
    // The parameter 'String major' maps to the ? placeholder.
    // Returns List because many students can share the same major.
    // EXAM: Know the naming convention: findBy<FieldName>(<FieldType> parameter)
    List<Student> findByMajor(String major);  // generates: WHERE major = ?

    // ============================================================
    // STYLE 2: DERIVED QUERY with Optional return type
    // ============================================================
    // findByStudentId → SELECT * FROM students WHERE student_id = ?
    // Returns Optional<Student> because studentId is UNIQUE:
    //   - 0 or 1 results are possible (never more than 1 due to unique constraint).
    //   - Optional<> signals to callers: "this might be empty — handle it!"
    //   - EXAM: Never return raw Student here — it would be null if not found,
    //           causing NullPointerException. Optional forces callers to check.
    Optional<Student> findByStudentId(String studentId);

    // ============================================================
    // STYLE 3: DERIVED QUERY with comparison keyword
    // ============================================================
    // 'GreaterThanEqual' is a keyword Spring understands in method names.
    // Generates: SELECT * FROM students WHERE gpa >= ?
    // Other comparison keywords: LessThan, GreaterThan, Between, IsNull, IsNotNull
    // EXAM: You can chain conditions: findByMajorAndGpaGreaterThanEqual(String, Double)
    List<Student> findByGpaGreaterThanEqual(Double minGpa);

    // ============================================================
    // STYLE 4: @Query with JPQL (Java Persistence Query Language)
    // ============================================================
    // When the method name gets too complex, use @Query with JPQL.
    // JPQL uses CLASS NAMES AND FIELD NAMES, not table/column names.
    // 'Student s' refers to the Java class, not the 'students' table.
    // 's.name' refers to the 'name' field in the Student class, not the DB column.
    //
    // LOWER() on both sides = case-insensitive search.
    // CONCAT('%', :name, '%') = builds a LIKE pattern: %searchTerm%
    // :name = named parameter — linked to @Param("name") below.
    //
    // EXAM: JPQL uses class/field names. Native SQL uses table/column names.
    @Query("SELECT s FROM Student s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Student> searchByName(@Param("name") String name);
    // @Param("name") — binds the method parameter to the :name placeholder in the query.
    // The string in @Param must match the :placeholder name in the @Query.

    // ============================================================
    // STYLE 5: @Query with Native SQL
    // ============================================================
    // nativeQuery = true → write plain SQL instead of JPQL.
    // Use table name ('students') and column name ('student_id'), NOT the Java names.
    // When to use native SQL:
    //   - Database-specific functions (MySQL's RAND(), PostgreSQL's ILIKE, etc.)
    //   - Complex JOINs that are awkward in JPQL
    //   - Performance-critical queries where you need exact control
    // EXAM: For the quiz, JPQL is usually preferred. Use native only when asked.
    @Query(value = "SELECT * FROM students WHERE gpa IS NOT NULL ORDER BY gpa DESC",
           nativeQuery = true)
    List<Student> findAllWithGpaOrderedDesc();

    // ============================================================
    // BONUS: COMBINED CONDITION (AND)
    // ============================================================
    // Spring generates: SELECT * FROM students WHERE major = ? AND gpa >= ?
    // The two parameters must be in the SAME ORDER as they appear in the method name.
    List<Student> findByMajorAndGpaGreaterThanEqual(String major, Double minGpa);
}
