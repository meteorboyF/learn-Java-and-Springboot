# Syntax — Bean Lifecycle

## @PostConstruct
`java
import jakarta.annotation.PostConstruct;

@PostConstruct  // method-level, void return, no parameters
public void init() { ... }
`

## @PreDestroy
`java
import jakarta.annotation.PreDestroy;

@PreDestroy  // method-level, void return, no parameters
public void cleanup() { ... }
`

## Bean Scope
`java
@Service                          // singleton (default) — one instance per app
@Service @Scope("prototype")      // prototype — new instance per injection
@Service @Scope("request")        // new instance per HTTP request (web)
@Service @Scope("session")        // new instance per HTTP session (web)
`
"@
    mistakes = @"
# Common Mistakes — Bean Lifecycle

## Mistake 1: Using the constructor instead of @PostConstruct to access other beans
**Wrong:** Calling a repository method inside the constructor.
**Problem:** During construction, other beans might not be injected yet.
**Fix:** Use @PostConstruct — it runs AFTER all dependencies are injected.

## Mistake 2: Confusing @PostConstruct and CommandLineRunner
- @PostConstruct: runs when the BEAN is initialized (before full startup, no JPA guarantee)
- CommandLineRunner: runs AFTER the entire app starts (JPA is ready, tables exist)
- For seeding data: always use CommandLineRunner.

## Mistake 3: @PostConstruct method has wrong signature
**Wrong:** @PostConstruct public String init() { ... } or @PostConstruct public void init(String arg)
**Fix:** @PostConstruct must be public void methodName() with no parameters and void return type.
