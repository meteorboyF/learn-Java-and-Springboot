# Primary Key Annotations: @Id and @GeneratedValue

## What Are They?
`@Id` marks a field as the primary key of the entity's database table. `@GeneratedValue` tells JPA how the primary key value is generated (usually by the database automatically).

## @Id Rules
- Every `@Entity` class must have exactly ONE `@Id` field
- The type must be a wrapper type: `Long`, `Integer`, `String`, `UUID`
- NEVER use primitive `long` — new (unsaved) entities need `id = null`
- Missing `@Id` → app crashes: "No identifier specified for entity"

## @GeneratedValue Strategies

| Strategy | How | Use When |
|---|---|---|
| `IDENTITY` | DB auto-increment | MySQL, H2 — EXAM standard |
| `SEQUENCE` | DB sequence | PostgreSQL, Oracle |
| `AUTO` | JPA decides | Avoid — unpredictable |
| `TABLE` | JPA tracking table | Avoid — very slow |

## Standard Pattern for Quizzes
```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

## EXAM: Why `Long` not `long`?
```java
private long id;   // ❌ defaults to 0 — JPA can't tell "new entity" from "id=0 entity"
private Long id;   // ✅ defaults to null — JPA knows: null = new entity, non-null = existing
```
JPA checks `id == null` to decide INSERT vs UPDATE. Primitive `long` can't be null.
