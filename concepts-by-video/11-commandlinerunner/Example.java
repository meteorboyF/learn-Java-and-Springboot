// Example: Complete DataSeeder using CommandLineRunner.
package com.example.app.seeder;

import com.example.app.model.Student;
import com.example.app.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component  // generic Spring bean — @Service would also work
public class DataSeeder implements CommandLineRunner {

    private final StudentRepository studentRepository;

    // Constructor injection — single constructor, no @Autowired needed.
    public DataSeeder(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // run() is called by Spring Boot after full startup:
    //   ✅ All beans created
    //   ✅ JPA connected to DB
    //   ✅ Hibernate ran ddl-auto (tables exist)
    //   ✅ Safe to call repo methods
    @Override
    public void run(String... args) throws Exception {
        // Duplicate guard — essential for persistent storage (H2 file, MySQL).
        // Without this: every restart inserts 3 more students.
        if (studentRepository.count() > 0) {
            System.out.println("[DataSeeder] Already seeded. Skipping.");
            return;  // exit immediately — no inserts
        }

        // Insert initial data.
        // EXAM: The quiz specifies exact studentId values — use them exactly.
        studentRepository.save(new Student("Alice Johnson",  "STU20240001", 3.85, "Computer Science"));
        studentRepository.save(new Student("Bob Rahman",     "STU20240002", 3.20, "Mathematics"));
        studentRepository.save(new Student("Clara Reyes",    "STU20240003", 3.60, "Physics"));

        System.out.println("[DataSeeder] Seeded " + studentRepository.count() + " students.");
    }
}

// ─── Multiple CommandLineRunners ──────────────────────────────────────────────
// You can have multiple CommandLineRunner beans. Use @Order to control sequence:
//
// @Component @Order(1)
// public class FirstRunner implements CommandLineRunner {
//     @Override public void run(String... args) { System.out.println("Runs first"); }
// }
//
// @Component @Order(2)
// public class SecondRunner implements CommandLineRunner {
//     @Override public void run(String... args) { System.out.println("Runs second"); }
// }
