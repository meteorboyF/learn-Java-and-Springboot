# Explanation — Problem 03: JPA Entity with Constraints

## Every Annotation Decision

### `@Entity` (line ~12)
- **What it does:** Registers this class with Hibernate. Without it, Hibernate ignores the class entirely.
- **Where it goes:** Always on the class declaration, before `@Table`.
- **Exam trap:** People sometimes put `@Entity` on methods or fields — it only works on the class.

### `@Table(name = "students")` (line ~17)
- **What it does:** Names the DB table `students`. Without it, Hibernate uses the class name as the table name: `Student` class → `student` table (lowercase).
- **When to use it:** Always, when the question specifies the table name. Even if the default matches, being explicit prevents surprises.

### `@Id` (line ~25)
- **What it does:** Designates this field as the primary key column. Every `@Entity` must have exactly one `@Id`.
- **What happens if missing:** `javax.persistence.PersistenceException: No identifier specified for entity: Student`

### `@GeneratedValue(strategy = GenerationType.IDENTITY)` (line ~31)
- **What it does:** Tells JPA "the database will assign the id value; don't try to set it."
- **How H2/MySQL implement IDENTITY:** They use an auto-increment sequence. The first insert gets id=1, next gets id=2, etc.
- **Other strategies (know these for exams):**
  - `AUTO` — JPA picks the best strategy (can behave differently across DB engines)
  - `SEQUENCE` — uses a database sequence object (common in PostgreSQL)
  - `TABLE` — uses a dedicated table to track IDs (slow, rarely used)
- **EXAM:** `IDENTITY` is the most commonly tested strategy.

### `Long id` vs `long id` (line ~37)
- **Why `Long` (wrapper)?** New entities have `id = null` before they are saved to the database. Primitive `long` cannot hold `null` — it defaults to `0`, which could be mistaken for a valid ID.
- **Rule:** Always use wrapper types (`Long`, `Integer`) for `@Id` fields.

### `@Column(nullable = false, length = 80)` for `name` (line ~41)
- **`nullable = false`:** Adds a `NOT NULL` constraint. Attempting to insert a null name will cause a database error.
- **`length = 80`:** The column type becomes `VARCHAR(80)`. Without this, it defaults to `VARCHAR(255)`.
- **Note:** `nullable` is different from Java's `@NonNull` or Bean Validation's `@NotNull` — those validate at the Java level, while `nullable = false` adds a DB-level constraint.

### `@Column(name = "student_id", unique = true, nullable = false)` (line ~50)
- **`name = "student_id"`:** Renames the DB column. Without it, Hibernate auto-converts `studentId` (camelCase) to `student_id` (snake_case) — same result here, but explicit is safer.
- **`unique = true`:** Creates a UNIQUE INDEX. Two rows cannot have the same `student_id` value. The database enforces this, not just your application code.
- **Combined with `nullable = false`:** The column is both UNIQUE and NOT NULL — a "business key."

### `@Column(precision = 4, scale = 2)` for `gpa` (line ~60)
- **`precision = 4`:** Maximum total digits: e.g., `99.99` (4 digits total).
- **`scale = 2`:** Digits after the decimal point: e.g., `3.75` (2 decimal places).
- **No `nullable = false`:** GPA is optional — null is a valid value meaning "no GPA recorded."
- **`Double` (wrapper):** Can hold null (meaning no GPA), unlike primitive `double`.

### `private String major;` (no annotation, line ~67)
- **What Hibernate uses as defaults:** Column name = `major` (field name), type = `VARCHAR(255)`, nullable = `true`.
- **When no `@Column` is needed:** When you're okay with the defaults (nullable, 255 chars, field name = column name).

## The Two Constructor Rule
1. **No-arg constructor:** Required by JPA for reflection-based instantiation when loading from DB.
2. **Parameterised constructor:** Convenience for creating objects in service/seeder code.

**EXAM:** If you define ONLY the parameterised constructor, Java no longer auto-generates the no-arg one. You must add it explicitly: `public Student() {}`

## Why Getters and Setters Are Required
- **Jackson (JSON library):** Uses getters to serialize objects to JSON responses. No getter = field missing from JSON.
- **Hibernate:** Uses setters to populate fields when loading from the database.
- **Rule of thumb:** Every field needs both a getter and a setter, except `id` (no setter — DB assigns it).
