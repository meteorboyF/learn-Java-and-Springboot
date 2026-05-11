# Common Mistakes — Service Layer

## Mistake 1: Calling the repository directly from the controller
**Wrong:** `return repo.findAll();` inside a `@RestController` method
**Problem:** Bypasses the service layer. Business logic ends up in the controller. Hard to test, hard to reuse, violates separation of concerns.
**Fix:** Controller → calls Service → calls Repository. Never skip a layer.

## Mistake 2: Updating the id or studentId in the update method
**Wrong:**
```java
public Student update(Long id, Student newData) {
    existing.setId(newData.getId());            // WRONG — overwrites DB pk
    existing.setStudentId(newData.getStudentId()); // WRONG — overwrites immutable key
}
```
**Problem:** A malicious or careless client could change a student's id or studentId, corrupting data.
**Fix:** Only update fields that are supposed to change: name, gpa, major. Never id, never studentId.

## Mistake 3: Using repo.save() with a new entity from the request body directly
**Wrong:**
```java
public Student update(Long id, Student newData) {
    return repo.save(newData);  // newData has no id (from request body) → INSERT instead of UPDATE!
}
```
**Problem:** If the request body doesn't include an id (which it shouldn't — id comes from URL), `save()` treats it as a new entity and INSERTs a duplicate.
**Fix:** Load the existing entity, modify it, then save: `existing = getById(id); ...; return repo.save(existing);`
