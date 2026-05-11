# Common Mistakes — Layer Structure

## Mistake 1: Injecting a Repository Directly Into a Controller (Layer Violation)
**Wrong:**
```java
@RestController
public class StudentController {
    @Autowired
    private StudentRepository repo; // ← should be StudentService, not the repository
    
    @GetMapping("/{id}")
    public Student get(@PathVariable Long id) {
        return repo.findById(id).get(); // no 404, no business logic, just raw DB access
    }
}
```
**Problem:** The controller is skipping the service layer entirely. Business logic (like "throw 404 if not found") ends up scattered in the controller instead of centralized in the service. The layers lose their separation.
**Fix:** Inject the service, not the repository:
```java
@RestController
public class StudentController {
    private final StudentService service;
    public StudentController(StudentService service) { this.service = service; }
}
```

---

## Mistake 2: Putting Business Logic in the Controller
**Wrong:**
```java
@PostMapping
public ResponseEntity<Student> create(@RequestBody Student s) {
    if (s.getGpa() < 0 || s.getGpa() > 4.0) { // ← validation belongs in service
        return ResponseEntity.badRequest().build();
    }
    Student saved = repo.save(s); // ← database call in controller
    return ResponseEntity.created(...).body(saved);
}
```
**Problem:** The controller is doing validation AND database access. It's doing three layers' jobs at once. If you want to reuse the validation (e.g., also validate on update), you'd have to duplicate it.
**Fix:** Move validation to the service:
```java
// Service:
public Student create(Student s) {
    if (s.getGpa() < 0 || s.getGpa() > 4.0)
        throw new IllegalArgumentException("GPA must be between 0 and 4");
    return repo.save(s);
}
// Controller just calls: service.create(s)
```

---

## Mistake 3: Putting @Entity on a Service or Controller
**Wrong:**
```java
@Entity          // ← wrong: belongs only on model/domain classes
@Service
public class StudentService { ... }
```
**Problem:** JPA tries to create a database table called `StudentService`. The application crashes at startup with a mapping error. `@Entity` should only go on classes that represent database rows (Student, Course, Product, etc.).
**Fix:** Use the correct annotation for each layer:
- `@Entity` → model class (Student, Course)
- `@Service` → service class (StudentService)
- `@RestController` → controller class (StudentController)
