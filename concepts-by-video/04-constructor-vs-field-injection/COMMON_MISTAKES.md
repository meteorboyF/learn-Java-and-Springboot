# Common Mistakes — Constructor vs Field Injection

## Mistake 1: Using field injection everywhere
**Problem:** Cannot use `private final`, harder to unit test, hidden dependencies.
**Fix:** Switch to constructor injection. It's the Spring team's official recommendation.

## Mistake 2: Not using `private final` with constructor injection
**Wrong:** `private StudentRepository repo;` (mutable)
**Fix:** `private final StudentRepository repo;` — once set in constructor, never changes. Guarantees the dependency is always valid.

## Mistake 3: Forgetting that field injection uses reflection (null in tests)
**Problem:** In unit tests without Spring context, `@Autowired` fields are `null`.
```java
StudentService service = new StudentService();
service.getAllStudents();  // NullPointerException: repo is null!
```
**Fix:** Use constructor injection so you can pass a mock:
```java
StudentService service = new StudentService(Mockito.mock(StudentRepository.class));
```
