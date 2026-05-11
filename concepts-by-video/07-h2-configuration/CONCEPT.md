# H2 Database Configuration

## What is it?
H2 is a lightweight, embedded Java database. It can run entirely in RAM (in-memory mode) or write to a file (file-based mode). No installation required — it's a JAR dependency.

## Why Does It Exist?
Development and testing need a database, but setting up MySQL/PostgreSQL for every developer and CI server is overhead. H2 gives you a full SQL database in seconds, embedded in your application.

## Two Modes

### In-Memory (default for quizzes)
- Data lives in RAM — lost when app stops
- Zero setup — just add the dependency
- Use for: development, testing, lab quizzes

### File-Based
- Data written to `.mv.db` file on disk
- Persists between restarts
- Use for: development with persistent data, demos

## Required Properties (In-Memory)
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

## H2 Console
After starting the app: visit `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave empty)

## EXAM: Key URL Differences
- `jdbc:h2:mem:testdb` — in-memory
- `jdbc:h2:file:./data/mydb` — file-based (creates `./data/mydb.mv.db`)
- `jdbc:mysql://localhost:3306/mydb` — MySQL (different driver needed)
