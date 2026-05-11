# Syntax Reference — Dependency Injection

## Constructor Injection (Use This)
```java
@Service
public class MyService {
    private final MyRepository repo;  // final = required

    // Single constructor: @Autowired optional
    public MyService(MyRepository repo) {
        this.repo = repo;
    }

    // Multiple constructors: mark the one Spring should use
    @Autowired
    public MyService(MyRepository repo, OtherService other) { ... }
}
```

## Field Injection (Avoid)
```java
@Service
public class MyService {
    @Autowired
    private MyRepository repo;  // Spring sets this via reflection
}
```

## @Autowired Placement Rules
| Where | @Autowired | Notes |
|---|---|---|
| Constructor (single) | Optional | Spring uses it automatically |
| Constructor (multiple) | Required on ONE | Marks which constructor to use |
| Field | Required | Injects directly into private field |
| Setter | Required | Spring calls this setter |

## Checking What Spring Injected
```java
// In a test — verify injection worked:
@SpringBootTest
class ServiceTest {
    @Autowired
    StudentService studentService;  // Spring injects this in tests too

    @Test
    void contextLoads() {
        assertThat(studentService).isNotNull();
    }
}
```
