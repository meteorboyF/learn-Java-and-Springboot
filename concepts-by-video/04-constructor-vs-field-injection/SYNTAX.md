# Syntax — Constructor vs Field Injection

## Constructor Injection
```java
// Single constructor — @Autowired optional:
@Service
public class MyService {
    private final MyRepo repo;
    public MyService(MyRepo repo) { this.repo = repo; }
}

// Multiple constructors — @Autowired required on the one Spring should use:
@Service
public class MyService {
    private final MyRepo repo;
    public MyService() { this.repo = null; }  // default
    @Autowired
    public MyService(MyRepo repo) { this.repo = repo; }  // Spring uses this
}
```

## Field Injection
```java
@Service
public class MyService {
    @Autowired  // required
    private MyRepo repo;  // cannot be final
}
```

## Setter Injection
```java
@Service
public class MyService {
    private MyRepo repo;
    @Autowired  // required
    public void setRepo(MyRepo repo) { this.repo = repo; }
}
```
