# SYNTAX — Full CRUD

## Repository Methods (free from JpaRepository)

```java
repo.save(entity)           // INSERT or UPDATE (returns saved entity with id)
repo.findById(id)           // returns Optional<T>
repo.findAll()              // returns List<T>
repo.deleteById(id)         // DELETE by primary key
repo.existsById(id)         // returns boolean
repo.count()                // returns long (total rows)
```

## Service — Standard CRUD Method Bodies

```java
// CREATE
public Student create(Student s) { return repo.save(s); }

// READ ALL
public List<Student> findAll() { return repo.findAll(); }

// READ ONE (with 404)
public Student findById(Long id) {
    return repo.findById(id)
               .orElseThrow(() -> new StudentNotFoundException(id));
}

// UPDATE (find → modify → save)
public Student update(Long id, Student updated) {
    Student existing = findById(id);
    existing.setName(updated.getName());
    existing.setGpa(updated.getGpa());
    return repo.save(existing);
}

// DELETE (check → delete)
public void delete(Long id) {
    if (!repo.existsById(id)) throw new StudentNotFoundException(id);
    repo.deleteById(id);
}
```

## Controller — All 5 Endpoints

```java
@GetMapping
public List<Student> getAll() { return service.findAll(); }

@GetMapping("/{id}")
public Student getById(@PathVariable Long id) { return service.findById(id); }

@PostMapping
public ResponseEntity<Student> create(
        @PathVariable String studentId,
        @RequestBody Student s) {
    Student saved = service.create(s);
    URI loc = URI.create("/api/" + studentId + "/students/" + saved.getId());
    return ResponseEntity.created(loc).body(saved);  // 201
}

@PutMapping("/{id}")
public ResponseEntity<Student> update(@PathVariable Long id, @RequestBody Student s) {
    return ResponseEntity.ok(service.update(id, s));  // 200
}

@DeleteMapping("/{id}")
public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();  // 204
}
```

## Status Code Cheat Sheet

```
POST   → 201 Created      (ResponseEntity.created(uri).body(saved))
GET    → 200 OK           (return list/object directly, or ResponseEntity.ok(x))
PUT    → 200 OK           (ResponseEntity.ok(updated))
DELETE → 204 No Content   (ResponseEntity.noContent().build())
Any    → 404 Not Found    (thrown via StudentNotFoundException → @ExceptionHandler)
```
