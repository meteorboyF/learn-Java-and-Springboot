// FIX 4: Add the duplicate guard to the DataSeeder
// The fix: check if data already exists before inserting.

@Component
public class DataSeeder implements CommandLineRunner {

    private final StudentRepository repo;

    public DataSeeder(StudentRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) throws Exception {
        // FIXED: Check count before inserting.
        // repo.count() → SELECT COUNT(*) FROM students
        // If count > 0: data exists from a previous run → skip seeding.
        // If count == 0: table is empty (first run or in-memory) → seed data.
        if (repo.count() > 0) {
            System.out.println("[DataSeeder] Data already exists. Skipping seed.");
            return;  // IMPORTANT: early return prevents any inserts
        }

        // Only reaches here if count == 0 (table is empty).
        repo.save(new Student("Alice Johnson",  "STU20240001", 3.85, "Computer Science"));
        repo.save(new Student("Bob Rahman",     "STU20240002", 3.20, "Mathematics"));
        repo.save(new Student("Clara Reyes",    "STU20240003", 3.60, "Physics"));

        System.out.println("[DataSeeder] Seeded " + repo.count() + " students.");

        // WHY THIS MATTERS:
        // With H2 in-memory (ddl-auto=update): on restart, the in-memory DB is fresh
        //   → count() = 0 → seeding happens → correct.
        // With H2 file-based or MySQL: data persists between restarts
        //   → count() > 0 on second startup → skip seeding → correct.
        //   → Without guard: second startup inserts 3 more students.
        //   → studentId UNIQUE constraint → ConstraintViolationException → app crash!
    }
}
