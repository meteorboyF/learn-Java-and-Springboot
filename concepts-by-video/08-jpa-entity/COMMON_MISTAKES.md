# Common Mistakes — JPA Entity

## Mistake 1: Missing the no-arg constructor
**Wrong:**
```java
@Entity
public class Student {
    public Student(String name, String studentId) { ... }
    // Missing: public Student() {}
}
```
**Error:** `javax.persistence.PersistenceException: No default constructor for entity: Student`
**Fix:** Always add `public Student() {}` whenever you define any other constructor.

---

## Mistake 2: Using primitive `long` instead of wrapper `Long` for the @Id
**Wrong:**
```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long id;  // primitive — default value is 0, not null
```
**Problem:** JPA checks `id == null` to decide INSERT vs UPDATE. Primitive `long` defaults to `0` (not null) — Spring might incorrectly treat new entities as existing ones with id=0.

**Fix:** Always use wrapper type: `private Long id;` (capital L).

---

## Mistake 3: Forgetting `@Entity` or `@Table`
**Wrong:**
```java
@Table(name = "students")  // @Table alone does nothing without @Entity!
public class Student { ... }
```
**Error:** Spring JPA doesn't recognise `Student` as an entity — it tries to scan it but can't map it to a table. Repository won't work.

**Fix:** `@Entity` is REQUIRED. `@Table` is optional (but recommended). Both go above the class declaration.
