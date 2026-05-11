# Common Mistakes — Project Structure

## Mistake 1: Package name doesn't match folder path
**Wrong:** File is at `src/main/java/com/example/app/Student.java` but has `package com.example.model;`
**Fix:** Always make the package declaration match the folder path exactly.

## Mistake 2: Main class not in the root package
**Wrong:** Main class in `com.example.app.main.AppApplication` but entities in `com.example.app.model`.
**Why breaks:** `@ComponentScan` only scans the main class's package and sub-packages. If entities aren't in sub-packages of the main class's package, they won't be found.
**Fix:** Keep the main class at the root: `com.example.app.AppApplication`.

## Mistake 3: Putting application.properties in the wrong location
**Wrong:** `src/application.properties` or `application.properties` (project root)
**Fix:** Must be at `src/main/resources/application.properties` — this is the ONLY location Spring Boot reads automatically.
