// Example: JpaRepository with all query styles.
package com.example.app.repository;

import com.example.app.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // JpaRepository<Student, Long>:
    //   Student = entity type
    //   Long    = @Id type (must match private Long id in Student)

    // ─── FREE methods (no code needed) ───────────────────────────────────────
    // save(), findById(), findAll(), deleteById(), existsById(), count() ...

    // ─── Derived query methods (method name = the query) ─────────────────────
    List<Student> findByMajor(String major);                          // WHERE major = ?
    Optional<Student> findByStudentId(String studentId);              // WHERE student_id = ?
    List<Student> findByGpaGreaterThanEqual(Double min);              // WHERE gpa >= ?
    List<Student> findByNameContainingIgnoreCase(String name);        // WHERE LOWER(name) LIKE %?%
    List<Student> findByMajorOrderByGpaDesc(String major);            // WHERE major = ? ORDER BY gpa DESC
    List<Student> findByMajorAndGpaGreaterThanEqual(String m, Double g); // WHERE major = ? AND gpa >= ?

    // ─── @Query with JPQL (class/field names) ────────────────────────────────
    @Query("SELECT s FROM Student s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Student> searchByName(@Param("name") String name);

    // ─── @Query with native SQL (table/column names) ─────────────────────────
    @Query(value = "SELECT * FROM students WHERE gpa IS NOT NULL ORDER BY gpa DESC",
           nativeQuery = true)
    List<Student> findTopStudents();
}
