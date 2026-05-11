// H2 Variants — all configuration is in application.properties, not Java code.
// This file demonstrates the three variants as code comments for clarity.

public class H2VariantsReference {

    // ─── VARIANT 1: H2 In-Memory ─────────────────────────────────────────────
    // application.properties:
    //   spring.datasource.url=jdbc:h2:mem:testdb
    //   spring.datasource.driver-class-name=org.h2.Driver
    //   spring.datasource.username=sa
    //   spring.datasource.password=
    //   spring.jpa.hibernate.ddl-auto=update
    //   spring.jpa.show-sql=true
    //   spring.h2.console.enabled=true
    //
    // When to use: standard quiz setup, unit tests
    // Data: gone when app stops (every restart = empty DB)
    // Console: http://localhost:8080/h2-console, URL=jdbc:h2:mem:testdb

    // ─── VARIANT 2: H2 File-Based ─────────────────────────────────────────────
    // application.properties:
    //   spring.datasource.url=jdbc:h2:file:./data/schooldb
    //   spring.datasource.driver-class-name=org.h2.Driver
    //   spring.datasource.username=sa
    //   spring.datasource.password=
    //   spring.jpa.hibernate.ddl-auto=update
    //   spring.h2.console.enabled=true
    //
    // When to use: dev with data persistence, no MySQL server available
    // Data: saved to ./data/schooldb.mv.db, survives restarts
    // Console: URL=jdbc:h2:file:./data/schooldb (must match datasource URL!)

    // ─── VARIANT 3: MySQL ─────────────────────────────────────────────────────
    // application.properties:
    //   spring.datasource.url=jdbc:mysql://localhost:3306/school?serverTimezone=UTC
    //   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    //   spring.datasource.username=root
    //   spring.datasource.password=root
    //   spring.jpa.hibernate.ddl-auto=update
    //
    // pom.xml addition required:
    //   <dependency>
    //       <groupId>com.mysql</groupId>
    //       <artifactId>mysql-connector-j</artifactId>
    //       <scope>runtime</scope>
    //   </dependency>
    //
    // Pre-requisite: CREATE DATABASE school; (in MySQL first)
    // When to use: production, real-DB quiz variants

    // KEY INSIGHT: @Entity, @Repository, @Service, @Controller — NO CHANGES for any variant.
}
