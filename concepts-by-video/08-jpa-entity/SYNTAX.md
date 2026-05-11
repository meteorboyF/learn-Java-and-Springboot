# Syntax Reference — JPA Entity Annotations

## @Entity
```java
@Entity  // required — no attributes
public class Student { ... }
```

## @Table
```java
@Table(name = "students")                      // set table name
@Table(name = "students", schema = "public")   // with schema (PostgreSQL)
```

## @Id + @GeneratedValue
```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)   // DB auto-increment (MySQL, H2)
@GeneratedValue(strategy = GenerationType.SEQUENCE)   // DB sequence (PostgreSQL preferred)
@GeneratedValue(strategy = GenerationType.AUTO)       // JPA picks best (unpredictable)
@GeneratedValue(strategy = GenerationType.TABLE)      // table-based (slow, avoid)
private Long id;  // must be wrapper type (Long, Integer), NOT primitive
```

## @Column — All Attributes
```java
@Column(
    name = "student_id",  // DB column name (default = field name in snake_case)
    nullable = false,     // NOT NULL constraint (default = true = nullable)
    unique = true,        // UNIQUE INDEX (default = false)
    length = 80,          // VARCHAR length (default = 255, only for String)
    precision = 4,        // total digits for decimal (BigDecimal, Double)
    scale = 2,            // digits after decimal (BigDecimal, Double)
    insertable = true,    // include in INSERT (default = true)
    updatable = true,     // include in UPDATE (default = true)
    columnDefinition = "TEXT"  // raw SQL type definition (override all)
)
private String name;
```

## Primitive vs Wrapper Types in Entities
| Field | Use | Why |
|---|---|---|
| Primary key | `Long id` | Must allow null for new (unsaved) entities |
| Optional numeric | `Double gpa` | null = "not provided" |
| Required numeric | `int credits` | OK if always provided and never null |
| All String fields | `String name` | Already nullable |

## No-Arg Constructor (REQUIRED)
```java
// ALWAYS include this — JPA requires it:
public Student() {}

// Combined with parameterised constructor:
public Student(String name, String studentId) { ... }
```
