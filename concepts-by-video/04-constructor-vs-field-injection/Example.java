// Constructor injection vs field injection — complete comparison.

// ─── CONSTRUCTOR INJECTION (USE THIS) ────────────────────────────────────────
@Service
public class StudentService {
    private final StudentRepository repo;  // final — set once, never changed

    // Single constructor → @Autowired is optional (Spring uses it automatically)
    public StudentService(StudentRepository repo) {
        this.repo = repo;
    }
    // In a unit test: new StudentService(Mockito.mock(StudentRepository.class)) — works!
}

// ─── FIELD INJECTION (AVOID) ─────────────────────────────────────────────────
@Service
public class StudentServiceBad {
    @Autowired  // required
    private StudentRepository repo;  // NOT final — Spring sets it via reflection

    // In a unit test: new StudentServiceBad() → repo is null → NullPointerException!
    // You'd need @SpringBootTest or Mockito.injectMocks() to set the field.
}

// ─── SETTER INJECTION (RARELY USED) ──────────────────────────────────────────
@Service
public class StudentServiceSetter {
    private StudentRepository repo;  // not final

    @Autowired  // required
    public void setRepo(StudentRepository repo) {
        this.repo = repo;
    }
    // Use only for truly optional dependencies where null is acceptable.
}
