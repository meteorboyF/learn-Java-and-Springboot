# Syntax — H2 Configuration

## In-Memory (EXAM: standard quiz config)
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

## File-Based
```properties
spring.datasource.url=jdbc:h2:file:./data/mydb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
```

## ddl-auto Options
```properties
spring.jpa.hibernate.ddl-auto=none        # do nothing (production)
spring.jpa.hibernate.ddl-auto=validate    # verify schema, crash if wrong
spring.jpa.hibernate.ddl-auto=update      # create missing, keep data ✅ quiz
spring.jpa.hibernate.ddl-auto=create      # drop + recreate (data lost!)
spring.jpa.hibernate.ddl-auto=create-drop # drop on shutdown (tests)
```

## pom.xml Dependency
```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```
