# Problem 02 — H2 File-Based vs MySQL Configuration

**Source:** CSE 2218 Advanced OOP Lab — Section 1, Question 2

## The Question

Your team needs to switch from an in-memory H2 database to:

**Variant A:** H2 File-Based (data persists between restarts, still no separate DB server)

**Variant B:** MySQL running on `localhost:3306`, database name `school`, user `root`, password `root`

Write both `application.properties` configurations.

**Key constraint:** The JPA and entity code does **NOT** change — only the properties file changes.

## What You Need to Know Before Solving

- The JDBC URL format changes between storage methods but everything else in your Java code stays the same
- For MySQL, you must first create the database: `CREATE DATABASE school;` in MySQL
- For MySQL, you must add `mysql-connector-j` to `pom.xml`
- H2 file-based creates a `.mv.db` file in the specified path

## Exam Tips

- `jdbc:h2:file:./data/schooldb` → creates `./data/schooldb.mv.db` on disk
- `jdbc:mysql://localhost:3306/school` → connects to MySQL running locally
- The MySQL driver class is `com.mysql.cj.jdbc.Driver` (NOT the old `com.mysql.jdbc.Driver`)
- Adding `?serverTimezone=UTC` to MySQL URL prevents timezone errors
- **EXAM CRITICAL:** The @Entity, @Repository, @Service code is 100% identical for all three variants
