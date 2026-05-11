# H2 Storage Variants: In-Memory vs File-Based

## The Three Storage Methods (EXAM: know all three)

### Method 1: H2 In-Memory
```properties
spring.datasource.url=jdbc:h2:mem:testdb
```
- Data lives in RAM
- **Lost when app stops**
- No files created on disk
- Perfect for: quizzes, tests, quick demos

### Method 2: H2 File-Based
```properties
spring.datasource.url=jdbc:h2:file:./data/mydb
```
- Data saved to `./data/mydb.mv.db` file on disk
- **Survives restarts**
- No external database server needed
- Perfect for: development with persistent data

### Method 3: MySQL/PostgreSQL
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```
- Real production database server
- **Survives restarts, scales, production-ready**
- Requires running database server
- Requires extra pom.xml dependency

## EXAM: The Golden Rule
**Your @Entity, @Repository, @Service, @Controller code is IDENTICAL for all three.**
**Only `application.properties` changes.**

## Comparison Table
| | In-Memory | File-Based | MySQL |
|---|---|---|---|
| URL prefix | `jdbc:h2:mem:` | `jdbc:h2:file:` | `jdbc:mysql://` |
| Data persists? | ❌ No | ✅ Yes | ✅ Yes |
| External server? | ❌ No | ❌ No | ✅ Yes |
| Duplicate guard needed? | Low risk | ✅ Essential | ✅ Essential |
| H2 console works? | ✅ Yes | ✅ Yes | ❌ No |
