# Full CRUD Tutorial

## What Is CRUD?
CRUD = Create, Read, Update, Delete. These are the four fundamental operations on any resource. A complete REST API maps each to an HTTP method:

| Operation | HTTP Method | URL              | Response Code |
|-----------|-------------|------------------|---------------|
| Create    | POST        | /students         | 201 Created   |
| Read all  | GET         | /students         | 200 OK        |
| Read one  | GET         | /students/{id}    | 200 OK        |
| Update    | PUT         | /students/{id}    | 200 OK        |
| Delete    | DELETE      | /students/{id}    | 204 No Content|

## The 4-Layer Architecture for CRUD

```
Client (browser / Postman / other service)
    ↕ HTTP (JSON)
Controller (@RestController)  — receives HTTP, delegates to service, returns ResponseEntity
    ↕ Java method calls
Service (@Service)            — business logic, calls repository, throws domain exceptions
    ↕ Java method calls
Repository (JpaRepository)   — talks to the database
    ↕ SQL
Database (H2 / MySQL)
```

## What Each Layer Does in CRUD

### Controller responsibilities:
- Extract data from HTTP (`@PathVariable`, `@RequestBody`)
- Call the correct service method
- Wrap the result in `ResponseEntity` with the right status code
- Never contain business logic or database calls

### Service responsibilities:
- Validate business rules (e.g., "GPA must be between 0 and 4")
- Call repository methods
- Throw `StudentNotFoundException` when a resource isn't found
- Never know about HTTP (no `ResponseEntity`, no `@RequestBody`)

### Repository responsibilities:
- Extend `JpaRepository<Student, Long>` — you get 10+ methods for free
- Add custom queries when the built-in ones aren't enough
- Never contain business logic

## EXAM: Common Patterns
- **Update pattern:** find first (throws 404 if missing), then modify fields, then save
- **Delete pattern:** check if exists first (throws 404 if missing), then deleteById
- **Create pattern:** convert request DTO → entity, save, return saved entity (with generated id)
