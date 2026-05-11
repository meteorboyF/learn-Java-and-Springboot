# Problem 01 — H2 In-Memory Database Configuration

**Source:** CSE 2218 Advanced OOP Lab — Section 1, Question 1

## The Question

You are given a blank Spring Boot project with `spring-boot-starter-data-jpa` and the H2 dependency. Configure `application.properties` so that:

a) The database runs **in memory** (data is lost on restart)  
b) Hibernate **creates / updates tables automatically**  
c) **SQL statements are printed** to the console  
d) The **H2 browser console** is accessible at `/h2-console`

## What You Need to Know Before Solving

- The file must be at `src/main/resources/application.properties`
- You do NOT need to write any Java code for this problem — only the properties file
- H2 is an embedded database — no separate server installation needed
- The JDBC URL format for in-memory H2 is: `jdbc:h2:mem:DBNAME`

## Exam Tips

- The default H2 username is `sa` and password is **empty string** (not "null")
- `ddl-auto=update` is the safe choice: creates missing tables, never drops data
- `ddl-auto=create` DESTROYS data every restart — never use in production
- `show-sql=true` prints every SQL statement Hibernate generates

## Dependencies Required in pom.xml

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```
