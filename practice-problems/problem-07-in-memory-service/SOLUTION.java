// SOLUTION — ProductService.java (In-Memory, No Database)
// Location: src/main/java/com/example/app/inMemory/ProductService.java
// This is a single file containing Product, ProductNotFoundException, and ProductService.
// In a real project, these would be in separate files.

package com.example.app.inMemory;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

// ─── Product class (plain Java — no JPA annotations) ────────────────────────
// Note: No @Entity, no @Id, no @Column — this is just a plain Java object.
// The data lives in the service's ArrayList, not in a database.
class Product {
    private int    id;     // int (primitive) — OK here since no JPA null constraint needed
    private String name;
    private double price;  // double (primitive) — price won't be null

    public Product() {}    // no-arg constructor — Jackson needs this for JSON deserialization

    public Product(int id, String name, double price) {
        this.id    = id;
        this.name  = name;
        this.price = price;
    }

    // Getters and setters — Jackson uses these for JSON serialization/deserialization.
    public int    getId()    { return id; }
    public void   setId(int id)           { this.id = id; }
    public String getName()  { return name; }
    public void   setName(String name)    { this.name = name; }
    public double getPrice() { return price; }
    public void   setPrice(double price)  { this.price = price; }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', price=" + price + "}";
    }
}

// ─── ProductNotFoundException (unchecked exception) ─────────────────────────
class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}

// ─── ProductService — the in-memory service ──────────────────────────────────
// @Service — Spring manages this as a SINGLETON (one instance per app).
// KEY INSIGHT: Because it's a singleton, the ArrayList and AtomicInteger
// are shared across ALL HTTP requests. The data persists for the app's lifetime.
// When the app restarts, the ArrayList starts fresh — no persistence.
@Service
public class ProductService {

    // ─── In-memory "database" ─────────────────────────────────────────────
    // final — the reference never changes (only the list's contents change).
    // ArrayList — ordered, allows duplicates, O(n) search (fine for small datasets).
    // This list IS the database for this no-JPA variant.
    private final List<Product> store = new ArrayList<>();

    // ─── Thread-safe ID counter ───────────────────────────────────────────
    // AtomicInteger — thread-safe auto-incrementing integer.
    // WHY not regular int?
    //   Multiple HTTP requests can arrive simultaneously (concurrent threads).
    //   Thread A: reads counter = 5, gets interrupted.
    //   Thread B: reads counter = 5, increments to 6, creates Product(id=5).
    //   Thread A: resumes, increments to 6, creates Product(id=5) — DUPLICATE ID!
    //   AtomicInteger.getAndIncrement() prevents this: it's one atomic CPU operation.
    // EXAM: AtomicInteger is the correct choice for thread-safe ID generation.
    private final AtomicInteger idCounter = new AtomicInteger(1);  // start at 1 (like DB)

    // ─── Constructor — pre-seed data ──────────────────────────────────────
    // Called by Spring when creating the @Service bean (at startup).
    // Pre-populates the store so the API isn't empty on first request.
    public ProductService() {
        // getAndIncrement(): returns current value THEN increments.
        // First call: returns 1, counter → 2.
        // Second call: returns 2, counter → 3.
        // Third call: returns 3, counter → 4.
        store.add(new Product(idCounter.getAndIncrement(), "Laptop",   999.99));
        store.add(new Product(idCounter.getAndIncrement(), "Mouse",     29.99));
        store.add(new Product(idCounter.getAndIncrement(), "Keyboard",  59.99));
        // After constructor: store has [Product(1), Product(2), Product(3)], counter=4
    }

    // ─── getAll() → return all products ──────────────────────────────────
    // JPA equivalent: return repo.findAll();
    // In-memory: return the whole ArrayList.
    public List<Product> getAll() {
        return store;  // returns the live list (not a copy)
    }

    // ─── getById(int id) → find one product ──────────────────────────────
    // JPA equivalent: return repo.findById(id).orElseThrow(...)
    // In-memory: use Java Stream API to search the ArrayList.
    //
    // store.stream()                    — creates a stream of Product objects
    // .filter(p -> p.getId() == id)     — keep only products where id matches
    //   == for int is correct (primitive comparison)
    // .findFirst()                       — returns Optional<Product>
    // .orElseThrow(...)                  — throw if nothing found
    public Product getById(int id) {
        return store.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product not found with id: " + id));
    }

    // ─── create(Product product) → add a new product ──────────────────────
    // JPA equivalent: return repo.save(product);
    // In-memory: assign ID manually, add to list.
    //
    // The client sends a product WITHOUT an id (client doesn't know the next id).
    // We assign the id from the counter.
    public Product create(Product product) {
        product.setId(idCounter.getAndIncrement());  // assign next available id
        store.add(product);                           // add to the "database" (ArrayList)
        return product;                               // return with the assigned id
    }

    // ─── delete(int id) → remove a product ───────────────────────────────
    // JPA equivalent: repo.deleteById(id); (after existsById check)
    // In-memory: find the object, then remove it.
    //
    // store.remove(Product object) — removes by OBJECT REFERENCE.
    //   Uses .equals() internally to find the matching object.
    //   EXAM TRAP: store.remove(id) would treat id as an INDEX (position in list), not an id value!
    //   Always remove by object reference, not by the int id value.
    public void delete(int id) {
        Product product = getById(id);  // find (throws ProductNotFoundException if not found)
        store.remove(product);          // remove by OBJECT — NOT store.remove(id)!
    }

    // ─── update(int id, Product updatedData) → modify a product ───────────
    // JPA equivalent: find, modify, repo.save(existing)
    // In-memory: find the object (it's IN the list), modify it directly.
    //   No "save" needed because modifying the object modifies it in the list directly.
    //   The ArrayList holds REFERENCES to objects, not copies.
    public Product update(int id, Product updatedData) {
        Product existing = getById(id);              // find (throws if not found)
        existing.setName(updatedData.getName());     // modify the object in the list
        existing.setPrice(updatedData.getPrice());
        // No save() call — the list already references this object.
        // In JPA, we need save() because Hibernate tracks entities separately.
        // In-memory, the object IS in the list — modifying it is sufficient.
        return existing;
    }
}
