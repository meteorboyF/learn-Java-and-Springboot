// Example: The minimal main application class — the root of every Spring Boot project.
// File location: src/main/java/com/example/app/AppApplication.java

package com.example.app;  // package declaration must match the folder path exactly
// com/example/app/AppApplication.java → package com.example.app

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// The main class MUST be in the ROOT package (com.example.app).
// Sub-packages (com.example.app.model, com.example.app.service, etc.)
// are automatically scanned by @ComponentScan (part of @SpringBootApplication).
@SpringBootApplication
public class AppApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }
}

// ─── Package naming convention ────────────────────────────────────
// Java package:    com.example.app.model
// Folder path:     src/main/java/com/example/app/model/
// These must match exactly. If they don't, the class won't compile.

// ─── application.properties location ─────────────────────────────
// File:   src/main/resources/application.properties
// Spring Boot reads this file AUTOMATICALLY on startup.
// You never need to load it manually.
// If the file doesn't exist, Spring uses defaults (which may not be correct).
