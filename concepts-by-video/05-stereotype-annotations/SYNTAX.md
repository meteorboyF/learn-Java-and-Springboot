# Syntax — Stereotype Annotations

## All Stereotype Annotations
```java
@Component          // class-level, generic bean
@Service            // class-level, business logic
@Repository         // class-level or interface-level, data access
@Controller         // class-level, MVC controller (returns view names)
@RestController     // class-level, REST controller (returns JSON)
@RestControllerAdvice  // class-level, global exception handler for REST
@ControllerAdvice   // class-level, global exception handler for MVC (HTML)
```

## Inheritance Chain
```
@Component
├── @Service         (extends @Component semantically)
├── @Repository      (extends @Component + exception translation)
└── @Controller      (extends @Component)
    └── @RestController (extends @Controller + @ResponseBody)
```

## Usage Pattern
```java
@Service            // on the class
public class MyService {
    @Autowired      // on the field OR constructor (optional for single constructor)
    private MyRepository repo;
}

@Repository         // optional on JpaRepository interfaces (Spring scans them anyway)
public interface MyRepository extends JpaRepository<Entity, Long> {}
```

## Annotation Quick Reference Table
| Layer | Annotation | On |
|---|---|---|
| Entity | `@Entity` | Class |
| Repository | `@Repository` | Interface |
| Service | `@Service` | Class |
| Controller (REST) | `@RestController` | Class |
| Controller (MVC) | `@Controller` | Class |
| Exception handler | `@RestControllerAdvice` | Class |
| Generic bean | `@Component` | Class |
| Startup runner | `@Component` + `implements CommandLineRunner` | Class |
