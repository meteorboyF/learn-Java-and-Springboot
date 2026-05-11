// Example: Complete runnable Spring Boot project with H2.
// All layers in one file for easy study — in a real project, split into separate files.

package com.example.app;

// ─── pom.xml dependencies needed (NOT shown here — see springboot-reference/pom.xml):
// spring-boot-starter-web, spring-boot-starter-data-jpa, com.h2database:h2 (runtime scope)

// ─── application.properties needed:
// spring.datasource.url=jdbc:h2:mem:testdb
// spring.datasource.driver-class-name=org.h2.Driver
// spring.datasource.username=sa
// spring.datasource.password=
// spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
// spring.jpa.hibernate.ddl-auto=update
// spring.jpa.show-sql=true
// spring.h2.console.enabled=true

import jakarta.persistence.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

// ═══════════════════════════════════════════════════════════════════════════════
// MAIN APPLICATION CLASS
// ═══════════════════════════════════════════════════════════════════════════════
// @SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan
// SpringApplication.run() boots the embedded Tomcat server and the Spring context.
@SpringBootApplication
public class Example {
    public static void main(String[] args) {
        SpringApplication.run(Example.class, args);
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// ENTITY
// ═══════════════════════════════════════════════════════════════════════════════
@Entity
@Table(name = "students")
class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment
    private Long id;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(name = "student_id", unique = true, nullable = false)
    private String studentId;

    @Column(precision = 4, scale = 2)
    private Double gpa;

    private String major;

    public Student() {} // EXAM Bug 3: missing this causes startup crash

    public Student(String name, String studentId, Double gpa, String major) {
        this.name = name;
        this.studentId = studentId;
        this.gpa = gpa;
        this.major = major;
    }

    public Long getId()         { return id; }
    public String getName()     { return name; }
    public void setName(String n) { this.name = n; }
    public String getStudentId(){ return studentId; }
    public void setStudentId(String s) { this.studentId = s; }
    public Double getGpa()      { return gpa; }
    public void setGpa(Double g) { this.gpa = g; }
    public String getMajor()    { return major; }
    public void setMajor(String m) { this.major = m; }
}

// ═══════════════════════════════════════════════════════════════════════════════
// REPOSITORY
// ═══════════════════════════════════════════════════════════════════════════════
interface StudentRepository extends JpaRepository<Student, Long> {
    // free methods: save, findById, findAll, deleteById, existsById, count
}

// ═══════════════════════════════════════════════════════════════════════════════
// EXCEPTION
// ═══════════════════════════════════════════════════════════════════════════════
class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(Long id) {
        super("Student not found with id: " + id);
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// SERVICE
// ═══════════════════════════════════════════════════════════════════════════════
@Service
class StudentService {

    private final StudentRepository repo;

    public StudentService(StudentRepository repo) { this.repo = repo; }

    public Student create(Student s)    { return repo.save(s); }
    public List<Student> findAll()      { return repo.findAll(); }

    public Student findById(Long id) {
        // EXAM Bug 2: .get() crashes if empty — always use .orElseThrow()
        return repo.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    public Student update(Long id, Student updated) {
        Student existing = findById(id); // throws 404 if not found
        existing.setName(updated.getName());
        existing.setGpa(updated.getGpa());
        existing.setMajor(updated.getMajor());
        return repo.save(existing);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) throw new StudentNotFoundException(id);
        repo.deleteById(id);
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// DATA SEEDER
// ═══════════════════════════════════════════════════════════════════════════════
@Component
class DataSeeder implements CommandLineRunner {

    private final StudentRepository repo;

    public DataSeeder(StudentRepository repo) { this.repo = repo; }

    @Override
    public void run(String... args) {
        // EXAM Bug 4: missing this guard causes duplicate inserts on every restart
        if (repo.count() > 0) return;

        repo.save(new Student("Alice Smith",   "STU20240001", 3.85, "Computer Science"));
        repo.save(new Student("Bob Johnson",   "STU20240002", 3.42, "Mathematics"));
        repo.save(new Student("Carol Williams","STU20240003", 3.91, "Computer Science"));
        System.out.println("Seeded 3 students.");
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// EXCEPTION HANDLER
// ═══════════════════════════════════════════════════════════════════════════════
class ErrorResponse {
    private int status;
    private String message;
    public ErrorResponse(int status, String message) { this.status = status; this.message = message; }
    public int getStatus()     { return status; }   // EXAM: getters required for Jackson
    public String getMessage() { return message; }
}

// EXAM Bug 5: @ControllerAdvice here causes 500 instead of correct status codes
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(StudentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ErrorResponse(404, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ErrorResponse(500, "An unexpected error occurred."));
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// CONTROLLER
// ═══════════════════════════════════════════════════════════════════════════════
@RestController
@RequestMapping("/api/{studentId}/students")
class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) { this.service = service; }

    @GetMapping
    public List<Student> getAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public Student getById(@PathVariable Long id) { return service.findById(id); }

    // EXAM Bug 1: must return 201, not 200 — use ResponseEntity.created()
    @PostMapping
    public ResponseEntity<Student> create(
            @PathVariable String studentId,
            @RequestBody Student student) {
        Student saved = service.create(student);
        URI loc = URI.create("/api/" + studentId + "/students/" + saved.getId());
        return ResponseEntity.created(loc).body(saved); // 201 Created
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> update(@PathVariable Long id, @RequestBody Student s) {
        return ResponseEntity.ok(service.update(id, s)); // 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
