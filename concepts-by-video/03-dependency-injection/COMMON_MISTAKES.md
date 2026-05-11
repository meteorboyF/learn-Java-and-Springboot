# Common Mistakes — Dependency Injection

## Mistake 1: Using field injection (NullPointerException in tests)
**Wrong:**
```java
@Autowired private StudentRepository repo;
```
**Problem:** Can't write `new StudentService(mockRepo)` in unit tests — you'd need Spring to inject the mock, which requires the full Spring context (slow). Also, repo can't be `final`.

**Fix:** Use constructor injection. `private final StudentRepository repo;` + constructor.

---

## Mistake 2: Calling `new` on a Spring-managed bean
**Wrong:**
```java
StudentService service = new StudentService();  // creates a NEW instance, NOT the Spring bean
```
**Problem:** This creates an unmanaged instance — the Spring-injected `repo` field will be null (not injected). Calling any method will throw NullPointerException.

**Fix:** Let Spring inject the bean for you (via constructor/field injection or `@Autowired`). Never call `new` on a `@Service`/`@Component`/`@Repository` class.

---

## Mistake 3: Forgetting `@Autowired` when there are multiple constructors
**Wrong:**
```java
@Service
public class MyService {
    public MyService() {}  // Spring picks this one (no-arg)
    public MyService(MyRepository repo) { this.repo = repo; }  // ignored!
}
```
**Problem:** Spring picks the no-arg constructor → `repo` stays null.

**Fix:** Add `@Autowired` to the constructor you want Spring to use, OR have only ONE constructor (no-arg constructor gets injected with nothing, the parameterised one gets injected by Spring only when marked).

Best practice: **have only ONE constructor** and make it the parameterised one.
