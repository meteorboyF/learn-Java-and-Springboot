# Syntax — Service Layer

## Basic Service Structure
```java
@Service
public class MyService {
    private final MyRepository repo;
    public MyService(MyRepository repo) { this.repo = repo; }

    public List<MyEntity> getAll() { return repo.findAll(); }
    
    public MyEntity getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new MyNotFoundException(id));
    }
    
    public MyEntity create(MyEntity e) { return repo.save(e); }
    
    public MyEntity update(Long id, MyEntity newData) {
        MyEntity existing = getById(id);  // reuse method
        existing.setName(newData.getName());
        return repo.save(existing);
    }
    
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new MyNotFoundException(id);
        repo.deleteById(id);
    }
}
```

## Key save() Behavior
```java
repo.save(entity);  // entity.getId() == null → INSERT, returns entity with DB-assigned id
repo.save(entity);  // entity.getId() != null → UPDATE, returns updated entity
```

## Validation in Service
```java
if (condition) throw new IllegalArgumentException("message");  // → 400 Bad Request
if (notFound)  throw new MyNotFoundException("message");       // → 404 Not Found
```
