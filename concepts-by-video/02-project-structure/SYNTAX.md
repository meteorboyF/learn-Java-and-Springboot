# Syntax Reference — Project Structure

## Package Declaration Rules
```java
// Folder: src/main/java/com/example/app/model/Student.java
package com.example.app.model;  // must match folder path exactly

// Folder: src/main/java/com/example/app/controller/StudentController.java
package com.example.app.controller;
```

## Standard Package Names (follow these conventions)
| Package | Contents | Annotation |
|---|---|---|
| `com.example.app` | Main class only | `@SpringBootApplication` |
| `com.example.app.model` | Entity classes | `@Entity` |
| `com.example.app.repository` | Repository interfaces | `@Repository` |
| `com.example.app.service` | Business logic | `@Service` |
| `com.example.app.controller` | REST controllers | `@RestController` |
| `com.example.app.exception` | Custom exceptions | *(none)* |
| `com.example.app.dto` | DTOs | *(none)* |
| `com.example.app.handler` | Exception handlers | `@RestControllerAdvice` |
| `com.example.app.seeder` | Data seeders | `@Component` |

## Key File Locations
```
pom.xml                                    ← project root (Maven config)
src/main/java/.../AppApplication.java      ← main entry point
src/main/resources/application.properties  ← configuration (auto-loaded)
src/test/java/.../AppApplicationTests.java ← unit/integration tests
```
