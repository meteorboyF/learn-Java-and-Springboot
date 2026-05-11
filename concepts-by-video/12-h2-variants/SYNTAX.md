# Syntax — H2 Variants

## In-Memory
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
```

## File-Based
```properties
spring.datasource.url=jdbc:h2:file:./data/mydb
# Creates: ./data/mydb.mv.db
# H2 console login URL: jdbc:h2:file:./data/mydb (same as datasource URL)
```

## MySQL
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mydb?serverTimezone=UTC
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=root
```

## URL Format Summary
```
jdbc:h2:mem:DBNAME         in-memory H2
jdbc:h2:file:PATH/DBNAME   file-based H2 (creates PATH/DBNAME.mv.db)
jdbc:mysql://HOST:PORT/DB  MySQL
jdbc:postgresql://HOST:PORT/DB  PostgreSQL
```
