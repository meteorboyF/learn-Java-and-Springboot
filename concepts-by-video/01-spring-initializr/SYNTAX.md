# Syntax Reference — Spring Initializr & Project Setup

## pom.xml Minimal Structure

```xml
<project>
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>  <!-- always inherit from this parent -->
    </parent>

    <groupId>com.example</groupId>
    <artifactId>my-app</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <properties>
        <java.version>17</java.version>  <!-- minimum for Spring Boot 3.x -->
    </properties>

    <dependencies>
        <!-- REST API support -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- JPA + Hibernate -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <!-- H2 in-memory database -->
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

## @SpringBootApplication Annotation

```java
@SpringBootApplication                          // all-in-one (preferred)
// is equivalent to:
@Configuration                                  // can define beans
@EnableAutoConfiguration                        // auto-configure based on classpath
@ComponentScan(basePackages = "com.example")   // scan for components
```

## Maven Commands

```bash
mvn spring-boot:run          # run the app (downloads dependencies first)
mvn clean package            # build a runnable JAR
java -jar target/my-app.jar  # run the built JAR
mvn test                     # run tests
```

## Dependency Scope Reference

| Scope | Available at | In JAR? | Example |
|---|---|---|---|
| (default) | compile + runtime | ✅ Yes | spring-boot-starter-web |
| `runtime` | runtime only | ✅ Yes | h2, mysql-connector |
| `test` | test only | ❌ No | spring-boot-starter-test |
| `provided` | compile only | ❌ No | lombok (sometimes) |
