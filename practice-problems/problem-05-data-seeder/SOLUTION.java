// SOLUTION — DataSeeder.java
// Location: src/main/java/com/example/app/seeder/DataSeeder.java

package com.example.app.seeder;

import com.example.app.model.Student;
import com.example.app.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// @Component — generic Spring bean. Spring creates one instance at startup.
// We use @Component (not @Service) because this is a startup utility, not business logic.
// Both would work, but @Component is semantically more correct for infrastructure classes.
@Component
// implements CommandLineRunner — this interface has one method: run(String... args).
// Spring Boot automatically calls run() after the application context is fully ready:
//   → All @Service, @Repository beans are created.
//   → The database connection is established.
//   → All @Entity tables have been created/updated by Hibernate.
//   → Perfect time to insert initial data.
public class DataSeeder implements CommandLineRunner {

    // private final — immutable, injected via constructor.
    // The StudentRepository gives us access to the students table.
    private final StudentRepository studentRepository;

    // Constructor injection — Spring automatically provides the StudentRepository bean.
    // Single constructor → @Autowired annotation is optional (not needed here).
    // EXAM: Prefer constructor injection over @Autowired field injection.
    public DataSeeder(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // run() is called by Spring Boot after full startup.
    // String... args = any command-line arguments (usually empty in quiz scenarios).
    // throws Exception = required by CommandLineRunner interface.
    @Override
    public void run(String... args) throws Exception {

        // ─── Duplicate guard (Part f) ─────────────────────────────────────
        // repo.count() → SELECT COUNT(*) FROM students
        // If count > 0, data already exists from a previous run → skip seeding.
        // WHY this matters:
        //   With H2 in-memory (ddl-auto=update): tables start empty every restart.
        //     → Guard not strictly needed, but harmless and a good habit.
        //   With H2 file-based or MySQL: data persists between restarts.
        //     → Without guard: 3 more students inserted on every restart.
        //     → After 5 restarts: 15 students instead of 3.
        // EXAM: This guard is a tested concept. Know why it's needed.
        if (studentRepository.count() > 0) {
            System.out.println("[DataSeeder] Database already has data. Skipping seed.");
            return; // exit immediately — no inserts
        }

        // ─── Seed 3 students (Parts c and d) ─────────────────────────────
        // EXAM CRITICAL: The first student MUST have studentId = "STU20240001".
        // The quiz JUnit test does:
        //   Optional<Student> s = repo.findByStudentId("STU20240001");
        //   assertThat(s).isPresent(); // FAILS if this studentId is missing
        // Always copy the required studentId EXACTLY from the quiz prompt.

        studentRepository.save(new Student(
                "Alice Johnson",    // name
                "STU20240001",      // studentId — REQUIRED VALUE for the quiz
                3.85,               // gpa
                "Computer Science"  // major
        ));

        studentRepository.save(new Student(
                "Bob Rahman",
                "STU20240002",
                3.20,
                "Mathematics"
        ));

        studentRepository.save(new Student(
                "Clara Reyes",
                "STU20240003",
                3.60,
                "Physics"
        ));

        // ─── Confirmation message (Part e) ───────────────────────────────
        // studentRepository.count() is now 3 (the three we just saved).
        // This message appears in the application startup console output.
        System.out.println("[DataSeeder] Successfully seeded "
                + studentRepository.count() + " students.");
    }
}
