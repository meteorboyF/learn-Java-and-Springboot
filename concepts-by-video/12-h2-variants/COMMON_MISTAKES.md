# Common Mistakes — H2 Variants

## Mistake 1: H2 console URL doesn't match datasource URL
**Wrong setup:**
```properties
spring.datasource.url=jdbc:h2:mem:testdb
```
Then in the console login, entering: `jdbc:h2:mem:mydb` (different name)
**Problem:** Connects to a different (empty) H2 database — your tables aren't there.
**Fix:** The console JDBC URL must exactly match `spring.datasource.url`.

## Mistake 2: Forgetting to add mysql-connector-j dependency for MySQL
**Problem:** Switching to MySQL URL without adding the driver → `ClassNotFoundException: com.mysql.cj.jdbc.Driver`
**Fix:** Add to pom.xml:
```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

## Mistake 3: Not creating the MySQL database before starting the app
**Wrong:** Switching to MySQL URL `jdbc:mysql://localhost:3306/school` without creating the `school` database.
**Problem:** Spring can't connect — database doesn't exist.
**Fix:** In MySQL first: `CREATE DATABASE school;` Then start the app.
