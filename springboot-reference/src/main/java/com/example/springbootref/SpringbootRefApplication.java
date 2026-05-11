// Every Spring Boot app has exactly ONE main class with @SpringBootApplication.
// This is the entry point — the class with the main() method that starts everything.
package com.example.springbootref;  // EXAM: package must match the folder path exactly

// @SpringBootApplication is a "mega-annotation" — it combines THREE annotations:
//   1. @Configuration       — this class can define Spring Beans
//   2. @EnableAutoConfiguration — Spring Boot automatically configures beans based on
//                                  what's on the classpath (e.g., sees H2 → sets up datasource)
//   3. @ComponentScan       — Spring scans this package AND all sub-packages for
//                             @Component, @Service, @Repository, @RestController, etc.
// EXAM: Without @SpringBootApplication, Spring has no idea what to configure.
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  // EXAM: this is tested — always present on the main class
public class SpringbootRefApplication {

    // main() is the JVM entry point — called first when you run the app.
    // SpringApplication.run() bootstraps the Spring container:
    //   1. Creates the ApplicationContext (the "box" holding all your beans)
    //   2. Reads application.properties
    //   3. Sets up datasource, JPA, Tomcat, etc. automatically
    //   4. Scans for @Component/@Service/@Repository/@RestController beans
    //   5. Runs all CommandLineRunner beans (like DataSeeder)
    //   6. Starts the embedded Tomcat server on port 8080
    public static void main(String[] args) {
        // SpringbootRefApplication.class — tells Spring which class to use as the
        // "root" for component scanning. Always pass the main class here.
        // args — command-line arguments (e.g., --server.port=9090). Usually ignored.
        SpringApplication.run(SpringbootRefApplication.class, args);
    }
}
