# Problem 03 — Create a JPA Entity with Constraints

**Source:** CSE 2218 Advanced OOP Lab — Section 2, Question 3

## The Question

Create a `Student` entity mapped to a table named `students` with these requirements:

- Auto-generated primary key (`Long`)
- `name` column: NOT NULL, max length 80
- `studentId` column: unique, NOT NULL (stores the university student number)
- `gpa` column: nullable, stored as decimal with precision
- `major` column: optional (nullable)
- Provide a **no-arg constructor** AND a **parameterised constructor**
- Include getters and setters for all fields

## What You Need to Know Before Solving

- `@Entity` maps the class to a DB table
- `@Table(name="students")` sets the exact table name
- `@Id` marks the primary key field
- `@GeneratedValue(strategy=GenerationType.IDENTITY)` enables auto-increment
- `@Column` attributes: `nullable`, `length`, `unique`, `name`, `precision`, `scale`
- The no-arg constructor is **required by JPA** — forgetting it crashes the app

## Exam Tips

- Use `Long` (capital L) not `long` (primitive) for the `@Id` field — new entities have `id = null`
- `nullable = false` in `@Column` = NOT NULL constraint in the database
- `unique = true` in `@Column` = UNIQUE index in the database
- `precision = 4, scale = 2` stores values like `3.75` (4 total digits, 2 after decimal)
- Fields without `@Column` are still mapped — they just use Hibernate's defaults (nullable VARCHAR(255))
