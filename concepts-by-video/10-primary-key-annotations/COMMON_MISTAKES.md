# Common Mistakes — @Id and @GeneratedValue

## Mistake 1: Using primitive `long` instead of wrapper `Long`
**Wrong:** `private long id;` (defaults to 0, not null)
**Problem:** JPA checks `id == null` to determine INSERT vs UPDATE. Primitive `long` defaults to `0` — Spring thinks every new entity already has id=0 and tries to UPDATE instead of INSERT.
**Fix:** `private Long id;` (wrapper type, defaults to null)

## Mistake 2: Missing @GeneratedValue — having to set id manually
**Wrong:**
```java
@Id
private Long id;  // no @GeneratedValue
```
**Problem:** You must set the id manually before saving. If you forget, you get a constraint violation (null primary key).
**Fix:** Add `@GeneratedValue(strategy = GenerationType.IDENTITY)` to let the DB assign ids.

## Mistake 3: Using @GeneratedValue.AUTO with H2 — unexpected sequence table created
**Wrong:** `@GeneratedValue(strategy = GenerationType.AUTO)` with H2
**Problem:** JPA might create a `hibernate_sequence` table and use it instead of IDENTITY — unexpected behavior, IDs may not start at 1.
**Fix:** Use `GenerationType.IDENTITY` explicitly for H2 and MySQL.
