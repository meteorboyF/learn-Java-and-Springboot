# Syntax — @Id and @GeneratedValue

```java
// Minimum required:
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

// All GenerationType options:
GenerationType.IDENTITY   // DB auto-increment (MySQL AUTO_INCREMENT, H2)
GenerationType.SEQUENCE   // DB sequence object (PostgreSQL)
GenerationType.AUTO       // JPA chooses (avoid)
GenerationType.TABLE      // tracking table (avoid — slow)
GenerationType.UUID       // UUID (Java 17+, Hibernate 6+)

// With custom sequence (PostgreSQL):
@SequenceGenerator(name = "gen", sequenceName = "my_seq", allocationSize = 1)
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
private Long id;
```

## ID Type Options
| Java Type | DB Type | Notes |
|---|---|---|
| `Long` | `BIGINT` | Most common — use this |
| `Integer` | `INT` | Use if max ~2 billion IDs is enough |
| `String` | `VARCHAR` | For business keys |
| `UUID` | `UUID`/`CHAR(36)` | Distributed systems |
