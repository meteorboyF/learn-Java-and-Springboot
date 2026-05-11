# Service Layer

## What is it?
The service layer (`@Service` classes) sits between the controller (web layer) and the repository (data layer). It contains ALL business logic: validation, data transformation, orchestration of multiple repository calls.

## Why Does It Exist?
- **Separation of Concerns:** Controllers handle HTTP. Repositories handle SQL. Services handle WHAT the app does.
- **Reusability:** Multiple controllers can use the same service.
- **Testability:** Business logic can be tested without HTTP or database.

## Layer Architecture (ALWAYS follow this)
```
HTTP Request → @RestController → @Service → @Repository → Database
HTTP Response ← @RestController ← @Service ← @Repository ← Database
```

## What Goes in the Service Layer?
- Finding entities with `.orElseThrow()` for proper 404 handling
- Validation (throw `IllegalArgumentException` for bad input)
- Combining multiple repository calls
- Data transformation/mapping
- Transaction management

## What Does NOT go in the Service Layer?
- HTTP details (`HttpStatus`, `ResponseEntity`, `@PathVariable`) — stay in controller
- SQL/JPQL queries — stay in repository
- `@Transactional` goes on service methods (sometimes)

## EXAM: The Update Pattern (find → modify → save)
```java
public Student update(Long id, Student newData) {
    Student existing = getById(id);          // throws 404 if not found
    existing.setName(newData.getName());     // modify only safe fields
    existing.setGpa(newData.getGpa());
    return repo.save(existing);              // save() → UPDATE (has id)
}
```
