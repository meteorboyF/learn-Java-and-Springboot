// ============================================================
// StudentService.java — The Service Layer (Business Logic)
// The service sits BETWEEN the controller (web layer) and the
// repository (data layer). It contains all business rules.
//
// LAYER ARCHITECTURE (always follow this order):
//   HTTP Request → Controller → Service → Repository → Database
//   HTTP Response ← Controller ← Service ← Repository ← Database
//
// Controllers handle HTTP. Repositories handle SQL. Services handle LOGIC.
// EXAM: Never call the Repository directly from the Controller.
// ============================================================
package com.example.springbootref.service;

import com.example.springbootref.exception.StudentNotFoundException;
import com.example.springbootref.model.Student;
import com.example.springbootref.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// @Service — marks this class as a Spring service bean.
// Functionally identical to @Component, but communicates INTENT:
//   "This class contains business logic" (not a web endpoint, not a DB layer).
// Spring scans for @Service and creates ONE instance (singleton) at startup.
// EXAM: @Service goes on the business logic class. @Repository on the data class.
@Service
public class StudentService {

    // private final — the two most important modifiers for a dependency field:
    //
    // private — the repository is an IMPLEMENTATION DETAIL of this service.
    //   Nobody outside should directly access the repo — only the service uses it.
    //   Enforces encapsulation.
    //
    // final — guarantees this field is set ONCE (in the constructor) and NEVER changed.
    //   Makes the class thread-safe (immutable dependency reference).
    //   Signals to readers: "this dependency is required and will not change."
    //   EXAM: Always use 'private final' for injected dependencies.
    private final StudentRepository studentRepository;

    // ============================================================
    // CONSTRUCTOR INJECTION — the preferred injection method
    // ============================================================
    // WHY CONSTRUCTOR INJECTION over @Autowired field injection?
    //
    // 1. TESTABILITY: In unit tests, you can pass a mock repository directly:
    //      StudentService service = new StudentService(mockRepo);
    //    With @Autowired field injection, you'd need reflection to inject the mock.
    //
    // 2. IMMUTABILITY: The 'final' keyword is only possible with constructor injection.
    //    @Autowired field injection cannot use 'final'.
    //
    // 3. EXPLICIT DEPENDENCIES: The constructor signature shows exactly what this
    //    class needs. You can't accidentally create a StudentService without a repo.
    //
    // 4. NO @Autowired NEEDED: When there is exactly ONE constructor in the class,
    //    Spring automatically uses it for injection. @Autowired is optional.
    //    EXAM: This is why you DON'T need @Autowired above this constructor.
    //
    // @Autowired field injection (BAD — avoid):
    //   @Autowired
    //   private StudentRepository studentRepository;  // can't be final, hard to test
    public StudentService(StudentRepository studentRepository) {
        // Assign the injected repository to the final field.
        this.studentRepository = studentRepository;
    }

    // ============================================================
    // METHOD: getAllStudents
    // ============================================================
    // Returns every student in the database.
    // repo.findAll() generates: SELECT * FROM students
    // Returns an empty List (not null) if no students exist — callers don't need null checks.
    public List<Student> getAllStudents() {
        return studentRepository.findAll();  // JpaRepository method — no code needed
    }

    // ============================================================
    // METHOD: getStudentById (find by database primary key)
    // ============================================================
    // findById() returns Optional<Student> — NEVER null.
    //   - If a student with this id exists: Optional contains the Student.
    //   - If not: Optional is empty.
    //
    // .orElseThrow(() -> new StudentNotFoundException(id))
    //   - If Optional is empty: execute the lambda, throw StudentNotFoundException.
    //   - If Optional has a value: return that Student.
    //
    // WHY NEVER USE .get() instead of .orElseThrow():
    //   Optional.get() throws NoSuchElementException if empty — a generic, unhelpful error.
    //   The GlobalExceptionHandler only catches StudentNotFoundException → returns 404.
    //   NoSuchElementException would NOT be caught → returns 500 instead of 404.
    //   EXAM: .get() on Optional is almost always wrong. Always use .orElseThrow().
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)            // returns Optional<Student>
                .orElseThrow(() -> new StudentNotFoundException(id));  // throws if empty
    }

    // ============================================================
    // METHOD: getStudentByStudentId (find by university ID number)
    // ============================================================
    // Uses the custom repository method findByStudentId().
    // Same orElseThrow pattern — if not found, throw with a descriptive message.
    // Note: 'id' (Long, database PK) ≠ 'studentId' (String, university number).
    // EXAM: These are TWO DIFFERENT fields — don't confuse them.
    public Student getStudentByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId)  // returns Optional<Student>
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student not found with studentId: " + studentId));
    }

    // ============================================================
    // METHOD: createStudent
    // ============================================================
    // repo.save() does an INSERT when the entity has no id (id == null).
    // After save, the returned entity has its id set by the database.
    // EXAM: Always return the SAVED entity (which has the generated id),
    //       not the input entity (which may still have id=null).
    public Student createStudent(Student student) {
        return studentRepository.save(student);  // INSERT — returns saved entity with id
    }

    // ============================================================
    // METHOD: updateStudent
    // ============================================================
    // Pattern: find → modify → save
    //
    // Step 1: getStudentById(id) — reuse our existing method.
    //   If the student doesn't exist, this throws StudentNotFoundException immediately.
    //   We don't need to duplicate the "not found" logic.
    //
    // Step 2: Update ONLY the fields that clients are allowed to change.
    //   We update: name, gpa, major.
    //   We do NOT update: id (always the DB key), studentId (immutable university ID).
    //   EXAM: Never trust the client's request body 'id' — always use the URL path variable.
    //
    // Step 3: repo.save(existing) → UPDATE.
    //   save() detects that 'existing' already has an id → generates UPDATE SQL.
    //   save() with no id → INSERT. save() with existing id → UPDATE.
    //   EXAM: same save() method for both INSERT and UPDATE.
    public Student updateStudent(Long id, Student updatedData) {
        Student existing = getStudentById(id);          // Step 1: find or throw
        existing.setName(updatedData.getName());        // Step 2: update allowed fields
        existing.setGpa(updatedData.getGpa());
        existing.setMajor(updatedData.getMajor());
        return studentRepository.save(existing);        // Step 3: UPDATE in DB
    }

    // ============================================================
    // METHOD: deleteStudent
    // ============================================================
    // Pattern: check existence → then delete.
    //
    // WHY check with existsById first?
    //   In Spring Boot 3.x, deleteById() silently does nothing if the id doesn't exist.
    //   Without the check, deleting a non-existent student would return 200 (success)
    //   instead of 404 (not found) — incorrect HTTP behavior.
    //   The check gives us control to throw our specific exception.
    //
    // WHY not reuse getStudentById() here?
    //   getStudentById() fetches the full Student object — wasted work if we just delete it.
    //   existsById() runs a more efficient COUNT query: SELECT COUNT(*) WHERE id=?
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {          // efficient existence check
            throw new StudentNotFoundException(id);        // throw before attempting delete
        }
        studentRepository.deleteById(id);                 // DELETE WHERE id=?
    }
}
