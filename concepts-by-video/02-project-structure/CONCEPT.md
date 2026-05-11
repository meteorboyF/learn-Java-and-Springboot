# Spring Boot Project Structure

## What is it?
The standard folder layout that every Spring Boot Maven project follows. Understanding this structure is essential — you need to know exactly where to put each file.

## Why Does This Structure Exist?
Maven (the build tool) requires specific folder paths for it to compile and package your code correctly. Spring Boot adds conventions on top: `src/main/resources/application.properties` is always read automatically.

## The Standard Layout
```
project-root/
├── pom.xml                           ← Maven build config + dependencies
└── src/
    ├── main/
    │   ├── java/                     ← ALL your Java source files go here
    │   │   └── com/example/app/
    │   │       ├── AppApplication.java        ← main class (@SpringBootApplication)
    │   │       ├── model/                     ← @Entity classes
    │   │       │   └── Student.java
    │   │       ├── repository/                ← @Repository interfaces
    │   │       │   └── StudentRepository.java
    │   │       ├── service/                   ← @Service classes
    │   │       │   └── StudentService.java
    │   │       ├── controller/                ← @RestController classes
    │   │       │   └── StudentController.java
    │   │       ├── exception/                 ← custom exception classes
    │   │       │   └── StudentNotFoundException.java
    │   │       ├── dto/                       ← Data Transfer Objects
    │   │       │   ├── StudentRequest.java
    │   │       │   └── StudentResponse.java
    │   │       ├── handler/                   ← @RestControllerAdvice
    │   │       │   └── GlobalExceptionHandler.java
    │   │       └── seeder/                    ← CommandLineRunner
    │   │           └── DataSeeder.java
    │   └── resources/                ← config files and static assets
    │       └── application.properties  ← Spring Boot reads this automatically
    └── test/
        └── java/
            └── com/example/app/
                └── AppApplicationTests.java  ← JUnit tests
```

## The Layered Architecture
Each folder/package has ONE responsibility:
- `model/` — data structure (what the DB stores)
- `repository/` — data access (how to read/write the DB)
- `service/` — business logic (what the app does)
- `controller/` — web interface (how HTTP maps to operations)
- `exception/` — error types
- `handler/` — error responses

## EXAM: Layer Rules
- Controllers call Services. ✅
- Services call Repositories. ✅
- Controllers NEVER call Repositories directly. ❌
- Repositories NEVER contain business logic. ❌
