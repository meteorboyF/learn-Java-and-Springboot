# Explanation — Problem 01: H2 In-Memory Configuration

## Line-by-Line Decisions

### Line: `spring.datasource.url=jdbc:h2:mem:testdb`
- **Why `mem:`?** The keyword `mem:` tells H2 to store the database entirely in RAM. There is no file written to disk. When the JVM stops, all data vanishes.
- **Why `testdb`?** Just a name for the in-memory database instance. You could call it anything (`mydb`, `schooldb`, etc.) — the name doesn't matter as long as it's consistent.
- **EXAM:** `mem:` = in-memory. `file:` = persistent to disk.

### Line: `spring.datasource.driver-class-name=org.h2.Driver`
- **Why explicit?** Spring Boot can usually auto-detect the driver from the URL. However, being explicit makes the intent clear and avoids potential startup issues.
- **EXAM:** The H2 driver class is always `org.h2.Driver`.

### Line: `spring.datasource.username=sa`
- **Why `sa`?** "sa" stands for System Administrator — it is H2's built-in default superuser account. All H2 databases start with this user.

### Line: `spring.datasource.password=` (empty)
- **Why empty?** H2's default password is an empty string — literally nothing after the `=`. Writing `password=null` would make the password the string "null", which is wrong.
- **EXAM:** Empty password is correct for H2. This trips up many students.

### Line: `spring.jpa.hibernate.ddl-auto=update`
- **Why `update` and not `create`?**
  - `create` drops and recreates tables on every startup — you lose all data on every restart.
  - `update` only creates what's missing and leaves existing data alone.
  - For the exam, `update` is always the safe choice unless the question explicitly says "fresh database every time."
- **The full list of options:**
  | Value | What it does |
  |---|---|
  | `none` | Don't touch the schema |
  | `validate` | Check schema matches entities; crash if not |
  | `update` | Create missing tables/columns; leave data alone ✅ |
  | `create` | Drop and recreate everything; all data lost |
  | `create-drop` | Like `create`, but also drop tables when app stops |

### Line: `spring.jpa.show-sql=true`
- **Why?** Invaluable for debugging. Without this you're blind to what SQL Hibernate is running.
- When a query is slow or returning wrong results, `show-sql=true` is the first debugging tool.

### Line: `spring.jpa.properties.hibernate.format_sql=true`
- **Why?** Without this, `show-sql` prints one long ugly line. With it, SQL is pretty-printed across multiple lines with proper indentation.

### Line: `spring.h2.console.enabled=true`
- **Why?** The H2 console is a browser-based SQL client. After starting your app, visit `http://localhost:8080/h2-console`, enter the JDBC URL (`jdbc:h2:mem:testdb`), username (`sa`), and empty password to inspect the database visually.
- **EXAM:** The console is NOT enabled by default. You must explicitly set `enabled=true`.

### Line: `spring.h2.console.path=/h2-console`
- **Why specify it?** It's already the default, but being explicit avoids confusion.

## Common Mistakes

1. **`password=null`** — This sets the password to the string "null". H2 has no such user. Use `password=` (empty).
2. **`ddl-auto=create`** — Destroys data every restart. Use `update` instead.
3. **Forgetting `spring.h2.console.enabled=true`** — The H2 console is disabled by default.
4. **Wrong JDBC URL in the H2 console login** — Must match exactly: `jdbc:h2:mem:testdb` (same name as in properties).

## How to Test Your Solution

1. Start the app (`mvn spring-boot:run`).
2. Open `http://localhost:8080/h2-console`.
3. Enter JDBC URL: `jdbc:h2:mem:testdb`, username: `sa`, password: (leave empty).
4. Click Connect → you should see your entity tables in the left panel.
5. Run `SELECT * FROM STUDENTS;` — should return the seeded data.
