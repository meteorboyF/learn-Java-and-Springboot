// Example: The main application class that Spring Initializr generates.
// This is the ONLY Java file Initializr creates — everything else you write yourself.

// Package matches the Group + Artifact you chose in Initializr.
// Group: com.example, Artifact: my-app → package: com.example.myapp
package com.example.myapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication — the magic annotation that starts everything.
// It combines:
//   @Configuration       — this class can define Spring beans
//   @EnableAutoConfiguration — Spring Boot reads your dependencies and auto-configures
//   @ComponentScan       — Spring scans this package and sub-packages for beans
// EXAM: This annotation is on the MAIN CLASS only. There is exactly ONE per project.
@SpringBootApplication
public class MyAppApplication {  // class name = Artifact name + "Application"

    // main() — the JVM entry point. This is what runs when you execute the JAR.
    public static void main(String[] args) {
        // SpringApplication.run() bootstraps the entire Spring container.
        // Steps:
        //   1. Creates the ApplicationContext
        //   2. Reads application.properties
        //   3. Auto-configures based on classpath (H2, JPA, Tomcat...)
        //   4. Scans for @Component/@Service/@Repository/@RestController beans
        //   5. Runs CommandLineRunner beans
        //   6. Starts embedded Tomcat on port 8080
        SpringApplication.run(MyAppApplication.class, args);
        // MyAppApplication.class = the root class for component scanning
        // args = command-line arguments (e.g., --server.port=9090)
    }
}
