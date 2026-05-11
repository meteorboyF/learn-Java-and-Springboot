// BUG 4: Seeder has no duplicate guard
// Symptom: Every time the app restarts (with persistent storage), 3 more students
//          are inserted. After 5 restarts: 15 students instead of 3.
//          With H2 in-memory, the table is always empty on restart so this won't
//          cause a crash — but with H2 file-based or MySQL, duplicates WILL happen.
//          If studentId has a UNIQUE constraint, the second restart throws a
//          ConstraintViolationException and the app FAILS TO START.
// Location: DataSeeder.run() method — missing the count check.

@Component
public class DataSeeder implements CommandLineRunner {

    private final StudentRepository repo;

    public DataSeeder(StudentRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) throws Exception {
        // BUG: No check before inserting!
        // With ddl-auto=update and persistent storage (file/MySQL):
        //   Restart 1: table is empty → inserts 3 students (ids 1, 2, 3)
        //   Restart 2: table has 3 → inserts 3 MORE (ids 4, 5, 6)
        //   Restart 3: table has 6 → inserts 3 MORE (ids 7, 8, 9)
        //   ...
        // With UNIQUE on studentId: Restart 2 tries to insert STU20240001 again
        //   → ConstraintViolationException → app crashes on startup!
        repo.save(new Student("Alice Johnson",  "STU20240001", 3.85, "Computer Science"));
        repo.save(new Student("Bob Rahman",     "STU20240002", 3.20, "Mathematics"));
        repo.save(new Student("Clara Reyes",    "STU20240003", 3.60, "Physics"));

        System.out.println("Seeding complete.");
        // MISSING: if (repo.count() > 0) return;   ← this is the fix
    }
}
