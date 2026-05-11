# Syntax — JPA Repository

## Interface Declaration
```java
@Repository  // optional but recommended
public interface StudentRepository extends JpaRepository<Student, Long> { }
//                                                        ↑       ↑
//                                                     Entity   @Id type
```

## Derived Query Keywords (EXAM: memorise the pattern)
```java
findBy<Field>                    // WHERE field = ?
findBy<Field>Is / Equals         // WHERE field = ? (explicit form)
findBy<Field>Not                 // WHERE field != ?
findBy<Field>Like                // WHERE field LIKE ?
findBy<Field>Containing          // WHERE field LIKE '%?%'
findBy<Field>StartingWith        // WHERE field LIKE '?%'
findBy<Field>EndingWith          // WHERE field LIKE '%?'
findBy<Field>IgnoreCase          // WHERE LOWER(field) = LOWER(?)
findBy<Field>LessThan            // WHERE field < ?
findBy<Field>LessThanEqual       // WHERE field <= ?
findBy<Field>GreaterThan         // WHERE field > ?
findBy<Field>GreaterThanEqual    // WHERE field >= ?
findBy<Field>Between             // WHERE field BETWEEN ? AND ?
findBy<Field>IsNull              // WHERE field IS NULL
findBy<Field>IsNotNull           // WHERE field IS NOT NULL
findBy<A>And<B>                  // WHERE a = ? AND b = ?
findBy<A>Or<B>                   // WHERE a = ? OR b = ?
findBy<Field>OrderBy<Other>Desc  // ... ORDER BY other DESC
```

## @Query Examples
```java
// JPQL (class/field names):
@Query("SELECT s FROM Student s WHERE s.major = :major")
List<Student> findByMajorJpql(@Param("major") String major);

// Native SQL (table/column names):
@Query(value = "SELECT * FROM students WHERE major = ?1", nativeQuery = true)
List<Student> findByMajorNative(String major);
// ?1 = positional parameter (first argument)
// :major = named parameter (use with @Param)
```
