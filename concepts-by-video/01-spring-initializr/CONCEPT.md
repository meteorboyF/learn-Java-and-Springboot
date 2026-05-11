# Spring Initializr

## What is it?
Spring Initializr (`start.spring.io`) is a web tool that generates a ready-to-run Spring Boot project structure. It creates the `pom.xml`, the main application class, and the folder structure — so you start with a working project, not a blank file.

## Why Does It Exist?
Setting up a Spring project from scratch requires correctly configuring Maven/Gradle, choosing compatible dependency versions, and creating the right folder structure. This is error-prone and takes time. Initializr automates all of this in 30 seconds.

## When to Use It
- Every time you start a new Spring Boot project
- In the exam: if asked to describe project setup, describe Initializr
- In the lab: the starting point for every assignment

## How to Use It
1. Go to `https://start.spring.io`
2. Choose:
   - **Project:** Maven
   - **Language:** Java
   - **Spring Boot version:** Latest stable (e.g., 3.2.x)
   - **Group:** `com.example`
   - **Artifact:** `my-app`
   - **Java version:** 17 (minimum for Spring Boot 3.x)
3. Add **Dependencies:**
   - `Spring Web` (for REST API)
   - `Spring Data JPA` (for database)
   - `H2 Database` (for in-memory DB)
4. Click **Generate** → downloads a ZIP
5. Unzip and open in IntelliJ or VS Code

## What Gets Generated
```
my-app/
├── pom.xml                          ← Maven config with your chosen dependencies
├── src/
│   ├── main/
│   │   ├── java/com/example/myapp/
│   │   │   └── MyAppApplication.java  ← main class with @SpringBootApplication
│   │   └── resources/
│   │       └── application.properties  ← empty config file (you fill this in)
│   └── test/
│       └── java/com/example/myapp/
│           └── MyAppApplicationTests.java
```
