# Common Mistakes — Full CRUD

## Mistake 1: Calling repo.save() Without Capturing the Return Value (for Create)
**Wrong:**
```java
public void create(Student student) {
    repo.save(student); // return value ignored
    // Now we can't return the saved entity or build a Location URI
}
```
**Problem:** `repo.save()` returns the saved entity, which now has the auto-generated `id` field populated. If you ignore the return value, you can't build the Location URI for the 201 response and you can't return the saved object to the client.
**Fix:**
```java
public Student create(Student student) {
    return repo.save(student); // capture and return the entity with its new id
}
```

---

## Mistake 2: Updating Without Finding First (Creates a New Record Instead of Updating)
**Wrong:**
```java
public Student update(Long id, Student updated) {
    updated.setId(null);      // id is null or wrong
    return repo.save(updated); // save() sees no id → INSERTS a new row instead of updating!
}
```
**Problem:** `repo.save()` decides INSERT vs UPDATE based on whether the entity has an `id`. If `updated.getId()` is null, it creates a NEW record instead of modifying the existing one.
**Fix:** Always find the existing record first, then modify its fields, then save:
```java
public Student update(Long id, Student updated) {
    Student existing = findById(id);     // throws 404 if not found
    existing.setName(updated.getName()); // modify fields on the EXISTING managed entity
    existing.setGpa(updated.getGpa());
    return repo.save(existing);          // now save() updates the correct row
}
```

---

## Mistake 3: Not Throwing an Exception on Delete When the Record Doesn't Exist
**Wrong:**
```java
public void delete(Long id) {
    repo.deleteById(id); // silently does nothing if id doesn't exist
}
```
**Problem:** `deleteById()` does nothing if the id doesn't exist — it won't throw an exception automatically. The controller returns 204 No Content, making it look like the delete succeeded even when the record was never there.
**Fix:** Check first:
```java
public void delete(Long id) {
    if (!repo.existsById(id)) {
        throw new StudentNotFoundException(id); // propagates to @ExceptionHandler → 404
    }
    repo.deleteById(id);
}
```
