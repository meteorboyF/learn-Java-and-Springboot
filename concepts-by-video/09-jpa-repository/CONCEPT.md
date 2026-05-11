# JPA Repository and Spring Data

## What is it?
A Spring Data repository is a Java INTERFACE that extends `JpaRepository`. Spring auto-generates the implementation — you write method signatures, Spring writes the SQL and the implementation class.

## Why Does It Exist?
Without Spring Data JPA, you'd write JDBC code: `PreparedStatement`, `ResultSet`, SQL strings, connection management. That's hundreds of lines for basic CRUD. With repositories, you write an interface with method names, and Spring handles everything.

## The Hierarchy
```
Repository  (marker interface)
└── CrudRepository<T, ID>  (save, findById, findAll, delete, count)
    └── PagingAndSortingRepository<T, ID>  (+ pagination, sorting)
        └── JpaRepository<T, ID>  (+ flush, batch operations)
                                   ← USE THIS ONE for Spring Boot
```

## What JpaRepository Gives You FREE
```java
// You get ALL of these without writing ANY code:
repo.save(entity)           // INSERT or UPDATE
repo.findById(id)           // SELECT WHERE id=? → Optional<T>
repo.findAll()              // SELECT * → List<T>
repo.findAll(Sort.by("name")) // SELECT * ORDER BY name
repo.deleteById(id)         // DELETE WHERE id=?
repo.existsById(id)         // SELECT COUNT(*) > 0 WHERE id=?
repo.count()                // SELECT COUNT(*)
repo.saveAll(list)          // batch INSERT/UPDATE
```

## Type Parameters
```java
JpaRepository<Student, Long>
//             ↑       ↑
//           Entity   @Id field type
// EXAM: Second type MUST match the @Id field's type in the entity.
// Student has 'private Long id' → use Long (not long, not Integer)
```

## EXAM: @Repository is optional on JpaRepository interfaces
Spring Data automatically detects interfaces extending `JpaRepository`. The `@Repository` annotation is redundant but good practice for clarity.
