# Problem 07 — Service Without a Database (In-Memory List Storage)

**Source:** CSE 2218 Advanced OOP Lab — Section 3, Question 7

## The Question

Some quiz variants do NOT use JPA at all. Instead, store data in an `ArrayList`. Implement a `ProductService` that:

a) Stores `Product` objects in a **private ArrayList** (no `@Repository`, no database)  
b) **Auto-increments an ID** for each new product using `AtomicInteger`  
c) Supports: `getAll()`, `getById(int id)`, `create(Product p)`, `delete(int id)`  
d) Throws `ProductNotFoundException` when a product is not found

**Product has:** `int id`, `String name`, `double price`

## What You Need to Know Before Solving

- `@Service` on the class — Spring manages it as a singleton (ONE instance per app)
- `ArrayList<Product>` holds all data in RAM — data is lost when app stops
- `AtomicInteger` for thread-safe ID generation (like database AUTO_INCREMENT)
- Stream API for searching: `stream().filter(...).findFirst().orElseThrow(...)`
- `store.remove(product)` removes by object reference (NOT by index!)

## Exam Tips

- Because `@Service` creates a singleton, the ArrayList lives for the entire app lifetime
- `AtomicInteger.getAndIncrement()` — returns current value THEN increments (like `i++`)
- `store.remove(index)` vs `store.remove(object)` — different behavior! Use `remove(object)`
- No `@Repository` needed — no `JpaRepository` — no `application.properties` datasource
- This pattern is used in quiz variants that say "no database" or don't include JPA deps
