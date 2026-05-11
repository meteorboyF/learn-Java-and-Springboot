// ============================================================
// ProductService.java — In-Memory Service (No Database)
// This variant stores data in an ArrayList instead of a database.
// Some quiz questions specify "no JPA" — this is the pattern to use.
//
// WHEN IS THIS USED?
//   - Quiz variant: "no database required" or no JPA dependency in pom.xml
//   - Simpler proof-of-concept where data doesn't need to persist
//   - Prototyping before setting up a real database
//   - Understanding the service pattern without DB complexity
//
// HOW IT DIFFERS FROM JPA VARIANT:
//   JPA:        Repository interface → Hibernate → SQL → Database → Persistent storage
//   In-Memory:  ArrayList in service → Java heap → Lost on restart
// ============================================================
package com.example.springbootref.inMemory;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

// Plain class inside the Product to keep everything in one file for this example.
// In a real project, Product would be in its own model/ file.
class Product {
    private int    id;
    private String name;
    private double price;

    // No-arg constructor — needed if you use Jackson or reflection anywhere.
    public Product() {}

    // Parameterised constructor — used when creating products in the service.
    public Product(int id, String name, double price) {
        this.id    = id;
        this.name  = name;
        this.price = price;
    }

    // Getters and setters — Jackson needs these to serialize/deserialize.
    public int    getId()    { return id; }
    public void   setId(int id)       { this.id = id; }
    public String getName()  { return name; }
    public void   setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void   setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', price=" + price + "}";
    }
}

// Custom exception for "not found" — extends RuntimeException (same reason as StudentNotFoundException).
class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);  // passes message to RuntimeException for getMessage()
    }
}

// @Service — Spring manages this class as a singleton bean.
// KEY POINT: Because this is a SINGLETON (one instance per application),
// the ArrayList and AtomicInteger live for the entire application lifetime.
// Every request to this service reads/writes the SAME ArrayList.
// Data is NOT shared between different instances (but there's only one instance).
@Service
public class ProductService {

    // ============================================================
    // IN-MEMORY STORAGE
    // ============================================================
    // An ArrayList as the "database".
    // final — the reference to the list never changes (only its contents change).
    // This list holds ALL products in "memory" (RAM).
    // All CRUD operations read/write this list.
    private final List<Product> store = new ArrayList<>();

    // ============================================================
    // ID COUNTER
    // ============================================================
    // AtomicInteger — a thread-safe auto-incrementing counter.
    // WHY AtomicInteger instead of regular int?
    //   Multiple HTTP requests can arrive simultaneously (concurrent threads).
    //   Regular int++ is NOT thread-safe — two threads could get the same ID.
    //   AtomicInteger.getAndIncrement() is guaranteed to be atomic (one operation).
    //   This simulates what the database's AUTO_INCREMENT does natively.
    //   EXAM: AtomicInteger is the correct choice for thread-safe ID generation.
    // Starts at 1 (not 0) — database IDs typically start at 1.
    private final AtomicInteger idCounter = new AtomicInteger(1);

    // ============================================================
    // CONSTRUCTOR — pre-seed some data
    // ============================================================
    // The constructor runs when Spring creates this bean (at startup).
    // We pre-populate the store with sample products.
    // @Service constructor = no @Autowired needed, no repository needed.
    public ProductService() {
        // getAndIncrement() returns the CURRENT value and THEN increments.
        // First call: returns 1, counter becomes 2.
        // Second call: returns 2, counter becomes 3.
        // This is equivalent to the database's AUTO_INCREMENT behavior.
        store.add(new Product(idCounter.getAndIncrement(), "Laptop",   999.99));
        store.add(new Product(idCounter.getAndIncrement(), "Mouse",     29.99));
        store.add(new Product(idCounter.getAndIncrement(), "Keyboard",  59.99));
        // After this: store has 3 products with ids 1, 2, 3. Counter is at 4.
    }

    // ============================================================
    // getAll() — return all products
    // ============================================================
    // Returns the entire ArrayList — all products in memory.
    // In JPA: return repo.findAll();
    // Here:   return store;
    public List<Product> getAll() {
        return store;  // returns the live list (not a copy) — callers can see all items
    }

    // ============================================================
    // getById(int id) — find one product by id
    // ============================================================
    // Uses Java 8 Stream API to search the ArrayList.
    //
    // store.stream()       — creates a stream of Product objects from the ArrayList
    // .filter(p -> p.getId() == id)  — keeps only products where id matches
    // .findFirst()         — returns Optional<Product> (first match or empty)
    // .orElseThrow(...)    — throw exception if nothing matched
    //
    // This is the functional equivalent of a for-loop:
    //   for (Product p : store) { if (p.getId() == id) return p; }
    //   throw new ProductNotFoundException("not found");
    // The stream version is more concise and composable.
    // EXAM: Know both the stream version and the for-loop version.
    public Product getById(int id) {
        return store.stream()
                .filter(p -> p.getId() == id)   // predicate: keep items where id matches
                .findFirst()                      // Optional<Product>: first match or empty
                .orElseThrow(() ->               // if empty Optional, execute this lambda
                        new ProductNotFoundException("Product not found with id: " + id));
    }

    // ============================================================
    // create(Product product) — add a new product
    // ============================================================
    // The client sends a Product WITHOUT an id (client doesn't know the next id).
    // We assign the next id from the counter, then add to the list.
    // In JPA: return repo.save(product); (JPA assigns id automatically)
    // Here: we manually assign and add.
    public Product create(Product product) {
        // Assign the next available ID from the atomic counter.
        // getAndIncrement(): returns current value (e.g., 4), then increments to 5.
        product.setId(idCounter.getAndIncrement());

        // Add to the ArrayList — this is our "insert" operation.
        store.add(product);

        // Return the product with its newly assigned id.
        return product;
    }

    // ============================================================
    // delete(int id) — remove a product
    // ============================================================
    // Pattern: find first (throws if not found), then remove from list.
    // In JPA: repo.deleteById(id);
    // Here: store.remove(p);
    //
    // store.remove(p) — removes the OBJECT from the ArrayList by reference.
    //   Uses .equals() internally. Works correctly because we're passing the exact object.
    //   Do NOT use store.remove(id) — that removes by INDEX (position), not by id value!
    //   EXAM: store.remove(object) vs store.remove(index) — different behavior!
    public void delete(int id) {
        Product product = getById(id);   // throws ProductNotFoundException if not found
        store.remove(product);           // remove by OBJECT reference (not by index)
    }

    // ============================================================
    // update(int id, Product updatedData) — modify a product
    // ============================================================
    // Pattern: find, modify in-place, done (no "save" needed — it's already in the list).
    // In JPA: entity.setName(...); repo.save(entity); (save triggers UPDATE)
    // Here:   product is already in the list — modifying it directly is sufficient.
    public Product update(int id, Product updatedData) {
        Product existing = getById(id);         // find (throws if not found)
        existing.setName(updatedData.getName()); // modify fields in the list object
        existing.setPrice(updatedData.getPrice());
        return existing;                         // no save() needed — list holds the reference
    }
}
