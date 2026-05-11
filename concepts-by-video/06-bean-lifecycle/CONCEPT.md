# Bean Lifecycle

## What is it?
The lifecycle of a Spring bean from creation to destruction. Spring manages bean instantiation, dependency injection, initialization, and destruction.

## Key Lifecycle Phases
1. **Instantiation** — Spring creates the bean (calls constructor)
2. **Dependency Injection** — Spring injects dependencies (@Autowired / constructor)
3. **Initialization** — @PostConstruct method runs (if present)
4. **Ready** — bean is available in the application context
5. **Destruction** — @PreDestroy method runs when context closes

## @PostConstruct (runs AFTER injection, BEFORE the bean is used)
`java
@Service
public class StudentService {
    private final StudentRepository repo;
    public StudentService(StudentRepository repo) { this.repo = repo; }
    
    @PostConstruct  // runs once, after injection is complete
    public void init() {
        System.out.println("StudentService initialized. Repo is: " + repo);
    }
}
`

## @PreDestroy (runs BEFORE the bean is destroyed)
`java
@PreDestroy
public void cleanup() {
    System.out.println("StudentService is being destroyed.");
    // Release resources, close connections, etc.
}
`

## EXAM: CommandLineRunner vs @PostConstruct
- @PostConstruct — runs when the bean is initialized (before full app startup)
- CommandLineRunner.run() — runs AFTER the entire application context is ready
- For seeding data: use CommandLineRunner (JPA is ready). For bean setup: use @PostConstruct.
