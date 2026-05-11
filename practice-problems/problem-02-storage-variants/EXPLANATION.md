# Explanation — Problem 02: Storage Variants

## The Central Insight

**Your Java code (Entity, Repository, Service, Controller) is IDENTICAL for all three storage variants. Only `application.properties` changes.**

This is the power of Spring Data JPA: it abstracts the database behind a common interface. You swap databases by changing configuration, not code.

## Comparison Table

| Property | H2 In-Memory | H2 File-Based | MySQL |
|---|---|---|---|
| `datasource.url` | `jdbc:h2:mem:testdb` | `jdbc:h2:file:./data/schooldb` | `jdbc:mysql://localhost:3306/school` |
| `driver-class-name` | `org.h2.Driver` | `org.h2.Driver` | `com.mysql.cj.jdbc.Driver` |
| `username` | `sa` | `sa` | `root` |
| `password` | *(empty)* | *(empty)* | `root` |
| Data persists? | ❌ No | ✅ Yes | ✅ Yes |
| External server? | ❌ No | ❌ No | ✅ Yes |
| pom.xml addition | `h2` | `h2` | `mysql-connector-j` |
| H2 console works? | ✅ Yes | ✅ Yes (different URL) | ❌ No |

## Key Decisions Explained

### Why `?serverTimezone=UTC` in MySQL URL?
Without it, MySQL Connector/J may throw `MysqlDataTruncation` or timezone warnings on startup. UTC is a safe universal timezone that avoids local timezone conflicts.

### Why `file:./data/schooldb` not `file:/absolute/path`?
Relative paths (`./`) are portable — they work on any developer's machine regardless of where the project is installed. Absolute paths like `C:\Users\John\...` would break on every other machine.

### Why is `ddl-auto=update` important for file-based/MySQL?
With in-memory H2, the database is always empty on startup (nothing to "update"). With file-based or MySQL, data persists. If you used `ddl-auto=create`, every restart would **DROP** your tables and recreate them — losing all data. `update` only adds what's missing.

### What does `mysql-connector-j` do?
It's the JDBC driver — the library that knows how to talk to a MySQL server using the MySQL wire protocol. Without it, Spring cannot connect to MySQL at all. H2 is bundled with its own driver; MySQL needs an external one.

## What Happens When You Switch

1. Change `application.properties` only (shown above)
2. Restart the app
3. Spring reads the new URL, loads the correct driver, connects to the new database
4. Hibernate creates the `students` table in the new database (on first run)
5. All your API endpoints work exactly the same

**The `@Entity`, `@Repository`, `@Service`, `@RestController` classes: zero changes.**
