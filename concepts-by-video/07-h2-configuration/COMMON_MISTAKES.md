# Common Mistakes — H2 Configuration

## Mistake 1: Setting password=null instead of leaving it empty
**Wrong:** `spring.datasource.password=null`
**Problem:** H2 has no user with password "null" — connection refused.
**Fix:** `spring.datasource.password=` (empty value after the equals sign)

## Mistake 2: Using ddl-auto=create in persistent storage
**Wrong:** `spring.jpa.hibernate.ddl-auto=create` with H2 file-based or MySQL
**Problem:** Every restart drops ALL tables and data. All your seeded data is gone on every restart. With a real app: catastrophic data loss.
**Fix:** Use `ddl-auto=update` for development and `ddl-auto=none` for production.

## Mistake 3: Forgetting to enable the H2 console
**Wrong:** Trying to access `http://localhost:8080/h2-console` without enabling it.
**Problem:** Spring returns 404 for /h2-console because the console is disabled by default.
**Fix:** Add `spring.h2.console.enabled=true` to application.properties.

## Mistake 4: Wrong JDBC URL in the H2 console login
**Wrong:** Entering `jdbc:h2:mem:mydb` when properties says `jdbc:h2:mem:testdb`
**Problem:** Connecting to a different (empty) database — your tables won't be there.
**Fix:** The JDBC URL in the console login must exactly match `spring.datasource.url` in properties.
