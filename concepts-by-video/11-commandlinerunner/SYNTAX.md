# Syntax — CommandLineRunner

## Basic Implementation
```java
@Component
public class MyRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        // code here runs after full startup
    }
}
```

## Lambda Version (for simple cases)
```java
@SpringBootApplication
public class App {
    public static void main(String[] args) { SpringApplication.run(App.class, args); }
    
    @Bean  // defines a CommandLineRunner bean inline
    public CommandLineRunner runner(StudentRepository repo) {
        return args -> {
            if (repo.count() > 0) return;
            repo.save(new Student("Alice", "STU001", 3.5, "CS"));
        };
    }
}
```

## With @Order (multiple runners)
```java
@Component @Order(1)  // runs first
public class FirstRunner implements CommandLineRunner { ... }

@Component @Order(2)  // runs second
public class SecondRunner implements CommandLineRunner { ... }
```

## Accessing Command-Line Arguments
```java
@Override
public void run(String... args) throws Exception {
    for (String arg : args) {
        System.out.println("Arg: " + arg);
    }
    // args are the JVM arguments: java -jar app.jar arg1 arg2
}
```

## Duplicate Guard Patterns
```java
// Pattern 1: count-based (simple)
if (repo.count() > 0) return;

// Pattern 2: specific record check (precise)
if (repo.findByStudentId("STU20240001").isPresent()) return;

// Pattern 3: both (robust)
if (repo.existsById(1L)) return;
```
