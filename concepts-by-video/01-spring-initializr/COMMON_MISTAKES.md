# Common Mistakes — Spring Initializr

## Mistake 1: Choosing the wrong Java version

**Wrong:** Selecting Java 8 or Java 11 when using Spring Boot 3.x.

**Why it breaks:** Spring Boot 3.x requires Java 17 minimum. The app won't compile.

**Fix:** In Initializr, select Java 17 or higher. Verify your installed JDK matches:
```bash
java -version  # should say 17 or higher
```
In `pom.xml`: `<java.version>17</java.version>`

---

## Mistake 2: Forgetting to add required dependencies

**Wrong:** Adding `spring-boot-starter-web` but NOT adding `H2` — then trying to use JPA.

**Why it breaks:** Without the H2 dependency, Spring can't connect to any database — JPA configuration fails with `DataSource` errors.

**Fix:** For a quiz/lab setup, always add ALL THREE:
- `Spring Web` (REST endpoints)
- `Spring Data JPA` (entity/repository support)
- `H2 Database` (the in-memory database)

---

## Mistake 3: Wrong package structure — putting files in the wrong package

**Wrong:** Main class is at `com.example.myapp.MyAppApplication` but you put your entity at `com.other.Student` (completely different package).

**Why it breaks:** `@SpringBootApplication` scans the package where the main class lives and all sub-packages. Classes outside this package hierarchy are NOT discovered — your `@Service`, `@Repository`, `@RestController` beans won't be found.

**Fix:** Always put all your classes INSIDE the same top-level package as the main class. Example:
```
com.example.myapp.MyAppApplication     ← main class
com.example.myapp.model.Student        ← ✅ found (sub-package)
com.example.myapp.service.StudentService ← ✅ found (sub-package)
com.other.Student                       ← ❌ NOT found (different root package)
```
