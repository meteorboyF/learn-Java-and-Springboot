// Example: Each stereotype annotation used correctly.

// @Repository — data access layer
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {}
// Spring creates a proxy that implements this interface.
// Also enables exception translation: raw SQL exceptions → Spring's DataAccessException.

// @Service — business logic layer
@Service
public class StudentService {
    private final StudentRepository repo;
    public StudentService(StudentRepository repo) { this.repo = repo; }
    // Business rules go here: validation, orchestration, transformation.
}

// @RestController — web/HTTP layer (= @Controller + @ResponseBody)
@RestController
@RequestMapping("/students")
public class StudentController {
    // HTTP mappings go here. Returns JSON automatically (no @ResponseBody needed on methods).
}

// @Component — generic bean (use when none of the above fit)
@Component
public class DataSeeder implements CommandLineRunner {
    // Startup utility. Not a service (no business logic), not a controller, not a repository.
}

// @Component vs @Service — when to use which?
// @Service: contains business logic (validation, calculations, orchestration).
// @Component: infrastructure utilities (seeders, scheduled tasks, event listeners).
// Both create singleton beans. The difference is only semantic.
