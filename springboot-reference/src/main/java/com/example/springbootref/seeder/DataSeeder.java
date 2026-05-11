// ============================================================
// DataSeeder.java — Database Initialization on Startup
// Seeds the database with initial data when the application starts.
// Uses Spring's CommandLineRunner interface to run code after startup.
// ============================================================
package com.example.springbootref.seeder;

import com.example.springbootref.model.Student;
import com.example.springbootref.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// @Component — generic Spring bean annotation.
// Spring will discover this class during component scanning and create a bean.
// We use @Component (not @Service) because this isn't a "service" in the
// business-logic sense — it's a startup utility. Both work, but @Component
// is more semantically correct here.
// EXAM: CommandLineRunner beans are executed after the Spring context is fully loaded.
@Component
public class DataSeeder implements CommandLineRunner {
    // implements CommandLineRunner — this interface has ONE method: run(String... args)
    // Spring Boot automatically calls run() after the application context is ready.
    // This means: ALL beans created, ALL repositories connected to DB, ALL tables exist.
    // Perfect timing for inserting initial data.
    // EXAM: CommandLineRunner.run() is called AFTER startup is complete.

    // private final — immutable, injected via constructor.
    private final StudentRepository studentRepository;

    // Constructor injection — Spring provides the StudentRepository bean.
    // No @Autowired needed (single constructor rule applies here too).
    public DataSeeder(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        // At construction time, the repo is ready but run() hasn't been called yet.
    }

    // run() is called automatically by Spring Boot after full startup.
    // String... args = command-line arguments passed to the app (usually empty).
    // throws Exception — required by the CommandLineRunner interface signature.
    @Override
    public void run(String... args) throws Exception {

        // ============================================================
        // DUPLICATE GUARD — prevent re-seeding on every restart
        // ============================================================
        // WHY this guard is critical:
        //   With ddl-auto=update (or any non-create strategy), the database
        //   is NOT wiped on restart. It KEEPS existing data.
        //   Without this guard: every app restart inserts 3 MORE students.
        //   After 5 restarts: 15 students in the DB instead of 3.
        //   With H2 in-memory (ddl-auto=update): data IS wiped (table dropped on stop),
        //   so the guard is less critical there — but it's ALWAYS good practice.
        //   With H2 file-based or MySQL: the guard is ESSENTIAL.
        //
        // repo.count() — runs: SELECT COUNT(*) FROM students
        // If count > 0, data already exists → skip seeding.
        if (studentRepository.count() > 0) {
            // Print a message so you can see in the console that seeding was skipped.
            System.out.println("[DataSeeder] Database already has data — skipping seed.");
            return;  // exit the method immediately — no inserts
        }

        // ============================================================
        // SEED DATA — insert initial students
        // ============================================================
        // EXAM: The quiz REQUIRES specific studentId values in the seeded data.
        //       The JUnit test does: repo.findByStudentId("STU20240001") and asserts it exists.
        //       If you use the wrong ID, the test FAILS even if all your code is otherwise correct.
        //       ALWAYS re-read the quiz prompt to find the exact required student IDs.

        // repo.save(new Student(...)) — runs: INSERT INTO students (name, student_id, gpa, major) VALUES (...)
        // The database assigns the id field automatically (AUTO_INCREMENT).
        // save() returns the saved entity WITH its new id — we don't use it here but it's available.
        studentRepository.save(new Student(
                "Alice Johnson",   // name
                "STU20240001",     // studentId — EXAM: this specific value is often required
                3.85,              // gpa
                "Computer Science" // major
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

        // Print confirmation with the final count.
        // studentRepository.count() now returns 3 (the three we just inserted).
        System.out.println("[DataSeeder] Successfully seeded "
                + studentRepository.count() + " students into the database.");
    }
}
