// H2 Configuration is done entirely in application.properties — no Java code needed.
// This Example.java shows the properties as Java-style comments for easy reference.

// ─── application.properties — H2 In-Memory ───────────────────────────────────
//
// spring.datasource.url=jdbc:h2:mem:testdb
//   'mem:' = in-memory mode. 'testdb' = the database name (any name is fine).
//
// spring.datasource.driver-class-name=org.h2.Driver
//   The JDBC driver. Spring can auto-detect this, but explicit is clearer.
//
// spring.datasource.username=sa
//   H2's default System Administrator user. Always 'sa' for H2.
//
// spring.datasource.password=
//   Empty password. NOT null. NOT "null". Just nothing after the =.
//   EXAM: This exact syntax (empty) is required.
//
// spring.jpa.hibernate.ddl-auto=update
//   'update' = create missing tables, never drop existing data.
//   EXAM: 'create' drops all data every restart. Never use in production.
//
// spring.jpa.show-sql=true
//   Prints every SQL statement to the console. Invaluable for debugging.
//
// spring.h2.console.enabled=true
//   Opens a browser SQL UI at http://localhost:8080/h2-console
//   EXAM: Disabled by default. Must explicitly enable.
//
// spring.h2.console.path=/h2-console
//   URL path for the console. /h2-console is also the default.

// ─── application.properties — H2 File-Based ──────────────────────────────────
//
// spring.datasource.url=jdbc:h2:file:./data/schooldb
//   'file:' = file-based. './data/schooldb' = relative path.
//   Creates schooldb.mv.db in the ./data/ directory.
//   Data persists between app restarts.

// ─── Accessing H2 Console from Java (for reference) ─────────────────────────
// No Java code needed. Just visit:
//   http://localhost:8080/h2-console
//   JDBC URL: jdbc:h2:mem:testdb (must match spring.datasource.url)
//   Username: sa
//   Password: (empty)

public class H2ConfigReference {
    // This class is intentionally empty — H2 config is in application.properties only.
    // You never configure H2 programmatically in normal Spring Boot usage.
}
