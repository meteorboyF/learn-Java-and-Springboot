# JPA Entity

## What is it?
A JPA Entity is a Java class annotated with `@Entity` that maps directly to a database table. Each instance of the class represents one row in the table. Hibernate (the JPA implementation) reads the annotations and manages the database schema and queries.

## Why Does It Exist?
Without JPA, you'd write raw SQL for every database operation. With JPA entities, you write Java objects — Hibernate translates them to SQL automatically. You stop thinking in "rows and columns" and think in "objects and fields."

## When to Use It
Every class that represents data you want to store in the database needs `@Entity`. Student, Course, Product, Order — all become entities.

## Required Annotations
```java
@Entity                          // tells Hibernate this is a managed entity
@Table(name = "students")        // names the DB table
public class Student {
    @Id                          // primary key field
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // auto-increment
    private Long id;
    
    // ... other fields
    
    public Student() {}          // REQUIRED by JPA — no-arg constructor
}
```

## Field Mapping Rules
| Java Field Type | DB Column Type | Notes |
|---|---|---|
| `String` | `VARCHAR(255)` | Default length 255 |
| `Long`/`Integer` | `BIGINT`/`INT` | |
| `Double`/`Float` | `DOUBLE`/`FLOAT` | |
| `Boolean` | `BOOLEAN`/`TINYINT` | |
| `LocalDate` | `DATE` | |
| `LocalDateTime` | `TIMESTAMP` | |

## EXAM: The Two Constructor Rule
Every `@Entity` MUST have:
1. A **no-arg constructor** (required by JPA for reflection)
2. A **parameterised constructor** (convenience for creating objects in code)

If you only define the parameterised constructor, Java removes the auto-generated no-arg one. You must add `public Student() {}` explicitly.
