# Layer Structure (3-Layer Architecture)

## The Three Layers

A Spring Boot application is divided into three layers. Each layer has ONE job and talks only to the layer directly below it.

```
┌─────────────────────────────────────────────────┐
│          Controller Layer (@RestController)      │
│   Receives HTTP requests, returns HTTP responses │
│   Knows about: HTTP, JSON, status codes          │
│   Does NOT know about: SQL, databases            │
└──────────────────┬──────────────────────────────┘
                   │ calls
┌──────────────────▼──────────────────────────────┐
│          Service Layer (@Service)                │
│   Business logic, validation, orchestration      │
│   Knows about: domain rules, exceptions          │
│   Does NOT know about: HTTP or SQL               │
└──────────────────┬──────────────────────────────┘
                   │ calls
┌──────────────────▼──────────────────────────────┐
│          Repository Layer (@Repository)          │
│   Database access only                           │
│   Knows about: JPA, SQL                          │
│   Does NOT know about: HTTP or business logic    │
└──────────────────┬──────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────┐
│               Database (H2 / MySQL)              │
└─────────────────────────────────────────────────┘
```

## Why This Separation?
- **Testability:** Each layer can be tested independently
- **Maintainability:** Changing the database doesn't require changing the controller
- **Clarity:** A developer knows exactly WHERE to look for each type of problem

## Dependency Direction
Dependencies flow downward only:
- Controller depends on Service (not vice versa)
- Service depends on Repository (not vice versa)
- Repository depends on JPA / database

If you see a Repository being injected directly into a Controller, that's a layer violation.

## Package Convention
```
com.example.app/
├── controller/     ← @RestController classes
├── service/        ← @Service classes
├── repository/     ← JpaRepository interfaces
├── model/          ← @Entity classes
├── exception/      ← custom exception classes
├── handler/        ← @RestControllerAdvice class
├── dto/            ← request/response DTOs
└── seeder/         ← CommandLineRunner for test data
```

## EXAM: Which Annotation Goes on Which Layer?
| Layer | Annotation | What it does |
|---|---|---|
| Controller | `@RestController` | Maps HTTP routes, serializes JSON |
| Service | `@Service` | Business logic, spring-managed bean |
| Repository | `@Repository` | Spring Data JPA, exception translation |
| Entity | `@Entity` | JPA table mapping |
| Handler | `@RestControllerAdvice` | Global exception handling |
