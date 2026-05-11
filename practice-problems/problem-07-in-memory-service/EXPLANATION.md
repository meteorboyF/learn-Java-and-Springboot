# Explanation — Problem 07: In-Memory Service (No Database)

## When Is This Pattern Used?

- Quiz variant says "no database required" or "use ArrayList"
- The `pom.xml` doesn't include `spring-boot-starter-data-jpa`
- You want to understand the service layer without DB complexity
- Rapid prototyping before setting up a real database

## Key Differences: In-Memory vs JPA

| | In-Memory (ArrayList) | JPA (Repository) |
|---|---|---|
| Data lives in | RAM (lost on restart) | Database (persists) |
| Entity class | Plain Java class | `@Entity` class |
| ID assignment | `AtomicInteger.getAndIncrement()` | Database AUTO_INCREMENT |
| Find by id | `stream().filter().findFirst()` | `repo.findById(id)` |
| Save | `store.add(product)` | `repo.save(product)` |
| Delete | `store.remove(product)` | `repo.deleteById(id)` |
| Thread-safe? | ⚠️ AtomicInteger for IDs, but ArrayList isn't thread-safe | ✅ DB handles concurrency |
| Repository interface | ❌ Not needed | ✅ Required |

## The Singleton and Why It Matters

```java
@Service  // → singleton bean (one instance for the entire application)
public class ProductService {
    private final List<Product> store = new ArrayList<>();
```

Because `@Service` creates exactly one instance, the `store` ArrayList is shared across all requests:
- Request 1: `create()` → adds Product to store
- Request 2: `getAll()` → sees the product from Request 1

If `@Service` created a new instance per request (like a web framework's request scope), each request would have its own empty list — nothing would persist between requests.

## `AtomicInteger` — Thread-Safety Explained

```java
private final AtomicInteger idCounter = new AtomicInteger(1);

// getAndIncrement() = atomic "read then add 1" — one indivisible CPU operation
int nextId = idCounter.getAndIncrement(); // returns 1, counter becomes 2
```

Why not `int idCounter = 1; idCounter++;`?

With concurrent HTTP requests, two threads could read `idCounter = 5` simultaneously, both increment it, and both create a product with `id = 5`. `AtomicInteger` prevents this by making the read+increment a single uninterruptible operation.

## The `store.remove()` Trap

```java
// ArrayList has TWO overloaded remove methods:
store.remove(product);     // ✅ CORRECT — removes by object reference (uses .equals())
store.remove(id);          // ❌ WRONG — removes element at INDEX 'id' (position in list)

// Example:
// store = [Product(id=1), Product(id=2), Product(id=3)]
// store.remove(1) as int → removes Product at INDEX 1 → removes Product(id=2)!
// store.remove(product) where product.id=1 → removes Product(id=1) ← correct!
```

This is a very common bug. Always pass the **object** to `remove()`, not the integer id.

## Stream API vs For-Loop

Both approaches work. Know both for the exam:

```java
// Stream approach (functional, preferred):
return store.stream()
        .filter(p -> p.getId() == id)
        .findFirst()
        .orElseThrow(() -> new ProductNotFoundException("Not found: " + id));

// For-loop approach (imperative, also valid):
for (Product p : store) {
    if (p.getId() == id) return p;
}
throw new ProductNotFoundException("Not found: " + id);
```

## In-Memory Update vs JPA Update

```java
// In-memory (no save needed):
Product existing = getById(id);
existing.setName("New Name");  // modifies the SAME object that's in the list
return existing;               // done — list already has the updated object

// JPA (save required):
Student existing = getStudentById(id);
existing.setName("New Name");   // modifies a managed entity
repo.save(existing);            // tells Hibernate to UPDATE — required!
return existing;
```

In-memory works because `ArrayList` stores object references, not copies. Modifying `existing` modifies the object inside the list. JPA requires `save()` because Hibernate manages entity state separately and needs to be told about changes.
